package webservice;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.*;
import de.minecraftadmin.ejb.beans.DatabaseService;
import org.apache.openejb.api.LocalClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Properties;

/**
 *
 */
@LocalClient
public class WebserviceTest {
    private static final String serviceURL = "http://127.0.0.1:4204/localhost/SecureBan/BanService";
    private static EJBContainer container;
    private static RemoteAPIManager remote;

    @BeforeClass
    public static void startup() throws Throwable {
        Properties properties = new Properties();
        properties.setProperty("httpejbd.print", "true");
        properties.setProperty("httpejbd.indent.xml", "true");
        properties.setProperty("openejb.embedded.remotable", "true");
        container = EJBContainer.createEJBContainer(properties);
        DatabaseService service = (DatabaseService) container.getContext().lookup("java:global/localhost/SecureBan/DatabaseService");
        Server server = new Server();
        server.setServerName("JUnitServer");
        server.setApiKey("apikey");
        service.persist(server);
        remote = new RemoteAPIManager(serviceURL, "apikey");
        Assert.assertNotNull(remote.getRemoteAPI());
    }

    public API getWebservice() throws Throwable {
        return remote.getRemoteAPI();
    }

    public API getFailedWebservice() throws Throwable {
        RemoteAPIManager manager = new RemoteAPIManager(serviceURL, "blabla");
        return manager.getRemoteAPI();
    }

    @AfterClass
    public static void shutdown() throws Throwable {
        container.close();
    }

    @Test(expected = SOAPFaultException.class)
    public void notAuthenticated() throws Throwable {
        getFailedWebservice().submitPlayerBans("", null);
    }

    @Test
    public void getCleanUser() throws Throwable {
        Player p = getWebservice().getPlayerBans("JUnitTestUser");
        Assert.assertNotNull(p);
        Assert.assertNull("Has no bans", p.getBans());
    }

    @Test
    public void getNewAddedBan() throws Throwable {
        PlayerBan ban = new PlayerBan();
        ban.setBanReason("Cause I Can");
        ban.setStaffName("StaffJUnitTestUser");
        ban.setBanType(BanType.GLOBAL);
        ban.setSaveState(SaveState.SAVED);
        getWebservice().submitPlayerBans("JUnitTestUser", ban);
        Player p = getWebservice().getPlayerBans("JUnitTestUser");
        Assert.assertNotNull(p);
        Assert.assertNotNull("Has bans", p.getBans());
        Assert.assertEquals("Has one ban", 1, p.getBans().size());
    }

    @Test
    public void getNewSecondBan() throws Throwable {
        PlayerBan ban = new PlayerBan();
        ban.setBanReason("Cause I Can, too");
        ban.setStaffName("StaffJUnitTestUser");
        ban.setBanType(BanType.GLOBAL);
        ban.setSaveState(SaveState.SAVED);
        getWebservice().submitPlayerBans("JUnitTestUser", ban);
        Player p = getWebservice().getPlayerBans("JUnitTestUser");
        Assert.assertNotNull(p);
        Assert.assertNotNull("Has bans", p.getBans());
        Assert.assertEquals("Has two ban", 2, p.getBans().size());
    }

    @Test
    public void updateBan() throws Throwable {
        PlayerBan ban = new PlayerBan();
        ban.setBanType(BanType.GLOBAL);
        ban.setBanReason("Cause I can");
        ban.setStaffName("Staff");

        getWebservice().submitPlayerBans("UpdateUnitPlayer", ban);
        Player p = getWebservice().getPlayerBans("UpdateUnitPlayer");
        int count = p.getBans().size();
        long time = System.currentTimeMillis();
        for (PlayerBan b : p.getBans()) {
            b.setExpired(time);
            getWebservice().updatePlayerBans("UpdateUnitPlayer", b);
        }
        p = getWebservice().getPlayerBans("UpdateUnitPlayer");
        Assert.assertEquals("has same ban count after an update", count, p.getBans().size());
        for (PlayerBan b : p.getBans()) {
            Assert.assertNotNull("has a expire time", b.getExpired());
            Assert.assertEquals("has now the right expire time", time, b.getExpired().longValue());
        }
    }
}
