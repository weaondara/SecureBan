package persistence;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.secureban.system.BanManager;
import de.minecraftadmin.secureban.system.Database;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 18.12.12
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public class BanManagerTest {

    private static BanManager banManager;
    private static Database db;

    @BeforeClass
    public static void startup() {
        db = new Database();
        db.setUserName("sa");
        db.setPassword("");
        db.setDebug(true);
        db.setDriverClass("org.h2.Driver");
        db.setJdbcUrl("jdbc:h2:mem:mysql:DB_CLOSE_DELAY=-1");
        db.injectDatabase(Thread.currentThread().getContextClassLoader());
        banManager = new BanManager(db, "", "");
    }

    @Test
    public void CleanUser() {
        Player p = banManager.getAllBansOfPlayer("JUnitTestUser");
        Assert.assertNotNull("Got an player object", p);
        Assert.assertNull("received player doesnt has bans", p.getBans());
    }

    @Test
    public void getBannedPlayer() {
        banManager.localBan("JUnitUser", "StaffJUnitUser", "Cause I can");
        Player p = banManager.getAllBansOfPlayer("JUnitUser");
        Assert.assertNotNull("Got an player object", p);
        Assert.assertNotNull("banned Player has bans", p.getBans());
        Assert.assertFalse("Player is not allowed to join cause local ban", banManager.allowedToJoin("JUnitUser"));
    }

    @Test
    public void getExpiredBannedPlayer() {
        banManager.tempBan("JUnitTempUser", "StaffJUnitUser", "Cause I can", -1);
        Player p = banManager.getAllBansOfPlayer("JUnitTempUser");
        Assert.assertNotNull("Got an player object", p);
        Assert.assertNotNull("banned Player has bans", p.getBans());
        Assert.assertTrue("Player is allowed to join cause expired temp ban", banManager.allowedToJoin("JUnitTempUser"));
    }

    @Test
    public void MultiBannedPlayer() {
        banManager.localBan("MultiPlayer", "StaffJUnitUser", "Cause I Can");
        banManager.globalBan("MultiPlayer", "StaffJUnitUser", "Cause I Can");
        banManager.localBan("MultiPlayer", "StaffJUnitUser", "Cause I Can");
        banManager.tempBan("MultiPlayer", "StaffJUnitUser", "Cause I Can", 600000);
        Assert.assertNotNull("Got an player object", banManager.getAllBansOfPlayer("MultiPlayer"));
        Assert.assertNotNull("banned Player has bans", banManager.getAllBansOfPlayer("MultiPlayer").getBans());
        Assert.assertEquals("banned player has multiple bans", 4, banManager.getAllBansOfPlayer("MultiPlayer").getBans().size());
        Assert.assertEquals("banned player has multiple active bans", 4, banManager.getActiveBansOfPlayer("MultiPlayer").getBans().size());
    }

    @Test
    public void unbanBannedPlayer() {
        banManager.localBan("MultiPlayer", "StaffJUnitUser", "Cause I Can");
        banManager.globalBan("MultiPlayer", "StaffJUnitUser", "Cause I Can");
        banManager.tempBan("MultiPlayer", "StaffJUnitUser", "Cause I Can", 600000);
        Assert.assertFalse("have bans so he cant connect", banManager.allowedToJoin("MultiPlayer"));
        banManager.unban("MultiPlayer");
        Assert.assertTrue("no longer has bans so he can connect", banManager.allowedToJoin("MultiPlayer"));
    }

    @Test
    public void unbanSingleLocalBan() {
        banManager.localBan("LocalBanUser", "Staff", "Cause i Can");
        Player player = banManager.getAllBansOfPlayer("LocalBanUser");
        Assert.assertEquals("has one ban", 1, player.getBans().size());
        banManager.unban("LocalBanUser");
        player = banManager.getAllBansOfPlayer("LocalBanUser");
        Assert.assertEquals("has one ban after unban", 1, player.getBans().size());
    }
}
