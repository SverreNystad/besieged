package com.softwarearchitecture.game_server;

import java.util.UUID;

public interface PersistenceAccess {
    public void updateHighscore(UUID playerID, int score);
}
