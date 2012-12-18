package webservice;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import java.util.Properties;

/**
 *
 */
public class WebserviceTest {
    private static final String serviceURL = "http://127.0.0.1:4204/localhost/SecureBan/BanService";
    private static EJBContainer container;
    private static RemoteAPIManager remote;

    @BeforeClass
    public static void startup()throws Throwable{
        Properties properties = new Properties();
        properties.setProperty("httpejbd.print", "true");
        properties.setProperty("httpejbd.indent.xml", "true");
        properties.setProperty("openejb.embedded.remotable", "true");
        container = EJBContainer.createEJBContainer(properties);
        remote = new RemoteAPIManager(serviceURL,"apikey");
        Assert.assertNotNull(remote.getRemoteAPI());
    }

    public API getWebservice() throws Throwable{
        return remote.getRemoteAPI();
    }

    @AfterClass
    public static void shutdown() throws Throwable{
        container.close();
    }

    @Test
    public void getCleanUser()throws Throwable{
        Player p = getWebservice().getPlayerBans("JUnitTestUser");
        Assert.assertNotNull(p);
        Assert.assertNull("Has no bans", p.getBans());
    }

    @Test
    public void getNewAddedBan()throws Throwable{
        PlayerBan ban = new PlayerBan();
        ban.setBanReason("Cause I Can");
        ban.setStaffName("StaffJUnitTestUser");
        ban.setBanType(BanType.GLOBAL);
        ban.setSaveState(SaveState.SAVED);
        getWebservice().submitPlayerBans("JUnitTestUser",ban);
        Player p = getWebservice().getPlayerBans("JUnitTestUser");
        Assert.assertNotNull(p);
        Assert.assertNotNull("Has bans", p.getBans());
        Assert.assertEquals("Has one ban",1,p.getBans().size());
    }

    @Test
    public void getNewSecondBan() throws Throwable{
        PlayerBan ban = new PlayerBan();
        ban.setBanReason("Cause I Can, too");
        ban.setStaffName("StaffJUnitTestUser");
        ban.setBanType(BanType.GLOBAL);
        ban.setSaveState(SaveState.SAVED);
        getWebservice().submitPlayerBans("JUnitTestUser",ban);
        Player p = getWebservice().getPlayerBans("JUnitTestUser");
        Assert.assertNotNull(p);
        Assert.assertNotNull("Has bans", p.getBans());
        Assert.assertEquals("Has two ban",2,p.getBans().size());
    }
}
