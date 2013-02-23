package de.minecraftadmin.secureban.listener.bungeecord;

import de.minecraftadmin.secureban.listener.PlayerListener;
import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    public BungeeCordPlayerListener(BanManager banManager) {
        super(banManager);
        Bukkit.getMessenger().registerIncomingPluginChannel(Bukkit.getPluginManager().getPlugin("SecureBan"), "SecureBanWire", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Bukkit.getPluginManager().getPlugin("SecureBan"), "SecureBanWire");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("SecureBanWire")) {
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendPluginMessage(Bukkit.getPluginManager().getPlugin("SecureBan"), "SecureBanWire", event.getPlayer().getName().getBytes());
    }
}
