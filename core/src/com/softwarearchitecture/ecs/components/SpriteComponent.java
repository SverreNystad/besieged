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
    public Vector2 size_uv;

    /**
     * Constructs a new SpriteComponent with specified texture, position, and size.
     * 
     * @param texture_path Path to the texture image.
     * @param size_uv      The size of the entity on the screen, expressed as a fraction of the screen's width and height.
     */
    public SpriteComponent(String texture_path, Vector2 size_uv) {
        this.texture_path = texture_path;
        this.size_uv = size_uv;
    }

}
