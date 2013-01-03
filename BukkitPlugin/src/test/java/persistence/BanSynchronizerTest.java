package persistence;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.utils.BanAnalyzer;
import de.minecraftadmin.secureban.system.BanManager;
import de.minecraftadmin.secureban.system.BanSynchronizer;
import de.minecraftadmin.secureban.system.Database;
import org.junit.Test;
import persistence.Utils.FakeRemoteAPIManager;

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
        db.setDebug(false);
        db.setDriverClass("org.h2.Driver");
        db.setJdbcUrl("jdbc:h2:mem:mysql:DB_CLOSE_DELAY=-1");
        db.injectDatabase(Thread.currentThread().getContextClassLoader());

        RemoteAPIManager remote = new RemoteAPIManager("", "");

        BanSynchronizer sync = new BanSynchronizer(db, remote, new BanAnalyzer(""), false);
        sync.run();

    }

    @Test
    public void checkForCorrectData() {
        Database db = new Database();
        db.setUserName("sa");
        db.setPassword("");
        db.setDebug(false);
        db.setDriverClass("org.h2.Driver");
        db.setJdbcUrl("jdbc:h2:mem:mysql:DB_CLOSE_DELAY=-1");
        db.injectDatabase(Thread.currentThread().getContextClassLoader());

        BanManager banManager = new BanManager(db, "", "");
        //create Testdata
        banManager.tempBan("TempBanUser", "Staff", "Cause I can", 100);
        banManager.globalBan("GlobalBanUser", "Staff", "Cause I can");
        banManager.localBan("LocalBanUser", "Staff", "Cause I can");
        RemoteAPIManager fakeRemote = new FakeRemoteAPIManager("", "");
        BanSynchronizer sync = new BanSynchronizer(db, fakeRemote, new BanAnalyzer(""), false);
        sync.run();
    }
}
