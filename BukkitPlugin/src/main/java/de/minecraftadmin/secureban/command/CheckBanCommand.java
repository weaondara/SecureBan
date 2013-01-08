package de.minecraftadmin.secureban.command;

import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;
import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 22.12.12
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public class CheckBanCommand implements CommandExecutor {

    private final BanManager banManager;
    private final DateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");

    public CheckBanCommand(BanManager banManager) {
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
            sender.sendMessage(command.getPermissionMessage());
            return true;
        }
        if (args.length == 0) return false;
        String userName = args[0];
        Player p = banManager.getAllBansOfPlayer(userName);
        sender.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.UNDERLINE + ChatColor.GOLD + " All known ban's for " + p.getUserName());
        if (p.getBans().isEmpty()) {
            sender.sendMessage(ChatColor.WHITE + "[SecureBan] No bans");
            return true;
        }
        for (PlayerBan ban : p.getBans()) {
            if (ban.getBanType().equals(BanType.GLOBAL) &&
                    ban.getSaveState().equals(SaveState.SAVED) &&
                    ban.getServer() == null) continue;
            sendMessage(sender, ban);

        }
        return true;
    }

    private void sendMessage(CommandSender sender, PlayerBan ban) {
        String serverName;
        ChatColor color;
        String exp = " ";
        if (ban.getExpired() == null)
            color = ChatColor.RED;
        else {
            if (new Date().before(new Date(ban.getExpired())))
                color = ChatColor.RED;
            else
                color = ChatColor.GRAY;
            exp = " until " + format.format(new Date(ban.getExpired()));
        }
        if (ban.getServer() == null)
            serverName = "this Server";
        else
            serverName = ban.getServer().getServerName();
        sender.sendMessage(ChatColor.WHITE + "[SecureBan] " +
                color +
                format.format(new Date(ban.getStart())) + " | " +
                ban.getBanType().name() +
                exp +
                ban.getBanReason() + " from " +
                ban.getStaffName() + " (" + serverName + ")");

    }
}
