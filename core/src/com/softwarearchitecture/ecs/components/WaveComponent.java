package com.softwarearchitecture.ecs.components;

public class WaveComponent {
    
    public int waveNumber;
    public int waveSize;
    public int monsterCounter;
    public float spawnTimer;
    public float spawnDuration;
    public float waveDuration;
    public float waveTimer;
    public int maxLiveMonsters;
    public int liveMonsterCounter;
    
    public WaveComponent(int waveNumber, int waveSize, float spawnTimer, float waveDuration, int maxLiveMonsters) {
        this.waveNumber = waveNumber;
        this.waveSize = waveSize;
        this.spawnTimer = spawnTimer;
        this.spawnDuration = 3f;
        this.maxLiveMonsters = maxLiveMonsters;
        this.waveDuration = waveDuration;
        this.waveTimer = waveDuration;
        this.monsterCounter = 0;
        this.liveMonsterCounter = 0;
    }

}
