package com.delivia_productions.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.managers.MainMenuScreen;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import com.badlogic.gdx.math.Interpolation;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TheEnd implements Screen {

    private final MarioBros app;
    private Stage stage;

    float acc = 0f;


    private Image backgroundImg, music_icon, audio_icon;



    public TheEnd(final MarioBros app) {
        this.app = app;
        this.stage = new Stage(new FillViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, app.camera));


        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();

        if (text.equals("z")) {
            file.writeString("a", false);
        }

    }

    @Override
    public void show() {




        Texture splashTex = new Texture(Gdx.files.internal("img/TheEnd.png"));
        backgroundImg = new Image(splashTex);
        backgroundImg.setSize(stage.getWidth(), stage.getHeight());



        stage.addActor(backgroundImg);


    }


    @Override
    public void render(float delta) {


        update(delta);

        stage.draw();


    }

    public void update(float delta) {
        acc += delta;
        if(acc >= 6){
            app.setScreen(new MainMenuScreen(app));
        }

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