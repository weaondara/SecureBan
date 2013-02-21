package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.entity.Maintenance;
import de.minecraftadmin.ejb.beans.MaintenanceService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 15.02.13
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "MaintenanceManager")
@SessionScoped
public class MaintenanceManagedBean {

    private Long id;
    private Date start;
    private Date end;
    private String message;
    @EJB
    private MaintenanceService maintenanceService;

    public List<Maintenance> allMaintenance() {
        return maintenanceService.getAllMaintenance();
    }

    public void removeMaintenance(Maintenance maintenance) {
        maintenanceService.removeMaintenance(maintenance);
    }

    public void addMaintenance() {
        Maintenance maintenance = new Maintenance();
        maintenance.setId(id);
        maintenance.setStartTime(start.getTime());
        maintenance.setEndTime(end.getTime());
        maintenance.setMessage(message);
        maintenanceService.addMaintenance(maintenance);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
