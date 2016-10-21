package com.delivia_productions.game.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.managers.SplashScreen;

public class LoadingScreen implements Screen {

    private final MarioBros app;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private float progress;

    public LoadingScreen(MarioBros app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, app.camera));

        this.shapeRenderer = new ShapeRenderer();


    }
    private void queueAssets() {
        app.assets.load("img/splash.png", Texture.class);
        app.assets.load("main_menu.png", Texture.class);
        app.assets.load("main_menu_2.png", Texture.class);
        app.assets.load("ui/uiskin.atlas", TextureAtlas.class);
        app.assets.load("mapview.png", Texture.class);
        app.assets.load("mapview_2.png", Texture.class);
        app.assets.load("story.png", Texture.class);
        app.assets.load("settingsmanager.png", Texture.class);
        app.assets.load("settingsmanager_audio.png", Texture.class);
        app.assets.load("settingsmanager_music.png", Texture.class);
        app.assets.load("img/TheEnd.png",Texture.class);
        app.assets.load("img/najran.png",Texture.class);
        app.assets.load("img/abha.png",Texture.class);

        app.assets.load("img/shar.png",Texture.class);
        app.assets.load("img/riyadh.png",Texture.class);
        app.assets.load("img/hafar.png",Texture.class);
        app.assets.load("img/hael.png",Texture.class);
        app.assets.load("img/tabuk.png",Texture.class);
        app.assets.load("img/qassim.png",Texture.class);
        app.assets.load("img/madinah.png",Texture.class);
        app.assets.load("img/jeddah.png",Texture.class);
    }

    @Override
    public void show() {

        this.progress = 0f;

        queueAssets();
    }

    private void update() {

        //a + (b-a)*lerp
        progress = MathUtils.lerp(progress, app.assets.getProgress(), 0.05f);
        if (app.assets.update() && progress >= app.assets.getProgress() - 0.01f) {
            app.setScreen(new SplashScreen(app));

    }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        update();
//
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(775, app.camera.viewportHeight / 2 - 8, app.camera.viewportWidth - 64, 8);


        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(775, app.camera.viewportHeight / 2 - 8, progress * (app.camera.viewportWidth - 64), 8);

        shapeRenderer.end();
//
//        app.batch.begin();
////        app.font24.draw(app.batch, "Screen: LOADING", 20, 20);
//        app.batch.end();


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
        shapeRenderer.dispose();
    }


}
