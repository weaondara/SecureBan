package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.ejb.authentication.AuthenticationManager;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

/**
 * @author BADMAN152
 * implements the remote api service
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
    public Player getPlayerBans(String userName) {
        return null;
    }

    @Override
    public void submitPlayerBans(Player playerBan, PlayerBan ban) {

    }
}
