package de.minecraftadmin.api.jaxws;

import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.PlayerBan;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;

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
    private Note note;
    private Integer noteCount;
    private ArrayList<String> altAccountName;

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

    public void addInactiveBanCount(int size) {
        if (this.banCountInactive == null) this.banCountInactive = 0;
        this.banCountInactive += size;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Integer getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(Integer noteCount) {
        this.noteCount = noteCount;
    }

    @XmlElementWrapper(name = "altAccountList", required = true)
    @XmlElement(name = "altAccount")
    public ArrayList<String> getAltAccountName() {
        return altAccountName;
    }

    public void setAltAccountName(ArrayList<String> altAccountName) {
        this.altAccountName = altAccountName;
    }
}
