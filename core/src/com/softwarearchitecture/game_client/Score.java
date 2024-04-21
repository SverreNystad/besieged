package com.softwarearchitecture.game_client;

import java.util.UUID;

public class Score {

    private UUID gameId;
    private int wavesSurvived;

    public Score(UUID gameId, int wavesSurvived) {
        this.gameId = gameId;
        this.wavesSurvived = wavesSurvived;
    }


    public UUID getGameId() {
        return gameId;
    }

    public int getWavesSurvived() {
        return wavesSurvived;
    }
}
