package bungee.config;

import de.minecraftadmin.secureban.wire.util.Configuration;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.03.13
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationTest {

    @Test
    public void loadConfig() throws Throwable {
        Configuration config = new Configuration(new File(this.getClass().getResource("/").getPath()));
        Assert.assertTrue("simple node", config.getBoolean("simple"));
        Assert.assertTrue("sub simple node", config.getBoolean("sub.node"));
    }
}
