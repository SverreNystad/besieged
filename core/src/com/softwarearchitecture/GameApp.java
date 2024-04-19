package com.softwarearchitecture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.clock.Clock;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.launcher.GameLauncher;

public class GameApp extends ApplicationAdapter {

	public static final int SCREEN_WIDTH = 1300;
    public static final int SCREEN_HEIGHT = 600;
	GameClient gameClient;
	OrthographicCamera camera;
	Viewport viewport;

	@Override
	public void create() {
		Clock.getInstance();
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
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
	}
}
