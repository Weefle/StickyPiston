package org.laxio.piston.sticky;

import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.protocol.Protocol;

public class StickyPistonServer implements PistonServer {

    private static final String MC_PROTOCOL_VERSION; // MC Version (Specification)
    private static final String STICKY_PISTON_VERSION; // Protocol Version (Implementation)

    private final Protocol protocol;

    public StickyPistonServer(Protocol protocol) {
        this.protocol = protocol;
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
    public Protocol getProtocol() {
        return protocol;
    }

    static {
        MC_PROTOCOL_VERSION = StickyPistonServer.class.getPackage().getSpecificationVersion();
        STICKY_PISTON_VERSION = StickyPistonServer.class.getPackage().getImplementationVersion();
    }

}
