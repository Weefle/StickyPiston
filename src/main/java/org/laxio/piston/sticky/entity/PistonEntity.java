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
package org.laxio.piston.sticky.entity;

import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.entity.Entity;
import org.laxio.piston.piston.entity.Velocity;
import org.laxio.piston.piston.world.Location;

import java.util.UUID;

public abstract class PistonEntity implements Entity {

    private static final Object lock = new Object();

    private static int entityId = 1;

    private final PistonServer server;

    private final int id;
    private final UUID uuid;
    private Location location;
    private Velocity velocity;

    public PistonEntity(PistonServer server) {
        this(server, null);
    }

    public PistonEntity(PistonServer server, UUID uuid) {
        this.server = server;
        synchronized (lock) {
            this.id = entityId++;
        }

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    @Override
    public PistonServer getServer() {
        return server;
    }

    @Override
    public int getEntityId() {
        return id;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getData() {
        return 0;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Velocity getVelocity() {
        return velocity;
    }

}
