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
        PlayerBan ban = new PlayerBan();
        ban.setBanType(BanType.TEMP);
        data.add(ban);
        ban = new PlayerBan();
        ban.setBanType(BanType.LOCAL);
        ban.setExpired(System.currentTimeMillis());
        data.add(ban);
        ban = new PlayerBan();
        ban.setBanType(BanType.GLOBAL);
        ban.setExpired(System.currentTimeMillis());
        data.add(ban);
        ban = new PlayerBan();
        ban.setBanType(BanType.TEMP);
        ban.setExpired(System.currentTimeMillis());
        data.add(ban);

        Collections.sort(data, new BanSorter());

        Assert.assertEquals(data.get(0).getBanType(), BanType.GLOBAL);
        Assert.assertEquals(data.get(1).getBanType(), BanType.LOCAL);
        Assert.assertEquals(data.get(2).getBanType(), BanType.TEMP);
        Assert.assertEquals(data.get(3).getBanType(), BanType.TEMP);


    }
}
