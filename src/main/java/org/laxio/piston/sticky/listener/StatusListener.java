/*-
 * ========================LICENSE_START========================
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
 * ========================LICENSE_END========================
 */
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
