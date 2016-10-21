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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.gamestates.PlayState;
import com.delivia_productions.game.managers.MainMenuScreen;

import javax.swing.Action;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Story implements Screen {

    private final MarioBros app;
    private Stage stage;

    private Skin skin;

//    private TextButton buttonPlay, buttonExit, buttonStory, buttonSettings, buttonMap;


    private Image backgroundImg;

    private ShapeRenderer shapeRenderer;
    private TextButton buttonPlay, buttonExit, buttonStory, buttonSettings, buttonMap;



    public static float SCALE_RATIO = 750 / Gdx.graphics.getWidth();


    public Story(final MarioBros app) {
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

        Texture splashTex = new Texture(Gdx.files.internal("story.png"));
        backgroundImg = new Image(splashTex);
        backgroundImg.setSize(stage.getWidth(), stage.getHeight());
//        backgroundImg.addAction((sequence(alpha(0), fadeIn(2.25f))));

        stage.addActor(backgroundImg);

        initButton();
    }


    @Override
    public void render(float delta) {


        update(delta);

        stage.draw();


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
        buttonPlay = new TextButton("back", skin, "default");
        buttonPlay.setPosition(stage.getWidth() / 16, stage.getHeight() / 2 + 60);
        buttonPlay.setSize(50, 50);
        buttonPlay.addAction(sequence(alpha(0)));
        buttonPlay.addListener(new ChangeListener() {
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

        stage.addActor(buttonPlay);
    }
    @Override
    public void dispose() {
        stage.dispose();
    }

}