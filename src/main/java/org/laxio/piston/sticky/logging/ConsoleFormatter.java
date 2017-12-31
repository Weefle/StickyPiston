package org.laxio.piston.sticky.logging;

import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.chat.StatusLevel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        Date dat = new Date();
        dat.setTime(record.getMillis());
        String message = this.formatMessage(record);
        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter level = new StringWriter();
            PrintWriter pw = new PrintWriter(level);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = level.toString();
        }

        StatusLevel status = StatusLevel.getLevel(record.getLevel());
        String level = "" + status.getColor() + record.getLevel() + ChatColor.RESET;
        return ChatColor.getConsoleString(ChatColor.RESET + String.format("[%1$tH:%1$tM:%1$tS %3$s]: %4$s%5$s%n", dat, record.getLoggerName(), level, message, throwable));
    }

}
