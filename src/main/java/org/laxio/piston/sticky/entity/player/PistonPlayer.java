package org.laxio.piston.sticky.entity.player;

import org.json.JSONObject;
import org.laxio.piston.piston.PistonServer;
import org.laxio.piston.piston.chat.ChatPosition;
import org.laxio.piston.piston.chat.MessageBuilder;
import org.laxio.piston.piston.chat.MessageComponent;
import org.laxio.piston.piston.chat.StatusLevel;
import org.laxio.piston.piston.entity.EntityType;
import org.laxio.piston.piston.entity.Metadata;
import org.laxio.piston.piston.entity.player.Player;
import org.laxio.piston.piston.entity.player.Statistic;
import org.laxio.piston.piston.protocol.Connection;
import org.laxio.piston.piston.session.Profile;
import org.laxio.piston.sticky.entity.PistonEntity;

import java.util.ArrayList;
import java.util.List;

public class PistonPlayer extends PistonEntity implements Player {

    private Profile profile;
    private Connection connection;

    private List<Statistic> statistics;

    public PistonPlayer(PistonServer server, Profile profile, Connection connection) {
        super(server, profile.getUniqueId());
        this.profile = profile;
        this.connection = connection;

        this.statistics = new ArrayList<>();
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
        return profile.getName();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void sendMessage(ChatPosition position, String message) {
        sendMessage(position, MessageBuilder.builder().message(message).build());
    }

    @Override
    public void sendMessage(ChatPosition position, StatusLevel level, String message) {
        sendMessage(position, level.getColor() + message);
    }

    @Override
    public void sendMessage(ChatPosition position, MessageComponent message) {
        JSONObject json = message.toJSON();
        // TODO: send packet
    }

    @Override
    public boolean hasPermission(String string) {
        // TODO: permissions system
        return false;
    }

    @Override
    public List<Statistic> getStatistics() {
        return statistics;
    }

    @Override
    public Statistic getStatistic(String name) {
        for (Statistic statistic : getStatistics()) {
            if (statistic.getName().equals(name)) {
                return statistic;
            }
        }

        return null;
    }

    @Override
    public Metadata getMetadata() {
        return null;
    }

}
