package de.minecraftadmin.secureban.command;

import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.12.12
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
public class GlobalBanCommand extends HookableBanCommand {
    public GlobalBanCommand(BanManager banManager, boolean multi) {
        super(banManager, multi);
    }

    @Override
    protected boolean banCommand(CommandSender sender, String command, String targetUser, String banReason, Long expireTimestamp) {
        this.getBanManager().globalBan(targetUser, sender.getName(), banReason);
        return true;
    }
}
