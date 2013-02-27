package de.minecraftadmin.secureban;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.api.utils.BanAnalyzer;
import de.minecraftadmin.secureban.command.*;
import de.minecraftadmin.secureban.listener.CommandListener;
import de.minecraftadmin.secureban.listener.PlayerListener;
import de.minecraftadmin.secureban.listener.bungeecord.BungeeCordPlayerListener;
import de.minecraftadmin.secureban.system.BanManager;
import de.minecraftadmin.secureban.system.BanSynchronizer;
import de.minecraftadmin.secureban.system.Database;
import de.minecraftadmin.secureban.system.NoteManager;
import de.minecraftadmin.secureban.utils.ConfigNode;
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

    private BanManager banManager;
    private Database db;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        int timeout = this.getConfig().getInt("remote.timeout", RemoteAPIManager.TIMEOUT);
        RemoteAPIManager.TIMEOUT = timeout;
        initBanManager();
        initCommand();
        initListener();
        setLogLevel();
        this.getLogger().info("Running on API Version " + Version.name);
    }

    /**
     * set log level of known loggers
     * .. please add other loggers here if there are new ones :)
     *
     * @author DT
     */
    private void setLogLevel() {
        try {
            Level lvl = Level.parse(getConfig().getString("loglevel"));
            Logger.getLogger("BanManager").setLevel(lvl);
            Logger.getLogger("BanSynchronizer").setLevel(lvl);
            Logger.getLogger("Wire").setLevel(lvl);
        } catch (Exception e) {
            // ignore, keep default levels if level in config is invalid
        }
    }

    public void initListener() {
        PlayerListener playerListener;
        if (this.getConfig().getBoolean("multiserver")) {
            if (this.getConfig().getBoolean("upload.active")) {
                playerListener = new BungeeCordPlayerListener(banManager, this.getConfig().getString("upload.url"));
            } else {
                playerListener = new BungeeCordPlayerListener(banManager);
            }
        } else {
            if (this.getConfig().getBoolean("upload.active")) {
                playerListener = new PlayerListener(banManager, this.getConfig().getString("upload.url"));
            } else {
                playerListener = new PlayerListener(banManager);
            }
        }
        this.getServer().getPluginManager().registerEvents(playerListener, this);
        this.getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this,
                new BanSynchronizer(db, new RemoteAPIManager(this.getConfig().getString("remote.serviceurl"), this.getConfig().getString("remote.apikey")), new BanAnalyzer(this.getConfig().getString("remote.apikey")), this.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false)), 600, 2400);
    }

    private void initBanManager() {
        banManager = new BanManager(db, this.getConfig().getString("remote.serviceurl"), this.getConfig().getString("remote.apikey"));
    }

    private void initCommand() {
        GlobalBanCommand.setCommand(this.getCommand("globalban"));
        LocalBanCommand.setCommand(this.getCommand("localban"));
        TempBanCommand.setCommand(this.getCommand("tempban"));
        boolean useUpload = this.getConfig().getBoolean("upload.active");
        String uploadUrl = this.getConfig().getString("upload.url");
        if (this.getConfig().getBoolean("command.global.active")) {
            this.getCommand("globalban").setExecutor(new GlobalBanCommand(banManager, this.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false)));
            ((HookableBanCommand) this.getCommand("globalban").getExecutor()).setShowUpload(useUpload);
            ((HookableBanCommand) this.getCommand("globalban").getExecutor()).setUploadUrl(uploadUrl);
        }
        if (this.getConfig().getBoolean("command.local.active")) {
            this.getCommand("localban").setExecutor(new LocalBanCommand(banManager, this.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false)));
            ((HookableBanCommand) this.getCommand("localban").getExecutor()).setShowUpload(useUpload);
            ((HookableBanCommand) this.getCommand("localban").getExecutor()).setUploadUrl(uploadUrl);
        }
        if (this.getConfig().getBoolean("command.temp.active")) {
            this.getCommand("tempban").setExecutor(new TempBanCommand(banManager, this.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false)));
            ((HookableBanCommand) this.getCommand("tempban").getExecutor()).setShowUpload(useUpload);
            ((HookableBanCommand) this.getCommand("tempban").getExecutor()).setUploadUrl(uploadUrl);
        }
        if (this.getConfig().getBoolean("command.unban.active")) {
            this.getCommand("unban").setExecutor(new UnBanCommand(banManager));
        }
        if (this.getConfig().getBoolean("command.checkban.active")) {
            this.getCommand("checkban").setExecutor(new CheckBanCommand(banManager));
        }
        this.getCommand("kick").setExecutor(new KickCommand(banManager, this.getConfig().getBoolean(ConfigNode.SAVEKICKTODB.getNode())));
        this.getCommand("note").setExecutor(new NoteCommand(new NoteManager(banManager.getRemote())));

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
        String nameSpace = this.getConfig().getString("database.namespace", null);
        if (nameSpace == null) db = new Database();
        else db = new Database(nameSpace);
        db.setUserName(this.getConfig().getString("database.username"));
        db.setPassword(this.getConfig().getString("database.password"));
        db.setDriverClass(this.getConfig().getString("database.driverclass"));
        db.setJdbcUrl(this.getConfig().getString("database.jdbcurl"));
        return db.injectDatabase(getClassLoader());
    }

    public BanManager getBanManager() {
        return banManager;
    }
}
