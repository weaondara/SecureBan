package persistence;

import de.minecraftadmin.api.entity.*;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

/**
 * @author BADMAN152
 *         simply test the annotations of the peristence entities.
 *         just test persist bans
 */
public class SimpleEntityTest {

    private static Logger LOG = Logger.getLogger("PersistenceTest");
    private static EntityManagerFactory emf;
    private static EntityManager entityManager;

    @BeforeClass
    public static void startUp() {
        emf = Persistence.createEntityManagerFactory("DB");
        entityManager = emf.createEntityManager();
    }

    @AfterClass
    public static void shutdown() {
        entityManager.close();
        emf.close();
    }

    @Before
    public void createTransaction() {
        entityManager.getTransaction().begin();
    }

    @After
    public void closeTransaction() {
        entityManager.flush();
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @Test
    public void createPlayerWithBan() throws Throwable {
        LOG.info("add First Ban to Player");
        Player player = new Player();
        player.setUserName("JUnitUser");
        PlayerBan ban = new PlayerBan();
        ban.setStaffName("StaffJUnitUser");
        ban.setBanReason("Cause I Can");
        ban.setBanType(BanType.GLOBAL);
        ban.setSaveState(SaveState.SAVED);
        Set<PlayerBan> bans = new HashSet<PlayerBan>();
        bans.add(ban);
        player.setBans(bans);
        entityManager.persist(player);
        Assert.assertNotNull("Persisted Player", player.getId());
        Assert.assertNotNull("Persisted Ban", ban.getId());
    }

    @Test
    public void createSecondBan() throws Throwable {
        LOG.info("Add Second Ban to player");
        Player p = entityManager.createQuery("SELECT p FROM Player p WHERE p.userName=\'JUnitUser\'", Player.class).getSingleResult();
        Assert.assertNotNull(p);
        PlayerBan ban2 = new PlayerBan();
        ban2.setBanType(BanType.LOCAL);
        ban2.setSaveState(SaveState.SAVED);
        ban2.setStaffName("StaffJUnitUser");
        ban2.setBanReason("Cause i can, too");
        p.getBans().add(ban2);
        entityManager.persist(p);
        Assert.assertNotNull("New Ban persisted", ban2.getId());
        p = entityManager.createQuery("SELECT p FROM Player p WHERE p.userName=\'JUnitUser\'", Player.class).getSingleResult();
        Assert.assertEquals("Has now 2 Bans", 2, p.getBans().size());
    }

    @Test
    public void createMaintenanceMessage() {
        Maintenance m = new Maintenance();
        m.setStartTime(System.currentTimeMillis());
        m.setEndTime(System.currentTimeMillis());
        m.setMessage("test message");
        entityManager.persist(m);
        Assert.assertNotNull(m.getId());
    }
}
