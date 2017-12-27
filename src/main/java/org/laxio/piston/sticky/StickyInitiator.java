package org.laxio.piston.sticky;

import org.laxio.piston.piston.Piston;
import org.laxio.piston.protocol.v340.StickyProtocolV340;

import java.io.IOException;

import static org.laxio.piston.piston.versioning.PistonModule.build;
import static org.laxio.piston.piston.versioning.PistonModuleType.*;

/**
 * Sets up Piston Modules
 *
 * @see org.laxio.piston.piston.versioning.PistonModule
 */
public class StickyInitiator {

    private static final Object lock = new Object();

    /**
     * Creates PistonModules for all available
     *
     * @see org.laxio.piston.piston.versioning.PistonModuleType
     */
    public static void setup() throws IOException {
        synchronized (lock) {
            /*
             * org.laxio.piston.ignition.json       / Impl / Ignition version     / Req: false
             * org.laxio.piston.piston.json         / Spec / Piston API version   / Req: true
             * org.laxio.piston.sticky.json         / Impl / StickyPiston version / Req: true
             * org.laxio.piston.protocol.{ver}.json / Impl / Protocol version     / Req: true
             * org.laxio.piston.protocol.{ver}.json / Spec / Minecraft version    / Req: true
             */

            try {
                IGNITION.setModule(build("org.laxio.piston.ignition.json", "Implementation"));
            } catch (Exception ex) {
                // ignore, not required
            }

            PISTON.setModule(build(Piston.class, "Specification"));
            STICKYPISTON.setModule(build(StickyPistonServer.class, "Implementation"));
            PROTOCOL.setModule(build(StickyProtocolV340.class, "Implementation"));
            MINECRAFT.setModule(build(StickyProtocolV340.class, "Specification"));
        }
    }

}
