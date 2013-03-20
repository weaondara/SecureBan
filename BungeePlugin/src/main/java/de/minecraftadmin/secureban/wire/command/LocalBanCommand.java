package de.minecraftadmin.secureban.wire.command;

import de.minecraftadmin.secureban.system.BanManager;
import net.md_5.bungee.api.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.03.13
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class LocalBanCommand extends HookableBanCommand {

    private final BanManager manager;

    public LocalBanCommand(BanManager manager) {
        super("localban", "secureban.localban");
        this.manager = manager;
    }

    @Override
    public Long executeBan(CommandSender staff, String username, String reason, Long expire) {
        return manager.localBan(username, staff.getName(), reason);
    }
}
