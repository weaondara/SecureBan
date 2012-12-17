package persistence;

import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;
import junit.framework.Assert;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

/**
 * @author BADMAN152
 * simply test the annotations of the peristence entities.
 * just test persist bans
 */
public class SimpleEntityTest {

    private static EntityManagerFactory emf;
    private static EntityManager entityManager;

    @BeforeClass
    public static void startUp(){
        emf = Persistence.createEntityManagerFactory("DB");
        entityManager = emf.createEntityManager();
    }
    @AfterClass
    public static void shutdown(){
        entityManager.close();
        emf.close();
    }
    @Before
    public void createTransaction(){
        entityManager.getTransaction().begin();
    }
    @After
    public void closeTransaction(){
        entityManager.flush();
        entityManager.getTransaction().commit();
        entityManager.clear();
    }
    @Test
    public void createPlayerWithBan()throws Throwable{
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
        Assert.assertNotNull("Persisted Ban",ban.getId());
    }
}
