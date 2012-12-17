package de.minecraftadmin.api;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;

import javax.jws.WebService;

/**
 * @author BADMAN152
 * Accessor Class to access the remote api
 */
@WebService(targetNamespace = "http://minecraftadmin.de/secureban")
public interface API {
    /**
     *
     * @param userName
     * @return
     */
    public Player getPlayerBans(String userName);

    /**
     *
     * @param playerBan
     * @param ban
     */
    public void submitPlayerBans(Player playerBan, PlayerBan ban);
}
