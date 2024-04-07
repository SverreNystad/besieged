package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.math.Vector2;

/**
 * Represents a drawable component of an entity with UV mapping for positioning
 * and sizing on the screen.
 * This component defines where and how an entity's texture is drawn in a 2D
 * space, using relative coordinates and sizes
 * based on the screen dimensions. It allows for flexible UI and game object
 * rendering across different screen sizes.
 *
 * @param screen_u     The relative X-coordinate (U) of the entity on the
 *                     screen, ranging from 0 (left edge) to 1 (right edge).
 * @param screen_v     The relative Y-coordinate (V) of the entity on the
 *                     screen, ranging from 0 (bottom edge) to 1 (top edge).
 * @param u_size       The relative width of the entity on the screen, expressed
 *                     as a fraction of the screen's width (0 to 1).
 * @param v_size       The relative height of the entity on the screen,
 *                     expressed as a fraction of the screen's height (0 to 1).
 * @param texture_path The path to the texture image file used for drawing the
 *                     entity.
 * @param z_index      The Z-index determines the rendering order of entities,
 *                     with higher values rendered in front of lower ones.
 *
 * @see <a href="https://en.wikipedia.org/wiki/UV_mapping">UV mapping on
 *      Wikipedia</a> for more information on UV mapping.
 */
public class SpriteComponent implements Serializable {
    public String texture_path;
    public float screen_u;
    public float screen_v;
    public float u_size;
    public float v_size;
    public int z_index;

    /**
     * Constructs a new SpriteComponent with specified texture, position, and size.
     * 
     * @param texture_path Path to the texture image.
     * @param screen_u     Relative X-coordinate on the screen.
     * @param screen_v     Relative Y-coordinate on the screen.
     * @param width        Relative width of the entity.
     * @param height       Relative height of the entity.
     */
    public SpriteComponent(String texture_path, float screen_u, float screen_v, float width, float height) {
        this.texture_path = texture_path;
        this.screen_u = screen_u;
        this.screen_v = screen_v;
        this.u_size = width;
        this.v_size = height;
        this.z_index = 0;
    }

    public SpriteComponent(String texture_path, float screen_u, float screen_v, float width, float height,
            int z_index) {
        this(texture_path, screen_u, screen_v, width, height);
        this.z_index = z_index;
    }

    public SpriteComponent(String texture, Vector2 size, int z_index2) {
        this(texture, 0, 0, size.x, size.y, z_index2); // TODO find out how to get the screen uv coordinates

    }

    public void setSprite(String currentFrame) {
        this.texture_path = currentFrame;
    }
}
