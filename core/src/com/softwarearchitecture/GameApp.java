package com.softwarearchitecture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.VelocityComponent;

public class GameApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ECSManager ecs;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		ecs = ECSManager.getInstance();

		// adding a new entity with position and velocity components
		Entity entity = new Entity();

		PositionComponent position = new PositionComponent(0, 0); // Example: starting at origin
		VelocityComponent velocity = new VelocityComponent(5, 5); // Example: moving at a velocity of (5,5)

		entity.addComponent(PositionComponent.class, position);
		entity.addComponent(VelocityComponent.class, velocity);

		ecs.addEntity(entity);
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
