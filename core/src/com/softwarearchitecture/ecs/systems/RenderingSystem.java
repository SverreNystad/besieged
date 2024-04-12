package com.softwarearchitecture.ecs.systems;

import com.softwarearchitecture.ecs.System;
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
            public int compare(Pair<SpriteComponent, PositionComponent> s1, Pair<SpriteComponent, PositionComponent> s2) {
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
    }

}