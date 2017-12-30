package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.logging.Logger;
import org.laxio.piston.protocol.v340.netty.NetworkClient;
import org.laxio.piston.protocol.v340.packet.handshake.server.HandshakePacket;

public class HandshakeListener implements Listener {

    @PacketHandler
    public void onHandshake(HandshakePacket packet) {
        NetworkClient client = (NetworkClient) packet.getConnection();
        Logger.getGlobal().info("Protocol version of [{}] is {}. Connected via {}:{}", client.getAddress(), packet.getProtocolVersion(), packet.getAddress(), packet.getPort());
    }

}
