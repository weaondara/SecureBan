package webservice;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.*;
import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.ejb.beans.DatabaseService;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.openejb.api.LocalClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import java.util.List;
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

        Maintenance maintenance = new Maintenance();
        maintenance.setStartTime(System.currentTimeMillis() - 50000);
        maintenance.setEndTime(System.currentTimeMillis() + 50000);
        maintenance.setMessage("This is a maintenance message");
        service.persist(maintenance);
        remote = new RemoteAPIManager(serviceURL, "apikey");
        Assert.assertNotNull(remote);
    }

    public API getWebservice() throws Throwable {
        return remote;
    }

    @Test
    public void checkForMaintenance() throws Throwable {
        remote.allowedToJoin("test");
        Assert.assertNotNull("is a maintenance message available", remote.getMaintenance());
    }

    public API getFailedWebservice() throws Throwable {
        RemoteAPIManager manager = new RemoteAPIManager(serviceURL, "blabla");
        return manager;
    }

    @AfterClass
    public static void shutdown() throws Throwable {
        container.close();
    }

    @Test(expected = SoapFault.class)
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
        getWebservice().unBanPlayer("UpdateUnitPlayer", time);
        p = getWebservice().getPlayerBans("UpdateUnitPlayer");
        Assert.assertEquals("has same ban count after an update", count, p.getBans().size());
        for (PlayerBan b : p.getBans()) {
            Assert.assertNotNull("has a expire time", b.getExpired());
            Assert.assertEquals("has now the right expire time", time, b.getExpired().longValue());
        }
    }

    @Test
    public void simpleBanUnban() throws Throwable {
        Player p = getWebservice().getPlayerBans("SimpleBanUser");
        Assert.assertNotNull(p);
        Assert.assertNull(p.getBans());
        PlayerBan ban = new PlayerBan();
        ban.setStaffName("Staff");
        ban.setBanType(BanType.GLOBAL);
        ban.setBanReason("cause i can");
        getWebservice().submitPlayerBans(p.getUserName(), ban);
        p = getWebservice().getPlayerBans("SimpleBanUser");
        Assert.assertEquals("has bans", 1, p.getBans().size());
        Long time = System.currentTimeMillis();
        getWebservice().unBanPlayer(p.getUserName(), time);
        p = getWebservice().getPlayerBans("SimpleBanUser");
        Assert.assertEquals("has bans after update", 1, p.getBans().size());
        Assert.assertEquals("has right expire date", time, p.getBans().iterator().next().getExpired());
    }

    @Test
    public void checkLoginNotification() throws Throwable {
        Login l = getWebservice().allowedToJoin("JUnitLoginUser");
        Assert.assertNotNull(l);

    }

    @Test
    public void noteToPlayer() throws Throwable {
        Note n = new Note();
        n.setType(NoteType.INFO);
        n.setNotes("this is a note for a player");
        getWebservice().sumitPlayerNote("NoteableUser", n);
        List<Note> notes = getWebservice().getPlayerNote("NoteableUser");
        Assert.assertNotNull(notes);
        Assert.assertNotNull(notes.get(0).getId());
        n = notes.get(0);
        getWebservice().deletePlayerNote("NoteableUser", n.getId());
        notes = getWebservice().getPlayerNote("NoteableUser");
        Assert.assertNull(notes);
    }
}
