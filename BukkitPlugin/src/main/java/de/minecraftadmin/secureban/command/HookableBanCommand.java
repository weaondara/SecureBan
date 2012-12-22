package de.minecraftadmin.secureban.command;

import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 18.12.12
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class HookableBanCommand implements CommandExecutor {

    private final BanManager banManager;

    public HookableBanCommand(BanManager banManager) {
        this.banManager = banManager;
    }

    protected abstract boolean banCommand(Player sender, String command, String targetUser, String banReason, Long expireTimestamp);

    /**
     * @param commandSender
     * @param command
     * @param s
     * @param args
     * @return
     * @author BADMAN152
     * <p/>
     * override method of org.bukkit.command.CommandExecuter implements:
     * - permissioncheck
     * - The Username of the target
     * - Banreason
     */
    @Override
    public final boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission(command.getPermission())) {
            commandSender.sendMessage(ChatColor.RED + command.getPermissionMessage());
            return true;
        }
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        if (arguments.isEmpty() || arguments.size() < 2) return false;
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(arguments.get(0));
        if (player == null) {
            commandSender.sendMessage(ChatColor.RED + " Unknown Player");
            return false;
        }
        String targetUserName = player.getName();
        arguments.remove(0);
        Long duration = timeTranslater(arguments.get(0));
        arguments.remove(0);
        String banReason = "";
        for (String split : arguments) {
            banReason += split + " ";
        }
        boolean success = banCommand((Player) commandSender, command.getName(), targetUserName, banReason, duration);
        if (success) {
            if (duration == null)
                Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " " + targetUserName + " has been banned");
            else
                Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " " + targetUserName + " has been banned until " + new Date(System.currentTimeMillis() + duration).toString());
        }
        return success;
    }

    private Long timeTranslater(String time) {
        String number = time.substring(0, time.length() - 1);
        char modifier = time.substring(time.length() - 1).toLowerCase().toCharArray()[0];
        try {
            int zahl = Integer.parseInt(number);
            switch (modifier) {
                case 's':
                    return (long) (zahl * 1000);
                case 'm':
                    return (long) (zahl * 60000);
                case 'h':
                    return (long) (zahl * 3600000);
                case 'd':
                    return (long) (zahl * 68400000);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public BanManager getBanManager() {
        return banManager;
    }
}
