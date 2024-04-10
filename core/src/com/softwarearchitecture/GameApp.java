package com.softwarearchitecture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.softwarearchitecture.game_client.states.Menu;
import com.softwarearchitecture.game_client.states.MenuEnum;
import com.softwarearchitecture.game_client.states.ScreenManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;
import com.softwarearchitecture.ecs.systems.InputSystem;
import com.softwarearchitecture.game_client.GameClient;
import com.softwarearchitecture.input.LibGDXInput;
import com.softwarearchitecture.launcher.GameLauncher;

public class GameApp extends ApplicationAdapter {
	// SpriteBatch batch;s
	// ECSManager ecs;
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
