package com.delivia_productions.game.TileObjects;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.delivia_productions.game.Sprites.Items.ItemDef;
import com.delivia_productions.game.Sprites.Items.Mushroom;
import com.delivia_productions.game.MarioBros;
import com.delivia_productions.game.Sprites.Mario;
import com.delivia_productions.game.Sprites.Others.Hud;
import com.delivia_productions.game.gamestates.PlayState;


public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayState screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {


        setCategoryFilter(MarioBros.DESTROYED_BIT);
        Hud.addScore(100);


    }

}