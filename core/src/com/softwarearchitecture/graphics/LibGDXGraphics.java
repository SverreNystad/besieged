package com.softwarearchitecture.graphics;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.components.SpriteComponent;

public class LibGDXGraphics implements GraphicsController {
    private SpriteBatch batch;
	private HashMap<String, Texture> textures;

    public LibGDXGraphics() {
        batch = new SpriteBatch();
        textures = new HashMap<>();
    }

    @Override
    public void draw(SpriteComponent component) {
        if (!textures.containsKey(component.texture_path)) {
            textures.put(component.texture_path, new Texture(component.texture_path));
        }
        Texture texture = textures.get(component.texture_path);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(texture, component.screen_u * width, component.screen_v * height, component.u_size * width, component.v_size * height);
        batch.end();
    }

    @Override
    public void clearScreen() {
        ScreenUtils.clear(0, 0, 0, 1);
    }


    @Override
    public int getScreenWidth() {
        return Gdx.graphics.getWidth();
    }

    @Override
    public int getScreenHeight() {
        return Gdx.graphics.getHeight();
    }
}
