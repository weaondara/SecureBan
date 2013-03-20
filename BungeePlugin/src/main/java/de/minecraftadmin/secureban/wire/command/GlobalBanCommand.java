package de.minecraftadmin.secureban.wire.command;

import de.minecraftadmin.secureban.system.BanManager;
import net.md_5.bungee.api.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.03.13
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class GlobalBanCommand extends HookableBanCommand {
    private final BanManager manager;

    public GlobalBanCommand(BanManager manager) {
        super("globalban", "secureban.globalban");
        this.manager = manager;
    }

    @Override
    public Long executeBan(CommandSender staff, String username, String reason, Long expire) {
        return manager.globalBan(username, staff.getName(), reason);
    }
}
