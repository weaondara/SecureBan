package de.minecraftadmin.secureban.listener;

import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
        final Login login = banManager.allowedToJoin(event.getPlayer().getName(), false);
        if (!login.isAllowed()) {
            if (login.getBan() == null) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Unwanted");
                return;
            }
            switch (login.getBan().getBanType()) {
                case TEMP:
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are " + login.getBan().getBanType().name() + " banned from this Server until " + new Date(login.getBan().getExpired()).toString() + " for " + login.getBan().getBanReason());
                    return;
                default:
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are " + login.getBan().getBanType().name() + " banned from this Server" + " for " + login.getBan().getBanReason());
            }
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        sendNotification(event.getPlayer());
    }

    protected void sendNotification(Player player) {
        if (player.hasPermission("secureban.silent")) return;
        final String userName = player.getName();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Login login = banManager.allowedToJoin(userName, true);
                for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.hasPermission("secureban.notifylogin") && login.getBanCountInactive() != 0) {
                        player.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " User " + userName + " has active bans " + login.getBanCountActive() + "/" + login.getBanCountInactive());
                        if (login.getNote() != null)
                            player.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.YELLOW + " User " + userName + " has " + login.getNoteCount() + " notes - Latest: " + login.getNote());
                    }
                }
                if (Bukkit.getPlayer(userName).hasPermission("secureban.update"))
                    banManager.showUpdateNotification(Bukkit.getPlayer(userName));
            }
        };
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("SecureBan"), r);

    }

}
