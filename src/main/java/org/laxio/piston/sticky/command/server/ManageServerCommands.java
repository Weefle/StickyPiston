package org.laxio.piston.sticky.command.server;

import me.hfox.aphelion.command.CommandContext;
import me.hfox.aphelion.command.annotations.Command;
import me.hfox.aphelion.command.annotations.CommandPermission;
import org.laxio.piston.piston.chat.StatusLevel;
import org.laxio.piston.piston.command.CommandSender;

public class ManageServerCommands {

    @Command(aliases = {"stop"}, description = "Stops the server", max = 0)
    @CommandPermission("piston.manage.stop")
    public static void stop(CommandSender sender, CommandContext args) {
        sender.sendMessage(StatusLevel.WARNING, "Stopping server...");
        sender.getServer().stop();
    }

}
