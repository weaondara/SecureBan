package de.minecraftadmin.secureban.system;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author BADMAN152
 * analyze the bans of a player form the global and local database
 *
 */
public class BanAnalyzer {

    /**
     * @param player
     * @return false if the user has an active ban else true
     * @author BADMAN152
     * <p/>
     * analyze the bans of a player
     */
    public boolean isPlayerAllowedToJoin(Player player) {
        return getActiveBansOfPlayer(player).size()==0;
    }

    /**
     * @author BADMAN152
     * returns a list of all active bans
     * @param player
     * @return
     */
    public List<PlayerBan> getActiveBansOfPlayer(Player player){
        List<PlayerBan> bans = new ArrayList<PlayerBan>();
        for (PlayerBan ban : player.getBans()){
            Logger.getAnonymousLogger()
                    .warning("\n\n*********\n" +
                            "Player "+player.getUserName()+" "+
                            "BanType "+ban.getBanType()+" " +
                            "expired "+isBanExpired(ban.getExpired())+"" +
                            "\n*********\n\n");
            if(!isBanExpired(ban.getExpired()))bans.add(ban);
        }
        return bans;
    }

    /**
     * @author BADMAN152
     * check if the timestamp is expired
     * @param expiredTimestamp
     * @return
     */
    private boolean isBanExpired(Long expiredTimestamp) {
        if(expiredTimestamp==null)return false;
        if(expiredTimestamp==0L)return false;
        return new Date(System.currentTimeMillis()).after(new Date(expiredTimestamp));
    }
}
