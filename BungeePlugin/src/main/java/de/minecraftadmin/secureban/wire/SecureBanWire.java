package de.minecraftadmin.secureban.wire;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 09.02.13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class SecureBanWire extends Plugin {
    @Override
    public void onDisable() {
        super.onDisable();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(new WiredListener());
    }
}
