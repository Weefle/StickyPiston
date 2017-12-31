package org.laxio.piston.sticky.command;

import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.chat.MessageComponent;
import org.laxio.piston.piston.chat.StatusLevel;
import org.laxio.piston.piston.command.ConsoleCommandSender;

public class PistonConsoleCommandSender implements ConsoleCommandSender {

    private final PistonServer server;

    public PistonConsoleCommandSender(PistonServer server) {
        this.server = server;
    }

    @Override
    public PistonServer getServer() {
        return server;
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        sendMessage(StatusLevel.NORMAL, message);
    }

    @Override
    public void sendMessage(StatusLevel level, String message) {
        getServer().getLogger().log(level.getLevel(), message);
    }

    @Override
    public void sendMessage(MessageComponent message) {
        // TODO: convert message into readable string
    }

    @Override
    public boolean hasPermission(String string) {
        return true;
    }

}
