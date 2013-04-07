package webservice;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.Server;
import de.minecraftadmin.api.exception.NotMalformedIPException;
import de.minecraftadmin.api.jaxws.Login;
import de.minecraftadmin.ejb.beans.DatabaseService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 29.03.13
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class WebserviceIPTest {

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
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("name", "JUnitServer");
        Server server = service.getSingleResult(Server.class, "SELECT s FROM Server s WHERE s.serverName=:name", parameter);
        if (server == null) {
            server = new Server();
            server.setServerName("JUnitServer");
            server.setApiKey("apikey");
            service.persist(server);
        }

        remote = new RemoteAPIManager(serviceURL, "apikey");
        Assert.assertNotNull(remote);
    }

    public API getWebservice() throws Throwable {
        return remote;
    }

    @Test(expected = NotMalformedIPException.class)
    public void joinWithInvalidIp() throws Throwable {
        getWebservice().allowedToJoin("test", "test");
    }

    @Test
    public void joinWithValidIp() throws Throwable {
        Login l = getWebservice().allowedToJoin("JUnitTest", "127.0.0.1");
        Assert.assertNotNull("Login Object is not null", l);
        Assert.assertNotNull("Alt Accountslist is not null", l.getAltAccountName());
        Assert.assertTrue("No Alt Accounts", l.getAltAccountName().isEmpty());
    }

    @Test
    public void getAltAccountsOnLogin() throws Throwable {
        getWebservice().allowedToJoin("AltAccountUser", "128.0.0.1");
        Login l = getWebservice().allowedToJoin("JUnitTest", "128.0.0.1");
        Assert.assertNotNull("Login Object ist null", l);
        Assert.assertFalse("Has Alt Accounts", l.getAltAccountName().isEmpty());
    }

}
