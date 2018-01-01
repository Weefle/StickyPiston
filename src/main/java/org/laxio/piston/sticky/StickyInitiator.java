package org.laxio.piston.sticky;

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
