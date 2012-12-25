package de.minecraftadmin.api.utils;

import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.PlayerBan;

import java.util.Comparator;
import java.util.Date;

/**
 * @author BADMAN152
 */
public class BanSorter implements Comparator<PlayerBan> {

    @Override
    public int compare(PlayerBan playerBan, PlayerBan playerBan2) {
        //less
        if (playerBan.getBanType().equals(BanType.TEMP) && !(playerBan2.getBanType().equals(BanType.TEMP))) return 1;
        if (playerBan.getBanType().equals(BanType.LOCAL) && playerBan2.getBanType().equals(BanType.GLOBAL)) return 1;

        if (playerBan.getBanType().equals(BanType.GLOBAL) && !(playerBan2.getBanType().equals(BanType.GLOBAL)))
            return -1;
        if (playerBan.getBanType().equals(BanType.LOCAL) && playerBan2.getBanType().equals(BanType.TEMP)) return -1;

        if (playerBan.getExpired() == null && playerBan2.getExpired() != null) return -1;
        if (playerBan.getExpired() != null && playerBan2.getExpired() == null) return 1;
        if (playerBan.getExpired() == null && playerBan2.getExpired() == null) return 0;

        if (new Date(playerBan.getExpired()).before(new Date(playerBan2.getExpired()))) return 1;
        if (new Date(playerBan2.getExpired()).before(new Date(playerBan.getExpired()))) return -1;


        return 0;
    }
}
