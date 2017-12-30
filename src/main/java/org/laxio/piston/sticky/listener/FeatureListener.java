package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.event.EventHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.event.login.PlayerPreLoginEvent;

public class FeatureListener implements Listener {

    @EventHandler
    public void onLogin(PlayerPreLoginEvent event) {
        event.setResult(PlayerPreLoginEvent.LoginResult.DISALLOWED);
        event.setKickMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "P I S T O N\n\n" + ChatColor.YELLOW + "Is not " + ChatColor.ITALIC + "currently" + ChatColor.YELLOW + " ready for logins");
    }

}
