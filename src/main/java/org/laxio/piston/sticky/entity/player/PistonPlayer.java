package org.laxio.piston.sticky.entity.player;

import org.laxio.piston.piston.entity.EntityType;
import org.laxio.piston.piston.player.Player;
import org.laxio.piston.sticky.entity.PistonEntity;

import java.util.UUID;

public class PistonPlayer extends PistonEntity implements Player {

    private String name;

    public PistonPlayer(String name) {
        super(null);
        this.name = name;
    }

    public PistonPlayer(UUID uuid, String name) {
        super(uuid);
        this.name = name;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public String getName() {
        return name;
    }

}
