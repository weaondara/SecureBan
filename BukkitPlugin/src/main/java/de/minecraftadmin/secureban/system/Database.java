package de.minecraftadmin.secureban.system;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.Server;

/**
 * @author BADMAN152
 * create the local database connection
 */
public class Database {

    private String driverClass;
    private String userName;
    private String password;
    private String jdbcUrl;
    private EbeanServer db;

    /**
     * @author BADMAN152
     * create the database if nessesary and return that db object
     * @return
     */
    public EbeanServer getDatabase(){
        if(db==null){
            ServerConfig dbServer = new ServerConfig();
            dbServer.setName("SecureBanDB");
            DataSourceConfig dbConfig = new DataSourceConfig();
            dbConfig.setDriver(driverClass);
            dbConfig.setUsername(userName);
            dbConfig.setPassword(password);
            dbConfig.setUrl(jdbcUrl);
            dbServer.setDataSourceConfig(dbConfig);
            dbServer.setDdlGenerate(true);
            dbServer.setDdlRun(true);
            dbServer.setDefaultServer(false);
            dbServer.setRegister(false);
            dbServer.addClass(Player.class);
            dbServer.addClass(PlayerBan.class);
            dbServer.addClass(Server.class);
            db = EbeanServerFactory.create(dbServer);

        }
        return db;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
}
