package de.minecraftadmin.secureban.system;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;
import de.minecraftadmin.api.utils.BanAnalyzer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 22.12.12
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class BanSynchronizer implements Runnable {

    private final Logger LOG = Logger.getLogger("BanSynchronizer");
    private final RemoteAPIManager remote;
    private final Database database;
    private final boolean multi;
    private final BanAnalyzer analyzer;

    private boolean isRunning = false;

    public BanSynchronizer(Database database, RemoteAPIManager remote, BanAnalyzer analyzer, boolean multi) {
        this.analyzer = analyzer;
        this.multi = multi;
        this.database = database;
        this.remote = remote;
    }

    @Override
    public void run() {
        if (!isRunning) {
            isRunning = true;
            try {
                syncGlobalBans();
                if (multi) {
                    refreshBannedPlayers();
                }
            } catch (Exception e) {
                LOG.severe("Unexpected exception while syncing bans: " + e);
                e.printStackTrace();
            } finally {
                isRunning = false;
            }
        } else {
            LOG.info("Another sync run is still running, skipped new one.");
        }
    }

    /**
     * synchronizes queued ban entries with global server
     */
    private void syncGlobalBans() {
        List<Player> players = database.getDatabase().find(Player.class).fetch("bans").where().eq("bans.saveState", SaveState.QUEUE).eq("bans.banType", BanType.GLOBAL).findList();
        if (players.isEmpty()) {
            return;
        }
        LOG.info("Start synchronizing bans");
        for (Player p : players) {
            try {
                for (PlayerBan ban : p.getBans()) {
                    if (ban.getBanType().equals(BanType.GLOBAL)) {
                        if (ban.getExpired() != null)
                            remote.unBanPlayer(p.getUserName(), ban.getExpired());
                        else
                            remote.submitPlayerBans(p.getUserName(), ban);
                        ban.setSaveState(SaveState.SAVED);
                    }
                }
                database.getDatabase().save(p);
            } catch (Throwable throwable) {
                LOG.warning("Error while synchronizing bans of player " + p.getUserName());
                LOG.warning("Synchronizing Error " + throwable.getLocalizedMessage());
            }
        }
        LOG.info("finished synchronizing bans");
    }

    /**
     * adds/clears bans in banned-players.txt by using bukkit API
     *
     * @author DT
     */
    private void refreshBannedPlayers() {
        LOG.info("updating banned-players.txt");
        List<Player> players = database.getDatabase().find(Player.class).findList();
        for (Player p : players) {
            boolean banned = (analyzer.getActiveBansOfPlayer(p).size() != 0);
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(p.getUserName());
            if (offlinePlayer.isBanned() != banned) {
                offlinePlayer.setBanned(banned);
            }
        }
    }

}
