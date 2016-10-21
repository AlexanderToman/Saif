package com.delivia_productions.game.gamestates;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.managers.MainMenuScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class FinishLine implements Screen {
    private Viewport viewport;

    private Music drops;
    private Music bproz;
    private Game game;
    private String stringat = "finish line";

    float acc = 0f;

    private final MarioBros app;
    private Stage stage;

    private Image splashImg;


    public FinishLine(final MarioBros app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);


        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();

        if (text.equals("b")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/najran.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);

        }
        if (text.equals("d")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/abha.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("f")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/jeddah.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("h")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/madinah.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("j")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/qassim.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("l")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/hael.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("n")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/tabuk.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("p")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/hafar.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("r")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/shar.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }
        if (text.equals("t")) {
            Texture splashTex = new Texture(Gdx.files.internal("img/riyadh.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
        }



        if (text.equals("a") || (text.equals("c")) || (text.equals("e")) || (text.equals("g"))
                || (text.equals("i")) || (text.equals("k")) || (text.equals("m"))
                || (text.equals("o")) || (text.equals("q")) || (text.equals("s"))) {

            Texture splashTex = new Texture(Gdx.files.internal("img/splash.png"));
            splashImg = new Image(splashTex);
            splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
            stage.addActor(splashImg);
    }

}


    @Override
    public void show() {


        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {
                app.setScreen(new PlayState(app));
            }
        };
        splashImg.setPosition(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 80);

        splashImg.addAction(sequence(alpha(0), scaleTo(0.1f, 0.1f),
                parallel(fadeIn(2f, Interpolation.pow2),scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                                moveTo(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 12, 2f, Interpolation.swing)

                        ),
                        delay(1.25f), fadeOut(1.25f),

                        run(transitionRunnable)

                ));


        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();


        if (text.equals("a") || (text.equals("c")) || (text.equals("e")) || (text.equals("g"))
                || (text.equals("i")) || (text.equals("k")) || (text.equals("m"))
                || (text.equals("o")) || (text.equals("q")) || (text.equals("s"))) {

            bproz = MarioBros.manager.get("audio/music/stage_clear.ogg", Music.class);
            bproz.play();


        }

        if (text.equals("b") || (text.equals("d")) || (text.equals("f")) || (text.equals("h"))
                || (text.equals("j")) || (text.equals("l")) || (text.equals("n"))
                || (text.equals("q")) || (text.equals("z")) || (text.equals("t"))) {

        }


    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

    }

    public void update(float delta) {
        stage.act(delta);

        acc += delta;

        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();

        if (text.equals("a") || (text.equals("c")) || (text.equals("e")) || (text.equals("g"))
                || (text.equals("i")) || (text.equals("k")) || (text.equals("m"))
                || (text.equals("o")) || (text.equals("q")) || (text.equals("s"))) {

            if (acc >= 0) {
                app.setScreen(new PlayState(app));

            }
        }

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
}
