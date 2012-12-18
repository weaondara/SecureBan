package de.minecraftadmin.secureban;

import de.minecraftadmin.secureban.system.BanManager;
import de.minecraftadmin.secureban.system.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 17.12.12
 * Time: 18:33
 * To change this template use File | Settings | File Templates.
 */
public class SecureBan extends JavaPlugin {

    private BanManager banManager;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) this.saveDefaultConfig();
        Database db = new Database();
        db.setUserName(this.getConfig().getString("database.username"));
        db.setPassword(this.getConfig().getString("database.password"));
        db.setDriverClass(this.getConfig().getString("database.driverclass"));
        db.setJdbcUrl(this.getConfig().getString("database.jdbcurl"));
        banManager = new BanManager(db, this.getConfig().getString("remote.serviceurl"), this.getConfig().getString("remote.apikey"));
    }

    private void initCommand(){
        if(this.getConfig().getBoolean("command.global.active",false)){

        }
        if(this.getConfig().getBoolean("command.local.active",false)){

        }
        if(this.getConfig().getBoolean("command.temp.active",false)){

        }

    }
}
