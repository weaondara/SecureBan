package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.entity.*;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.api.utils.BanSorter;
import de.minecraftadmin.api.utils.NoteSorter;
import de.minecraftadmin.ejb.interceptor.AuthenticationManager;
import de.minecraftadmin.ejb.interceptor.MetaDataManager;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.ws.rs.Path;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.*;

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

    @Override
    public Login allowedToJoin(String playerName) {
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
        return l;
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

    @Override
    public String getAPIVersion() {
        return Version.name;
    }

    private Server getRequestedServer() {
        MessageContext messageContext = webservice.getMessageContext();
        Map headerData = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List keys = (List) headerData.get("server");
        return (Server) keys.get(0);
    }
}
