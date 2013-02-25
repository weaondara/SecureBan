package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.entity.Server;
import de.minecraftadmin.ejb.beans.DatabaseService;
import org.primefaces.model.chart.PieChartModel;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 22.02.13
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "StatisticManager")
public class StatisticManagedBean {

    @EJB
    private DatabaseService database;

    public PieChartModel createBanPieChartModel() {
        PieChartModel model = new PieChartModel();
        model.set("Bans", this.globalBanActiveCount());
        model.set("Unbans", this.globalBanInactiveCount());
        return model;
    }

    public PieChartModel createServerBanModel() {
        PieChartModel model = new PieChartModel();
        List<Server> servers = database.getResultList(Server.class, "SELECT s FROM Server s", new HashMap<String, Object>());
        for (Server s : servers) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("id", s.getId());
            model.set(s.getServerName(), (Long) database.querySingeResult("SELECT count(*) FROM PlayerBan p WHERE p.expired is null and p.server.id=:id", params));
        }
        return model;
    }

    public PieChartModel createServerBanModelInactive() {
        PieChartModel model = new PieChartModel();
        List<Server> servers = database.getResultList(Server.class, "SELECT s FROM Server s", new HashMap<String, Object>());
        for (Server s : servers) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("id", s.getId());
            model.set(s.getServerName(), (Long) database.querySingeResult("SELECT count(*) FROM PlayerBan p WHERE p.expired is not null and p.server.id=:id", params));
        }
        return model;
    }

    public long globalBanActiveCount() {
        return (Long) database.querySingeResult("SELECT count(*) FROM PlayerBan p WHERE p.expired is null", new HashMap<String, Object>());
    }

    public long globalBanInactiveCount() {
        return (Long) database.querySingeResult("SELECT count(*) FROM PlayerBan p WHERE p.expired is not null", new HashMap<String, Object>());

    }

    public long globalPlayerCount() {
        return (Long) database.querySingeResult("SELECT count(*) FROM Player", new HashMap<String, Object>());
    }

}
