package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.chat.MessageBuilder;
import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.protocol.ProtocolState;
import org.laxio.piston.protocol.v340.netty.NetworkClient;
import org.laxio.piston.protocol.v340.packet.handshake.server.HandshakePacket;
import org.laxio.piston.protocol.v340.packet.login.client.DisconnectPacket;

public class HandshakeListener implements Listener {

    @PacketHandler
    public void onHandshake(HandshakePacket packet) {
        NetworkClient client = (NetworkClient) packet.getConnection();
        if (packet.getNextState() == ProtocolState.LOGIN) {
            if (client.getServer().getProtocol().getVersion() != packet.getProtocolVersion()) {
                MessageBuilder builder = MessageBuilder.builder();
                if (client.getServer().getProtocol().getVersion() > packet.getProtocolVersion()) {
                    builder.message(ChatColor.RED + "Client out of date");
                } else {
                    builder.message(ChatColor.RED + "Server out of date");
                }

                packet.reply(new DisconnectPacket(builder.build()));
            }
        }
    }

}
