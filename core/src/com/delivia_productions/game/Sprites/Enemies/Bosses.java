package com.delivia_productions.game.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
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
import com.delivia_productions.game.Sprites.Mario;
import com.delivia_productions.game.Sprites.Others.Fireball;
import com.delivia_productions.game.gamestates.PlayState;


public class Bosses extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean runningRight;

    private int ready_tojump = 3;
    private int ready_to_fire = 1;
    private double delay = 0.8; // seconds
    private Array<Fireball> fireballs;

    float angle;



    public Bosses(PlayState screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(screen.getBosses().findRegion("jafar"), i * 23, 0, 23, 28));
        walkAnimation = new Animation(0.2f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 23 / MarioBros.PPM, 28 / MarioBros.PPM);
        setToDestroy = false;
        destroyed = false;
        angle = 0;
        fireballs = new Array<Fireball>();
        runningRight = true;


        FileHandle file = Gdx.files.local("bosses_hits.txt");
        file.writeString("0", false);

    }
    @Override
    public void hitByFire(Fireball userData) {
        FileHandle file = Gdx.files.local("bosses_hits.txt");
        String text = file.readString();

        if (text.equals("0")) {
            file.writeString("1", false);
        }
        if (text.equals("1")) {
            file.writeString("0", false);
            setToDestroy = true;


    }

}

    @Override
    public void belowTheEquator(Enemy enemy) {

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);


        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / MarioBros.PPM);
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
        vertice[0] = new Vector2(-9, 15).scl(1 / MarioBros.PPM);
        vertice[1] = new Vector2(9, 15).scl(1 / MarioBros.PPM);
        vertice[2] = new Vector2(-9, 9).scl(1 / MarioBros.PPM);
        vertice[3] = new Vector2(9, 9).scl(1 / MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }

    @Override
    public void update(float dt) {
        stateTime += dt;

        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            setRegion(new TextureRegion(screen.getBosses().findRegion("jafar"), 800, 0, 23, 28));
            destroyed = true;
            stateTime = 0;
        } else if (!destroyed) {

            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));

        }


    }


    @Override
    public void hitOnHead(Mario mario) {
        setToDestroy = true;
    }




    @Override
    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true, false);
    }




    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);


    }
}
