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
package org.laxio.piston.sticky;

import me.hfox.aphelion.Aphelion;
import me.hfox.aphelion.CommandRegistration;
import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.command.CommandSender;
import org.laxio.piston.piston.command.ConsoleCommandSender;
import org.laxio.piston.piston.event.ListenerManager;
import org.laxio.piston.piston.list.LockableLinkedList;
import org.laxio.piston.piston.logging.Logger;
import org.laxio.piston.piston.protocol.Protocol;
import org.laxio.piston.piston.session.MinecraftSessionService;
import org.laxio.piston.piston.translator.ProtocolTranslator;
import org.laxio.piston.piston.versioning.PistonModuleType;
import org.laxio.piston.piston.versioning.Version;
import org.laxio.piston.protocol.v340.netty.NetworkServer;
import org.laxio.piston.protocol.v340.session.MojangSessionService;
import org.laxio.piston.sticky.command.AphelionHandler;
import org.laxio.piston.sticky.command.PistonConsoleCommandSender;
import org.laxio.piston.sticky.command.server.ManageServerCommands;
import org.laxio.piston.sticky.listener.FeatureListener;
import org.laxio.piston.sticky.listener.HandshakeListener;
import org.laxio.piston.sticky.listener.LoginListener;
import org.laxio.piston.sticky.listener.StatusListener;
import org.laxio.piston.sticky.session.OfflineSessionService;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickyPistonServer implements PistonServer {

    private final String name;
    private final Logger logger;

    private final KeyPair keyPair;
    private final boolean onlineMode;
    private final MinecraftSessionService sessionService;
    private final Protocol protocol;
    private final ListenerManager manager;

    private final ConsoleCommandSender console;
    private final AphelionHandler aphelion;
    private final Map<Integer, Protocol> loadedProtocols;
    private final List<ProtocolTranslator> translators;
    private NetworkServer network;

    public StickyPistonServer(Protocol protocol) {
        this(protocol, null);
    }

    public StickyPistonServer(Protocol protocol, String name) {
        this.logger = name == null ? Logger.getGlobal() : Logger.getLogger(name);
        this.name = name == null ? "DEFAULT" : name;

        this.onlineMode = true;
        this.keyPair = (onlineMode ? generate() : null);
        this.sessionService = (onlineMode ? new MojangSessionService(this) : new OfflineSessionService());

        this.protocol = protocol;
        this.protocol.setServer(this);

        this.manager = new ListenerManager();

        this.manager.register(new StatusListener(this));
        this.manager.register(new LoginListener(this));
        this.manager.register(new HandshakeListener(this));
        this.manager.register(new FeatureListener(this));

        this.console = new PistonConsoleCommandSender(this);
        this.aphelion = new AphelionHandler();
        this.getCommandRegistration().register(ManageServerCommands.class);

        this.loadedProtocols = new HashMap<>();
        addProtocol(protocol);

        this.translators = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Version getVersion() {
        return PistonModuleType.STICKYPISTON.getModule().getVersion();
    }

    @Override
    public Version getMinecraftVersion() {
        return PistonModuleType.MINECRAFT.getModule().getVersion();
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
    public Protocol getProtocol(int version) {
        return this.loadedProtocols.get(version);
    }

    @Override
    public void addProtocol(Protocol protocol) {
        this.loadedProtocols.put(protocol.getVersion(), protocol);
    }

    public List<ProtocolTranslator> getTranslators() {
        return LockableLinkedList.createLocked(translators);
    }

    @Override
    public boolean addTranslator(ProtocolTranslator translator) {
        for (ProtocolTranslator item : translators) {
            if (item.getClass().equals(translator.getClass())) {
                return false;
            }
        }

        translators.add(translator);
        return true;
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
        return logger;
    }

    @Override
    public ConsoleCommandSender getConsole() {
        return console;
    }

    @Override
    public void runCommand(CommandSender sender, String command) {
        aphelion.handle(sender, command);
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

    @Override
    public void handle(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public boolean start() {
        if (isRunning()) {
            return false;
        }

        long start = System.currentTimeMillis();
        synchronized (network.getRequest()) {
            network.run();

            boolean started = network.getRequest().isStarted();
            long duration = System.currentTimeMillis() - start;
            long ms = duration % 1000;

            String time = (duration - ms) + "." + String.format("%03d", ms) + "s";

            getLogger().info("Server{}started ({}) {}:{}", (started ? " " : " not "), time, getBindAddress().getHostString(), getBindAddress().getPort());

            return started;
        }
    }

    @Override
    public boolean isRunning() {
        return network.isAlive();
    }

    @Override
    public void stop() {
        getLogger().warning("Server shutting down...");
        network.shutdown();
        getLogger().warning("Server shut down");
    }

}
