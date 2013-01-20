package persistence;

import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.jaxws.Login;
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
        Assert.assertEquals("received player doesnt has bans", 0, p.getBans().size());
        Assert.assertNotNull("has no inactive bans", banManager.allowedToJoin("JUnitTestUser", true).getBanCountInactive());
    }

    @Test
    public void getBannedPlayer() {
        banManager.localBan("JUnitUser", "StaffJUnitUser", "Cause I can");
        Player p = banManager.getAllBansOfPlayer("JUnitUser");
        Assert.assertNotNull("Got an player object", p);
        Assert.assertNotNull("banned Player has bans", p.getBans());
        Assert.assertEquals("has 1 active ban", 1, banManager.getActiveBansCountOfPlayer("JUnitUser"));
        Assert.assertFalse("Player is not allowed to join cause local ban", banManager.allowedToJoin("JUnitUser", false).isAllowed());
    }

    @Test
    public void getExpiredBannedPlayer() {
        banManager.tempBan("JUnitTempUser", "StaffJUnitUser", "Cause I can", -1);
        Player p = banManager.getAllBansOfPlayer("JUnitTempUser");
        Assert.assertNotNull("Got an player object", p);
        Assert.assertNotNull("banned Player has bans", p.getBans());
        Assert.assertTrue("Player is allowed to join cause expired temp ban", banManager.allowedToJoin("JUnitTempUser", false).isAllowed());
    }

    @Test
    public void MultiBannedPlayer() {
        banManager.localBan("MultiPlayer", "StaffJUnitUser", "Cause I Can");
        banManager.globalBan("MultiPlayer", "StaffJUnitUser", "Cause I Can");
        banManager.localBan("MultiPlayer", "StaffJUnitUser", "Cause I Can second time");
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
        Assert.assertFalse("have bans so he cant connect", banManager.allowedToJoin("MultiPlayer", false).isAllowed());
        banManager.unban("MultiPlayer");
        Assert.assertTrue("no longer has bans so he can connect", banManager.allowedToJoin("MultiPlayer", false).isAllowed());
        Assert.assertNotNull("Has Notification after login", banManager.allowedToJoin("MultiPlayer", true).getBanCountInactive());
    }

    @Test
    public void unbanSingleLocalBan() {
        banManager.localBan("LocalBanUser", "Staff", "Cause i Can");
        Player player = banManager.getAllBansOfPlayer("LocalBanUser");
        Assert.assertEquals("has one ban", 1, player.getBans().size());
        banManager.unban("LocalBanUser");
        player = banManager.getAllBansOfPlayer("LocalBanUser");
        Assert.assertEquals("has one ban after unban", 1, player.getBans().size());
        Assert.assertEquals("has after login a notification", 1, banManager.allowedToJoin("LocalBanUser", true).getBanCountInactive().intValue());
    }

    @Test
    public void globalBanSetsStart() {
        String testuser = "globalBanSetsStart";
        long timeA = System.currentTimeMillis();
        banManager.globalBan(testuser, "Staff", "Cause i Can");
        long timeB = System.currentTimeMillis();
        Player player = banManager.getAllBansOfPlayer(testuser);
        Assert.assertEquals("has one ban", 1, player.getBans().size());
        PlayerBan ban = player.getBans().iterator().next();
        Assert.assertNotNull("start time set", ban.getStart());
        Assert.assertTrue("start set to now", (ban.getStart() >= timeA && ban.getStart() <= timeB));
    }

    @Test
    public void localBanSetsStart() {
        String testuser = "localBanSetsStart";
        long timeA = System.currentTimeMillis();
        banManager.localBan(testuser, "Staff", "Cause i Can");
        long timeB = System.currentTimeMillis();
        Player player = banManager.getAllBansOfPlayer(testuser);
        Assert.assertEquals("has one ban", 1, player.getBans().size());
        PlayerBan ban = player.getBans().iterator().next();
        Assert.assertNotNull("start time set", ban.getStart());
        Assert.assertTrue("start set to now", (ban.getStart() >= timeA && ban.getStart() <= timeB));
    }

    @Test
    public void tempBanSetsStart() {
        String testuser = "tempBanSetsStart";
        long timeA = System.currentTimeMillis();
        banManager.tempBan(testuser, "StaffJUnitUser", "Cause I Can", 600000);
        long timeB = System.currentTimeMillis();
        Player player = banManager.getAllBansOfPlayer(testuser);
        Assert.assertEquals("has one ban", 1, player.getBans().size());
        PlayerBan ban = player.getBans().iterator().next();
        Assert.assertNotNull("start time set", ban.getStart());
        Assert.assertTrue("start set to now", (ban.getStart() >= timeA && ban.getStart() <= timeB));
    }

    @Test
    public void getRightBanOnJoin() throws Throwable {
        banManager.tempBan("RightBannedUser", "Staff", "Cause I Can", 0);
        Thread.sleep(500);
        banManager.localBan("RightBannedUser", "Staff", "Cause I Can");
        Login l = banManager.allowedToJoin("RightBannedUser", false);
        Assert.assertEquals("Local ban message at join", BanType.LOCAL, l.getBan().getBanType());
        Assert.assertFalse(l.isAllowed());
    }

    @Test
    public void dontShowKickOnLogin() throws Throwable {
        banManager.kick("KickTestUser", "Staff", " kick kick");
        banManager.localBan("KickTestUser", "Staff", "localban");
        Assert.assertEquals(1, banManager.allowedToJoin("KickTestUser", false).getBanCountInactive().intValue());
    }
}
