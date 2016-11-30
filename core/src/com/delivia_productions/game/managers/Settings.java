package com.delivia_productions.game.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.gamestates.PlayState;
import com.delivia_productions.game.managers.MainMenuScreen;

import java.util.Random;

import javax.swing.Action;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Settings implements Screen {

    private final MarioBros app;
    private Stage stage;

    private Skin skin;


    private Image backgroundImg, music_icon, audio_icon;

    private ShapeRenderer shapeRenderer;

    private TextButton buttonReturn, MusicButton, AudioButton;

    public static float SCALE_RATIO = 750 / Gdx.graphics.getWidth();

    private Music mymusic;

    private Label labelAudio,labelMusic;

    public Settings(final MarioBros app) {
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

//
//        backgroundImg.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);//
//        backgroundImg.addAction(sequence(alpha(0), scaleTo(0.1f, 0.1f),
//                parallel(fadeIn(2f, Interpolation.pow2), scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
//                        moveTo(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 32, 2f, Interpolation.swing)
//
//                ),
//                delay(1.5f)
//
//        ));

        Texture splashTex = new Texture(Gdx.files.internal("settingsmanager.png"));
        backgroundImg = new Image(splashTex);
        backgroundImg.setSize(stage.getWidth(), stage.getHeight());

        Texture bizk = new Texture(Gdx.files.internal("settingsmanager_music.png"));
        music_icon = new Image(bizk);
        music_icon.setSize(stage.getWidth(), stage.getHeight());

        Texture bai = new Texture(Gdx.files.internal("settingsmanager_audio.png"));
        audio_icon = new Image(bai);
        audio_icon.setSize(stage.getWidth(), stage.getHeight());

        FileHandle filezz = Gdx.files.local("audio/sounds_control.txt");
        String textzz = filezz.readString();

        if (textzz.equals("on")) {
            audio_icon.addAction(alpha(0));
        } else {
            audio_icon.addAction(alpha(1));
        }

        FileHandle pfile = Gdx.files.local("audio/music_control.txt");
        String textz = pfile.readString();

        if (textz.equals("on")) {
            music_icon.addAction(alpha(0));
        } else {
            music_icon.addAction(alpha(1));
        }


        stage.addActor(backgroundImg);
        stage.addActor(music_icon);
        stage.addActor(audio_icon);
        initInfoLabel();

    }


    @Override
    public void render(float delta) {


        update(delta);

        stage.draw();


        initButton();
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
        buttonReturn = new TextButton("back", skin, "default");
        buttonReturn.setPosition(stage.getWidth() / 16, stage.getHeight() / 2 + 60);
        buttonReturn.setSize(50, 50);
        buttonReturn.addAction(sequence(alpha(0)));
        buttonReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MainMenuScreen(app));
                FileHandle cdds = Gdx.files.local("audio/sounds_control.txt");
                String textz = cdds.readString();

                if (textz.equals("on")) {
                    MarioBros.manager.get("audio/sounds/return_button.ogg", Sound.class).play();
                } else {
                }


            }

        });





        MusicButton = new TextButton("On", skin, "default");
        MusicButton.setPosition(stage.getWidth() / 2 - 20, stage.getHeight() / 2 + 55);
        MusicButton.setSize(45, 30);
        MusicButton.addAction(sequence(alpha(0)));
        MusicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileHandle file = Gdx.files.local("audio/music_control.txt");
                String text = file.readString();

                if (text.equals("on")) {
                    file.writeString("off", false);
                    music_icon.addAction(alpha(1));

                    MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class).stop();


                    labelMusic.clearActions();
                    labelMusic.setText("Music Off");
                    labelMusic.addAction(sequence(alpha(0), delay(0.25f), fadeIn(0.5f), delay(1f), fadeOut(0.5f)));
                } else {

                    tRm();


                    file.writeString("on", false);
                    music_icon.addAction(alpha(0));








                    labelMusic.clearActions();
                    labelMusic.setText("Music On");
                    labelMusic.addAction(sequence(alpha(0), delay(0.25f), fadeIn(0.5f), delay(1f), fadeOut(0.5f)));

                }

            }



        });


        AudioButton = new TextButton("On", skin, "default");
        AudioButton.setPosition(stage.getWidth() / 2 - 20, stage.getHeight() / 2 + 10);
        AudioButton.setSize(45, 30);
        AudioButton.addAction(sequence(alpha(0)));
        AudioButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        FileHandle file = Gdx.files.local("audio/sounds_control.txt");
                                        String text = file.readString();

                                        if (text.equals("on")) {
                                            file.writeString("off", false);
                                            audio_icon.addAction(alpha(1));

                                            labelAudio.clearActions();
                                            labelAudio.setText("Sounds Off");
                                            //labelAudio.addAction(sequence(alpha(1f), delay(3f), fadeOut(2f, Interpolation.pow5Out)));
                                            labelAudio.addAction(sequence(alpha(0), delay(0.25f), fadeIn(0.5f),delay(1f),fadeOut(0.5f)));


                                        } else {
                                            file.writeString("on", false);
                                            audio_icon.addAction(alpha(0));
                                            labelAudio.clearActions();
                                            labelAudio.setText("Sounds On");
                                            labelAudio.addAction(sequence(alpha(0), delay(0.25f), fadeIn(0.5f),delay(1f),fadeOut(0.5f)));
                                        }

                                    }
                                }


        );


        stage.addActor(MusicButton);
        stage.addActor(AudioButton);
        stage.addActor(buttonReturn);
    }



    // Initialize the info label ;
    private void initInfoLabel() {
        labelAudio = new Label("", skin, "default");
        labelAudio.setPosition(app.camera.viewportWidth / 2 - 1, app.camera.viewportHeight / 8 + 12);
        labelAudio.setAlignment(Align.center);
        labelAudio.addAction(sequence(alpha(0), delay(0.5f), fadeIn(0.5f)));

        labelMusic = new Label("", skin, "default");
        labelMusic.setPosition(app.camera.viewportWidth / 2 - 1, app.camera.viewportHeight / 8 + 12);
        labelMusic.setAlignment(Align.center);
        labelMusic.addAction(sequence(alpha(0), delay(0.5f), fadeIn(0.5f)));



        stage.addActor(labelMusic);
        stage.addActor(labelAudio);


    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void tRm() {


            mymusic = MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class);
            mymusic.setVolume(0.3f);
            mymusic.play();
            mymusic.setLooping(true);


    }

}