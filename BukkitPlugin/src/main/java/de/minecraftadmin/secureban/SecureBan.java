package de.minecraftadmin.secureban;

import de.minecraftadmin.secureban.command.GlobalBanCommand;
import de.minecraftadmin.secureban.command.LocalBanCommand;
import de.minecraftadmin.secureban.command.TempBanCommand;
import de.minecraftadmin.secureban.system.BanManager;
import de.minecraftadmin.secureban.system.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 17.12.12
 * Time: 18:33
 * To change this template use File | Settings | File Templates.
 */
public class SecureBan extends JavaPlugin {

    private BanManager banManager;
    private Database db;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        initBanManager();
        initCommand();

    }

    private void initBanManager() {
        banManager = new BanManager(db, this.getConfig().getString("remote.serviceurl"), this.getConfig().getString("remote.apikey"));
    }

    private void initCommand() {
        if (this.getConfig().getBoolean("command.global.active")) {
            this.getCommand("globalban").setExecutor(new GlobalBanCommand(banManager));
        }
        if (this.getConfig().getBoolean("command.local.active")) {
            this.getCommand("localban").setExecutor(new LocalBanCommand(banManager));
        }
        if (this.getConfig().getBoolean("command.temp.active")) {
            this.getCommand("tempban").setExecutor(new TempBanCommand(banManager));
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
}
