package com.softwarearchitecture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.launcher.GameLauncher;

public class GameApp extends ApplicationAdapter {
	GameClient gameClient;
	OrthographicCamera camera;
	Viewport viewport;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		gameClient = GameLauncher.createGameClient(this.camera, this.viewport);
	}

	@Override
	public void render() {
		gameClient.update();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		viewport.apply();
	}

	@Override
	public void dispose() {
		// gameClient.dispose();
	}
}
