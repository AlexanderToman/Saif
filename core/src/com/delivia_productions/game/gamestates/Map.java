package com.delivia_productions.game.gamestates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.gamestates.PlayState;
import com.delivia_productions.game.managers.MainMenuScreen;

import javax.swing.Action;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Map implements Screen {

    private final MarioBros app;
    private Stage stage;
    private Skin skin;
    private Image backgroundImg;
    private Image backgroundImg_2;

    private TextButton returnButton;

    private double timing = 3;
    private double delay = 1; // seconds



    public Map(final MarioBros app) {
        this.app = app;
        this.stage = new Stage(new FillViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, app.camera));



    }

    @Override
    public void show() {


        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin();

        //this will definitely crash
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));

        this.skin.add("default-font", app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));



        Texture splashTex = new Texture(Gdx.files.internal("mapview.png"));
        backgroundImg = new Image(splashTex);
        backgroundImg.setSize(stage.getWidth(), stage.getHeight());

        Texture splashTex_2 = new Texture(Gdx.files.internal("mapview_2.png"));
        backgroundImg_2 = new Image(splashTex_2);
        backgroundImg_2.setSize(stage.getWidth(), stage.getHeight());

        stage.addActor(backgroundImg_2);
        stage.addActor(backgroundImg);

        initButton();
    }


    @Override
    public void render(float delta) {


        update(delta);

        stage.draw();


        if(timing == 3) {
            backgroundImg.addAction(alpha(1));
            backgroundImg_2.addAction(alpha(0));
            timing--;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    backgroundImg.addAction(alpha(0));
                    backgroundImg_2.addAction(alpha(1));

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            backgroundImg.addAction(alpha(1));
                            backgroundImg_2.addAction(alpha(0));
                            timing = 3;
                        }
                    }, (float) delay);
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


    @Override
    public void dispose() {
        stage.dispose();
    }


    private void initButton() {
        returnButton = new TextButton("back", skin, "default");
        returnButton.setPosition(stage.getWidth() / 16, stage.getHeight() / 2 + 60);
        returnButton.setSize(50, 50);
        returnButton.addAction(sequence(alpha(0)));
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MainMenuScreen(app));

                FileHandle cdds = Gdx.files.local("audio/sounds_control.txt");
                String textz = cdds.readString();

                if (textz.equals("on")) {
                    MarioBros.manager.get("audio/sounds/return_button.ogg", Sound.class).play();
                }


            }

        });

        stage.addActor(returnButton);
    }

}