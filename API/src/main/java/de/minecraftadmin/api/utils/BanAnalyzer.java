package de.minecraftadmin.api.utils;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;

import java.util.*;

/**
 * @author BADMAN152
 *         analyze the bans of a player form the global and local database
 */
public class BanAnalyzer {

    private final String apiKey;

    public BanAnalyzer(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @param player
     * @return false if the user has an active ban else true
     * @author BADMAN152
     * <p/>
     * analyze the bans of a player
     */
    public boolean isPlayerAllowedToJoin(Player player) {
        return getActiveBlockedBansOfPlayer(player).size() == 0;
    }

    /**
     * @param player
     * @return
     * @author BADMAN152
     * returns a list of all active bans
     */
    public List<PlayerBan> getActiveBansOfPlayer(Player player) {
        List<PlayerBan> bans = new ArrayList<PlayerBan>();
        Set<PlayerBan> existingBans = player.getBans();
        if (existingBans != null) {
            for (PlayerBan ban : existingBans) {
                if (!isBanExpired(ban.getExpired())) {
                    bans.add(ban);
                }
            }
        }
        Collections.sort(bans, new BanSorter());
        return bans;
    }

    /**
     * @param player
     * @return
     */
    public List<PlayerBan> getActiveBlockedBansOfPlayer(Player player) {
        List<PlayerBan> bans = new ArrayList<PlayerBan>();
        for (PlayerBan ban : getActiveBansOfPlayer(player)) {
            if (ban.getServer() == null) {
                bans.add(ban);
            }
            if (ban.getServer() != null) {
                if (ban.getServer().getApiKey().equals(apiKey)) {
                    bans.add(ban);
                }
            }
        }
        Collections.sort(bans, new BanSorter());
        return bans;
    }

    /**
     * @param expiredTimestamp
     * @return
     * @author BADMAN152
     * check if the timestamp is expired
     */
    private boolean isBanExpired(Long expiredTimestamp) {
        if (expiredTimestamp == null) return false;
        if (expiredTimestamp == 0L) return false;
        return new Date(System.currentTimeMillis()).after(new Date(expiredTimestamp));
    }
}
