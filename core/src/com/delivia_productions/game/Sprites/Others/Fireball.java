package com.delivia_productions.game.Sprites.Others;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.Sprites.Enemies.Enemy;
import com.delivia_productions.game.gamestates.PlayState;


public class Fireball extends Sprite {

    PlayState screen;
    World world;
    Array<TextureRegion> frames;
    Animation fireAnimation;
    Body b2body;

    float stateTime;


    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;


    public Fireball(PlayState screen, float x, float y, boolean fireRight) {
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();


//        the_fire_atlas = new TextureAtlas(Gdx.files.internal("firez.atlas"));
//        fireAnimation = new Animation(0.5f, the_fire_atlas.getRegions());
//
//        setRegion(fireAnimation.getKeyFrame(0));
//        setBounds(x, y, 6 / MarioBros.PPM, 6 / MarioBros.PPM);
//        defineFireBall();

        frames = new Array<TextureRegion>();
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(screen.getMyFire().findRegion("1fire"), i * 9, 0, 9, 9));
        }
        fireAnimation = new Animation(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 7 / MarioBros.PPM, 7 / MarioBros.PPM);
        defineFireBall();
    }

    public void defineFireBall() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 / MarioBros.PPM : getX() - 12 / MarioBros.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if (!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.FIREBALL_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.FIREBALL_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.DESTROYED_BIT |
                MarioBros.FINISHLINE_BIT |
                MarioBros.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 2 : -2, 2.5f));
    }

    public void update(float dt) {
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if ((stateTime > 3 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        if (b2body.getLinearVelocity().y > 3f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);

        if ((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy() {
        setToDestroy = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

}
