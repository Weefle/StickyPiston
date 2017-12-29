package org.laxio.piston.sticky;

import me.hfox.aphelion.Aphelion;
import me.hfox.aphelion.CommandRegistration;
import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.command.CommandSender;
import org.laxio.piston.piston.command.ConsoleCommandSender;
import org.laxio.piston.piston.event.ListenerManager;
import org.laxio.piston.piston.logging.Logger;
import org.laxio.piston.piston.protocol.Protocol;
import org.laxio.piston.piston.session.MinecraftSessionService;
import org.laxio.piston.protocol.v340.netty.NetworkServer;
import org.laxio.piston.protocol.v340.session.MojangSessionService;
import org.laxio.piston.sticky.command.AphelionHandler;
import org.laxio.piston.sticky.command.PistonConsoleCommandSender;
import org.laxio.piston.sticky.listener.FeatureListener;
import org.laxio.piston.sticky.listener.LoginListener;
import org.laxio.piston.sticky.listener.StatusListener;
import org.laxio.piston.sticky.session.OfflineSessionService;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class StickyPistonServer implements PistonServer {

    private static final String MC_PROTOCOL_VERSION; // MC Version (Specification)
    private static final String STICKY_PISTON_VERSION; // Protocol Version (Implementation)

    static {
        MC_PROTOCOL_VERSION = StickyPistonServer.class.getPackage().getSpecificationVersion();
        STICKY_PISTON_VERSION = StickyPistonServer.class.getPackage().getImplementationVersion();
    }

    private final KeyPair keyPair;
    private final boolean onlineMode;
    private final MinecraftSessionService sessionService;
    private final Protocol protocol;
    private final ListenerManager manager;

    private final ConsoleCommandSender console;
    private final AphelionHandler aphelion;
    private NetworkServer network;

    public StickyPistonServer(Protocol protocol) {
        this.onlineMode = true;
        this.keyPair = (onlineMode ? generate() : null);
        this.sessionService = (onlineMode ? new MojangSessionService(this) : new OfflineSessionService());

        this.protocol = protocol;
        this.manager = new ListenerManager();

        this.manager.register(new StatusListener());
        this.manager.register(new LoginListener(this));
        this.manager.register(new FeatureListener());

        this.console = new PistonConsoleCommandSender();
        this.aphelion = new AphelionHandler();
    }

    @Override
    public String getVersion() {
        return STICKY_PISTON_VERSION;
    }

    @Override
    public String getMinecraftVersion() {
        return MC_PROTOCOL_VERSION;
    }

    public NetworkServer getNetwork() {
        return network;
    }

    public void setNetwork(NetworkServer network) {
        this.network = network;
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

    @Override
    public MinecraftSessionService getSessionService() {
        return sessionService;
    }

    @Override
    public InetSocketAddress getBindAddress() {
        return network.getAddress();
    }

    @Override
    public Logger getLogger() {
        return Logger.getGlobal();
    }

    @Override
    public ConsoleCommandSender getConsole() {
        return console;
    }

    @Override
    public Aphelion<CommandSender> getAphelion() {
        return aphelion.getAphelion();
    }

    public AphelionHandler getAphelionHandler() {
        return aphelion;
    }

    @Override
    public CommandRegistration<CommandSender> getCommandRegistration() {
        return aphelion.getRegistration();
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

}
