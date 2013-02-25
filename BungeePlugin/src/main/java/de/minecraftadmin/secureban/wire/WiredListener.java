package de.minecraftadmin.secureban.wire;

import com.google.common.eventbus.Subscribe;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 09.02.13
 * Time: 23:12
 * To change this template use File | Settings | File Templates.
 */
public class WiredListener implements Listener {
    private final List<String> cache = new ArrayList<String>();

    private static final String requestChannel = "SecureBanReq";
    private static final String responseChannel = "SecureBanResp";

    public WiredListener() {
        ProxyServer.getInstance().registerChannel(requestChannel);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        // handle messages from secureban
        if (event.getTag().equalsIgnoreCase(requestChannel)) {
            String userName = new String(event.getData());
            if (cache.contains(userName))
                ((ServerConnection) event.getSender()).sendData(responseChannel, (userName + " " + false).getBytes());
            else {
                ((ServerConnection) event.getSender()).sendData(responseChannel, (userName + " " + true).getBytes());
                cache.add(userName);
            }
        }
    }

    @Subscribe
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        cache.remove(event.getPlayer().getName());
    }

    @Subscribe
    public void onPlayerConnect(ServerConnectedEvent event) {
        event.getServer().sendData("REGISTER", requestChannel.getBytes());
    }
}
