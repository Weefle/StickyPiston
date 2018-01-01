package org.laxio.piston.sticky.logging;

        /*-
         * #%L
         * StickyPiston
         * %%
         * Copyright (C) 2017 - 2018 Laxio
         * %%
         * This file is part of Piston, licensed under the MIT License (MIT).
         *
         * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
         *
         * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
         *
         * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
         * #L%
         */

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
