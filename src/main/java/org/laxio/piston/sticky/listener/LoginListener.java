package org.laxio.piston.sticky.listener;

import org.laxio.piston.piston.event.PacketHandler;
import org.laxio.piston.piston.event.listener.Listener;
import org.laxio.piston.piston.event.listener.ListenerPriority;
import org.laxio.piston.piston.entity.player.Player;
import org.laxio.piston.piston.protocol.Connection;
import org.laxio.piston.piston.protocol.Packet;
import org.laxio.piston.piston.protocol.ProtocolState;
import org.laxio.piston.piston.session.MinecraftSessionService;
import org.laxio.piston.protocol.v340.netty.NetworkClient;
import org.laxio.piston.protocol.v340.netty.pipeline.PacketEncryption;
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
import java.util.logging.Logger;

public class LoginListener implements Listener {

    @PacketHandler(priority = ListenerPriority.MONITOR)
    public void onStart(LoginStartPacket packet) {
        Logger.getGlobal().info("Start: " + packet);
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
        client.setEncryption(client.getEncryptionHold());

        PublicKey key = packet.getServer().getKeyPair().getPublic();
        PrivateKey priv = packet.getServer().getKeyPair().getPrivate();

        SecretKey secret = packet.getSecretKey(priv);
        String hash = BrokenHash.hash("", key, secret);
        Logger.getGlobal().info("hash: " + hash);
        client.getProfile().authenticate(hash);
    }

    private void login(Packet packet, UserProfile profile) {
        packet.reply(login(profile, packet.getConnection()));
        Logger.getGlobal().info("Login success!");
    }

    private LoginSuccessPacket login(UserProfile profile, Connection connection) {
        Player player = new PistonPlayer(profile, connection);
        connection.setState(ProtocolState.PLAY);
        return new LoginSuccessPacket(player);
    }

}
