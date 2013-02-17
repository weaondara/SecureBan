package de.minecraftadmin.secureban.system;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.NoteType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 08.02.13
 * Time: 19:00
 * To change this template use File | Settings | File Templates.
 */
public class NoteManager {

    private final RemoteAPIManager manager;

    public NoteManager(RemoteAPIManager manager) {
        this.manager = manager;
    }

    public void addNoteToPlayer(String playerName, String note, NoteType type) throws Throwable {
        Note n = new Note();
        n.setType(type);
        n.setNotes(note);
        manager.sumitPlayerNote(playerName, n);
    }

    public void removeNoteFromPlayer(String playerName, Long id) throws Throwable {
        manager.deletePlayerNote(playerName, id);

    }

    public List<Note> getNoteFromPlayer(String playerName) throws Throwable {
        return manager.getPlayerNote(playerName);
    }


}
