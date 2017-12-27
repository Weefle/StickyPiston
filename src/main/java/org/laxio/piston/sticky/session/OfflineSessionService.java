package org.laxio.piston.sticky.session;

import org.laxio.piston.piston.session.Profile;
import org.laxio.piston.piston.session.MinecraftSessionService;
import org.laxio.piston.piston.session.SessionResponse;

import java.util.UUID;

public class OfflineSessionService implements MinecraftSessionService {

    @Override
    public SessionResponse hasJoined(Profile profile, String serverId) {
        return hasJoined(profile, serverId, null);
    }

    @Override
    public SessionResponse hasJoined(Profile profile, String serverId, String ip) {
        return new SessionResponse(profile.getName(), UUID.randomUUID());
    }

}
