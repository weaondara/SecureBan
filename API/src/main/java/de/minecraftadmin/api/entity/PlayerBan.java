package de.minecraftadmin.api.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author BADMAN152
 *         <p/>
 *         Represent a Player wich has been banned
 */
@Entity
public class PlayerBan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String staffName;
    @Enumerated(value = EnumType.STRING)
    private BanType banType;
    @Enumerated(value = EnumType.STRING)
    private SaveState saveState = SaveState.QUEUE;
    private Long start;
    private Long expired;
    private String banReason;
    @ManyToOne(fetch = FetchType.EAGER)
    private Server server;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public SaveState getSaveState() {
        return saveState;
    }

    public void setSaveState(SaveState saveState) {
        this.saveState = saveState;
    }

    public BanType getBanType() {
        return banType;
    }

    public void setBanType(BanType banType) {
        this.banType = banType;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "[" + this.getBanType().name() + "] Reason: " + getBanReason() + " Expire: " + getExpired() + " Server: " + server;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getStart() {
        return start;
    }
}
