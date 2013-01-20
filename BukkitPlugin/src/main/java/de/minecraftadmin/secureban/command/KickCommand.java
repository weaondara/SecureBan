package de.minecraftadmin.secureban.command;

import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.01.13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class KickCommand implements CommandExecutor {

    private final BanManager banManager;

    public KickCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission(command.getPermission())) {
            commandSender.sendMessage(ChatColor.RED + command.getPermissionMessage());
            return true;
        }
        List<String> arguments = new ArrayList<String>(Arrays.asList(strings));
        if (arguments.isEmpty() || arguments.size() < 2) return false;
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(arguments.get(0));
        if (player == null) {
            commandSender.sendMessage(ChatColor.RED + " Unknown Player");
            return false;
        }
        final String targetUserName = player.getName();
        arguments.remove(0);
        String banReason = "";
        for (String split : arguments) {
            banReason += " " + split;
        }
        final String finalbanReason = banReason.trim();

        banManager.kick(player.getName(), commandSender.getName(), finalbanReason);
        if (player.isOnline()) player.getPlayer().kickPlayer(finalbanReason);
        return false;
    }
}
