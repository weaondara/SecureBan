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
    private final String banURL;

    public PlayerListener(BanManager banManager, String banURL) {
        this.banManager = banManager;
        this.banURL = (banURL.endsWith("/") ? banURL : banURL + "/");
    }

    public PlayerListener(BanManager banManager) {
        this(banManager, null);
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
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are " + login.getBan().getBanType().name() + " banned from this Server until " + new Date(login.getBan().getExpired()).toString() + " for " + login.getBan().getBanReason() + ((banURL != null) ? " " + banURL + login.getBan().getId() : ""));
                    return;
                default:
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are " + login.getBan().getBanType().name() + " banned from this Server" + " for " + login.getBan().getBanReason() + ((banURL != null) ? " " + banURL + login.getBan().getId() : ""));
            }
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        sendNotification(event.getPlayer());
    }

    protected void sendNotification(final Player player) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Login login = banManager.allowedToJoin(player.getName(), true);
                if (!player.hasPermission("secureban.silent")) {
                    for (org.bukkit.entity.Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (p.hasPermission("secureban.notifylogin")) {
                            if (login.getBanCountInactive() != 0)
                                p.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " User " + player.getName() + " has active bans " + login.getBanCountActive() + "/" + login.getBanCountInactive());
                            if (login.getNote() != null) {
                                p.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " User " + player.getName() + " has " + login.getNoteCount() + " notes - Latest: ");
                                p.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + login.getNote().getNotes());
                            }
                        }
                    }
                }
                if (player.hasPermission("secureban.update"))
                    banManager.showUpdateNotification(player);
                if (player.hasPermission("secureban.maintenance"))
                    banManager.showMaintenanceNotification(player);
            }
        };
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("SecureBan"), r);

    }

}
