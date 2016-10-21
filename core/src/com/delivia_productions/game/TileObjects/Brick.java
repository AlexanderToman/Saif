package com.delivia_productions.game.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.Sprites.Mario;
import com.delivia_productions.game.Sprites.Others.Hud;
import com.delivia_productions.game.gamestates.PlayState;


public class Brick extends InteractiveTileObject {
    public Brick(PlayState screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {


            setCategoryFilter(MarioBros.DESTROYED_BIT);
            getCell().setTile(null);

            Hud.addScore(868);

        }


}