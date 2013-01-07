package sort;

import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.utils.BanSorter;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 22.12.12
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class BanSortTest {

    @Test
    public void sortBans() {
        List<PlayerBan> data = new ArrayList<PlayerBan>();
        PlayerBan ban1 = new PlayerBan();
        ban1.setBanType(BanType.LOCAL);
        data.add(ban1);
        PlayerBan ban3 = new PlayerBan();
        ban3.setBanType(BanType.GLOBAL);
        data.add(ban3);
        PlayerBan ban4 = new PlayerBan();
        ban4.setBanType(BanType.TEMP);
        data.add(ban4);

        Collections.sort(data, new BanSorter());

        Assert.assertEquals("Pos 1", ban3, data.get(0));
        Assert.assertEquals("Pos 2", ban1, data.get(1));
        Assert.assertEquals("Pos 3", ban4, data.get(2));
    }

    @Test
    public void sortComplexBans() {
        List<PlayerBan> data = new ArrayList<PlayerBan>();
        //expired localban
        PlayerBan ban1 = new PlayerBan();
        ban1.setBanType(BanType.LOCAL);
        ban1.setExpired(System.currentTimeMillis() - 100);
        data.add(ban1);
        //non expired localban
        PlayerBan ban2 = new PlayerBan();
        ban2.setBanType(BanType.LOCAL);
        data.add(ban2);
        //expired globalban
        PlayerBan ban3 = new PlayerBan();
        ban3.setBanType(BanType.GLOBAL);
        ban3.setExpired(System.currentTimeMillis() - 100);
        data.add(ban3);
        //non expired globalban
        PlayerBan ban4 = new PlayerBan();
        ban4.setBanType(BanType.GLOBAL);
        data.add(ban4);
        //tempban
        PlayerBan ban5 = new PlayerBan();
        ban5.setBanType(BanType.TEMP);
        ban5.setExpired(System.currentTimeMillis());
        data.add(ban5);
        Collections.sort(data, new BanSorter());
        Assert.assertEquals("Pos 1", ban4, data.get(0));
        Assert.assertEquals("Pos 2", ban3, data.get(1));
        Assert.assertEquals("Pos 3", ban2, data.get(2));
        Assert.assertEquals("Pos 4", ban1, data.get(3));
        Assert.assertEquals("Pos 5", ban5, data.get(4));
    }

    @Test
    public void sortExpiredBans() {
        long time = System.currentTimeMillis();
        List<PlayerBan> data = new ArrayList<PlayerBan>();
        PlayerBan ban1 = new PlayerBan();
        ban1.setBanType(BanType.TEMP);
        ban1.setBanReason("pos3");
        ban1.setExpired(time - 200);
        data.add(ban1);

        PlayerBan ban2 = new PlayerBan();
        ban2.setBanType(BanType.TEMP);
        ban2.setExpired(time - 150);
        ban2.setBanReason("pos2");
        data.add(ban2);

        PlayerBan ban3 = new PlayerBan();
        ban3.setBanType(BanType.TEMP);
        ban3.setExpired(time - 100);
        ban3.setBanReason("pos1");
        data.add(ban3);

        Collections.sort(data, new BanSorter());

        Assert.assertEquals("pos 1", ban3, data.get(0));
        Assert.assertEquals("pos 2", ban2, data.get(1));
        Assert.assertEquals("pos 3", ban1, data.get(2));

    }

    @Test
    public void sortForIssue18(){
        List<PlayerBan> bans = new ArrayList<PlayerBan>();
        PlayerBan localBan = new PlayerBan();
        localBan.setBanType(BanType.LOCAL);
        localBan.setBanReason("local ban");

        PlayerBan tempBan = new PlayerBan();
        tempBan.setBanType(BanType.TEMP);
        tempBan.setExpired(1332101225000L);
        tempBan.setBanReason("temp ban");

        bans.add(tempBan);
        bans.add(localBan);

        Collections.sort(bans, new BanSorter());

        Assert.assertEquals("pos 1",localBan,bans.get(0));
        Assert.assertEquals("pos 2",tempBan,bans.get(1));
    }
}
