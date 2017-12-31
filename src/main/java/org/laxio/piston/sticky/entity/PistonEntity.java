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
