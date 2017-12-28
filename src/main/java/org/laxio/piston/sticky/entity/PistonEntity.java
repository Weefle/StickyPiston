package org.laxio.piston.sticky.entity;

import org.laxio.piston.piston.entity.Entity;
import org.laxio.piston.piston.entity.Velocity;
import org.laxio.piston.piston.world.Location;

import java.util.UUID;

public abstract class PistonEntity implements Entity {

    private static final Object lock = new Object();

    protected static int entityId = 1;

    private int id;
    private UUID uuid;
    private Location location;
    private Velocity velocity;

    public PistonEntity() {
        this(null);
    }

    public PistonEntity(UUID uuid) {
        synchronized (lock) {
            this.id = entityId++;
            entityId++;
        }

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
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
