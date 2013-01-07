package command;

import de.minecraftadmin.secureban.command.HookableBanCommand;
import de.minecraftadmin.secureban.command.LocalBanCommand;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 07.01.13
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class TimeTranslator {

    private static HookableBanCommand cmd;

    @BeforeClass
    public static void startUp(){
        cmd = new LocalBanCommand(null,false);
    }

    @Test
    public void checkExpireCalcDays(){
        Long day1 = cmd.timeTranslater("1d");
        Date start = new Date("01.01.2013");
        Date end = new Date("02.01.2013");
        Assert.assertEquals("One Day",end.getTime()-start.getTime(),day1.longValue());
    }
}
