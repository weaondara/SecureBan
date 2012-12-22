package de.minecraftadmin.api;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;

import javax.jws.WebService;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author BADMAN152
 *         Accessor Class to access the remote api
 */
@WebService(targetNamespace = "http://minecraftadmin.de/secureban")
public interface API extends Remote {
    /**
     * @param playerName
     * @return
     */
    public Player getPlayerBans(String playerName) throws RemoteException;

    /**
     * @param playerName
     * @param ban
     */
    public void submitPlayerBans(String playerName, PlayerBan ban) throws RemoteException;

    public void unbanPlayer(String playerName) throws RemoteException;
}
