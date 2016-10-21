package com.delivia_productions.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.delivia_productions.game.MarioBros;

import com.delivia_productions.game.gamestates.PlayState;
import com.delivia_productions.game.Sprites.Others.Fireball;
import com.delivia_productions.game.Sprites.Enemies.*;


public class Mario extends Sprite {


    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, FINISHED, STORY, MapView}

    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private int soul = 3;


    private TextureRegion SaifStand;
    private Animation SaifRun;
    private TextureRegion SaifJump;
    private TextureRegion saifIsDead;

    private TextureRegion bigshaheenStand;
    private TextureRegion bigshaheenJump;

    private Animation bigshaheenRun;
    private Animation growMario;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean marioIsTiny;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean SaifIsDead;


    private PlayState screen;

    private boolean SaifIsFinished;
    private boolean ShaheenViewMap;
    private boolean shaheenIsStory;


    private Array<Fireball> fireballs;


    //personal

    private float timeCount;


    public Mario(PlayState screen) {

        marioIsTiny = true;
        //initializing default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to SaifRun Animation
        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();


        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getShaheen().findRegion("1"), i * 219, 0, 219, 322));
        if (!text.equals("z")) {
            SaifRun = new Animation(0.1f, frames);
        }
        if (text.equals("z")) {
            SaifRun = new Animation(0.15f, frames);

        }
        frames.clear();


        //get jump animation frames and add them to SaifJump Animation
        SaifJump = new TextureRegion(screen.getShaheenJump().findRegion("5"), 0, 0, 220, 294);

        //create texture region for mario standing
        SaifStand = new TextureRegion(screen.getShaheen().findRegion("1"), 0, 0, 219, 322);

        //create dead mario texture region
        saifIsDead = new TextureRegion(screen.getShaheenDead().findRegion("deadShaheen"), 0, 0, 206, 216);

        //define mario in Box2d
        defineMario();

        //set initial values for marios location, width and height. And initial frame as SaifStand.
        setBounds(0, 0, 10 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(SaifStand);

        fireballs = new Array<Fireball>();


//        bigshaheenJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
//        bigshaheenStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
//        //get set animation frames from growing mario
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
//        growMario = new Animation(0.2f, frames);
//        for(int i = 1; i < 4; i++)
//            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
//        bigshaheenRun = new Animation(0.1f, frames);
//
//        frames.clear();

    }


    public void update(float dt) {

        // time is up : too late mario dies T_T
        // the !isDead() method is used to prevent multiple invocation
        // of "die music" and jumping
        // there is probably better ways to do that but it works for now.

        timeCount += dt;

        //update our sprite to correspond with the position of our Box2D body
        if (marioIsBig)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / MarioBros.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //update sprite with the correct frame depending on marios current action
        setRegion(getFrame(dt));
        if (timeToDefineBigMario)
            defineBigMario();
        if (timeToRedefineMario)
            redefineMario();

        for (Fireball ball : fireballs) {
            ball.update(dt);
            if (ball.isDestroyed())
                fireballs.removeValue(ball, true);
        }


    }

    public TextureRegion getFrame(float dt) {
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch (currentState) {
            case DEAD:
                region = saifIsDead;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = marioIsBig ? bigshaheenJump : SaifJump;
                break;
            case RUNNING:
                region = marioIsBig ? bigshaheenRun.getKeyFrame(stateTimer, true) : SaifRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigshaheenStand : SaifStand;
                break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;


    }


    public State getState() {
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump stat
        if (SaifIsDead)
            return State.DEAD;

        if (SaifIsFinished)
            return State.FINISHED;

        if (ShaheenViewMap)
            return State.MapView;

        if (shaheenIsStory)
            return State.STORY;

        if (runGrowAnimation)
            return State.GROWING;

        else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;


    }


//    public void grow() {
//        if (!isBig()) {
//            runGrowAnimation = true;
//            marioIsBig = true;
//            timeToDefineBigMario = true;
//            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
//            MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
//        }
//    }


    public void die() {

        if (!isDead() && !SaifIsFinished) {

//            MarioBros.manager.get("audio/music/shaheen_music_1.mp3", Music.class).stop();
            //MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            SaifIsDead = true;


            Filter filter = new Filter();
            filter.maskBits = MarioBros.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }


    }


    public boolean isDead() {
        return SaifIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean isBig() {
        return marioIsBig;
    }

    public boolean isTiny() {
        return marioIsTiny;
    }
//    public void jump() {
//        if (currentState != State.JUMPING) {
//            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
//            currentState = State.JUMPING;
//        }
//    }

    public void hit(Enemy enemy) {
//        if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.STANDING_SHELL)
//            ((Turtle) enemy).kick(enemy.b2body.getPosition().x > b2body.getPosition().x ? Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
//
//
        die();
    }


//            }


//    if (marioIsBig)
//
//    {
//        marioIsBig = false;
//        timeToRedefineMario = true;
//        setBounds(getX(), getY(), getWidth(), getHeight() / 2);
//        // MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
//


    public void redefineMario() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.PLAY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.STORY_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT |
                MarioBros.FINISHLINE_BIT |
                MarioBros.KILL_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineMario = false;
    }

    public void defineBigMario() {

        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.PLAY_BIT |
                MarioBros.STORY_BIT |
                MarioBros.ITEM_BIT |
                MarioBros.FINISHLINE_BIT |
                MarioBros.KILL_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    public void defineMario() {
        BodyDef bdef = new BodyDef();

        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();


        if (text.equals("b")) {
            bdef.position.set(390 / MarioBros.PPM, 250 / MarioBros.PPM);
        }
        if (text.equals("d")) {
            bdef.position.set(248 / MarioBros.PPM, 200 / MarioBros.PPM);
        }
        if (text.equals("f")) {
            bdef.position.set(270 / MarioBros.PPM, 150 / MarioBros.PPM);
        }
        if (text.equals("h")) {
            bdef.position.set(270 / MarioBros.PPM, 150 / MarioBros.PPM);
        }
        if (text.equals("j")) {
            bdef.position.set(400 / MarioBros.PPM, 150 / MarioBros.PPM);
        }
        if (text.equals("l")) {
            bdef.position.set(285 / MarioBros.PPM, 140 / MarioBros.PPM);
        }
        if (text.equals("n")) {
            bdef.position.set(285 / MarioBros.PPM, 80 / MarioBros.PPM);
        }
        if (text.equals("p")) {
            bdef.position.set(555 / MarioBros.PPM, 20 / MarioBros.PPM);
        }
        if (text.equals("r")) {
            bdef.position.set(515 / MarioBros.PPM, 100 / MarioBros.PPM);
        }

        if (text.equals("t")) {
            bdef.position.set(335 / MarioBros.PPM, 100 / MarioBros.PPM);
        }
        if (text.equals("z")) {
            bdef.position.set(305 / MarioBros.PPM, 100 / MarioBros.PPM);
        }


        if (text.equals("a") || (text.equals("c")) || (text.equals("e")) || (text.equals("g")) || (text.equals("i"))
                || (text.equals("k")) || (text.equals("m")) || (text.equals("o")) || (text.equals("q")) || (text.equals("s")))

        {
            bdef.position.set(433 / MarioBros.PPM, 40 / MarioBros.PPM);

        }


        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |

                MarioBros.ENEMY_BIT |
                MarioBros.MAP_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.PLAY_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT |
                MarioBros.STORY_BIT |
                MarioBros.FINISHLINE_BIT |
                MarioBros.KILL_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);


    }

    public void fire() {
        fireballs.add(new Fireball(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight));
    }


    public void draw(Batch batch) {
        super.draw(batch);
        for (Fireball ball : fireballs)
            ball.draw(batch);
    }

    public void the_finish_linemethod() {


        FileHandle file = Gdx.files.local("stages/stage_selector.txt");
        String text = file.readString();

        if (text.equals("a")) {
            file.writeString("b", false);
        }
        if (text.equals("b")) {
            file.writeString("c", false);
        }
        if (text.equals("c")) {
            file.writeString("d", false);
        }
        if (text.equals("d")) {
            file.writeString("e", false);
        }
        if (text.equals("e")) {
            file.writeString("f", false);
        }
        if (text.equals("f")) {
            file.writeString("g", false);
        }
        if (text.equals("g")) {
            file.writeString("h", false);
        }
        if (text.equals("h")) {
            file.writeString("i", false);
        }
        if (text.equals("i")) {
            file.writeString("j", false);
        }
        if (text.equals("j")) {
            file.writeString("k", false);
        }
        if (text.equals("k")) {
            file.writeString("l", false);
        }
        if (text.equals("l")) {
            file.writeString("m", false);
        }
        if (text.equals("m")) {
            file.writeString("n", false);
        }
        if (text.equals("n")) {
            file.writeString("o", false);
        }
        if (text.equals("o")) {
            file.writeString("p", false);
        }
        if (text.equals("p")) {
            file.writeString("q", false);
        }
        if (text.equals("q")) {
            file.writeString("r", false);
        }
        if (text.equals("r")) {
            file.writeString("s", false);
        }
        if (text.equals("s")) {
            file.writeString("t", false);
        }
        if (text.equals("t")) {
            file.writeString("z", false);
        }

        //moved to the final page
//        if (text.equals("z")) {
//            file.writeString("a", false);
//        }


        // now with the finish line animation
        SaifIsFinished = true;
        Filter filter = new Filter();
        filter.maskBits = MarioBros.GROUND_BIT;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
        //animate mario when the he reaches the finish line
        //b2body.applyLinearImpulse(new Vector2(0, 1000), b2body.getWorldCenter(), true);

    }

    public void killlines() {
        die();
    }

    public void storyMethod() {
        shaheenIsStory = true;
    }


    public void injectMario() {

    }

    public void Map_view() {


        // now with the finish line animation
        ShaheenViewMap = true;
        Filter filter = new Filter();
        filter.maskBits = MarioBros.GROUND_BIT;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

    }
}