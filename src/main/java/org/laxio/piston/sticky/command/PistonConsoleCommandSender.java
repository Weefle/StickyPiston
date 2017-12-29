package org.laxio.piston.sticky.command;

import org.laxio.piston.piston.chat.MessageComponent;
import org.laxio.piston.piston.chat.StatusLevel;
import org.laxio.piston.piston.command.ConsoleCommandSender;

public class PistonConsoleCommandSender implements ConsoleCommandSender {

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
        level.log(message);
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
