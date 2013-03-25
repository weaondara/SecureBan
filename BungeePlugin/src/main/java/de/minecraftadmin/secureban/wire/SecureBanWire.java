package de.minecraftadmin.secureban.wire;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.secureban.system.BanManager;
import de.minecraftadmin.secureban.system.BanSynchronizer;
import de.minecraftadmin.secureban.system.Database;
import de.minecraftadmin.secureban.system.NoteManager;
import de.minecraftadmin.secureban.wire.command.*;
import de.minecraftadmin.secureban.wire.listener.PlayerListener;
import de.minecraftadmin.secureban.wire.util.Configuration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 09.02.13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class SecureBanWire extends Plugin {

    private final static Logger LOG = Logger.getLogger("SecureBan");
    private Configuration config;
    private static SecureBanWire instance;

    public static SecureBanWire getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        super.onDisable();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            config = new Configuration(this.getDataFolder());
        } catch (IOException e) {
            e.printStackTrace();
            LOG.severe("Could not load Config file");
            this.onDisable();
            return;
        }
        Database db = new Database();
        db.setUserName(config.getString("database.username"));
        db.setPassword(config.getString("database.password"));
        db.setJdbcUrl(config.getString("database.jdbcurl"));
        db.setDriverClass(config.getString("database.driverclass"));
        db.injectDatabase(Thread.currentThread().getContextClassLoader());
        BanManager banManager = new BanManager(db, config.getString("remote.serviceurl"), config.getString("remote.apikey"));
        RemoteAPIManager remote = new RemoteAPIManager(config.getString("remote.serviceurl"), config.getString("remote.apikey"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GlobalBanCommand(banManager));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new LocalBanCommand(banManager));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TempBanCommand(banManager));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CheckBanCommand(banManager));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new NoteCommand(new NoteManager(remote)));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerListener(banManager, config.getString("upload.url")));
        ProxyServer.getInstance().getScheduler().schedule(this, new BanSynchronizer(db, remote, banManager, false), 2, 2, TimeUnit.MINUTES);
        LOG.info("Enabled SecureBan successfully on API Version " + Version.name);
    }
}
