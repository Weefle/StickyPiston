package org.laxio.piston.sticky.command;

import me.hfox.aphelion.Aphelion;
import me.hfox.aphelion.CommandRegistration;
import me.hfox.aphelion.command.CommandHandler;
import me.hfox.aphelion.exception.CommandException;
import me.hfox.aphelion.exception.CommandPermissionException;
import me.hfox.aphelion.exception.CommandUsageException;
import org.laxio.piston.piston.chat.StatusLevel;
import org.laxio.piston.piston.command.CommandSender;

public class AphelionHandler {

    private final Aphelion<CommandSender> aphelion;

    public AphelionHandler() {
        this.aphelion = new Aphelion<CommandSender>(CommandSender.class) {
            @Override
            public boolean hasPermission(CommandSender sender, String s) {
                return sender.hasPermission(s);
            }
        };
    }

    public Aphelion<CommandSender> getAphelion() {
        return aphelion;
    }

    public CommandRegistration<CommandSender> getRegistration() {
        return aphelion.getRegistration();
    }

    public void handle(CommandSender sender, String string) {
        if (string == null) {
            return;
        }

        boolean blank = true;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c != ' ') {
                blank = false;
                break;
            }
        }

        if (blank) {
            return;
        }

        String[] split = string.split(" ");

        String command = split[0];
        String[] args = new String[split.length - 1];
        if (args.length > 0) {
            System.arraycopy(split, 1, args, 0, split.length - 1);
        }

        CommandHandler<CommandSender> handler = aphelion.getCommand(command);
        if (handler == null) {
            sender.sendMessage(StatusLevel.WARNING, "Command not found");
            return;
        }

        try {
            aphelion.invoke(sender, command, args, handler);
        } catch(CommandPermissionException e) {
            sender.sendMessage(StatusLevel.WARNING, "No permission.");
        } catch(CommandUsageException e) {
            sender.sendMessage(StatusLevel.WARNING, e.getMessage());
            sender.sendMessage(StatusLevel.WARNING, "/" + command + " " + handler.getUsage());
        } catch(CommandException e) {
            if (e.getCause() != null) {
                if(e.getCause() instanceof NumberFormatException) {
                    sender.sendMessage(StatusLevel.WARNING, "Number expected, string received instead.");
                } else {
                    sender.sendMessage(StatusLevel.WARNING, "An error has occurred. See console.");
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage(StatusLevel.WARNING, e.getMessage());
            }
        }
    }

}
