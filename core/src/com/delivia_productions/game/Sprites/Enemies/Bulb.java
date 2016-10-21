package com.delivia_productions.game.Sprites.Enemies;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.Sprites.Others.Fireball;
import com.delivia_productions.game.Sprites.Others.Hud;
import com.delivia_productions.game.gamestates.PlayState;
import com.delivia_productions.game.Sprites.Mario;

import java.util.Random;


public class Bulb extends com.delivia_productions.game.Sprites.Enemies.Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    // files existing checking
    boolean exists = Gdx.files.local("enemy_hits.txt").exists();

    float angle;


    public Bulb(PlayState screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getBulbatlas().findRegion("bulb1"), i * 31, 0, 31, 31));
        walkAnimation = new Animation(0.1f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setToDestroy = false;
        destroyed = false;
        angle = 0;


        FileHandle file = Gdx.files.local("enemy_hits.txt");
        file.writeString("0",false);



    }


    @Override
    public void belowTheEquator(Enemy enemy) {


    }

    @Override
    public void hitByFire(Fireball userData) {


        setToDestroy = true;
        Hud.addScore(15);

//        FileHandle file = Gdx.files.local("enemy_hits.txt");
//        String text = file.readString();
//
//        if (text.equals("0")) {
//            file.writeString("1", false);
//        }
//        if (text.equals("1")) {
//            file.writeString("0", false);
//            setToDestroy = true;
//
//        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);


        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.FIREBALL_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10, 16).scl(1 / MarioBros.PPM);
        vertice[1] = new Vector2(10, 16).scl(1 / MarioBros.PPM);
        vertice[2] = new Vector2(-10, 12).scl(1 / MarioBros.PPM);
        vertice[3] = new Vector2(10, 12).scl(1 / MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1.0f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }


    public void update(float dt) {
        stateTime += dt;

        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getBulbatlas().findRegion("bulb5"), 64, 0, 32, 32));
            stateTime = 0;
        } else if (!destroyed) {

            FileHandle file = Gdx.files.local("stages/stage_selector.txt");
            String text = file.readString();

            if (!text.equals("b")) {
                if (b2body.getLinearVelocity().y == 0f) {
                    b2body.setLinearVelocity(new Vector2(-1f, 4.3f));

                }
            }
            else{
                if (b2body.getLinearVelocity().y == 0f) {
                    b2body.setLinearVelocity(new Vector2(-0.1f, 4.0f));

                }
            }


// I just made a change

            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

    }


    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }


    @Override
    public void hitOnHead(Mario mario) {
        setToDestroy = true;
        Hud.addScore(10);



        FileHandle bit = Gdx.files.local("audio/sounds_control.txt");
        String textz = bit.readString();

        if (textz.equals("on")) {
            MarioBros.manager.get("audio/sounds/stomp.ogg", Sound.class).play();

        }
        if (textz.equals("off")) {

        }
    }



    @Override
    public void hitByEnemy(Enemy enemy) {
        if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
            setToDestroy = true;
        else
            reverseVelocity(true, false);
    }
}