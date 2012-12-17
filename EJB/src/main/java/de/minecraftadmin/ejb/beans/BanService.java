package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.API;
import de.minecraftadmin.ejb.authentication.AuthenticationManager;
import de.minecraftadmin.api.entity.PlayerBan;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;

/**
 * implements the remote api service
 */
@EJB
@Interceptors(value = AuthenticationManager.class)
public class BanService implements API {
    @Override
    public PlayerBan getPlayerBans(String userName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void submitPlayerBans(PlayerBan playerBan) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
