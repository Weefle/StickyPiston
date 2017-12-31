package org.laxio.piston.sticky.listener;

        /*-
         * #%L
         * StickyPiston
         * %%
         * Copyright (C) 2017 Laxio
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
import org.laxio.piston.piston.entity.player.Player;
import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.event.listener.ListenerOwner;
import org.laxio.piston.piston.event.listener.ListenerPriority;
import org.laxio.piston.piston.event.login.PlayerPreLoginEvent;
import org.laxio.piston.piston.protocol.Connection;
import org.laxio.piston.piston.protocol.Packet;
import org.laxio.piston.piston.protocol.ProtocolState;
import org.laxio.piston.protocol.v340.netty.NetworkClient;
import org.laxio.piston.protocol.v340.netty.pipeline.encryption.PacketEncryption;
import org.laxio.piston.protocol.v340.packet.login.client.DisconnectPacket;
import org.laxio.piston.protocol.v340.packet.login.client.EncryptionRequestPacket;
import org.laxio.piston.protocol.v340.packet.login.client.LoginSuccessPacket;
import org.laxio.piston.protocol.v340.packet.login.server.EncryptionResponsePacket;
import org.laxio.piston.protocol.v340.packet.login.server.LoginStartPacket;
import org.laxio.piston.protocol.v340.util.BrokenHash;
import org.laxio.piston.protocol.v340.util.UserProfile;
import org.laxio.piston.sticky.entity.player.PistonPlayer;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class LoginListener implements Listener {

    private final PistonServer server;

    public LoginListener(PistonServer server) {
        this.server = server;
    }

    @Override
    public ListenerOwner getOwner() {
        return server;
    }

    @PacketHandler(priority = ListenerPriority.MONITOR)
    public void onStart(LoginStartPacket packet) {
        NetworkClient client = (NetworkClient) packet.getConnection();

        UserProfile profile = new UserProfile(client.getServer().getSessionService(), packet.getName());
        client.setProfile(profile);

        if (packet.getServer().isOnlineMode()) {
            // login

            PacketEncryption encryption = new PacketEncryption();
            client.setEncryptionHold(encryption);

            PublicKey key = packet.getServer().getKeyPair().getPublic();
            byte[] verifyToken = encryption.getVerifyToken();

            EncryptionRequestPacket request = new EncryptionRequestPacket("", key, verifyToken);
            packet.reply(request);
        } else {
            profile.authenticate("");
            login(packet, profile);
        }
    }

    @PacketHandler(priority = ListenerPriority.MONITOR)
    public void onResponse(EncryptionResponsePacket packet) {
        NetworkClient client = (NetworkClient) packet.getConnection();
        PublicKey key = packet.getServer().getKeyPair().getPublic();
        PrivateKey priv = packet.getServer().getKeyPair().getPrivate();

        SecretKey secret = packet.getSecretKey(priv);
        client.getEncryptionHold().generate(secret);
        client.setEncryption(client.getEncryptionHold());

        byte[] token = packet.getVerifyToken();
        token = packet.decipher(client.getServer().getKeyPair().getPrivate(), token);

        byte[] verify = client.getEncryptionHold().getVerifyToken();

        if (!Arrays.equals(token, verify)) {
            packet.reply(new DisconnectPacket("Invalid nonce"));
            return;
        }

        String hash = BrokenHash.hash("", key, secret);
        client.getProfile().authenticate(hash);

        login(packet, client.getProfile());
    }

    private void login(Packet packet, UserProfile profile) {
        Player player = new PistonPlayer(server, profile, packet.getConnection());
        PlayerPreLoginEvent event = new PlayerPreLoginEvent(player);
        player.getServer().getManager().call(event);

        if (event.getResult() != PlayerPreLoginEvent.LoginResult.ALLOWED) {
            packet.getServer().getLogger().info("Player '{}' disconnected [{}]: {}", player.getName(), player.getConnection().getAddress(), event.getResult().name());
            packet.reply(new DisconnectPacket(event.getKickMessage()));
            return;
        }

        packet.reply(login(profile, packet.getConnection()));
        packet.getServer().getLogger().info("Player '{}' logged in [{}]", player.getName(), player.getConnection().getAddress());
    }

    private LoginSuccessPacket login(UserProfile profile, Connection connection) {
        Player player = new PistonPlayer(server, profile, connection);
        connection.setState(ProtocolState.PLAY);
        return new LoginSuccessPacket(player);
    }

}
