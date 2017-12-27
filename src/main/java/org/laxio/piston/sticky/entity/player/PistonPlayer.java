package org.laxio.piston.sticky.entity.player;

import org.laxio.piston.piston.entity.EntityType;
import org.laxio.piston.piston.entity.player.Player;
import org.laxio.piston.piston.session.Profile;
import org.laxio.piston.piston.protocol.Connection;
import org.laxio.piston.sticky.entity.PistonEntity;

public class PistonPlayer extends PistonEntity implements Player {

    private Profile profile;

    private String name;
    private Connection connection;

    public PistonPlayer(Profile profile, Connection connection) {
        super(profile.getUniqueId());
        this.profile = profile;

        this.name = profile.getName();
        this.connection = connection;
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

}
