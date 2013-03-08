package de.minecraftadmin.secureban.listener.bungeecord;

import de.minecraftadmin.secureban.listener.PlayerListener;
import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 23.02.13
 * Time: 23:21
 * To change this template use File | Settings | File Templates.
 */
public class BungeeCordPlayerListener extends PlayerListener implements PluginMessageListener {

    private static final String requestChannel = "SecureBanReq";
    private static final String responseChannel = "SecureBanResp";

    public BungeeCordPlayerListener(BanManager banManager, String banURL) {
        super(banManager, banURL);
        Bukkit.getMessenger().registerIncomingPluginChannel(Bukkit.getPluginManager().getPlugin("SecureBan"), responseChannel, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Bukkit.getPluginManager().getPlugin("SecureBan"), requestChannel);
    }

    public BungeeCordPlayerListener(BanManager banManager) {
        this(banManager, null);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase(responseChannel)) {
            String m = new String(message);
            String[] split = m.split(" ");
            if (split.length == 2) {
                boolean show = Boolean.valueOf(split[1]);
                if (show) {
                    Player p = Bukkit.getPlayer(split[0]);
                    sendNotification(p);
                }
            } else {
                Logger.getLogger("Wire").severe("Received damaged message from SecureBanWire (Raw: " + m + ")");
            }
        }
    }

    @Override
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player p = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("SecureBan"), new Runnable() {
            @Override
            public void run() {
                p.sendPluginMessage(Bukkit.getPluginManager().getPlugin("SecureBan"), requestChannel, p.getName().getBytes());
            }
        });

    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLoginEvent(PlayerLoginEvent event) {
        super.onLoginEvent(event);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
