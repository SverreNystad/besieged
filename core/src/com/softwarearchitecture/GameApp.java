package com.softwarearchitecture;

import com.badlogic.gdx.ApplicationAdapter;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.launcher.GameLauncher;

public class GameApp extends ApplicationAdapter {
	GameClient gameClient;

	@Override
	public void create() {
		gameClient = GameLauncher.createGameClient();
	}

	@Override
	public void render() {
		gameClient.update();
	}

	@Override
	public void dispose() {
		//gameClient.dispose();
	}
}
