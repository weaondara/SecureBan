package de.minecraftadmin.secureban.system;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;

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

    public BanSynchronizer(Database database, RemoteAPIManager remote) {
        this.database = database;
        this.remote = remote;
    }

    @Override
    public void run() {
        List<Player> players = database.getDatabase().find(Player.class).fetch("bans").where().eq("bans.saveState", SaveState.QUEUE).eq("bans.banType", BanType.GLOBAL).findList();
        if (players.isEmpty()) {
            LOG.info("No Bans to synchronize");
            return;
        }
        for (Player p : players) {
            try {
                for (PlayerBan ban : p.getBans()) {
                    if (ban.getBanType().equals(BanType.GLOBAL))
                        remote.getRemoteAPI().submitPlayerBans(p.getUserName(), ban);
                    ban.setSaveState(SaveState.SAVED);
                }
                database.getDatabase().save(p);
            } catch (Throwable throwable) {
                LOG.warning("Error while synchronizing bans of player " + p.getUserName());
                LOG.warning("Synchronizing Error " + throwable.getLocalizedMessage());
            }
        }

    }
}
