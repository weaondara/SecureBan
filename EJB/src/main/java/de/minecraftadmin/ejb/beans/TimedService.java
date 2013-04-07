package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.entity.PlayerIP;

import javax.ejb.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 25.03.13
 * Time: 23:49
 * To change this template use File | Settings | File Templates.
 */
@Stateless
@Startup
public class TimedService {

    @EJB
    private DatabaseService databaseService;

    @Lock(LockType.WRITE)
    @Schedule(minute = "0", hour = "4")
    public void invalidateMultiAccountTag() {
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("invalidTime", System.currentTimeMillis() - (60 * 60 * 1000 * 18));
        List<PlayerIP> playerIps = databaseService.getResultList(PlayerIP.class, "SELECT pip FROM PlayerIP pip WHERE pip.lastUpdate<:invalidTime", parameter);
        for (PlayerIP pip : playerIps) {
            databaseService.delete(pip);
        }
    }
}
