package com.delivia_productions.game.managers;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.gamestates.PlayState;

public class ResetPosition implements Screen {

    private Game game;

    private Music Main_Menu_music;
    boolean sounds_exists = Gdx.files.local("audio/sounds_control.txt").exists();

    public ResetPosition(Game game) {
        this.game = game;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (sounds_exists) {
            FileHandle file = Gdx.files.local("audio/sounds_control.txt");
            String text = file.readString();

            if (text.equals("on")) {
                Main_Menu_music = MarioBros.manager.get("audio/music/died.ogg", Music.class);
                Main_Menu_music.play();

            }
            if (text.equals("off")) {


            }
        } else {
            FileHandle file = Gdx.files.local("audio/sounds_control.txt");
            file.writeString("on", false);
            Main_Menu_music = MarioBros.manager.get("audio/music/died.ogg", Music.class);
            Main_Menu_music.play();

        }

        game.setScreen(new PlayState((MarioBros) game));
        dispose();


    }

    @Override
    public void resize(int width, int height) {

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
