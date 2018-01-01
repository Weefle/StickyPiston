package org.laxio.piston.sticky.listener;

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

import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.chat.ChatColor;
import org.laxio.piston.piston.chat.MessageBuilder;
import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.event.listener.ListenerOwner;
import org.laxio.piston.piston.protocol.ProtocolState;
import org.laxio.piston.piston.translator.ProtocolTranslator;
import org.laxio.piston.protocol.v340.netty.NetworkClient;
import org.laxio.piston.protocol.v340.packet.handshake.server.HandshakePacket;
import org.laxio.piston.protocol.v340.packet.login.client.DisconnectPacket;

public class HandshakeListener implements Listener {

    private final PistonServer server;

    public HandshakeListener(PistonServer server) {
        this.server = server;
    }

    @Override
    public ListenerOwner getOwner() {
        return server;
    }

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
