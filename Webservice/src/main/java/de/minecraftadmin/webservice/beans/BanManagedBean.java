package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.ejb.beans.DatabaseService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.HashMap;
import java.util.List;

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

    public int globalBanCount() {
        List<PlayerBan> list = database.getResultList(PlayerBan.class, "SELECT p FROM PlayerBan p", new HashMap<String, Object>());
        if (list == null) return 0;
        if (list.isEmpty()) return 0;
        return list.size();
    }

    public int globalPlayerCount() {
        List<Player> list = database.getResultList(Player.class, "SELECT p FROM Player p", new HashMap<String, Object>());
        if (list == null) return 0;
        if (list.isEmpty()) return 0;
        return list.size();
    }

    public String getVersion() {
        return version;
    }
}
