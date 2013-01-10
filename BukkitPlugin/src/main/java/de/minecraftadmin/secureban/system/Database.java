package de.minecraftadmin.secureban.system;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BADMAN152
 *         create the local database connection
 */
public class Database {

    private String driverClass;
    private String userName;
    private String password;
    private String jdbcUrl;
    private EbeanServer db;
    private boolean debug = false;
    private final NameSpace nameSpace;

    public Database() {
        nameSpace = null;
    }

    public Database(String nameSpace) {
        this.nameSpace = new NameSpace(nameSpace);
    }

    /**
     * @return
     * @author BADMAN152
     * return that db object
     */
    public EbeanServer getDatabase() {
        return db;
    }

    /**
     * @param cl
     * @return
     * @author BADMAN152
     * bukkit use more then one classloader. database, bukkitsystem and plugins each of them
     * have his own classloader. so i have to inject/load the classes for the database in the
     * right classloader. its kind a bit tricky.
     */
    public List<Class<?>> injectDatabase(ClassLoader cl) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(Server.class);
        classes.add(Player.class);
        classes.add(PlayerBan.class);

        ServerConfig dbServer = new ServerConfig();
        dbServer.setDefaultServer(true);
        dbServer.setRegister(true);
        dbServer.setClasses(classes);
        dbServer.setName("SecureBan");
        if (nameSpace != null) dbServer.setNamingConvention(nameSpace);
        DataSourceConfig ds = dbServer.getDataSourceConfig();
        ds.setUsername(userName);
        ds.setPassword(password);
        ds.setHeartbeatSql("SELECT 1");
        ds.setUrl(jdbcUrl);
        ds.setDriver(driverClass);
        dbServer.setDebugSql(debug);
        dbServer.setLoggingToJavaLogger(debug);

        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
        db = EbeanServerFactory.create(dbServer);
        Thread.currentThread().setContextClassLoader(previous);
        try {
            //recognize the initialized database
            db.find(Player.class).findList();
        } catch (Exception e) {
            // database not jet initialized! create tables etc
            SpiEbeanServer serv = (SpiEbeanServer) db;
            DdlGenerator gen = serv.getDdlGenerator();
            gen.runScript(false, gen.generateCreateDdl());
        }
        return classes;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
