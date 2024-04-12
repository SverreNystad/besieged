package com.softwarearchitecture.graphics;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;

public class LibGDXGraphics implements GraphicsController {
    private SpriteBatch batch;
    private HashMap<String, Texture> textures;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    public LibGDXGraphics(OrthographicCamera camera, Viewport viewport) {
        batch = new SpriteBatch();
        textures = new HashMap<>();
        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);
        this.camera = camera;
        this.viewport = viewport;
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
    }

    @Override
    public void draw(SpriteComponent component, PositionComponent positionComponent) {
        if (!textures.containsKey(component.texture_path)) {
            textures.put(component.texture_path, new Texture(component.texture_path));
        }
        Texture texture = textures.get(component.texture_path);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        // System.out.println(width);
        batch.begin();
        batch.draw(texture, positionComponent.position.x * width, positionComponent.position.y * height,
                component.size_uv.x * width,
                component.size_uv.y * height);
        batch.end();
    }

    @Override
    public void clearScreen() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.update();
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        batch.setProjectionMatrix(camera.combined);
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
