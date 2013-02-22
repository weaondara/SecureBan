package de.minecraftadmin.ejb.beans;

import de.minecraftadmin.api.entity.Maintenance;

import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;

/**
 * search for the maintenance
 */
@Stateless
@Startup
public class MaintenanceService {

    @EJB(lookup = "global/localhost/SecureBan/DatabaseService")
    private DatabaseService databaseService;

    public Maintenance getMaintenance() {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("time", System.currentTimeMillis());
        Maintenance maintenance = databaseService.getSingleResult(Maintenance.class, "SELECT m FROM Maintenance m WHERE m.startTime<:time and m.endTime>:time", param);

        return maintenance;
    }

    public List<Maintenance> getAllMaintenance() {
        return databaseService.getResultList(Maintenance.class, "SELECT m FROM Maintenance m", new HashMap<String, Object>());
    }

    public void addMaintenance(Maintenance maintenance) {
        databaseService.persist(maintenance);
    }

    public void removeMaintenance(Maintenance maintenance) {
        databaseService.delete(maintenance);
    }
}
