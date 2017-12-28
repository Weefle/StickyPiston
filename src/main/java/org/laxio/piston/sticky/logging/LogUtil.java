package org.laxio.piston.sticky.logging;

import org.fusesource.jansi.Ansi;
import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.chat.StatusLevel;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.fusesource.jansi.Ansi.Attribute;
import static org.laxio.piston.piston.chat.ChatColor.*;

public class LogUtil {

    static final Logger logger;
    static final PrintStream out;

    static final List<Level> levels = new ArrayList<>();
    static final Map<Level, String> names = new HashMap<>();

    static {
        BLACK.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        DARK_BLUE.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        DARK_GREEN.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        DARK_AQUA.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        DARK_RED.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        DARK_PURPLE.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        GOLD.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        GRAY.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        DARK_GRAY.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        BLUE.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        GREEN.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        AQUA.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        RED.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        LIGHT_PURPLE.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        YELLOW.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        WHITE.setConsole(Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        MAGIC.setConsole(Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
        BOLD.setConsole(Ansi.ansi().bold().toString());
        STRIKETHROUGH.setConsole(Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
        UNDERLINE.setConsole(Ansi.ansi().a(Attribute.UNDERLINE).toString());
        ITALIC.setConsole(Ansi.ansi().a(Attribute.ITALIC).toString());
        RESET.setConsole(Ansi.ansi().a(Attribute.RESET).toString());

        levels.add(Level.ALL);
        levels.add(Level.CONFIG);
        levels.add(Level.FINE);
        levels.add(Level.FINER);
        levels.add(Level.FINEST);
        levels.add(Level.INFO);
        levels.add(Level.OFF);
        levels.add(Level.SEVERE);
        levels.add(Level.WARNING);

        for (Level level : levels) {
            StatusLevel status = StatusLevel.NORMAL;
            if (level == Level.CONFIG) {
                status = StatusLevel.CONFIG;
            } else if (level == Level.WARNING) {
                status = StatusLevel.WARNING;
            } else if (level == Level.SEVERE) {
                status = StatusLevel.SEVERE;
            }

            colour(level, status);
        }

        logger = Logger.getGlobal();

        out = System.out;
    }

    public static void init(Handler handle) {
        // set console reader

        // logger.addHandler(new ConsoleHandler());
        System.setOut(new PrintStream(new LoggerOutputStream(logger, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(logger, Level.WARNING), true));

        logger.setUseParentHandlers(false);
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        logger.addHandler(handle);
        logger.info("Loaded");
    }

    private static void colour(Level level, StatusLevel status) {
        names.put(level, "" + status.getColor() + level + ChatColor.RESET);
    }

}
