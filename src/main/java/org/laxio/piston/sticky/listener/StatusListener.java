package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.chat.MessageBuilder;
import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.event.listener.ListenerOwner;
import org.laxio.piston.piston.event.listener.ListenerPriority;
import org.laxio.piston.protocol.v340.netty.NetworkClient;
import org.laxio.piston.protocol.v340.packet.status.client.PongPacket;
import org.laxio.piston.protocol.v340.packet.status.client.ResponsePacket;
import org.laxio.piston.protocol.v340.packet.status.server.PingPacket;
import org.laxio.piston.protocol.v340.packet.status.server.RequestPacket;

public class StatusListener implements Listener {

    private final PistonServer server;

    public StatusListener(PistonServer server) {
        this.server = server;
    }

    @Override
    public ListenerOwner getOwner() {
        return server;
    }

    @PacketHandler(priority = ListenerPriority.MONITOR)
    public void onRequest(RequestPacket packet) {
        MessageBuilder builder = MessageBuilder.builder();
        builder.message(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "P I S T O N" + ChatColor.GRAY + " - " + ChatColor.RED + packet.getServer().getName() + "\n"
                + ChatColor.YELLOW + "Your Protocol version " + packet.getConnection().getProtocolVersion());
        packet.reply(new ResponsePacket(builder.build()));
    }

    @PacketHandler(priority = ListenerPriority.MONITOR)
    public void onPing(PingPacket packet) {
        NetworkClient client = (NetworkClient) packet.getConnection();
        PongPacket pong = new PongPacket(packet.getPayload());
        // pong.setServer(packet.getServer());
        // pong.setConnection(packet.getConnection());
        // client.getChannel().writeAndFlush(pong).addListener(ChannelFutureListener.CLOSE);

        packet.reply(pong);
        client.getChannel().closeFuture();
    }

}
