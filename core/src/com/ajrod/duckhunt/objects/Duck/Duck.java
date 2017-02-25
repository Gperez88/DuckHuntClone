package com.ajrod.duckhunt.objects.Duck;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Box;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public abstract class Duck extends Box {

    private final static int BOUNDS = 200;
    protected int speed, direction, value; // 0 -> left, 1 -> upleft, 2 -> upright, 3 -> right
    private boolean offScreen, isShot, isFalling, isClickable;
    private float stateTime, fallTime, changeTime;

    private Animation side, angle, dead;
    private TextureRegion shot, currentFrame;
    private Random rand;

    protected Duck(String resourceKey) {
        // TODO: change up code to not have to pass fake values to box constructor
        super(new Point(DuckHunt.WIDTH / 2, 190), 50, 50);
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

        speed = 3;
        value = 500;

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

        direction = rand.nextInt(2) + 1;

        changeTime = fallTime = stateTime = 0;
        offScreen = isShot = isFalling = false;
        isClickable = true;
    }

    public void update(float dt) {
        if (isShot) {
            fallTime += dt;
            if (fallTime > 0.5f) {
                isShot = false;
                isFalling = true;
            }
        } else if (isFalling) {
            fallTime += dt;
            if (center.y > 190) {
                center.y -= 5;

            } else offScreen = true;
        } else if (!offScreen) {
            stateTime += dt;
            changeDirection();
            move();
            if (center.y > DuckHunt.HEIGHT + 50) offScreen = true;
        }
    }

    private void move() {
        switch (direction) {
            case 0:
                center.x -= speed;
                break;
            case 1:
                center.x -= speed / 1.4f;
                center.y += speed / 1.4f;
                break;
            case 2:
                center.x += speed / 1.4f;
                center.y += speed / 1.4f;
                break;
            default:
                center.x += speed;
                break;
        }
    }

    private void changeDirection() {
        if (center.x < DuckHunt.WIDTH / 2 - BOUNDS) {
            direction = rand.nextInt(2) + 2;
            changeTime = 0;
        } else if (center.x > DuckHunt.WIDTH / 2 + BOUNDS) {
            direction = rand.nextInt(2);
            changeTime = 0;
        } else {
            changeTime += 1;
            if (changeTime >= 30) {
                direction = rand.nextInt(4);
                changeTime = 0;
            }
        }
    }

    public void render(SpriteBatch sb) {
        float x = center.x;
        float y = center.y;

        Point p = getLowerLeftCorner();

        if (isShot)
            sb.draw(shot, p.x, p.y, width, height);
        else if (isFalling) {
            currentFrame = dead.getKeyFrame(fallTime, true);
            sb.draw(currentFrame, p.x, p.y, width - 10, height);
        } else
            switch (direction) {
                case 0:
                    currentFrame = side.getKeyFrame(stateTime, true);
                    if (!currentFrame.isFlipX()) currentFrame.flip(true, false);
                    sb.draw(currentFrame, x - width * 0.5f, y - height * 0.5f, width * 1f, height * 1f);
                    break;
                case 1:
                    currentFrame = angle.getKeyFrame(stateTime, true);
                    if (!currentFrame.isFlipX()) currentFrame.flip(true, false);
                    //sb.draw(currentFrame, x - width * 0.6f, y - height * 0.6f, width * 1.2f, height * 1.2f);
                    sb.draw(currentFrame, x - width * 0.5f, y - height * 0.5f, width * 1f, height * 1f);
                    break;
                case 2:
                    currentFrame = angle.getKeyFrame(stateTime, true);
                    if (currentFrame.isFlipX()) currentFrame.flip(true, false);
                    //sb.draw(currentFrame, x - width * 0.6f, y - height * 0.6f, width * 1.2f, height * 1.2f);
                    sb.draw(currentFrame, x - width * 0.5f, y - height * 0.5f, width * 1f, height * 1f);
                    break;
                default:
                    currentFrame = side.getKeyFrame(stateTime, true);
                    if (currentFrame.isFlipX()) currentFrame.flip(true, false);
                    //sb.draw(currentFrame, x - width * 0.6f, y - height * 0.6f, width * 1.2f, height * 1.2f);
                    sb.draw(currentFrame, x - width * 0.5f, y - height * 0.5f, width * 1f, height * 1f);
                    break;
            }
    }

    public void onClick() {
        if (isClickable) {
            isShot = true;
            isClickable = false;
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isGone() {
        return offScreen;
    }

    public boolean isClickable() {
        return isClickable;
    }
}
