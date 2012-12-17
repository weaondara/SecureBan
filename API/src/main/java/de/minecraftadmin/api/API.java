package de.minecraftadmin.api;

import de.minecraftadmin.api.entity.PlayerBan;

/**
 * Accessor Class to access the remote api
 */
public interface API {

    public PlayerBan getPlayerBans(String userName);
    public void submitPlayerBans(PlayerBan playerBan);
}
