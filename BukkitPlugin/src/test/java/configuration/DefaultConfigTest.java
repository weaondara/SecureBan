package configuration;

import de.minecraftadmin.secureban.SecureBan;
import junit.framework.Assert;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 21.12.12
 * Time: 00:09
 * To change this template use File | Settings | File Templates.
 */
public class DefaultConfigTest {

    @Test
    public void loadableConfig() throws Throwable {
        YamlConfiguration config = new YamlConfiguration();
        config.load(SecureBan.class.getResource("/config.yml").getFile());
        Assert.assertEquals("root", config.get("database.username"));
    }
}
