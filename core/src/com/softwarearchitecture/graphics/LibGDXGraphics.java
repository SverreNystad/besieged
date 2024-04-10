package com.softwarearchitecture.graphics;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;

public class LibGDXGraphics implements GraphicsController {
    private SpriteBatch batch;
    private HashMap<String, Texture> textures;
    private BitmapFont font;

    public LibGDXGraphics() {
        batch = new SpriteBatch();
        textures = new HashMap<>();
        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);
    }

    @Override
    public void draw(SpriteComponent component, PositionComponent positionComponent) {
        if (!textures.containsKey(component.texture_path)) {
            textures.put(component.texture_path, new Texture(component.texture_path));
        }
        Texture texture = textures.get(component.texture_path);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        batch.begin();
        batch.draw(texture, positionComponent.position.x * width, positionComponent.position.y * height,
                component.size_uv.x * width,
                component.size_uv.y * height);
        batch.end();
    }

    @Override
    public void clearScreen() {
        ScreenUtils.clear(0, 0, 0, 1);
    }

    @Override
    public void drawText(TextComponent textComponent, PositionComponent positionComponent) {
        float charWidth = Gdx.graphics.getWidth() * textComponent.fontScale.x;
        float charHeight = Gdx.graphics.getHeight() * textComponent.fontScale.y;

        font.getData().setScale(1.0f, 1.0f);
        float scaleHeight = charHeight / font.getCapHeight();
        float scaleWidth = charWidth / font.getCapHeight();
        font.getData().setScale(scaleWidth, scaleHeight);

        batch.begin();
        font.draw(batch, textComponent.text, positionComponent.position.x * Gdx.graphics.getWidth(), charHeight + positionComponent.position.y * Gdx.graphics.getHeight());
        batch.end();
    }
}
