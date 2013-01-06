package de.minecraftadmin.api.jaxws;

import de.minecraftadmin.api.entity.PlayerBan;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 04.01.13
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
public class Login implements Serializable {

    private boolean allowed;
    private Integer banCountActive;
    private Integer banCountInactive;
    private PlayerBan ban;

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public PlayerBan getBan() {
        return ban;
    }

    public void setBan(PlayerBan ban) {
        this.ban = ban;
    }

    public Integer getBanCountActive() {
        return banCountActive;
    }

    public void setBanCountActive(Integer banCountActive) {
        this.banCountActive = banCountActive;
    }

    public Integer getBanCountInactive() {
        return banCountInactive;
    }

    public void setBanCountInactive(Integer banCountInactive) {
        this.banCountInactive = banCountInactive;
    }

    public void addActiveBanCount(int size) {
        if (this.banCountActive == null) this.banCountActive = 0;
        this.banCountActive += size;
    }
}
