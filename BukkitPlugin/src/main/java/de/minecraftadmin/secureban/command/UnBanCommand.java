package de.minecraftadmin.secureban.command;

import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 22.12.12
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
public class UnBanCommand implements CommandExecutor {

    private final BanManager banManager;

    public UnBanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(command.getPermission())) {
            sender.sendMessage(ChatColor.RED + command.getPermissionMessage());
            return false;
        }

        if (args.length != 1) return false;

        banManager.unban(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
        if (offlinePlayer != null) {
            if (offlinePlayer.isBanned()) {
                offlinePlayer.setBanned(false);
            }
        }
        Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "[SecureBan] " + ChatColor.RED + args[0] + " has been unbanned");
        return true;
    }
}
