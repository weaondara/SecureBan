package de.minecraftadmin.secureban.wire.command;

import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.NoteType;
import de.minecraftadmin.secureban.system.NoteManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 25.03.13
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public class NoteCommand extends Command {
    private final NoteManager noteManager;

    public NoteCommand(NoteManager noteManager) {
        super("note");
        this.noteManager = noteManager;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        List<String> arguments = new ArrayList<String>(Arrays.asList(strings));
        if (arguments.size() < 1) return;

        String subCommand = arguments.remove(0);

        if (subCommand.equalsIgnoreCase("list") && !arguments.isEmpty() && commandSender.hasPermission("secureban.note.list")) {
            try {
                String username = arguments.remove(0);
                List<Note> notes = noteManager.getNoteFromPlayer(username);
                commandSender.sendMessage(ChatColor.WHITE + "[SecureBan]" + ChatColor.YELLOW + " All Notes of " + username);
                if (notes.isEmpty()) {
                    commandSender.sendMessage(ChatColor.WHITE + "[SecureBan] " + ChatColor.YELLOW + "No Notes");
                } else {
                    for (Note note : notes) {
                        commandSender.sendMessage(ChatColor.WHITE + "[SecureBan] " + ChatColor.YELLOW + note);
                    }
                }

            } catch (Throwable throwable) {
                commandSender.sendMessage(ChatColor.YELLOW + "Remoteservice temporary not available");
            }
        } else if (subCommand.equalsIgnoreCase("add") && arguments.size() > 1 && commandSender.hasPermission("secureban.note.add")) {
            String username = arguments.remove(0);
            String note = "";
            for (String s : arguments) {
                note += s;
            }
            try {
                noteManager.addNoteToPlayer(username, note, NoteType.INFO);
            } catch (Throwable throwable) {
                commandSender.sendMessage(ChatColor.YELLOW + "Remoteservice temporary not available");
            }
        } else if (subCommand.equalsIgnoreCase("remove") && arguments.size() > 1 && commandSender.hasPermission("secureban.note.remove")) {
            String username = arguments.remove(0);
            try {
                Long id = Long.parseLong(arguments.remove(0));
                noteManager.removeNoteFromPlayer(username, id);
            } catch (Throwable throwable) {
                commandSender.sendMessage(ChatColor.YELLOW + "Remoteservice temporary not available");
            }
        }
    }
}
