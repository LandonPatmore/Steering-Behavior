package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VectorHelper {

    private VectorHelper(){}

    public static Vector2 sub(Vector2 v1, Vector2 v2){
        float x = v1.x - v2.x;
        float y = v1.y - v2.y;

        return new Vector2(x,y);
    }

    public static Vector2 add(Vector2 v1, Vector2 v2){
        float x = v1.x + v2.x;
        float y = v1.y + v2.y;

        return new Vector2(x,y);
    }

    public static void negate(Vector2 v){
        float x = -v.x;
        float y = -v.y;

        v.set(x,y);
    }

    public static double distanceToTarget(Vector2 currentPosition, Vector2 target){
        Vector2 vector = sub(target, currentPosition);

        double a = vector.x;
        double b = vector.y;

        return Math.sqrt(Math.pow(a,2) + Math.pow(b,2));

    }

    public static float angleToTarget(Vector2 currentPosition, Vector2 target, int vision){
        Vector2 vector = sub(target, currentPosition);

        float angle = MathUtils.atan2(vector.y, vector.x);

        return (MathUtils.radiansToDegrees * angle) - vision / 2;
    }
}
