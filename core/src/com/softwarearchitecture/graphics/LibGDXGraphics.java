package com.softwarearchitecture.graphics;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

        // batch.draw(texture, component.screen_u, component.screen_v, component.u_size, component.v_size);

        // // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

}
