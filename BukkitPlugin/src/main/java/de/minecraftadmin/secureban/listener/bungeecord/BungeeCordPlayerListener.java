package de.minecraftadmin.secureban.listener.bungeecord;

import de.minecraftadmin.secureban.listener.PlayerListener;
import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

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

    public BungeeCordPlayerListener(BanManager banManager) {
        super(banManager);
        Bukkit.getMessenger().registerIncomingPluginChannel(Bukkit.getPluginManager().getPlugin("SecureBan"), responseChannel, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Bukkit.getPluginManager().getPlugin("SecureBan"), requestChannel);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase(responseChannel)) {
            String m = new String(message);
            String[] split = m.split(" ");
            if (split.length == 2) {
                boolean show = Boolean.getBoolean(split[1]);
                if (show) {
                    Player p = Bukkit.getPlayer(split[0]);
                    sendNotification(p);
                }
            } else {
                Bukkit.getLogger().severe("Received damaged message from SecureBanWire");
            }
        }
    }

    @Override
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player p = event.getPlayer();
        Bukkit.getScheduler().runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("SecureBan"), new Runnable() {
            @Override
            public void run() {
                p.sendPluginMessage(Bukkit.getPluginManager().getPlugin("SecureBan"), requestChannel, p.getName().getBytes());
                System.out.println("send Message");
            }
        }, 40);

    }
}
