package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.ejb.authentication.AuthenticationManager;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import java.util.HashSet;

/**
 * @author BADMAN152
 *         implements the remote api service
 */
@EJB
@WebService(
        portName = "RemoteApiPort",
        serviceName = "RemoteApi",
        targetNamespace = "http://minecraftadmin.de/secureban",
        endpointInterface = "de.minecraftadmin.api.API")
@Remote(value = API.class)
@Interceptors(value = AuthenticationManager.class)
public class BanService implements API {

    @EJB
    private DatabaseService database;

    @Override
    public Player getPlayerBans(String playerName) {
        Player p = database.getSingleResult(Player.class, "SELECT * FROM Player p WHERE p.userName= ?", new Object[]{playerName});
        if (p == null) {
            p = new Player();
            p.setUserName(playerName);
        }
        return p;
    }

    @Override
    public void submitPlayerBans(final String playerName, PlayerBan ban) {
        Player p = database.getSingleResult(Player.class, "SELECT * FROM Player p WHERE p.userName= ?", new Object[]{playerName});
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
