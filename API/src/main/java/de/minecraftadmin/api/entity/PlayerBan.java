package de.minecraftadmin.api.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * Represent a PlayerBan
 */
public class PlayerBan {
    @Id
    private Long id;
    private String userName;
    private String staffName;
    @Enumerated(value = EnumType.STRING)
    private BanState banState;
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

    public BanState getBanState() {
        return banState;
    }

    public void setBanState(BanState banState) {
        this.banState = banState;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
