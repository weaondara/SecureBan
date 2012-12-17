package de.minecraftadmin.secureban.system;

import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.Player;

/**
 * @author BADMAN152
 * manage every ban on the server
 */
public class BanManager {

    private final RemoteAPIManager remote;
    private final Database db;

    public BanManager(Database localDB,String remoteUrl,String apiKey){
        this.db = localDB;
        this.remote = new RemoteAPIManager(remoteUrl,apiKey);
    }

    /**
     * @author BADMAN152
     * create a global ban and save it to the global & local database
     * @param userName the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     */
    public void globalBan(String userName, String staffName, String banReason){

    }

    /**
     * @author BADMAN152
     * create a local ban and save it to the local database
     * @param userName the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     */
    public void localBan(String userName, String staffName,String banReason){

    }

    /**
     * @author BADMAN152
     * create a temp ban and save it to the local database
     * @param userName the UserName which has been banned
     * @param staffName the UserName which commit the ban
     * @param banReason the reason why the player has been banned
     * @param expired timestamp when it expires
     */
    public void tempBan(String userName, String staffName, String banReason, long expired){

    }

    /**
     * @author BADMAN152
     * get the persisted player with all bans
     * @param userName
     * @return
     */
    public Player getBansOfPlayer(String userName){
    return db.getDatabase().createQuery(Player.class,"SELECT * FROM Player p where p.userName=\'"+userName+"\'").findUnique();
    }
}
