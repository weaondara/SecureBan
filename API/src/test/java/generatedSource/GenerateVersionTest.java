package generatedSource;

import de.minecraftadmin.api.generated.Version;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 02.01.13
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public class GenerateVersionTest {

    @Test
    public void versionCheck() {
        System.out.println(Version.name);
        Assert.assertFalse(Version.name.equals("${project.version}-${BUILD_NUMBER}"));
    }
}
