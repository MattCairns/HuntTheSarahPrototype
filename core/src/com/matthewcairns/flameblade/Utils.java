package com.matthewcairns.flameblade;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Utils {

    //Checks if there has been a collision between the map walls and any object passed to the function.
    //Returns true if there is a collision.
    public static Boolean wallCollision(TiledMap map, Rectangle objectRec) {
        for(MapObject object :  map.getLayers().get("collide").getObjects()) {
            if(object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, objectRec)) {
                   return true;
                }
            }
        }
        return false;
    }
}
