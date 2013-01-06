package de.minecraftadmin.api;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.jaxws.Login;

import javax.jws.WebService;

/**
 * @author BADMAN152
 *         Accessor Class to access the remote api
 */
@WebService(targetNamespace = "http://minecraftadmin.de/secureban")
public interface API {

    public Login allowedToJoin(String playerName);

    /**
     * @param playerName
     * @return
     */
    public Player getPlayerBans(String playerName);

    /**
     * @param playerName
     * @param ban
     */
    public void submitPlayerBans(String playerName, PlayerBan ban);

    public void unBanPlayer(String playerName, Long expire);

    public String getAPIVersion();
}
