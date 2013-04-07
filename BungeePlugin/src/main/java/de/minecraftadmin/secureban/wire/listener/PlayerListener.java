package de.minecraftadmin.secureban.wire.listener;

import com.google.common.eventbus.Subscribe;
import de.minecraftadmin.api.entity.Maintenance;
import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.secureban.system.BanManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.03.13
 * Time: 19:49
 * To change this template use File | Settings | File Templates.
 */
public class PlayerListener implements Listener {

    private final BanManager banManager;
    private final String banURL;

    public PlayerListener(BanManager banManager) {
        this(banManager, null);
    }

    public PlayerListener(BanManager banManager, String banURL) {
        this.banManager = banManager;
        this.banURL = banURL;
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        final Login login = banManager.allowedToJoin(event.getConnection().getName(), event.getConnection().getAddress().getAddress().toString(), false);
        if (!login.isAllowed()) {
            if (login.getBan() == null) {
                event.setCancelled(true);
                event.setCancelReason("Unwanted");
                return;
            }
            switch (login.getBan().getBanType()) {
                case TEMP:
                    event.setCancelled(true);
                    event.setCancelReason("You are " + login.getBan().getBanType().name() + " banned from this Server until " + new Date(login.getBan().getExpired()).toString() + " for " + login.getBan().getBanReason() + ((banURL != null) ? " " + banURL + login.getBan().getId() : ""));
                    return;
                default:
                    event.setCancelled(true);
                    event.setCancelReason("You are " + login.getBan().getBanType().name() + " banned from this Server" + " for " + login.getBan().getBanReason() + ((banURL != null) ? " " + banURL + login.getBan().getId() : ""));
            }
            return;
        }

    }

    @Subscribe
    public void onJoinevent(PostLoginEvent event) {
        sendNotification(event.getPlayer());
    }

    protected void sendNotification(final ProxiedPlayer player) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Login login = banManager.allowedToJoin(player.getName(), player.getAddress().getAddress().toString(), true);
                if (!player.hasPermission("secureban.silent")) {
                    for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                        if (login.getBanCountInactive() != 0 && p.hasPermission("secureban.notifylogin.inactive"))
                            p.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " User " + player.getName() + " has active bans " + login.getBanCountActive() + "/" + login.getBanCountInactive());
                        else if (login.getBanCountActive() != 0 && p.hasPermission("secureban.notifylogin.active"))
                            p.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " User " + player.getName() + " has active bans " + login.getBanCountActive() + "/" + login.getBanCountInactive());
                        if (login.getNote() != null && p.hasPermission("secureban.notifylogin.note")) {
                            p.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " User " + player.getName() + " has " + login.getNoteCount() + " notes - Latest: ");
                            p.sendMessage(ChatColor.WHITE + "[SecureBan] " + ChatColor.RED + login.getNote().getNotes());
                        }
                    }
                }
                if (player.hasPermission("secureban.maintenance")) {
                    Maintenance maintenance = banManager.getRemote().getMaintenance();
                    if (maintenance != null) {
                        if (System.currentTimeMillis() > maintenance.getStartTime() && System.currentTimeMillis() < maintenance.getEndTime()) {
                            player.sendMessage(ChatColor.DARK_PURPLE + maintenance.getMessage());
                        }
                    }
                }
                if (player.hasPermission("secureban.update") && banManager.getRemote().updateVersion())
                    player.sendMessage(ChatColor.WHITE + "[SecureBan] " + ChatColor.GREEN + banManager.getRemote().getUpdateMessage());
            }
        };
        ProxyServer.getInstance().getScheduler().runAsync(ProxyServer.getInstance().getPluginManager().getPlugin("SecureBan"), r);

    }
}
