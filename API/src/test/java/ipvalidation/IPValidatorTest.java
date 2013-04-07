package ipvalidation;

import de.minecraftadmin.api.utils.IPValidator;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 30.03.13
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public class IPValidatorTest {

    @Test
    public void simpleIPTest() {
        Assert.assertTrue(IPValidator.isValid("127.0.0.1"));
        Assert.assertTrue(IPValidator.isValid("255.255.255.255"));
        Assert.assertFalse(IPValidator.isValid("127.0.0"));
        Assert.assertFalse(IPValidator.isValid("128.0.0.0.0"));
    }

    @Test
    public void ipRangeTest() {
        Assert.assertFalse(IPValidator.isValid("256.255.255.255"));
    }

    @Test
    public void ipWithPortTest() {
        Assert.assertFalse(IPValidator.isValid("25.25.25.25:25"));
    }


}
