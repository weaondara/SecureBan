package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.entity.*;
import de.minecraftadmin.api.exception.NotAHashedIpException;
import de.minecraftadmin.api.exception.WrongBanTypeException;
import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.api.utils.BanSorter;
import de.minecraftadmin.api.utils.NoteSorter;
import de.minecraftadmin.ejb.interceptor.AuthenticationManager;
import de.minecraftadmin.ejb.interceptor.MetaDataManager;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.ws.rs.Path;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author BADMAN152
 *         implements the remote api service
 */
@Stateless
@Startup
@Path(value = "/webservice")
@WebService(
        portName = "RemoteApiPort",
        serviceName = "RemoteApi",
        targetNamespace = "http://minecraftadmin.de/secureban",
        endpointInterface = "de.minecraftadmin.api.API")
@Remote(value = API.class)
@Interceptors(value = {AuthenticationManager.class, MetaDataManager.class})
public class BanService implements API {

    private Logger LOG = Logger.getLogger("BanService");
    @Resource
    private WebServiceContext webservice;
    @EJB(lookup = "global/localhost/SecureBan/DatabaseService")
    private DatabaseService database;

    //@Override
    private Login allowedToJoin(String playerName) {
        Player p = getPlayerBans(playerName);
        if (p == null) {
            p = new Player();
            p.setUserName(playerName);

        }
        if (p.getBans() == null) p.setBans(new HashSet<PlayerBan>());
        Login l = new Login();
        for (PlayerBan ban : p.getBans()) {
            if (ban.getExpired() == null) l.addActiveBanCount(1);
            else l.addInactiveBanCount(1);
        }
        List<Note> notes = getPlayerNote(playerName);
        if (notes != null && !notes.isEmpty()) {
            Collections.sort(notes, new NoteSorter());
            l.setNote(notes.get(0));
            l.setNoteCount(notes.size());
        } else
            l.setNote(null);
        List<PlayerBan> bans = new ArrayList<PlayerBan>(p.getBans());
        Collections.sort(bans, new BanSorter());
        if (!bans.isEmpty()) l.setBan(bans.get(0));
        l.setAllowed(true);
        l.setAltAccountName(getAltAccounts(p));
        if (l.getAltAccountName() == null) l.setAltAccountName(new ArrayList<String>());
        return l;
    }

    @Override
    public Login allowedToJoin(String playerName, String ipHash) throws Exception {
        saveIpOfPlayer(playerName, ipHash);
        return allowedToJoin(playerName);
    }

    private ArrayList<String> getAltAccounts(Player player) {
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("playerId", player.getId());
        PlayerIP pip = database.getSingleResult(PlayerIP.class, "SELECT pip FROM PlayerIP pip WHERE pip.player.id=:playerId", parameter);
        if (pip == null) return new ArrayList<String>();
        parameter = new HashMap<String, Object>();
        parameter.put("addressId", pip.getAddress().getId());
        List<PlayerIP> pips = database.getResultList(PlayerIP.class, "SELECT pip FROM PlayerIP pip WHERE pip.address.id=:addressId", parameter);
        ArrayList<String> alt = new ArrayList<String>();
        for (PlayerIP altPlayer : pips) {
            alt.add(altPlayer.getPlayer().getUserName());
        }
        alt.remove(player.getUserName());
        LOG.info("Found " + alt.size() + " alternative Accounts for " + player.getUserName());
        return alt;
    }

    private void saveIpOfPlayer(String playerName, String ipHash) {
        if (ipHash.contains(".")) throw new NotAHashedIpException();
        Player player = getPlayerBans(playerName);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("id", player.getId());
        PlayerIP pip = database.getSingleResult(PlayerIP.class, "SELECT pip FROM PlayerIP pip WHERE pip.player.id=:id", parameter);
        parameter = new HashMap<String, Object>();
        parameter.put("hash", ipHash);
        IPAddress address = database.getSingleResult(IPAddress.class, "SELECT a FROM IPAddress a WHERE a.ipHash=:hash", parameter);
        if (address == null) {
            address = new IPAddress();
            address.setIpHash(ipHash);
        }
        if (pip == null) {
            pip = new PlayerIP();
            pip.setPlayer(player);
        }
        pip.setAddress(address);
        pip.setLastUpdate(System.currentTimeMillis());
        database.persist(pip);
    }

    @Override
    public Player getPlayerBans(String playerName) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("name", playerName);
        Player p = database.getSingleResult(Player.class, "SELECT p FROM Player p WHERE p.userName=:name", param);
        if (p == null) {
            p = new Player();
            p.setUserName(playerName);
        }
        return p;
    }

    @Override
    @Asynchronous
    public void submitPlayerBans(final String playerName, PlayerBan ban) {
        if (!ban.getBanType().equals(BanType.GLOBAL)) throw new WrongBanTypeException();
        ban.setServer(getRequestedServer());
        ban.setSaveState(SaveState.SAVED);
        if (ban.getStart() == null) ban.setStart(System.currentTimeMillis());
        ban.setId(null);
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("name", playerName);
        Player p = database.getSingleResult(Player.class, "SELECT p FROM Player p WHERE p.userName=:name", param);
        if (p == null) {
            p = new Player();
            p.setUserName(playerName);
        }
        if (p.getBans() == null) {
            p.setBans(new HashSet<PlayerBan>());
        }
        p.getBans().add(ban);
        database.persist(p);
        LOG.info(ban.getServer() + " added Ban for " + playerName + " " + ban);
    }

    @Override
    public void unBanPlayer(String playerName, Long expire) {
        Player p = this.getPlayerBans(playerName);
        for (PlayerBan b : p.getBans()) {
            if (b.getServer().equals(getRequestedServer())) {
                b.setExpired(expire);
            }
        }
        database.update(p);
        LOG.info(getRequestedServer() + " unbanned " + playerName);
    }

    @Override
    public void sumitPlayerNote(String playerName, Note playerNote) {
        Player p = this.getPlayerBans(playerName);
        playerNote.setUser(p);
        playerNote.setServer(getRequestedServer());
        playerNote.setId(null);
        database.persist(playerNote);
    }

    @Override
    public void deletePlayerNote(String playerName, Long noteId) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("name", playerName);
        param.put("id", noteId);
        Note note = database.getSingleResult(Note.class, "SELECT n FROM Note n WHERE n.user.userName=:name and n.id=:id", param);
        if (note == null) return;
        if (note.getUser().getUserName().equalsIgnoreCase(playerName)) {
            database.delete(note);
        }
    }

    @Override
    public List<Note> getPlayerNote(String playerName) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("name", playerName);
        return database.getResultList(Note.class, "SELECT n FROM Note n WHERE n.user.userName=:name", param);
    }

/*    @Override
    public String getAPIVersion() {
        return Version.name;
    }*/

    private Server getRequestedServer() {
        MessageContext messageContext = webservice.getMessageContext();
        Map headerData = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List keys = (List) headerData.get("server");
        return (Server) keys.get(0);
    }
}
