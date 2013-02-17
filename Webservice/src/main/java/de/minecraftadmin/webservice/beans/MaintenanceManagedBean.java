package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.entity.Maintenance;
import de.minecraftadmin.ejb.beans.MaintenanceService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 15.02.13
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
public class MaintenanceManagedBean {

    @EJB
    private MaintenanceService maintenanceService;

    public List<Maintenance> getAllMaintenance() {
        return maintenanceService.getAllMaintenance();
    }

    public void removeMaintenance(Maintenance maintenance) {
        maintenanceService.removeMaintenance(maintenance);
    }

    public void addMaintenance() {

    }
}
