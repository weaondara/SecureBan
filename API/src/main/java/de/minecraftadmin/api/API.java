package de.minecraftadmin.api;

import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.jaxws.Login;

import javax.jws.WebService;
import java.util.List;

/**
 * @author BADMAN152
 *         Accessor Class to access the remote api
 */
@WebService(targetNamespace = "http://minecraftadmin.de/secureban")
public interface API {

//    public Login allowedToJoin(String playerName) throws Exception;

    public Login allowedToJoin(String playerName, String ipHash) throws Exception;

    /**
     * @param playerName
     * @return
     */
    public Player getPlayerBans(String playerName) throws Exception;

    /**
     * @param playerName
     * @param ban
     */
    public void submitPlayerBans(String playerName, PlayerBan ban) throws Exception;

    public void unBanPlayer(String playerName, Long expire) throws Exception;

    public void sumitPlayerNote(String playerName, Note playerNote) throws Exception;

    public void deletePlayerNote(String playerName, Long noteId) throws Exception;

    public List<Note> getPlayerNote(String playerName) throws Exception;

}
