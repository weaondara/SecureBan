package de.minecraftadmin.secureban;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.secureban.command.*;
import de.minecraftadmin.secureban.listener.CommandListener;
import de.minecraftadmin.secureban.listener.PlayerListener;
import de.minecraftadmin.secureban.system.BanManager;
import de.minecraftadmin.secureban.system.BanSynchronizer;
import de.minecraftadmin.secureban.system.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 17.12.12
 * Time: 18:33
 * To change this template use File | Settings | File Templates.
 */
public class SecureBan extends JavaPlugin {

	private static SecureBan sInstance;
	
    private BanManager banManager;
    private Database db;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable () {
    	sInstance=this;
        initBanManager();
        initCommand();
        initListener();
        setLogLevel();
    }

    /**
     * set log level of known loggers
     * .. please add other loggers here if there are new ones :)
     * @author DT
     */
	private void setLogLevel() {
		try{
			Level lvl = Level.parse(getConfig().getString("loglevel"));
			Logger.getLogger("BanManager").setLevel(lvl);
			Logger.getLogger("BanSynchronizer").setLevel(lvl);
		}catch (Exception e) {
			// ignore, keep default levels if level in config is invalid
		}
	}

	public void initListener() {
		this.getServer().getPluginManager().registerEvents(new PlayerListener(banManager), this);
		this.getServer().getPluginManager().registerEvents(new CommandListener(this), this);
		this.getServer().getScheduler().runTaskTimerAsynchronously(this, new BanSynchronizer(db, new RemoteAPIManager(this.getConfig().getString("remote.serviceurl"), this.getConfig().getString("remote.apikey"))), 600, 2400);
	}

    private void initBanManager() {
        banManager = new BanManager(db, this.getConfig().getString("remote.serviceurl"), this.getConfig().getString("remote.apikey"));
    }

    private void initCommand() {
        GlobalBanCommand.setCommand(this.getCommand("globalban"));
        LocalBanCommand.setCommand(this.getCommand("localban"));
        TempBanCommand.setCommand(this.getCommand("tempban"));
        if (this.getConfig().getBoolean("command.global.active")) {
            this.getCommand("globalban").setExecutor(new GlobalBanCommand(banManager));
        }
        if (this.getConfig().getBoolean("command.local.active")) {
            this.getCommand("localban").setExecutor(new LocalBanCommand(banManager));
        }
        if (this.getConfig().getBoolean("command.temp.active")) {
            this.getCommand("tempban").setExecutor(new TempBanCommand(banManager));
        }
        if (this.getConfig().getBoolean("command.unban.active")) {
            this.getCommand("unban").setExecutor(new UnBanCommand(banManager));
        }
        if (this.getConfig().getBoolean("command.checkban.active")) {
            this.getCommand("checkban").setExecutor(new CheckBanCommand(banManager));
        }

    }

    /**
     * @return
     * @author BADMAN152
     * the database have to initialize while the plugin initialize pluginstuff. since bukkit
     * does not allow overriding some methods it has to be done here
     */
    @Override
    public List<Class<?>> getDatabaseClasses() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) this.saveDefaultConfig();
        db = new Database();
        db.setUserName(this.getConfig().getString("database.username"));
        db.setPassword(this.getConfig().getString("database.password"));
        db.setDriverClass(this.getConfig().getString("database.driverclass"));
        db.setJdbcUrl(this.getConfig().getString("database.jdbcurl"));
        return db.injectDatabase(getClassLoader());
    }

    public BanManager getBanManager() {
        return banManager;
    }
    
    /**
     * @return Singleton instance
     * @author DT
     */
    public static SecureBan getInstance(){
    	return sInstance;
    }
}
