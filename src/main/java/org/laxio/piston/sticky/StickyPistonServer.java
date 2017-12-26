package org.laxio.piston.sticky;

import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.event.ListenerManager;
import org.laxio.piston.piston.protocol.Protocol;
import org.laxio.piston.sticky.listener.LoginListener;
import org.laxio.piston.sticky.listener.StatusListener;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class StickyPistonServer implements PistonServer {

    private static final String MC_PROTOCOL_VERSION; // MC Version (Specification)
    private static final String STICKY_PISTON_VERSION; // Protocol Version (Implementation)

    private final KeyPair keyPair;
    private final boolean onlineMode;

    private final Protocol protocol;
    private final ListenerManager manager;

    public StickyPistonServer(Protocol protocol) {
        this.onlineMode = false;
        this.keyPair = (onlineMode ? generate() : null);
        this.protocol = protocol;
        this.manager = new ListenerManager();

        this.manager.register(new StatusListener());
        this.manager.register(new LoginListener());
    }

    @Override
    public String getVersion() {
        return STICKY_PISTON_VERSION;
    }

    @Override
    public String getMinecraftVersion() {
        return MC_PROTOCOL_VERSION;
    }

    @Override
    public boolean isOnlineMode() {
        return onlineMode;
    }

    @Override
    public KeyPair getKeyPair() {
        return keyPair;
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    @Override
    public ListenerManager getManager() {
        return manager;
    }

    private KeyPair generate() {
        try {
            KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");

            keypairgenerator.initialize(1024);
            return keypairgenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static {
        MC_PROTOCOL_VERSION = StickyPistonServer.class.getPackage().getSpecificationVersion();
        STICKY_PISTON_VERSION = StickyPistonServer.class.getPackage().getImplementationVersion();
    }

}
