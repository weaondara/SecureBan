package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.ejb.authentication.AuthenticationManager;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author BADMAN152
 *         implements the remote api service
 */
@Stateless
@WebService(
        portName = "RemoteApiPort",
        serviceName = "RemoteApi",
        targetNamespace = "http://minecraftadmin.de/secureban",
        endpointInterface = "de.minecraftadmin.api.API")
@Remote(value = API.class)
@Interceptors(value = AuthenticationManager.class)
public class BanService implements API {

    @EJB(lookup = "global/localhost/SecureBan/DatabaseService")
    private DatabaseService database;

    @Override
    public Player getPlayerBans(String playerName) {
        HashMap<String,Object> param = new HashMap<String, Object>();
        param.put("name",playerName);
        Player p = database.getSingleResult(Player.class, "SELECT p FROM Player p WHERE p.userName=:name", param);
        if (p == null) {
            p = new Player();
            p.setUserName(playerName);
        }
        return p;
    }

    @Override
    public void submitPlayerBans(final String playerName, PlayerBan ban) {
        HashMap<String,Object> param = new HashMap<String, Object>();
        param.put("name",playerName);
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
}
