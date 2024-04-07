package com.softwarearchitecture.game_server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * The TexturePack is a collection contains all the textures used in the game.
 */
public class TexturePack {
    // Backgrounds
    public static final String BACKGROUND_TOR = "background_tor.png";
    public static final String BACKGROUND_VIKING_BATTLE_ICE = "background_viking_battle_ice.png";
    public static final String BACKGROUND_VIKING_BATTLE_FIRE = "background_viking_battle_fire.png";
    public static final String BACKGROUND_FENRIS = "background_fenris.png";
    public static final String BACKGROUND_GRIFFIN = "background_griffin.png";
    public static final String BACKGROUND_ABYSS = "chad.jpg";

    // Map-textures
    public static final Texture placeableTexture = new Texture(Gdx.files.internal("grass.png"));
    public static final Texture pathTexture = new Texture(Gdx.files.internal("road.jpg"));
    public static final Texture waterTexture = new Texture(Gdx.files.internal("water.jpg"));
    public static final Texture rockTexture = new Texture(Gdx.files.internal("rock.png"));
    public static final Texture treeTexture = new Texture(Gdx.files.internal("tree.png"));
    public static final Texture defaultTexture = new Texture(Gdx.files.internal("chad.jpg"));

    // Buttons
    public static final String BUTTON_PLACEHOLDER = "button_placeholder.png";

}
