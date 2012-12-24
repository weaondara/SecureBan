package de.minecraftadmin.secureban.listener;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 18.12.12
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
public class PlayerListener implements Listener {

    private final BanManager banManager;

    public PlayerListener(BanManager banManager) {
        this.banManager = banManager;
    }

    /**
     * @param event
     * @author BADMAN152
     * checks while login the user and kick him if nessesary
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLoginEvent(PlayerLoginEvent event) {
        boolean allowed = banManager.allowedToJoin(event.getPlayer().getName());
        Player p = banManager.getActiveBansOfPlayer(event.getPlayer().getName());
        PlayerBan ban = null;
        if (!p.getBans().isEmpty()) ban = p.getBans().toArray(new PlayerBan[p.getBans().size()])[0];
        if (!allowed) {
            if (ban == null) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Unwanted");
                return;
            }
            switch (ban.getBanType()) {
                case TEMP:
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are " + ban.getBanType().name() + " banned from this Server until " + new Date(ban.getExpired()).toString());
                    return;
                default:
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are " + ban.getBanType().name() + " banned from this Server");
            }
            return;
        }

        Bukkit.getServer().broadcastMessage(ChatColor.RED + "User " + event.getPlayer().getName() + " has active bans " + p.getBans().size());

    }
}
