package de.minecraftadmin.secureban.system;

import com.avaje.ebean.annotation.Transactional;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;

import java.util.HashSet;
import java.util.logging.Logger;

/**
 * @author BADMAN152
 *         manage every ban on the server
 */
public class BanManager {
    private final Logger LOG = Logger.getLogger("BanManager");
    private final RemoteAPIManager remote;
    private final Database db;
    private final BanAnalyzer analyzer = new BanAnalyzer();

    public BanManager(Database localDB, String remoteUrl, String apiKey) {
        this.db = localDB;
        this.remote = new RemoteAPIManager(remoteUrl, apiKey);
    }

    /**
     * @param userName  the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     * @author BADMAN152
     * create a global ban and save it to the global & local database
     */
    @Transactional
    public void globalBan(String userName, String staffName, String banReason) {
        Player localPlayer = getLocalPlayer(userName);
        PlayerBan ban = new PlayerBan();
        ban.setStaffName(staffName);
        ban.setBanType(BanType.GLOBAL);
        ban.setBanReason(banReason);
        if (localPlayer.getBans() == null) localPlayer.setBans(new HashSet<PlayerBan>());
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
    }

    /**
     * @param userName  the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     * @author BADMAN152
     * create a local ban and save it to the local database
     */
    @Transactional
    public void localBan(String userName, String staffName, String banReason) {
        Player localPlayer = getLocalPlayer(userName);
        PlayerBan ban = new PlayerBan();
        ban.setStaffName(staffName);
        ban.setSaveState(SaveState.SAVED); //local bans allways in SAVED State
        ban.setBanType(BanType.LOCAL);
        ban.setBanReason(banReason);
        if (localPlayer.getBans() == null) localPlayer.setBans(new HashSet<PlayerBan>());
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
    }

    /**
     * @param userName  the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     * @param duration  timestamp when it expires
     * @author BADMAN152
     * create a temp ban and save it to the local database
     */
    @Transactional
    public void tempBan(String userName, String staffName, String banReason, long duration) {
        Player localPlayer = getLocalPlayer(userName);
        PlayerBan ban = new PlayerBan();
        ban.setStaffName(staffName);
        ban.setSaveState(SaveState.SAVED); //temp bans allways in SAVED State
        ban.setBanType(BanType.TEMP);
        ban.setBanReason(banReason);
        ban.setExpired(Long.valueOf(System.currentTimeMillis() + duration));
        if (localPlayer.getBans() == null) localPlayer.setBans(new HashSet<PlayerBan>());
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
    }

    public void unban(String userName) {

    }

    /**
     * @param userName
     * @return
     * @author BADMAN152
     * get the persisted player with all bans
     */
    @Transactional
    public Player getAllBansOfPlayer(String userName) {
        Player localPlayer = getLocalPlayer(userName);
        Player remotePlayer = null;
        try {
            remotePlayer = this.remote.getRemoteAPI().getPlayerBans(userName);
        } catch (Throwable throwable) {
            LOG.warning("Could not get remote Bans cause :\n\t" + throwable.getLocalizedMessage());
        }
        if (remotePlayer == null) return localPlayer;
        localPlayer.getBans().addAll(remotePlayer.getBans());
        return localPlayer;
    }

    public Player getActiveBansOfPlayer(String userName) {
        Player p = getAllBansOfPlayer(userName);
        p.setBans(new HashSet<PlayerBan>(analyzer.getActiveBansOfPlayer(p)));
        return p;
    }

    /**
     * load the bans of a specific player from the local database
     *
     * @param userName
     * @return
     */
    private Player getLocalPlayer(String userName) {
        Player p = db.getDatabase().createQuery(Player.class).where().eq("userName", userName).findUnique();
        if (p == null) {
            p = new Player();
            p.setUserName(userName);
        }
        return p;
    }

    /**
     * simple check for a player to allow to join or not
     *
     * @param userName
     * @return true if he has no active bans
     */
    public boolean allowedToJoin(String userName) {
        return analyzer.isPlayerAllowedToJoin(getActiveBansOfPlayer(userName));
    }
}
