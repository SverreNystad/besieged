package com.softwarearchitecture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.softwarearchitecture.game_server.states.GameMenuState;
import com.softwarearchitecture.game_server.states.GameStateManager;

public class GameApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	private GameStateManager gameStateManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		batch = new SpriteBatch();
		gameStateManager = new GameStateManager();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gameStateManager.push(new GameMenuState());
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

		// usikker på om dette skal være her
		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.render(batch);
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
