package de.minecraftadmin.secureban.system;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;

import java.util.logging.Logger;

/**
 * @author BADMAN152
 * manage every ban on the server
 */
public class BanManager {
    private final Logger LOG = Logger.getLogger("BanManager");
    private final RemoteAPIManager remote;
    private final Database db;

    public BanManager(Database localDB,String remoteUrl,String apiKey){
        this.db = localDB;
        this.remote = new RemoteAPIManager(remoteUrl,apiKey);
    }

    /**
     * @author BADMAN152
     * create a global ban and save it to the global & local database
     * @param userName the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     */
    public void globalBan(String userName, String staffName, String banReason){
        Player localPlayer = db.getDatabase().createQuery(Player.class, "SELECT * FROM Player p where p.userName=\'" + userName + "\'").findUnique();
        PlayerBan ban = new PlayerBan();
        ban.setStaffName(staffName);
        ban.setBanType(BanType.GLOBAL);
        ban.setBanReason(banReason);
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
    }

    /**
     * @author BADMAN152
     * create a local ban and save it to the local database
     * @param userName the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     */
    public void localBan(String userName, String staffName,String banReason){
        Player localPlayer = db.getDatabase().createQuery(Player.class, "SELECT * FROM Player p where p.userName=\'" + userName + "\'").findUnique();
        PlayerBan ban = new PlayerBan();
        ban.setStaffName(staffName);
        ban.setSaveState(SaveState.SAVED); //local bans allways in SAVED State
        ban.setBanType(BanType.LOCAL);
        ban.setBanReason(banReason);
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);    }

    /**
     * @author BADMAN152
     * create a temp ban and save it to the local database
     * @param userName the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     * @param expired timestamp when it expires
     */
    public void tempBan(String userName, String staffName, String banReason, long expired){
        Player localPlayer = db.getDatabase().createQuery(Player.class, "SELECT * FROM Player p where p.userName=\'" + userName + "\'").findUnique();
        PlayerBan ban = new PlayerBan();
        ban.setStaffName(staffName);
        ban.setSaveState(SaveState.SAVED); //temp bans allways in SAVED State
        ban.setBanType(BanType.TEMP);
        ban.setBanReason(banReason);
        ban.setExpired(expired);
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
    }

    /**
     * @author BADMAN152
     * get the persisted player with all bans
     * @param userName
     * @return
     */
    public Player getBansOfPlayer(String userName) {
        Player localPlayer = db.getDatabase().createQuery(Player.class, "SELECT * FROM Player p where p.userName=\'" + userName + "\'").findUnique();
        Player remotePlayer = null;
        try {
            remotePlayer = this.remote.getRemoteAPI().getPlayerBans(userName);
        } catch (Throwable throwable) {
            LOG.info("Could not get remote result");
        }
        if (remotePlayer == null)return localPlayer;
        localPlayer.getBans().addAll(remotePlayer.getBans());
        return localPlayer;
    }
}
