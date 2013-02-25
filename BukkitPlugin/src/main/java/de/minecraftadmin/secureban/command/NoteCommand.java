package de.minecraftadmin.secureban.command;

import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.NoteType;
import de.minecraftadmin.secureban.system.NoteManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 18.02.13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class NoteCommand implements CommandExecutor {

    private final NoteManager noteManager;

    public NoteCommand(NoteManager noteManager) {
        this.noteManager = noteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) return false;
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        String subCommand = arguments.remove(0);
        String userName = arguments.remove(0);
        if (subCommand.equalsIgnoreCase("add")) {
            if (arguments.isEmpty()) return false;
            try {
                this.noteManager.addNoteToPlayer(userName, getNoteMessage(arguments), NoteType.INFO);
                sender.sendMessage("[SecureBan] " + ChatColor.GREEN + "added note");
            } catch (Throwable throwable) {
                sender.sendMessage(ChatColor.YELLOW + "Remote service not available! please try again later!");
            }
            return true;
        } else if (subCommand.equalsIgnoreCase("list")) {
            try {
                List<Note> notes = this.noteManager.getNoteFromPlayer(userName);
                for (Note n : notes) {
                    sender.sendMessage("[SecureBan] " + ChatColor.YELLOW + n);
                }
            } catch (Throwable throwable) {
                sender.sendMessage(ChatColor.YELLOW + "Remote service not available! please try again later!");
            }
            return true;
        } else if (subCommand.equalsIgnoreCase("remove")) {
            if (arguments.isEmpty()) return false;
            try {
                this.noteManager.removeNoteFromPlayer(userName, getNoteIdFromArgument(arguments.remove(0)));
                sender.sendMessage("[SecureBan] " + ChatColor.GREEN + "Remove note");
            } catch (Throwable throwable) {
                sender.sendMessage(ChatColor.YELLOW + "Remote service not available! please try again later!");
            }
            return true;
        }
        return false;
    }

    private String getNoteMessage(List<String> arguments) {
        String noteMessage = "";
        for (String part : arguments) {
            noteMessage += " " + part;
        }
        return noteMessage.trim();
    }

    private Long getNoteIdFromArgument(String argument) {
        try {
            return Long.decode(argument);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
