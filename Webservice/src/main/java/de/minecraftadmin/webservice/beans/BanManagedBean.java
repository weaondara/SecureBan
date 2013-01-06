package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.Server;
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

    public void editSelectedBan() {

    }

    public void deleteAllBansByServer(Server server) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(":id", server.getId());
        database.getResultList(PlayerBan.class, "SELECT b FROM PlayerBan b where b.server.id=:id", params);

    }

    public String getVersion() {
        return version;
    }
}
