package com.delivia_productions.game.managers;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.delivia_productions.game.MarioBros;

import java.util.Random;

import javax.swing.Action;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {

    private final MarioBros app;
    private Stage stage;
    private Image splashImg;

//    long startTime = 0;

    private Music mymusic;
    //   private double saif_3_timer = 2;

    boolean music_exists = Gdx.files.local("audio/music_control.txt").exists();
    boolean sounds_exists = Gdx.files.local("audio/sounds_control.txt").exists();
    boolean main_menu = Gdx.files.local("animations/dr.txt").exists();
    boolean MMRM = Gdx.files.local("audio/MMMRM.txt").exists();

    public SplashScreen(final MarioBros app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);

        Texture splashTex = new Texture(Gdx.files.internal("img/splash.png"));
        splashImg = new Image(splashTex);
        splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);

        stage.addActor(splashImg);


        if(main_menu) {


        }else{
            FileHandle file = Gdx.files.local("animations/dr.txt");
            file.writeString("splash", false);
        }


            if (music_exists) {
            } else {
                FileHandle sdfsgs = Gdx.files.local("audio/music_control.txt");
                sdfsgs.writeString("on", false);

            }


        if (MMRM) {
        } else {
                tRm();
    }




    if (sounds_exists) {
        } else {
            FileHandle sacadscas = Gdx.files.local("audio/sounds_control.txt");
            sacadscas.writeString("on", false);

        }
    }




    private void tRm() {

        Random rasdad = new Random();
        int random_number = rasdad.nextInt(2) + 1;


        if (random_number == 2) {
            FileHandle sacadscasZZ = Gdx.files.local("audio/MMMRM.txt");
            sacadscasZZ.writeString("2", false);
        }
        if (random_number == 1) {
            FileHandle sacadscasZZ = Gdx.files.local("audio/MMMRM.txt");
            sacadscasZZ.writeString("1", false);
        }

    }

    @Override
    public void show() {

        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {
                app.setScreen(new MainMenuScreen(app));
            }
        };
        splashImg.setPosition(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 32);

        splashImg.addAction(sequence(alpha(0), scaleTo(0.1f, 0.1f),
                parallel(fadeIn(2f, Interpolation.pow2), scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 32, 2f, Interpolation.swing)

                ),
                delay(1.5f), fadeOut(3.25f),

                run(transitionRunnable)

        ));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        app.batch.begin();
//        app.font24.draw(app.batch, "SplashScreen!", 120, 120);
        app.batch.end();


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
}
