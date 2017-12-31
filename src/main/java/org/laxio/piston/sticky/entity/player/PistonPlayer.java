package org.laxio.piston.sticky.entity.player;

        /*-
         * #%L
         * StickyPiston
         * %%
         * Copyright (C) 2017 Laxio
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
import org.laxio.piston.protocol.v340.packet.play.client.ServerChatMessagePacket;
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
        getConnection().sendPacket(new ServerChatMessagePacket(message, position));
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
