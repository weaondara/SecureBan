package de.minecraftadmin.secureban.wire.command;

import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.SaveState;
import de.minecraftadmin.secureban.system.BanManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.03.13
 * Time: 20:56
 * To change this template use File | Settings | File Templates.
 */
public class CheckBanCommand extends Command {

    private final BanManager manager;
    private final DateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");

    public CheckBanCommand(BanManager manager) {
        super("checkban", "secureban.checkban");
        this.manager = manager;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission(this.getPermission())) return;
        if (strings.length < 1) return;
        String userName = strings[0];
        Player p = manager.getAllBansOfPlayer(userName);
        commandSender.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.UNDERLINE + ChatColor.GOLD + " All known ban's for " + p.getUserName());
        if (p.getBans().isEmpty()) {
            commandSender.sendMessage(ChatColor.WHITE + "[SecureBan] No bans");
            return;
        }
        for (PlayerBan ban : p.getBans()) {
            if (ban.getBanType().equals(BanType.GLOBAL) &&
                    ban.getSaveState().equals(SaveState.SAVED) &&
                    ban.getServer() == null) continue;
            sendMessage(commandSender, ban);

        }
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
            exp = " until " + format.format(new Date(ban.getExpired())) + " ";
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
