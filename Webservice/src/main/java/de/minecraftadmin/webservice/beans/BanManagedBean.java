package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.ejb.beans.DatabaseService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 23.12.12
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "BanManager")
@SessionScoped
public class BanManagedBean {

    @EJB
    private DatabaseService database;
    private final String version = Version.name;

    public long globalBanCount() {
        return (Long) database.querySingeResult("SELECT count(*) FROM PlayerBan", new HashMap<String, Object>());
    }

    public long globalPlayerCount() {
        return (Long) database.querySingeResult("SELECT count(*) FROM Player", new HashMap<String, Object>());
    }

    public String getVersion() {
        return version;
    }
}
