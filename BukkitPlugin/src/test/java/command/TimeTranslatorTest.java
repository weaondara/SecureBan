package command;

import de.minecraftadmin.secureban.command.HookableBanCommand;
import de.minecraftadmin.secureban.command.LocalBanCommand;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 07.01.13
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class TimeTranslatorTest {

    private static HookableBanCommand cmd;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    @BeforeClass
    public static void startUp(){
        cmd = new LocalBanCommand(null,false);
    }

    @Test
    public void checkExpireCalcDays() throws Throwable{
        Long day1 = cmd.timeTranslater("1d");
        Date start = DATE_FORMAT.parse("01.01.2013");
        Date end = DATE_FORMAT.parse("02.01.2013");
        Assert.assertEquals("One Day",end.getTime()-start.getTime(),day1.longValue());
    }
}
