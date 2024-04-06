package com.softwarearchitecture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.softwarearchitecture.game_server.states.Menu;
import com.softwarearchitecture.game_server.states.MenuEnum;
import com.softwarearchitecture.game_server.states.ScreenManager;

public class GameApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 600;
	private ScreenManager screenManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		screenManager = ScreenManager.getInstance();
		screenManager.nextState(new Menu(MenuEnum.MENU));
		Gdx.gl.glClearColor(1, 0, 0, 1);
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);

		// usikker på om dette skal være her
		float deltaTime = Gdx.graphics.getDeltaTime();
		screenManager.update(deltaTime);
		screenManager.render(batch);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
