package com.delivia_productions.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.delivia_productions.game.gamestates.PlayState;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Controller implements Screen {
    Viewport viewport;
    boolean upPressed, downPressed, leftPressed, rightPressed, backPressed;
    OrthographicCamera cam;


    //personally made


    Stage stage;


    //booleans




    public Controller() {


        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 445, cam);
        stage = new Stage(viewport, PlayState.batch);



        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();

        Image upImg = new Image(new Texture("jumpactor.png"));
        upImg.setSize(150, 350);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                upPressed = true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image downImg = new Image(new Texture("fireactor.png"));
        downImg.setSize(150, 350);


        downImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;


                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("flatDark24.png"));
        rightImg.setSize(250, 350);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = new Image(new Texture("flatDark25.png"));
        leftImg.setSize(300, 350);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image backImg = new Image(new Texture("back_button.png"));
        backImg.setSize(200, 100);
        backImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                backPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backPressed = false;
            }
        });



//        table.row().pad(-350, -10, -10, 100);


        table.add(backImg).size(backImg.getWidth(), backImg.getHeight());
        table.add();
        table.row().padBottom(5);
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        table.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        table.add();
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.add();



        stage.addActor(table);


    }

    public void draw() {

        stage.draw();

    }

    public boolean isUpPressed() {

        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }
    public boolean isBackPressed() {
        return backPressed;
    }


    public boolean isRightPressed() {
        return rightPressed;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {



    }

    public void resize(int width, int height) {
        viewport.update(width, height);
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

    }


}
