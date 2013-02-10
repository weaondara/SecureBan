package de.minecraftadmin.secureban.jumpstring;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
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

    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getTag().equalsIgnoreCase("SecureBanWire")) {
            String userName = new String(event.getData());
            if (cache.contains(userName))
                ((ServerInfo) event.getSender()).sendData(event.getTag(), "hide".getBytes());
            else {
                ((ServerInfo) event.getSender()).sendData(event.getTag(), "show".getBytes());
                cache.add(userName);
            }
        }
    }

    public void onPlayerDisconnect() {

    }


}
