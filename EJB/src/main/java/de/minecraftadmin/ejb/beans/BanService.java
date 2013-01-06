package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;
import de.minecraftadmin.api.entity.Server;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.ejb.authentication.AuthenticationManager;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.ws.rs.Path;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
@Interceptors(value = AuthenticationManager.class)
public class BanService implements API {

    @Resource
    private WebServiceContext webservice;
    @EJB(lookup = "global/localhost/SecureBan/DatabaseService")
    private DatabaseService database;

    @Override
    public Login allowedToJoin(String playerName) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("name", playerName);
        param.put("expired", System.currentTimeMillis());
        Player p = database.getSingleResult(Player.class, "SELECT p FROM Player p WHERE p.userName=:name and ( p.bans.expired>:expired or p.bans.expired is null )", param);
        if (p == null) {
            p = new Player();
            p.setUserName(playerName);

        }
        if (p.getBans() == null) {
            p.setBans(new HashSet<PlayerBan>());
        }
        Login l = new Login();
        l.setAllowed(p.getBans().isEmpty());
        if (!l.isAllowed())
            l.setBan(p.getBans().iterator().next());
        return l;  //To change body of implemented methods use File | Settings | File Templates.
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
