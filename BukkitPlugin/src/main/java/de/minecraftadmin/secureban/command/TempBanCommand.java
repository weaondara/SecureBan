package de.minecraftadmin.secureban.command;

import de.minecraftadmin.secureban.system.BanManager;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.12.12
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class TempBanCommand extends HookableBanCommand {
    public TempBanCommand(BanManager banManager) {
        super(banManager);
    }

    @Override
    protected boolean banCommand(CommandSender sender, String command, String targetUser, String banReason, Long expireTimestamp) {
        if (expireTimestamp == null) return false;
        this.getBanManager().tempBan(targetUser, sender.getName(), banReason, expireTimestamp);
        return true;
    }
}
