package com.delivia_productions.game.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;

import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.gamestates.Map;
import com.delivia_productions.game.gamestates.PlayState;
import com.delivia_productions.game.gamestates.Story;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen {

    private final MarioBros app;
    private Stage stage;

    private Skin skin;

    private TextButton buttonPlay, buttonExit, buttonStory, buttonSettings, buttonMap;


    private Image backgroundImg;
    private Image backgroundImg_2;

    private ShapeRenderer shapeRenderer;

    private Music mymusic;
    public static float SCALE_RATIO = 750 / Gdx.graphics.getWidth();


    private float timePassed = 0;

    private double timing = 3;
    private double delay = 3; // seconds
    private double delay_milisecond = 0.2; // seconds
    private double delay_long = 5;


    public MainMenuScreen(final MarioBros app) {
        this.app = app;
        this.stage = new Stage(new FillViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();


    }

    @Override
    public void show() {


        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin();

        //this will definitely crash
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));

        this.skin.add("default-font", app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        Texture splashTex = new Texture(Gdx.files.internal("main_menu.png"));
        backgroundImg = new Image(splashTex);
        backgroundImg.setSize(stage.getWidth(), stage.getHeight());

        Texture splashTex_2 = new Texture(Gdx.files.internal("main_menu_2.png"));
        backgroundImg_2 = new Image(splashTex_2);
        backgroundImg_2.setSize(stage.getWidth(), stage.getHeight());


        //if the relocater came from the SplashScreen, play these animations
        FileHandle file = Gdx.files.local("animations/dr.txt");
        String pizza = file.readString();

        if (pizza.equals("splash")) {
            backgroundImg.addAction((sequence(alpha(1f), fadeIn(1.25f))));
        }


        stage.addActor(backgroundImg);
        stage.addActor(backgroundImg_2);

        initButton();


        FileHandle pfile = Gdx.files.local("audio/music_control.txt");
        String textz = pfile.readString();

        if (textz.equals("on")) {


            FileHandle sacadscasZZ = Gdx.files.local("audio/MMMRM.txt");
            String bz = sacadscasZZ.readString();
            if (bz.equals("1")) {
                mymusic = MarioBros.manager.get("audio/music/main_menu_music.ogg", Music.class);
                mymusic.setVolume(0.7f);
                mymusic.play();
                mymusic.setLooping(true);
                MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class).stop();
            }


            if (bz.equals("2")) {

                mymusic = MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class);
                mymusic.setVolume(0.3f);
                mymusic.play();
                mymusic.setLooping(true);
                MarioBros.manager.get("audio/music/main_menu_music.ogg", Music.class).stop();
            }


            MarioBros.manager.get("audio/music/nature.ogg", Music.class).stop();
            MarioBros.manager.get("audio/music/level_music_1.ogg", Music.class).stop();
            MarioBros.manager.get("audio/music/level_music_2.ogg", Music.class).stop();


        }
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();


        if (timing == 3) {
            timing--;

            // EYES ON

            backgroundImg_2.addAction(fadeOut(0f));


            // EYES OFF
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    backgroundImg.addAction(fadeOut(0f));
                    backgroundImg_2.addAction(fadeIn(0f));


                    // EYES ON
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            backgroundImg.addAction(fadeIn(0f));
                            backgroundImg_2.addAction(fadeOut(0f));


                            // EYES OFF
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    backgroundImg.addAction(fadeOut(0f));
                                    backgroundImg_2.addAction(fadeIn(0f));

                                    // EYES ON
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            backgroundImg.addAction(fadeIn(0f));
                                            backgroundImg_2.addAction(fadeOut(0f));
                                            timing = 3;

                                        }

                                    }, (float) delay_milisecond);


                                }

                            }, (float) delay_long);


                        }

                    }, (float) delay_milisecond);

                }


            }, (float) delay);
        }


    }

    public void update(float delta) {
        stage.act(delta);

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    private void initButton() {
        buttonPlay = new TextButton("Play", skin, "default");
        buttonPlay.setPosition(stage.getWidth() / 8, stage.getHeight() / 2 + 30);
        buttonPlay.setSize(300, 20);
        buttonPlay.addAction(sequence(alpha(0)));
        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new PlayState(app));

                FileHandle cdds = Gdx.files.local("audio/sounds_control.txt");
                String textz = cdds.readString();

                if (textz.equals("on")) {
                    MarioBros.manager.get("audio/sounds/menu_click.ogg", Sound.class).play();
                }


            }
        });
        buttonStory = new TextButton("Tale", skin, "default");
        buttonStory.setPosition(stage.getWidth() / 8, stage.getHeight() / 2);
        buttonStory.setSize(300, 20);
        buttonStory.addAction(sequence(alpha(0)));
        buttonStory.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new Story(app));

                FileHandle cdds = Gdx.files.local("audio/sounds_control.txt");
                String textz = cdds.readString();

                if (textz.equals("on")) {
                    MarioBros.manager.get("audio/sounds/menu_click.ogg", Sound.class).play();
                }


            }
        });


        buttonMap = new TextButton("Map", skin, "default");
        buttonMap.setPosition(stage.getWidth() / 8, stage.getHeight() / 2 - 30);
        buttonMap.setSize(300, 20);
        buttonMap.addAction(sequence(alpha(0)));
        buttonMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                FileHandle cdds = Gdx.files.local("audio/sounds_control.txt");
                String textz = cdds.readString();

                if (textz.equals("on")) {
                    MarioBros.manager.get("audio/sounds/menu_click.ogg", Sound.class).play();
                }

                app.setScreen(new Map(app));

            }
        });

        buttonSettings = new TextButton("Settings", skin, "default");
        buttonSettings.setPosition(stage.getWidth() / 8, stage.getHeight() / 2 - 55);
        buttonSettings.setSize(300, 20);
        buttonSettings.addAction(sequence(alpha(0)));
        buttonSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new Settings(app));

                FileHandle cdds = Gdx.files.local("audio/sounds_control.txt");
                String textz = cdds.readString();

                if (textz.equals("on")) {
                    MarioBros.manager.get("audio/sounds/menu_click.ogg", Sound.class).play();
                }


            }
        });

        buttonExit = new TextButton("Exit", skin, "default");
        buttonExit.setPosition(stage.getWidth() / 18, stage.getHeight() / 2 - 100);
        buttonExit.setSize(400, 20);
        buttonExit.addAction(sequence(alpha(0)));
        buttonExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(buttonSettings);
        stage.addActor(buttonMap);
        stage.addActor(buttonStory);
        stage.addActor(buttonExit);
        stage.addActor(buttonPlay);


    }

    @Override
    public void dispose() {
        stage.dispose();

    }

}