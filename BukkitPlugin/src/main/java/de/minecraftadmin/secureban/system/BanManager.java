package de.minecraftadmin.secureban.system;

import com.avaje.ebean.Expr;
import com.avaje.ebean.annotation.Transactional;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;
import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.api.utils.BanAnalyzer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author BADMAN152
 *         manage every ban on the server
 */
public class BanManager {
    private final Logger LOG = Logger.getLogger("BanManager");
    private final RemoteAPIManager remote;
    private final Database db;
    private final BanAnalyzer analyzer;

    public BanManager(Database localDB, String remoteUrl, String apiKey) {
        this.db = localDB;
        this.remote = new RemoteAPIManager(remoteUrl, apiKey);
        analyzer = new BanAnalyzer(apiKey);
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
        ban.setStart(System.currentTimeMillis());
        ban.setBanType(BanType.GLOBAL);
        ban.setBanReason(banReason);
        if (localPlayer.getBans() == null) localPlayer.setBans(new HashSet<PlayerBan>());
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
        LOG.info("Staff " + staffName + " globally banned player " + userName);
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
        ban.setStart(System.currentTimeMillis());
        ban.setSaveState(SaveState.SAVED); //local bans allways in SAVED State
        ban.setBanType(BanType.LOCAL);
        ban.setBanReason(banReason);
        if (localPlayer.getBans() == null) localPlayer.setBans(new HashSet<PlayerBan>());
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
        LOG.info("Staff " + staffName + " locally banned player " + userName);
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
        ban.setStart(System.currentTimeMillis());
        ban.setSaveState(SaveState.SAVED); //temp bans always in SAVED State
        ban.setBanType(BanType.TEMP);
        ban.setBanReason(banReason);
        ban.setExpired(System.currentTimeMillis() + duration);
        if (localPlayer.getBans() == null) localPlayer.setBans(new HashSet<PlayerBan>());
        localPlayer.getBans().add(ban);
        db.getDatabase().save(localPlayer);
        LOG.info("Staff " + staffName + " temporary banned player " + userName);
    }

    /**
     * @param userName
     * @author BADMAN152
     * unban a player. the bans are not deleted they are marked as invalid via expire timestamp
     */
    @Transactional
    public void unban(String userName) {
        Player player = getActiveBansOfPlayer(userName);
        List<PlayerBan> removeBans = new ArrayList<PlayerBan>();
        if (player.getBans() == null) player.setBans(new HashSet<PlayerBan>());
        if (player.getBans().isEmpty()) return;
        for (PlayerBan ban : player.getBans()) {
            if (ban.getServer() != null) {
                removeBans.add(ban);
                continue;
            }
            ban.setExpired(System.currentTimeMillis());
            if (ban.getBanType().equals(BanType.GLOBAL)) {
                if (ban.getSaveState().equals(SaveState.QUEUE)) ban.setSaveState(SaveState.SAVED);
                else ban.setSaveState(SaveState.QUEUE);
            }
        }
        player.getBans().removeAll(removeBans);
        db.getDatabase().update(player);

        LOG.info("Player " + userName + " has been unbanned");
    }

    @Transactional
    public void kick(String userName, String staffName, String reason) {
        Player player = getLocalPlayer(userName);
        PlayerBan kick = new PlayerBan();
        kick.setExpired(System.currentTimeMillis());
        kick.setBanType(BanType.KICK);
        kick.setSaveState(SaveState.SAVED);
        kick.setStaffName(staffName);
        kick.setBanReason(reason);
        player.getBans().add(kick);
        db.getDatabase().save(player);
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
            remotePlayer = this.remote.getPlayerBans(userName);
        } catch (Throwable throwable) {
            LOG.warning("Could not get remote Bans cause :\n\t" + throwable.getLocalizedMessage());
        }
        if (remotePlayer == null) return localPlayer;
        if (remotePlayer.getBans() != null) {
            remotePlayer.getBans().addAll(localPlayer.getBans());
            localPlayer.setBans(remotePlayer.getBans());
        }
        return localPlayer;
    }

    /**
     * @param userName
     * @return
     * @author BADMAN152
     * returns a Player object containing all active(not expired) bans
     */
    public Player getActiveBansOfPlayer(String userName) {
        Player player = db.getDatabase().find(Player.class).fetch("bans")
                .where().eq("userName", userName).or(Expr.gt("bans.expired", Long.valueOf(System.currentTimeMillis())), Expr.isNull("bans.expired")).findUnique();
        if (player == null) {
            player = new Player();
            player.setUserName(userName);
        }
        if (player.getBans() == null) player.setBans(new HashSet<PlayerBan>());

        player.setBans(new HashSet<PlayerBan>(analyzer.getActiveBansOfPlayer(player)));
        return player;
    }

    /**
     * @param userName
     * @return
     * @author BADMAN152
     * returns a rowcount
     */
    public int getActiveBansCountOfPlayer(String userName) {
        Player player = db.getDatabase().find(Player.class).where().eq("userName", userName).or(Expr.gt("bans.expired", Long.valueOf(System.currentTimeMillis())), Expr.isNull("bans.expired")).findUnique();
        if (player == null) return 0;
        if (player.getBans() == null) return 0;
        return player.getBans().size();
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
        if (p.getBans() == null) p.setBans(new HashSet<PlayerBan>());
        return p;
    }

    public int getLocalPlayerBanCount(String userName) {
        return db.getDatabase().createQuery(Player.class).where().eq("userName", userName).not(Expr.eq("bans.banType", BanType.KICK)).findRowCount();
    }

    /**
     * simple check for a player to allow to join or not
     *
     * @param userName
     * @return true if he has no active bans
     */
    public Login allowedToJoin(String userName, boolean useRemote) {
        Login l;
        if (useRemote) {
            try {
                l = remote.allowedToJoin(userName);
            } catch (Throwable throwable) {
                l = new Login();
                l.setAllowed(true);
            }
        } else {
            l = new Login();
            l.setAllowed(true);
        }
        if (l.isAllowed() || !useRemote) {
            // call internal database only of user can join server
            l.addActiveBanCount(getActiveBansCountOfPlayer(userName));
            l.addInactiveBanCount(getLocalPlayerBanCount(userName));
            l.setAllowed(getActiveBansCountOfPlayer(userName) == 0);
            if (!l.isAllowed()) {
                l.setBan(this.getActiveBansOfPlayer(userName).getBans().iterator().next());
            }
        }
        if (l.getBanCountActive() == null) l.setBanCountActive(0);
        if (l.getBanCountInactive() == null) l.setBanCountInactive(0);
        return l;
    }

    public void showUpdateNotification(org.bukkit.entity.Player player) {
        if (!remote.updateVersion()) return;
        String message = remote.getUpdateMessage();
        player.sendMessage("[SecureBan]" + ChatColor.DARK_GREEN + message);
    }

    public RemoteAPIManager getRemote() {
        return remote;
    }
}
