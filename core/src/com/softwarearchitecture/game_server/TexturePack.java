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

    // Tiles
    public static final String PLACEABLE = "grass.png";
    public static final String PATH = "road.jpg";
    public static final String WATER = "water.jpg";
    public static final String ROCK = "rock.png";
    public static final String TREE = "tree.png";
    public static final String DEFAULT = "chad.jpg";
    // Buttons
    public static final String BUTTON_PLACEHOLDER = "button_placeholder.png";
    public static final String BUTTON_BACK = "button_placeholder.png";
    public static final String BUTTON_OPTION = "button_placeholder.png";
    public static final String BUTTON_GAME_MENU = "button_placeholder.png";
    public static final String BUTTON_QUIT = "button_placeholder.png";
    public static final String BUTTON_JOIN = "button_placeholder.png";
    public static final String BUTTON_HOST = "button_placeholder.png";
    public static final String BUTTON_PAUSE = "button_placeholder.png";
    public static final String BUTTON_MULTI_PLAYER = "button_placeholder.png";
    public static final String BUTTON_SINGLE_PLAYER = "button_placeholder.png";
    public static final String BUTTON_PLAY = "button_placeholder.png";
    public static final String BUTTON_PLUSS = "button_placeholder.png";
    public static final String BUTTON_MINUS = "button_placeholder.png";
    public static final String BUTTON_MUTE = "button_placeholder.png";

    // Cards
    public static final String CARD_ICE = "chad.jpg";
    public static final String CARD_FIRE = "water.jpg";
    public static final String CARD_TECHNOLOGY = "chad.jpg";
    public static final String CARD_LIGHTNING = "chad.jpg";
    public static final String CARD_BOW = "chad.jpg";
    public static final String CARD_MAGIC = "chad.jpg";

    // Towers
    public static final String FIRE_MAGIC = "chad.jpg";
    public static final String ICE_MAGIC = "chad.jpg";
    public static final String TOR = "chad.jpg";
    public static final String MAGIC = "chad.jpg";
    public static final String FIRE_BOW = "chad.jpg";
    public static final String SHARP_SHOOTER = "chad.jpg";
    public static final String BOW = "chad.jpg";

    // Towers 2
    public static final Texture TOWER_TOR_FRAME1 = new Texture("tor_frame1.png");
    public static final Texture TOWER_TOR_FRAME2 = new Texture("tor_frame2.png");
    public static final Texture TOWER_TOR_FRAME3 = new Texture("tor_frame3.png");

    public static final Texture TOWER_MAGIC_FRAME1 = new Texture("magic_tower_frame1.png");
    public static final Texture TOWER_MAGIC_FRAME2 = new Texture("magic_tower_frame2.png");
    public static final Texture TOWER_MAGIC_FRAME3 = new Texture("magic_tower_frame3.png");

    public static final Texture TOWER_FIRE_FRAME1 = new Texture("magic_fire_frame1.png");
    public static final Texture TOWER_FIRE_FRAME2 = new Texture("magic_fire_frame2.png");
    public static final Texture TOWER_FIRE_FRAME3 = new Texture("magic_fire_frame3.png");

    public static final Texture TOWER_FIRE_ATTACK_FRAME1 = new Texture("magic_fire_attack_frame1.png");
    public static final Texture TOWER_FIRE_ATTACK_FRAME2 = new Texture("magic_fire_attack_frame2.png");
    public static final Texture TOWER_FIRE_ATTACK_FRAME3 = new Texture("magic_fire_attack_frame3.png");

    public static final Texture TOWER_BOW = new Texture("bow_tower.png");
    public static final Texture TOWER_BOW_FIRE = new Texture("bow_fire.png");
    public static final Texture TOWER_SHARPSHOOTER = new Texture("sharpshooter_tower.png");


    // Enemies
    public static final Texture ENEMY_ANT_FRAME1 = new Texture("ant_frame1.png");
    public static final Texture ENEMY_ANT_FRAME2 = new Texture("ant_frame2.png");
    public static final Texture ENEMY_ANT_FRAME3 = new Texture("ant_frame3.png");
    public static final Texture ENEMY_ANT_FRAME4 = new Texture("ant_frame4.png");

    public static final Texture ENEMY_DEAD_ANT__FRAME1 = new Texture("dead_ant_frame1.png");
    public static final Texture ENEMY_DEAD_ANT__FRAME2 = new Texture("dead_ant_frame2.png");
    

}
