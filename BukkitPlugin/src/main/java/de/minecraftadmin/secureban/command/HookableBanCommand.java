package de.minecraftadmin.secureban.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 18.12.12
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class HookableBanCommand implements CommandExecutor {

    protected abstract boolean banCommand(Player sender,String command,String targetUser,String banReason);

    /**
     * @author BADMAN152
     *
     * override method of org.bukkit.command.CommandExecuter implements:
     * - permissioncheck
     * - The Username of the target
     * - Banreason
     *
     * @param commandSender
     * @param command
     * @param s
     * @param args
     * @return
     */
    @Override
    public final boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission(command.getPermission())){
            commandSender.sendMessage(ChatColor.RED+command.getPermissionMessage());
            return false;
        }
        List<String> arguments = Arrays.asList(args);
        if(arguments.isEmpty()) return false;
        String targetUserName=arguments.get(0);
        arguments.remove(0);
        String banReason ="";
        for(String split : arguments){
            banReason=split+" ";
        }
        return banCommand((Player)commandSender,command.getName(),targetUserName,banReason);
    }
}
