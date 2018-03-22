package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Vehicle extends Rectangle {

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;

    private final float R = 5;
    private final float MAX_FORCE = 0.03f;
    private final float MAX_SPEED = 2;
    private final int VISION = 120;
    private final int VISION_RADIUS = 150;
    private Color visionColor = new Color(0, 0, 1, 0.5f);

    private RoutePoint[] routePoints;

    private final Vector2 initialPosition = new Vector2(100, 200);

    private boolean closeToHome = false;

    public enum Turning {
        LEFT,
        RIGHT
    }

    public enum Thinking {
        GUARD,
        CHASE,
        RETURN_HOME
    }

    private Turning turning;
    private Thinking state;

    private float looking = 150;

    public Vehicle() {
        position = new Vector2(initialPosition);
        acceleration = new Vector2(0, 0);
        velocity = new Vector2(0, 0);

        routePoints = new RoutePoint[2];
        routePoints[0] = new RoutePoint(100, 400);
        routePoints[1] = new RoutePoint(500, 200);

        state = Thinking.GUARD;
    }

    public void state(Vector2 player) {
        switch (state) {
            case GUARD:
                guard(player);
                break;
            case CHASE:
                chase(player);
                break;
            case RETURN_HOME:
                returnHome(player);
                break;
        }

        update();
    }

    public void update() {
        velocity.add(acceleration);
        velocity.limit(MAX_SPEED);
        position.add(velocity);
        setPosition(position);

        acceleration = acceleration.scl(0);
    }

    public void guard(Vector2 player) {
        closeToHome = false;
        float turnRate = 1f;
        if (turning == Turning.LEFT && looking <= 270) {
            looking += turnRate;
        } else {
            turning = Turning.RIGHT;
        }

        if (turning == Turning.RIGHT && looking >= 30) {
            looking -= turnRate;
        } else {
            turning = Turning.LEFT;
        }

        if (inSight(player)) {
            visionColor.set(1, 0, 0, 0.5f);
            state = Thinking.CHASE;
        }
    }

    private void applyForce(Vector2 force) {
        acceleration.add(force);
    }

    public void chase(Vector2 player) {
        closeToHome = false;
        heading();
        Vector2 desired = VectorHelper.sub(player, position);

        desired.nor();
        desired.scl(MAX_SPEED);

        Vector2 steer = desired.sub(velocity);
        steer.limit(MAX_FORCE);

        applyForce(steer);

        if (!inSight(player)) {
            visionColor.set(0, 0, 1, 0.5f);
            state = Thinking.RETURN_HOME;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    private float watchTarget(Vector2 target) {
        return VectorHelper.angleToTarget(position, target, VISION);
    }

    public void returnHome(Vector2 player) {
        heading();
        Vector2 desired = new Vector2(0, 0);

        // To check to go to points
        if (!closeToHome) {
            double distanceToPoint = Double.MAX_VALUE;
            Vector2 target = new Vector2(0, 0);
            for (RoutePoint r : routePoints) {
                double dist = VectorHelper.distanceToTarget(position, r);
                if (dist < distanceToPoint) {
                    distanceToPoint = dist;
                    target = r;
                }
            }
            if (VectorHelper.distanceToTarget(position, initialPosition) < distanceToPoint) {
                closeToHome = true;
            } else {
                if (distanceToPoint < 5) {
                    closeToHome = true;
                } else {
                    desired = VectorHelper.sub(target, position);
                }
            }
        } else {
            desired = VectorHelper.sub(initialPosition, position);
            double distanceToHome = VectorHelper.distanceToTarget(position, initialPosition);
            if (distanceToHome < 100 && distanceToHome >= 50) {
                desired.scl(MAX_SPEED / 2);
            } else if (distanceToHome < 50 && distanceToHome >= 5) {
                desired.scl(MAX_SPEED / 4);
            } else if (VectorHelper.distanceToTarget(position, initialPosition) < 5) {
                visionColor.set(0, 0, 1, 0.5f);
                state = Thinking.GUARD;
                velocity.scl(0);
                return;
            } else {
                desired.scl(MAX_SPEED);
            }
        }

        desired.nor();

        Vector2 steer = desired.sub(velocity);
        steer.limit(MAX_FORCE);

        applyForce(steer);

        if (inSight(player)) {
            visionColor.set(1, 0, 0, 0.5f);
            state = Thinking.CHASE;
        }
    }

    public boolean inSight(Vector2 target) {
        float pos = Math.abs(looking()) + 60;
        float neg = Math.abs(looking()) - 60;

        float targetPosition = Math.abs(watchTarget(target));
        return (targetPosition <= pos && targetPosition >= neg) && (VectorHelper.distanceToTarget(position, target) <= VISION_RADIUS);
    }

    public void heading() {
        float angle = MathUtils.atan2(velocity.y, velocity.x);

        looking = (MathUtils.radiansToDegrees * angle) - VISION / 2;
    }

    public float looking() {
        return looking;
    }

    public float getR() {
        return R;
    }

    public int getVision() {
        return VISION;
    }

    public int getVisionRadius() {
        return VISION_RADIUS;
    }

    public RoutePoint[] getRoutePoints() {
        return routePoints;
    }

    public Color getVisionColor() {
        return visionColor;
    }

    public Vector2 getVelocity() {
        return velocity;
    }


}
