package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.ejb.beans.DatabaseService;
import org.primefaces.model.chart.PieChartModel;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.HashMap;

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
        model.set("Active Bans", this.globalBanActiveCount());
        model.set("Inactive Bans", this.globalBanInactiveCount());
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
