package de.minecraftadmin.secureban.command;

import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.12.12
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public class LocalBanCommand extends HookableBanCommand {
    public LocalBanCommand(BanManager banManager, boolean multi) {
        super(banManager, multi);
    }

    @Override
    protected Long banCommand(CommandSender sender, String command, String targetUser, String banReason, Long expireTimestamp) {
        return this.getBanManager().localBan(targetUser, sender.getName(), banReason);
    }
}
