package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.System;
import com.softwarearchitecture.ecs.components.HealthComponent;
import com.softwarearchitecture.ecs.components.PositionComponent;
import com.softwarearchitecture.ecs.components.SpriteComponent;
import com.softwarearchitecture.ecs.components.TextComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import com.softwarearchitecture.ecs.ComponentManager;
import com.softwarearchitecture.ecs.ECSManager;
import com.softwarearchitecture.ecs.Entity;
import com.softwarearchitecture.ecs.GraphicsController;

/**
 * RenderingSystem is responsible for rendering entities that have a
 * SpriteComponent.
 * It implements the System interface and defines logic in the update method to
 * render entities.
 */
public class RenderingSystem implements System {
    private ComponentManager<SpriteComponent> drawableManager;
    private ComponentManager<TextComponent> textManager;
    private ComponentManager<PositionComponent> positionManager;
    private ComponentManager<HealthComponent> healthManager;

    /**
     * Graphics controller - Not optional and will be needed to check against null.
     * Optional<T> is a option here, but since this is performance critical, we have
     * considered not to use it and instead check for null.
     */
    private GraphicsController graphicsController;

    public RenderingSystem(GraphicsController graphicsController) {
        this.drawableManager = ECSManager.getInstance().getOrDefaultComponentManager(SpriteComponent.class);
        this.textManager = ECSManager.getInstance().getOrDefaultComponentManager(TextComponent.class);
        this.graphicsController = graphicsController;
        this.positionManager = ECSManager.getInstance().getOrDefaultComponentManager(PositionComponent.class);
        this.healthManager = ECSManager.getInstance().getOrDefaultComponentManager(HealthComponent.class);
    }

    private class Pair<T, U> {
        public T first;
        public U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    @Override
    public void update(Set<Entity> entities, float deltaTime) throws IllegalStateException {
        if (graphicsController == null) {
            throw new IllegalStateException("Graphics controller is not set.");
        }

        graphicsController.clearScreen();

        ArrayList<Pair<SpriteComponent, PositionComponent>> sprites = new ArrayList<>();

        for (Entity entity : entities) {
            Optional<SpriteComponent> sprite = drawableManager.getComponent(entity);
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            if (sprite.isPresent() && position.isPresent()) {
                sprites.add(new Pair<SpriteComponent, PositionComponent>(sprite.get(), position.get()));
            } else if (sprite.isPresent() && !position.isPresent()) {
                throw new IllegalStateException(
                        "Entity " + entity + " has a SpriteComponent but no PositionComponent.");
            }
        }

        sprites.sort(new Comparator<Pair<SpriteComponent, PositionComponent>>() {
            @Override
            public int compare(Pair<SpriteComponent, PositionComponent> s1,
                    Pair<SpriteComponent, PositionComponent> s2) {
                return Integer.compare(s1.second.z_index, s2.second.z_index);
            }
        });

        ArrayList<Pair<TextComponent, PositionComponent>> texts = new ArrayList<>();

        for (Entity entity : entities) {
            Optional<TextComponent> text = textManager.getComponent(entity);
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            if (text.isPresent() && position.isPresent()) {
                texts.add(new Pair<TextComponent, PositionComponent>(text.get(), position.get()));
            } else if (text.isPresent() && !position.isPresent()) {
                throw new IllegalStateException(
                        "Entity " + entity + " has a TextComponent but no PositionComponent.");
            }
        }

        texts.sort(new Comparator<Pair<TextComponent, PositionComponent>>() {
            @Override
            public int compare(Pair<TextComponent, PositionComponent> s1, Pair<TextComponent, PositionComponent> s2) {
                return Integer.compare(s1.second.z_index, s2.second.z_index);
            }
        });

        int i_sprite = 0;
        int i_text = 0;
        for (int c = 0; c < sprites.size() + texts.size(); c++) {
            Pair<SpriteComponent, PositionComponent> sprite = null;
            Pair<TextComponent, PositionComponent> text = null;
            if (i_sprite < sprites.size()) {
                sprite = sprites.get(i_sprite);
            }
            if (i_text < texts.size()) {
                text = texts.get(i_text);
            }

            if (sprite != null && text != null) {
                if (sprite.second.z_index > text.second.z_index) {
                    graphicsController.drawText(text.first, text.second);
                    i_text++;
                } else {
                    graphicsController.draw(sprite.first, sprite.second);
                    i_sprite++;
                }
            } else if (sprite != null) {
                if (sprite.first.texture_path == "button_placeholder.png") {
                    // java.lang.System.out.println(sprite.first.size_uv);
                }
                graphicsController.draw(sprite.first, sprite.second);
                i_sprite++;
            } else if (text != null) {
                graphicsController.drawText(text.first, text.second);
                i_text++;
            }
        }

        for (Entity entity : entities) {
            Optional<HealthComponent> health = healthManager.getComponent(entity);
            Optional<PositionComponent> position = positionManager.getComponent(entity);
            Optional<SpriteComponent> sprite = drawableManager.getComponent(entity);
            // render health bars with two rectangles using graphicsController
            if (health.isPresent() && position.isPresent() && sprite.isPresent()) {
                float width = 0.05f;
                float height = 0.008f;
                float[] green = { 0.451f, 0.922f, 0.333f };
                float[] yellow = { 0.922f, 0.922f, 0.333f };
                float[] red = { 0.922f, 0.333f, 0.333f };
                graphicsController.drawSquare(position.get(), width, height, 0, 0, 0, 1);

                if ((float) health.get().getHealth() / (float) health.get().getMaxHealth() > 0.5) {
                    graphicsController.drawSquare(position.get(),
                            ((float) health.get().getHealth() / (float) health.get().getMaxHealth()) * width,
                            height, green[0], green[1], green[2], 1);
                } else if ((float) health.get().getHealth() / (float) health.get().getMaxHealth() > 0.2) {
                    graphicsController.drawSquare(position.get(),
                            ((float) health.get().getHealth() / (float) health.get().getMaxHealth()) * width,
                            height, yellow[0], yellow[1], yellow[2], 1);
                } else {
                    graphicsController.drawSquare(position.get(),
                            ((float) health.get().getHealth() / (float) health.get().getMaxHealth()) * width,
                            height, red[0], red[1], red[2], 1);
                }
                java.lang.System.out.println("DRAWN HEALTH BAR");
            }
        }
    }

}