package com.delivia_productions.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.delivia_productions.game.managers.ResetPosition;
import com.delivia_productions.game.managers.*;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.Sprites.Others.Hud;
import com.delivia_productions.game.Sprites.Enemies.Enemy;
import com.delivia_productions.game.Sprites.Items.Item;
import com.delivia_productions.game.Sprites.Items.ItemDef;
import com.delivia_productions.game.Sprites.Items.Mushroom;
import com.delivia_productions.game.Sprites.Mario;
import com.delivia_productions.game.Tools.B2WorldCreator;
import com.delivia_productions.game.Tools.WorldContactListener;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;


public class PlayState implements Screen {


    //Reference to our Game, used to set Screens
    private MarioBros game;
    private TextureAtlas atlas;
    private TextureAtlas bulbatlas;
    private TextureAtlas Bosses;
    private TextureAtlas Shaheen;
    private TextureAtlas ShaheenJump;
    private TextureAtlas ShaheenDead;
    private TextureAtlas MyFire;


    public static boolean alreadyDestroyed = false;

    private boolean resetpositionz;
    public static SpriteBatch batch;
    public static SpriteBatch mysecondbatch;

    //basic playscreen variables
    private OrthographicCamera camera;
    private Viewport gamePort;
    private Hud hud;


    //Tiled map variables
    public TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Mario player;


    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    Controller controller;


    // PERSONAL

    private int ready_tojump = 3;
    private int ready_to_fire = 1;
    private double delay = 0.8; // seconds
    private double delayhalf = 0.4;

    // files existing checking
    boolean exists = Gdx.files.local("stages/stage_selector.txt").exists();
    boolean waterDrops = false;


    private Viewport viewport;
    private Stage stage;

    private Sprite sprite;


    //final conversation textures and triggers

    private Texture saif_1;
    private Texture saif_2;
    private Texture saif_3;
    private Texture saif_4;

    private Sprite saif_1_sprite;
    private Sprite saif_2_sprite;
    private Sprite saif_3_sprite;
    private Sprite saif_4_sprite;


    private double saif_1_timer = 42;
    private double saif_2_timer = 45;
    private double saif_3_timer = 48;
    private double saif_4_timer = 51;

    private double delaydancing = 10;
    private double EndGame_float = 7;


    public Music nature_music;

    private Music level_stage_music;
    public Music drops;
    private static final float PPM = 32;




    public PlayState(MarioBros game) {


        //personal

        viewport = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MarioBros) game).batch);


        saif_1 = new Texture("conversation/saif_1.png");
        saif_2 = new Texture("conversation/saif_2.png");
        saif_3 = new Texture("conversation/saif_3.png");
        saif_4 = new Texture("conversation/saif_4.png");

        saif_1_sprite = new Sprite(saif_1);
        saif_2_sprite = new Sprite(saif_2);
        saif_3_sprite = new Sprite(saif_3);
        saif_4_sprite = new Sprite(saif_4);

        saif_1_sprite.setAlpha(0f);
        saif_2_sprite.setAlpha(0f);
        saif_3_sprite.setAlpha(0f);
        saif_4_sprite.setAlpha(0f);


        saif_1_sprite.setPosition(1100, 170);
        saif_3_sprite.setPosition(1100, 170);

        saif_2_sprite.setPosition(835, 170);
        saif_4_sprite.setPosition(835, 170);


// two methods for animations this is method B
        atlas = new TextureAtlas("Mario_and_Enemies.pack");


        bulbatlas = new TextureAtlas("bulb.atlas");
        Bosses = new TextureAtlas("bosses.atlas");
        Shaheen = new TextureAtlas("shaheen.atlas");
        ShaheenJump = new TextureAtlas("shaheen_jump.atlas");
        ShaheenDead = new TextureAtlas("deads.atlas");
        MyFire = new TextureAtlas("myfire.atlas");

        this.game = game;
        batch = new SpriteBatch();
        mysecondbatch = new SpriteBatch();
        controller = new Controller();


        //create cam used to follow mario through cam world
        camera = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, camera);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);


        //Load our map and setup our map renderer
        maploader = new

                TmxMapLoader();


        if (exists) {
            FileHandle file = Gdx.files.local("stages/stage_selector.txt");
            String text = file.readString();

            if (text.equals("a")) {
                map = maploader.load("stages/Main_Stage.tmx");
            }
            if (text.equals("b")) {
                map = maploader.load("stages/levels/level1.tmx");
            }
            if (text.equals("c")) {
                map = maploader.load("stages/Second_Stage.tmx");
            }
            if (text.equals("d")) {
                map = maploader.load("stages/levels/level2.tmx");
            }
            if (text.equals("e")) {
                map = maploader.load("stages/Third_Stage.tmx");
            }
            if (text.equals("f")) {
                map = maploader.load("stages/levels/level3.tmx");
            }
            if (text.equals("g")) {
                map = maploader.load("stages/Fourth_Stage.tmx");
            }
            if (text.equals("h")) {
                map = maploader.load("stages/levels/level4.tmx");
            }
            if (text.equals("i")) {
                map = maploader.load("stages/Fifth_Stage.tmx");
            }
            if (text.equals("j")) {
                map = maploader.load("stages/levels/level5.tmx");
            }
            if (text.equals("k")) {
                map = maploader.load("stages/Sixth_Stage.tmx");
            }
            if (text.equals("l")) {
                map = maploader.load("stages/levels/level6.tmx");
            }
            if (text.equals("m")) {
                map = maploader.load("stages/Seventh_Stage.tmx");
            }
            if (text.equals("n")) {
                map = maploader.load("stages/levels/level7.tmx");
            }
            if (text.equals("o")) {
                map = maploader.load("stages/8th_Stage.tmx");
            }
            if (text.equals("p")) {
                map = maploader.load("stages/levels/level8.tmx");
            }
            if (text.equals("q")) {
                map = maploader.load("stages/9th_Stage.tmx");
            }
            if (text.equals("r")) {
                map = maploader.load("stages/levels/level9.tmx");
            }
            if (text.equals("s")) {
                map = maploader.load("stages/10th_Stage.tmx");
            }
            if (text.equals("t")) {
                map = maploader.load("stages/levels/level10.tmx");
            }
            if (text.equals("z")) {
                map = maploader.load("stages/levels/final_stage.tmx");

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        saif_1_sprite.setAlpha(1f);
                    }
                }, (float) saif_1_timer);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        saif_1_sprite.setAlpha(0f);
                        saif_2_sprite.setAlpha(1f);
                    }
                }, (float) saif_2_timer);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        saif_2_sprite.setAlpha(0f);
                        saif_3_sprite.setAlpha(1f);
                    }
                }, (float) saif_3_timer);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        saif_3_sprite.setAlpha(0f);
                        saif_4_sprite.setAlpha(1f);
                        EndGame();
                    }
                }, (float) saif_4_timer);

            }


            // if starting stages file doesn't exist, create a new one and write in the value "a", then load the first main Stage
        } else {
            FileHandle file = Gdx.files.local("stages/stage_selector.txt");
            file.writeString("a", false);
            map = maploader.load("stages/Main_Stage.tmx");


        }


        FileHandle file = Gdx.files.local("audio/music_control.txt");
        String text = file.readString();

        if (text.equals("on")) {


            FileHandle bfile = Gdx.files.local("stages/stage_selector.txt");
            String stage_text = bfile.readString();


            // M A I N _ S T A G E S _ M U S I C
            if (stage_text.equals("a") || (stage_text.equals("c")) || (stage_text.equals("e")) || (stage_text.equals("g"))
                    || (stage_text.equals("i")) || (stage_text.equals("k")) || (stage_text.equals("m"))
                    || (stage_text.equals("q")) || (stage_text.equals("s"))) {

                nature_music = MarioBros.manager.get("audio/music/nature.ogg", Music.class);
                nature_music.play();
                nature_music.setLooping(true);



                MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class).stop();









                MarioBros.manager.get("audio/music/level_music_2.ogg", Music.class).stop();
                MarioBros.manager.get("audio/music/level_music_1.ogg", Music.class).stop();

            }
            //  L E V E L S _ M U S I C
            if (stage_text.equals("b") || (stage_text.equals("f")) || (stage_text.equals("j")) || (stage_text.equals("n"))
                    || (stage_text.equals("t"))) {


                level_stage_music = MarioBros.manager.get("audio/music/level_music_2.ogg", Music.class);
                level_stage_music.play();
                level_stage_music.setVolume(0.7f);
                level_stage_music.setLooping(true);

                nature_music = MarioBros.manager.get("audio/music/nature.ogg", Music.class);
                nature_music.stop();

                level_stage_music = MarioBros.manager.get("audio/music/level_music_1.ogg", Music.class);
                level_stage_music.stop();

                MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class).stop();



            }

            if (stage_text.equals("d") || (stage_text.equals("h"))
                    || (stage_text.equals("l")) || (stage_text.equals("p")) || (stage_text.equals("r"))) {


                level_stage_music = MarioBros.manager.get("audio/music/level_music_1.ogg", Music.class);
                level_stage_music.play();
                level_stage_music.setLooping(true);

                nature_music = MarioBros.manager.get("audio/music/nature.ogg", Music.class);
                nature_music.stop();
                level_stage_music = MarioBros.manager.get("audio/music/level_music_2.ogg", Music.class);
                level_stage_music.stop();

                MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class).stop();



            }


            if (stage_text.equals("z")) {
                level_stage_music = MarioBros.manager.get("audio/music/ending_music.ogg", Music.class);
                level_stage_music.play();

                nature_music = MarioBros.manager.get("audio/music/nature.ogg", Music.class);
                nature_music.stop();
                level_stage_music = MarioBros.manager.get("audio/music/level_music_1.ogg", Music.class);
                level_stage_music.stop();
                level_stage_music = MarioBros.manager.get("audio/music/level_music_2.ogg", Music.class);
                level_stage_music.stop();

                MarioBros.manager.get("audio/music/main_menu_music_2.ogg", Music.class).stop();



            }


        } else {


        }


        renderer = new

                OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

        //initially set our gamcam to be centered correctly at the start of of map
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        //create mario in our game world
        player = new Mario(this);
        world.setContactListener(new WorldContactListener());
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();


        stage.clear();


    }

    public ParticleEffect dustParticles = new ParticleEffect();

    public void EndGame() {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new TheEnd(game));
                dispose();
            }
        }, (float) EndGame_float);


    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }


    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TextureAtlas getBulbatlas() {
        return bulbatlas;
    }

    public TextureAtlas getBosses() {
        return Bosses;
    }

    public TextureAtlas getShaheen() {
        return Shaheen;
    }

    public TextureAtlas getShaheenJump() {
        return ShaheenJump;
    }

    public TextureAtlas getShaheenDead() {
        return ShaheenDead;
    }

    public TextureAtlas getMyFire() {
        return MyFire;
    }


    @Override
    public void show() {


    }



    public void handleInput(float dt) {

        if (controller.isBackPressed()) {

            game.setScreen(new MainMenuScreen(game));




        }
        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();

        if (!text.equals("z")) {
            if (player.currentState != Mario.State.FINISHED && player.currentState != Mario.State.DEAD) {


                if (((controller.isRightPressed()) && player.b2body.getLinearVelocity().x <= 1.4))
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                else if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <= 1.4) {
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);


                } else if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -1.4)
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                else if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -1.4)) {
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                } else if ((controller.isDownPressed() && ready_to_fire == 1) || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && ready_to_fire == 1)) {
                    ready_to_fire--;
                    player.fire();
                    player.b2body.applyLinearImpulse(new Vector2(0, -0.1f), player.b2body.getWorldCenter(), true);


                    FileHandle zxzcs = Gdx.files.local("audio/sounds_control.txt");
                    String textzzz = zxzcs.readString();

                    if (textzzz.equals("on")) {
                        drops = MarioBros.manager.get("audio/music/fireball.ogg", Music.class);
                        drops.play();
                    }

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            ready_to_fire = 1;

                        }
                    }, (float) delay);

                } else if ((controller.isUpPressed() && ready_tojump == 3) || (Gdx.input.isKeyPressed(Input.Keys.UP) && ready_tojump == 3)) {
                    ready_tojump--;
                    player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);


                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            ready_tojump = 3;
                        }
                    }, (float) delay);


                }
            }
        }
        if (text.equals("z") && player.getX() <= 4366 / MarioBros.PPM) {


            if ((player.b2body.getLinearVelocity().x <= 1.0))
                player.b2body.applyLinearImpulse(new Vector2(0.06f, 0), player.b2body.getWorldCenter(), true);


        }


        if (text.equals("z") && player.getX() >= 4366 / MarioBros.PPM) {

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (player.b2body.getLinearVelocity().y == 0f) {
                        player.b2body.setLinearVelocity(new Vector2(0f, 2.5f));
                    }
                }
            }, (float) delaydancing);

        }

    }

    public void update(float dt) {


        //handle user input first
        handleInput(dt);
        handleSpawningItems();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(dt);

        if (player.getY() <= -1) {
            resetpositionz = true;

        }


        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (!enemy.isDestroyed() && enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
                enemy.b2body.setActive(true);
            }
        }


        for (Item item : items)
            item.update(dt);

        hud.update(dt);


        //attach our camera to our players.x coordinate
        camera.position.x = player.b2body.getPosition().x;
        //  extension for the video trailer * 1.5f;


//        Vector3 position = camera.position;
        //linear interpolation,smoothing the updating position of our camera to optimize the player's experience
        //a + (b - a ) * lerp
        // b = target
        // a = current camera position
//        position.x = camera.position.x + (player.getPosition().x * PPM - camera.position.x) * 0.1f;
//        position.y = camera.position.y + (player.getPosition().y * PPM - camera.position.y) * 0.1f;
//        camera.position.set(position);


        //update our camera with correct coordinates after changes
        camera.update();


        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(camera);

    }


    @Override
    public void render(float delta) {


        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        //b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();



        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);


        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //hud.stage.draw();


        mysecondbatch.begin();


        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();

        if (!text.equals("z")) {
            controller.draw();
        }


        mysecondbatch.end();


        if (gameOver()) {
            game.setScreen(new ResetPosition(game));
            dispose();
        }
        if (resetposition()) {
            game.setScreen(new ResetPosition(game));
            dispose();
        }
        if (stageclear()) {
            game.setScreen(new FinishLine(game));
            dispose();
        }

        if (storytime()) {
            game.setScreen(new Story(game));
            dispose();
        }
        if (mapView()) {
            game.setScreen(new Map(game));
            dispose();
        }


    }

    private boolean mapView() {
        if (player.currentState == Mario.State.MapView && player.getStateTimer() > 0.001) {
            return true;
        }
        return false;

    }


    public boolean stageclear() {
        if (player.currentState == Mario.State.FINISHED && player.getStateTimer() > 0.001) {
            return true;
        }
        return false;
    }

    public boolean storytime() {
        if (player.currentState == Mario.State.STORY && player.getStateTimer() > 0.001) {
            return true;
        }
        return false;
    }


    // wait 3 seconds then execute the gameover screen
    public boolean gameOver() {
        if (player.currentState == Mario.State.DEAD && player.getStateTimer() > 1) {
            return true;
        }
        return false;
    }

    public boolean resetposition() {
        if (resetpositionz) {
            return true;
        }
        return false;
    }


    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width, height);
        stage.getViewport().update(width, height, false);
        controller.resize(width, height);

    }


    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
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
        //dispose of all our opened resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        batch.dispose();
        mysecondbatch.dispose();
        stage.dispose();
        hud.dispose();

    }
    public Hud getHud(){
        return hud;
    }

}
