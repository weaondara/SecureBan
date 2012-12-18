package de.minecraftadmin.secureban.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 18.12.12
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLoginEvent(PlayerLoginEvent event){

    }
    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onCommandEvent(PlayerCommandPreprocessEvent event){


    }
}
