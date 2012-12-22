package persistence;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.secureban.system.BanSynchronizer;
import de.minecraftadmin.secureban.system.Database;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 22.12.12
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */
public class BanSynchronizerTest {

    /**
     * simple test for exceptions while sync
     */
    @Test
    public void synchronizeBans() {
        Database db = new Database();
        db.setUserName("sa");
        db.setPassword("");
        db.setDebug(true);
        db.setDriverClass("org.h2.Driver");
        db.setJdbcUrl("jdbc:h2:mem:mysql:DB_CLOSE_DELAY=-1");
        db.injectDatabase(Thread.currentThread().getContextClassLoader());

        RemoteAPIManager remote = new RemoteAPIManager("", "");

        BanSynchronizer sync = new BanSynchronizer(db, remote);
        sync.run();

    }
}
