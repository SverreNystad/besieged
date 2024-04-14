package com.softwarearchitecture.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.softwarearchitecture.ecs.GraphicsController;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;

import java.util.HashMap;

public class LibGDXGraphics implements GraphicsController {
    private SpriteBatch batch;
    private HashMap<String, Texture> textures;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    public LibGDXGraphics(OrthographicCamera camera, Viewport viewport) {
        batch = new SpriteBatch();
        textures = new HashMap<>();
        // Initialize the font using FreeTypeFontGenerator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/odinson.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36; // You can adjust the size here
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);
        generator.dispose(); // Don't forget to dispose of the generator

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
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void drawText(TextComponent textComponent, PositionComponent positionComponent) {
        font.getData().setScale(1, 1);
        float charWidth = Gdx.graphics.getWidth() * textComponent.fontScale.x;
        float charHeight = Gdx.graphics.getHeight() * textComponent.fontScale.y;
        float scale = charHeight / font.getCapHeight();

        font.getData().setScale(scale); // Set the scale uniformly based on height
        font.setColor(textComponent.color.x, textComponent.color.y, textComponent.color.z, 1);

        float xPos = Math.round(positionComponent.position.x * Gdx.graphics.getWidth());
        float yPos = Math.round(charHeight + positionComponent.position.y * Gdx.graphics.getHeight());

        batch.begin();
        font.draw(batch, textComponent.text, xPos, yPos);
        batch.end();
    }
}