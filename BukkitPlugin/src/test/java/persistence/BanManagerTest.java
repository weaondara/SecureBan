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
    public static void startup(){
        db = new Database();
        db.setUserName("sa");
        db.setPassword("");
        db.setDebug(true);
        db.setDriverClass("org.h2.Driver");
        db.setJdbcUrl("jdbc:h2:mem:mysql:DB_CLOSE_DELAY=-1");
        banManager = new BanManager(db,"","");
    }

    @Test
    public void getCleanUser(){
        Player p = banManager.getBansOfPlayer("JUnitTestUser");
        Assert.assertNotNull("Got an player object",p);
        Assert.assertNull("received player doesnt has bans", p.getBans());
    }

    @Test
    public void getBannedPlayer(){
        banManager.localBan("JUnitUser","StaffJUnitUser","Cause I can");
        Player p = banManager.getBansOfPlayer("JUnitUser");
        Assert.assertNotNull("Got an player object",p);
        Assert.assertNotNull("banned Player has bans",p.getBans());
    }
}
