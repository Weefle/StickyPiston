package org.laxio.piston.sticky.logging;

import org.laxio.piston.piston.chat.ChatColor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import static org.laxio.piston.sticky.logging.LogUtil.names;

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

        String level = names.get(record.getLevel());
        if (level == null) {
            level = record.getLevel().getLocalizedName();
            names.put(record.getLevel(), level);
        }

        return ChatColor.getConsoleString(ChatColor.RESET + String.format("[%1$tl:%1$tM:%1$tS %1$Tp %3$s]: %4$s%5$s%n", dat, record.getLoggerName(), level, message, throwable));
    }

}
