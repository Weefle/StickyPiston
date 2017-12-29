package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.event.EventHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.event.login.PlayerPreLoginEvent;

public class FeatureListener implements Listener {

    @EventHandler
    public void onLogin(PlayerPreLoginEvent event) {
        event.setResult(PlayerPreLoginEvent.LoginResult.DISALLOWED);
        event.setKickMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "L A X I O\n\n" + ChatColor.WHITE + "Is not " + ChatColor.ITALIC + "currently" + ChatColor.WHITE + " ready for logins");
    }

}
