package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.chat.MessageBuilder;
import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.protocol.ProtocolState;
import org.laxio.piston.piston.translator.ProtocolTranslator;
import org.laxio.piston.protocol.v340.netty.NetworkClient;
import org.laxio.piston.protocol.v340.packet.handshake.server.HandshakePacket;
import org.laxio.piston.protocol.v340.packet.login.client.DisconnectPacket;

public class HandshakeListener implements Listener {

    @PacketHandler
    public void onHandshake(HandshakePacket packet) {
        NetworkClient client = (NetworkClient) packet.getConnection();
        if (packet.getNextState() != ProtocolState.LOGIN) {
            return;
        }

        PistonServer server = client.getServer();
        if (server.getProtocol().getVersion() == packet.getProtocolVersion()) {
            return;
        }

        if (!translate(packet.getProtocolVersion(), server)) {
            MessageBuilder builder = MessageBuilder.builder();
            if (server.getProtocol().getVersion() > packet.getProtocolVersion()) {
                builder.message(ChatColor.RED + "Client out of date");
            } else {
                builder.message(ChatColor.RED + "Server out of date");
            }

            packet.reply(new DisconnectPacket(builder.build()));
        }
    }

    /**
     * Checks if the packet version can be translated to the native version
     *
     * @param start  The version to start at
     * @param server The server the client is connecting to
     *
     * @return true if the packet can be translated, false otherwise
     */
    private boolean translate(int start, PistonServer server) {
        int version = start;
        while (version != server.getProtocol().getVersion()) {
            boolean updated = false;
            for (ProtocolTranslator translator : server.getTranslators()) {
                if (translator.getTranslatedVersion() == version) {
                    version = translator.getNativeVersion();
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                return false;
            }
        }

        return true;
    }

}
