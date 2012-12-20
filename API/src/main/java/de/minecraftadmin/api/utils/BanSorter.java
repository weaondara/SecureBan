package de.minecraftadmin.api.utils;

import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.PlayerBan;

import java.util.Comparator;

/**
 * @author BADMAN152
 */
public class BanSorter implements Comparator<PlayerBan> {

    @Override
    public int compare(PlayerBan playerBan, PlayerBan playerBan2) {
        //less
        if ((playerBan.getBanType().equals(BanType.TEMP) && !playerBan2.getBanType().equals(BanType.TEMP)) ||
                (playerBan.getBanType().equals(BanType.LOCAL) && playerBan2.getBanType().equals(BanType.GLOBAL))) {
            if (playerBan.getExpired() == null || playerBan2.getExpired() == null) return -1;
            if (playerBan.getExpired() < playerBan2.getExpired()) return -1;
            if (playerBan2.getExpired() < playerBan.getExpired()) return 1;
            return 0;
        }

        //grater
        if ((playerBan.getBanType().equals(BanType.GLOBAL) && !playerBan2.getBanType().equals(BanType.GLOBAL)) ||
                (playerBan.getBanType().equals(BanType.LOCAL) && playerBan2.getBanType().equals(BanType.TEMP))) {
            if (playerBan.getExpired() == null || playerBan2.getExpired() == null) return 1;
            if (playerBan.getExpired() < playerBan2.getExpired()) return -1;
            if (playerBan2.getExpired() < playerBan.getExpired()) return 1;
            return 0;
        }
        //equals
        if (playerBan.getExpired() == null || playerBan2.getExpired() == null) return 0;
        if (playerBan.getExpired() < playerBan2.getExpired()) return -1;
        if (playerBan2.getExpired() < playerBan.getExpired()) return 1;
        return 0;
    }
}
