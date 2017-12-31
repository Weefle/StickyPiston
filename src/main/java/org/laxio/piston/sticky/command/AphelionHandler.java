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

    public AphelionHandler(Aphelion<CommandSender> aphelion) {
        this.aphelion = aphelion;
    }

    public Aphelion<CommandSender> getAphelion() {
        return aphelion;
    }

    public CommandRegistration<CommandSender> getRegistration() {
        return aphelion.getRegistration();
    }

    /**
     * Handles the supplied input
     *
     * @param sender The sender of the input
     * @param string The input being sent
     */
    public void handle(CommandSender sender, String string) {
        if (!valid(string)) {
            return;
        }

        String[] split = string.split(" ");

        String command = split[0];
        String[] args = args(split);

        CommandHandler<CommandSender> handler = aphelion.getCommand(command);
        if (handler == null) {
            sender.sendMessage(StatusLevel.WARNING, "Command not found");
            return;
        }

        invoke(sender, command, args, handler);
    }

    /**
     * Checks the supplied string to see if it is a valid command input
     *
     * @param string The string to validate
     *
     * @return true if it is valid, false otherwise
     */
    private boolean valid(String string) {
        if (string == null) {
            return false;
        }

        boolean blank = true;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c != ' ') {
                blank = false;
                break;
            }
        }

        return !blank;
    }

    /**
     * Returns the arguments for the supplied array
     *
     * @param split The full split
     *
     * @return The shortened split
     */
    private String[] args(String[] split) {
        String[] args = new String[split.length - 1];
        if (args.length > 0) {
            System.arraycopy(split, 1, args, 0, split.length - 1);
        }

        return args;
    }

    /**
     * Invokes the command and handles any exceptions
     *
     * @param sender  The sender of the command
     * @param command The label of the command being invoked
     * @param args    The arguments of the command
     * @param handler The command being invoked
     */
    private void invoke(CommandSender sender, String command, String[] args, CommandHandler<CommandSender> handler) {
        try {
            aphelion.invoke(sender, command, args, handler);
        } catch (CommandPermissionException e) {
            sender.sendMessage(StatusLevel.WARNING, "No permission.");
        } catch (CommandUsageException e) {
            sender.sendMessage(StatusLevel.WARNING, e.getMessage());
            sender.sendMessage(StatusLevel.WARNING, "/" + command + " " + handler.getUsage());
        } catch (CommandException e) {
            if (e.getCause() != null) {
                if (e.getCause() instanceof NumberFormatException) {
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
