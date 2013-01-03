package de.minecraftadmin.secureban.listener;

import de.minecraftadmin.secureban.SecureBan;
import de.minecraftadmin.secureban.command.GlobalBanCommand;
import de.minecraftadmin.secureban.command.LocalBanCommand;
import de.minecraftadmin.secureban.command.TempBanCommand;
import de.minecraftadmin.secureban.utils.ConfigNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 24.12.12
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class CommandListener implements Listener {

    private final SecureBan plugin;

    public CommandListener(SecureBan instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPluginEnabled(PluginEnableEvent event) {
        if (plugin.getConfig().getBoolean(ConfigNode.OverrideGlobalBanCommand.getNode())) {
            PluginCommand globalcommand = Bukkit.getServer().getPluginCommand("globalban");
            CommandExecutor cmd = globalcommand.getExecutor();
            if (!(cmd instanceof GlobalBanCommand)) {
                globalcommand.setExecutor(new GlobalBanCommand(plugin.getBanManager(), plugin.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false)));
            }
        }

        if (plugin.getConfig().getBoolean(ConfigNode.OverrideLocalBanCommand.getNode())) {
            PluginCommand globalcommand = Bukkit.getServer().getPluginCommand("localban");
            CommandExecutor cmd = globalcommand.getExecutor();
            if (!(cmd instanceof LocalBanCommand)) {
                globalcommand.setExecutor(new LocalBanCommand(plugin.getBanManager(), plugin.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false)));
            }

        }

        if (plugin.getConfig().getBoolean(ConfigNode.OverrideTempBanCommand.getNode())) {
            PluginCommand globalcommand = Bukkit.getServer().getPluginCommand("tempban");
            CommandExecutor cmd = globalcommand.getExecutor();
            if (!(cmd instanceof TempBanCommand)) {
                globalcommand.setExecutor(new TempBanCommand(plugin.getBanManager(), plugin.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false)));
            }

        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String[] commandargs = event.getMessage().split(" ");
        String command = commandargs[0];
        List<String> args = new ArrayList<String>(Arrays.asList(commandargs));
        args.remove(0);
        commandargs = args.toArray(new String[args.size()]);

        if (!plugin.getConfig().getBoolean(ConfigNode.ActivateGlobalBanCommand.getNode()) &&
                command.equalsIgnoreCase(GlobalBanCommand.getCommand().getName())) {
            if (event.getPlayer().hasPermission(GlobalBanCommand.getCommand().getPermission())) {
                GlobalBanCommand cmd = new GlobalBanCommand(plugin.getBanManager(), plugin.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false));
                cmd.onCommand(event.getPlayer(), GlobalBanCommand.getCommand(), command, commandargs);
            }
        }
        if (!plugin.getConfig().getBoolean(ConfigNode.ActivateLocalBanCommand.getNode()) &&
                command.equalsIgnoreCase(LocalBanCommand.getCommand().getName())) {
            if (event.getPlayer().hasPermission(LocalBanCommand.getCommand().getPermission())) {
                LocalBanCommand cmd = new LocalBanCommand(plugin.getBanManager(), plugin.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false));
                cmd.onCommand(event.getPlayer(), LocalBanCommand.getCommand(), command, commandargs);
            }
        }
        if (!plugin.getConfig().getBoolean(ConfigNode.ActivateTempBanCommand.getNode()) &&
                command.equalsIgnoreCase(TempBanCommand.getCommand().getName())) {
            if (event.getPlayer().hasPermission(TempBanCommand.getCommand().getPermission())) {
                TempBanCommand cmd = new TempBanCommand(plugin.getBanManager(), plugin.getConfig().getBoolean(ConfigNode.MultiServer.getNode(), false));
                cmd.onCommand(event.getPlayer(), TempBanCommand.getCommand(), command, commandargs);
            }
        }
    }
}
