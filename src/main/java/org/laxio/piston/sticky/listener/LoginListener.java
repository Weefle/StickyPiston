package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.event.listener.ListenerPriority;
import org.laxio.piston.piston.player.Player;
import org.laxio.piston.piston.protocol.ProtocolState;
import org.laxio.piston.protocol.v340.packet.login.client.LoginSuccessPacket;
import org.laxio.piston.protocol.v340.packet.login.server.LoginStartPacket;
import org.laxio.piston.sticky.entity.player.PistonPlayer;

import java.util.logging.Logger;

public class LoginListener implements Listener {

    @PacketHandler(priority = ListenerPriority.MONITOR)
    public void onStart(LoginStartPacket packet) {
        if (packet.getServer().isOnlineMode()) {
            // login
            Logger.getGlobal().info("Login requires authentication");
        } else {
            Player player = new PistonPlayer(packet.getName());
            packet.getConnection().setState(ProtocolState.PLAY);
            packet.reply(new LoginSuccessPacket(player));
            Logger.getGlobal().info("Login success!");
        }
    }

}
