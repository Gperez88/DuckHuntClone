package com.ajrod.duckhunt.objects.Duck;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Box;
import com.ajrod.duckhunt.objects.Direction;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public abstract class Duck extends Box {

    private final static int BOUNDS = 200;
    protected int speed, value;
    private float stateTime, fallTime, timeSinceLastDirectionChange;

    private Animation side, angle, dead, currentAnimation;
    private TextureRegion shot, currentFrame;
    private Random rand;

    private Direction direction;
    private State currentState;

    protected Duck(String resourceKey) {
        // TODO: change up code to not have to pass fake values to box constructor
        super(new Point(DuckHunt.WIDTH / 2, 190), 50, 50);

        // defaults
        speed = 3;
        value = 500;
        currentState = State.Flying;

        initializeDuck(resourceKey);
    }

    // TODO: reimplement randomized start location
    protected void initializeDuck(String resourceKey) {
        rand = new Random();
        currentFrame = new TextureRegion();

        TextureRegion[][] side, angle, dead;
        TextureRegion[] t1, t2, t3;

        t1 = new TextureRegion[3];
        t2 = new TextureRegion[3];
        t3 = new TextureRegion[2];

        side = DuckHunt.res.getAtlas("pack").findRegion(resourceKey + "Side").split(40, 33);
        angle = DuckHunt.res.getAtlas("pack").findRegion(resourceKey + "Angled").split(40, 34);
        dead = DuckHunt.res.getAtlas("pack").findRegion(resourceKey + "Fall").split(20, 31);
        shot = DuckHunt.res.getAtlas("pack").findRegion(resourceKey + "Shot");


        for (int i = 0; i < 3; i++) {
            t1[i] = side[0][i];
            t2[i] = angle[0][i];
            if (i < 2) t3[i] = dead[0][i];
        }

        this.side = new Animation(0.1f, t1);
        this.angle = new Animation(0.1f, t2);
        this.dead = new Animation(0.1f, t3);

        this.side.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.angle.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.dead.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        fallTime = stateTime = 0;
        initDirection();
    }

    private void initDirection() {
        direction = Direction.Right;
        setDirection(rand.nextBoolean() ? Direction.UpLeft : Direction.UpRight);
    }

    public void update(float dt) {
        switch (currentState) {
            case Flying:
                stateTime += dt;

                if (outOfBounds())
                    reverseDirection();

                if (timeSinceLastDirectionChange++ >= 30)
                    randomizeDirection();

                move();

                if (center.y > DuckHunt.HEIGHT + 50)
                    currentState = State.Offscreen;

                // will transition to dying if clicked on
                break;

            case Dying:
                fallTime += dt;

                if (fallTime > 0.5f)
                    currentState = State.Falling;
                break;

            case Falling:
                fallTime += dt;

                if (center.y <= 190)
                    currentState = State.Offscreen;
                else
                    center.y -= 5;

                break;
        }
    }

    private void move() {
        center.x += direction.deltaX() * speed;
        center.y += direction.deltaY() * speed;
    }

    private boolean outOfBounds() {
        return Math.abs(DuckHunt.WIDTH / 2 - center.x) > BOUNDS;
    }

    private void setDirection(Direction next) {
        currentAnimation = next.isMovingUp() ? angle : side;
        direction = next;
        timeSinceLastDirectionChange = 0;
    }

    private void reverseDirection() {
        Direction nextDirection = direction.nextValidDirections()[rand.nextInt(2)];
        setDirection(nextDirection);
    }

    private void randomizeDirection() {
        Direction nextDirection = Direction.values()[rand.nextInt(4)];
        setDirection(nextDirection);
    }

    public void render(SpriteBatch sb) {
        float x = center.x;
        float y = center.y;

        Point p = getLowerLeftCorner();

        switch (currentState) {
            case Dying:
                sb.draw(shot, p.x, p.y, width, height);
                break;

            case Falling:
                currentFrame = dead.getKeyFrame(fallTime, true);
                sb.draw(currentFrame, p.x, p.y, width - 10, height);
                break;

            case Flying:
                if (direction.isMovingRight() == currentFrame.isFlipX())
                    currentFrame.flip(true, false);

                currentFrame = currentAnimation.getKeyFrame(stateTime, true);
                sb.draw(currentFrame, p.x, p.y, width, height);
                break;
        }
    }

    public void onClick() {
        if (currentState != State.Flying)
            return;

        currentState = State.Dying;
    }

    public int getValue() {
        return value;
    }

    public boolean isGone() {
        return currentState == State.Offscreen;
    }

    public boolean isAlive() {
        return currentState == State.Flying;
    }
}
