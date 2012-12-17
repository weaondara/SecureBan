package de.minecraftadmin.api.entity;

import javax.persistence.*;

/**
 * @author BADMAN152
 *
 * Represent a Player wich has been banned
 */
@Entity
public class PlayerBan {
    @Id
    private Long id;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String staffName;
    @Enumerated(value = EnumType.STRING)
    private BanType banType;
    @Enumerated(value = EnumType.STRING)
    private SaveState saveState = SaveState.QUEUE;
    private String banReason;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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
}
