package br.com.ts3.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import br.com.ts3.game.FluxoGame;
import br.com.ts3.game.Screens.PlayScreen;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;


public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / FluxoGame.PPM, (bounds.getY() + bounds.getHeight()/2) / FluxoGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/2 / FluxoGame.PPM, bounds.getHeight()/2 / FluxoGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }
    
    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2);

       
       return layer.getCell((int)(body.getPosition().x * FluxoGame.PPM / 16),
                             (int)(body.getPosition().y * FluxoGame.PPM / 16));
      
       
    }
    
    public abstract void onBodyHit();
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    
    
}
