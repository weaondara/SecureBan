package de.minecraftadmin.secureban.wire.command;

import de.minecraftadmin.api.entity.BanType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.03.13
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class HookableBanCommand extends Command {
    private String uploadUrl;
    private boolean showUpload;

    public HookableBanCommand(String command, String permission) {
        super(command, permission, new String[]{});
    }

    public abstract Long executeBan(CommandSender staff, String username, String reason, Long expire);

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(this.getPermission())) return;
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        if (arguments.isEmpty() || arguments.size() < 2) return;
        final String targetUserName = arguments.remove(0);
        final Long duration = timeTranslater(arguments.get(0));
        if (duration != null) arguments.remove(0);
        if (arguments.isEmpty()) return;
        String banReason = "";
        for (String split : arguments) {
            banReason += " " + split;
        }
        final String finalbanReason = banReason.trim();
        Long banId = executeBan(sender, targetUserName, finalbanReason, duration);
        ;
        boolean success = banId != null;
        if (success) {
            Runnable r;
            final BanType type;
            final List<ProxiedPlayer> players = new ArrayList<ProxiedPlayer>(ProxyServer.getInstance().getPlayers());
            if (duration == null) {
                if (this instanceof GlobalBanCommand) type = BanType.GLOBAL;
                else type = BanType.LOCAL;
                for (ProxiedPlayer player : players) {
                    if (player.hasPermission("secureban.banotify")) {
                        player.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " " + targetUserName + " has been " + type + " banned (" + finalbanReason + ")");
                    }
                }
            } else {
                type = BanType.TEMP;
                for (ProxiedPlayer player : players) {
                    if (player.hasPermission("secureban.banotify")) {
                        player.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.RED + " " + targetUserName + " has been banned until " + new Date(System.currentTimeMillis() + duration).toString() + " (" + finalbanReason + ")");
                    }
                }
            }
        }

        if (success && showUpload)

        {
            sender.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.YELLOW + "Upload Screens to " + uploadUrl + banId.intValue());
        }

    }

    public Long timeTranslater(String time) {
        String number = time.substring(0, time.length() - 1);
        char modifier = time.substring(time.length() - 1).toLowerCase().toCharArray()[0];
        try {
            long zahl = Long.parseLong(number);
            switch (modifier) {
                case 's':
                    return (long) (zahl * 1000);
                case 'm':
                    return (long) (zahl * 60000);
                case 'h':
                    return (long) (zahl * 3600000);
                case 'd':
                    return (long) (zahl * 86400000);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setUploadUrl(String uploadUrl) {
        if (!uploadUrl.endsWith("/")) uploadUrl = uploadUrl + "/";
        this.uploadUrl = uploadUrl;
    }

    public void setShowUpload(boolean showUpload) {
        this.showUpload = showUpload;
    }
}
