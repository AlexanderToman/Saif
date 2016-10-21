package com.delivia_productions.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.delivia_productions.game.managers.LoadingScreen;
import com.delivia_productions.game.managers.MainMenuScreen;


public class MarioBros extends Game {
    //Virtual Screen size and Box2D Scale(Pixels Per Meter)


    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    public static final float PPM = 100;
    public static final float VERSION = .8f;

    //Box2D Collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short MARIO_HEAD_BIT = 512;
    public static final short FIREBALL_BIT = 1024;


    public static final short FINISHLINE_BIT = 1048;
    public static final short MAP_BIT = 2048;
    public static final short KILL_BIT = 4096;
    public static final short STORY_BIT = 8192;
    public static final short PLAY_BIT = 16384;


    public SpriteBatch batch;


    /* WARNING Using AssetManager in a static way can cause issues, especially on Android.
    Instead you may want to pass around Assetmanager to those the classes that need it.
    We will use it in the static context to save time for now. */
    public static AssetManager manager;

    public OrthographicCamera camera;

    public BitmapFont font24;

    public AssetManager assets;

    public MainMenuScreen mainMenuScreen;


    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

        manager = new AssetManager();
        manager.load("audio/music/ending_music.ogg", Music.class);
        manager.load("audio/music/main_menu_music.ogg", Music.class);
        manager.load("audio/music/main_menu_music_2.ogg", Music.class);

        manager.load("audio/music/nature.ogg", Music.class);
        manager.load("audio/music/fireball.ogg", Music.class);
        manager.load("audio/music/stage_clear.ogg", Music.class);
        manager.load("audio/music/died.ogg", Music.class);

        manager.load("audio/music/level_music_2.ogg", Music.class);
        manager.load("audio/music/level_music_1.ogg", Music.class);

        manager.load("audio/sounds/menu_click.ogg", Sound.class);
        manager.load("audio/sounds/menu_click_2.ogg", Sound.class);
        manager.load("audio/sounds/return_button.ogg", Sound.class);
        manager.load("audio/sounds/stomp.ogg", Sound.class);
        manager.finishLoading();

        initFonts();
        assets = new AssetManager();
        mainMenuScreen = new MainMenuScreen(this);

        this.setScreen(new LoadingScreen(this));
    }


    @Override
    public void dispose() {
        super.dispose();

        manager.dispose();
        batch.dispose();
        assets.dispose();
        mainMenuScreen.dispose();
        font24.dispose();
        this.getScreen().dispose();


    }

    @Override
    public void render() {
        super.render();
    }
    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/dade.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 12;
        params.color = Color.WHITE;


        font24 = generator.generateFont(params);


    }

}
