package com.ajrod.duckhunt.objects;

import com.ajrod.duckhunt.DuckHunt;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class Duck extends Box {

    private final int BOUNDS = 200;
    private int speed, direction, value; // 0 -> left, 1 -> upleft, 2 -> upright, 3 -> right
    private boolean offScreen, onSide, isShot, isFalling, isClickable;
    private float stateTime, timer, fallTime, changeTime;

    private Animation side, angle, dead;
    private TextureRegion shot, currentFrame;
    private Random rand;

    public Duck(int mode) {

        rand = new Random();
        currentFrame = new TextureRegion();

        TextureRegion[][] side, angle, dead;
        TextureRegion[] t1, t2, t3;

        t1 = new TextureRegion[3];
        t2 = new TextureRegion[3];
        t3 = new TextureRegion[2];

        switch (mode) {
            case 0: // easy mode
                side = DuckHunt.res.getAtlas("pack").findRegion("duck1Side").split(40, 33);
                angle = DuckHunt.res.getAtlas("pack").findRegion("duck1Angled").split(40, 34);
                dead = DuckHunt.res.getAtlas("pack").findRegion("duck1Fall").split(20, 31);
                shot = DuckHunt.res.getAtlas("pack").findRegion("duck1Shot");
                speed = 3;
                value = 500;
                break;
            case 1: // normal mode
                side = DuckHunt.res.getAtlas("pack").findRegion("duck2Side").split(40, 33);
                angle = DuckHunt.res.getAtlas("pack").findRegion("duck2Angled").split(40, 34);
                dead = DuckHunt.res.getAtlas("pack").findRegion("duck2Fall").split(20, 31);
                shot = DuckHunt.res.getAtlas("pack").findRegion("duck2Shot");
                speed = 6;
                value = 1000;
                break;
            default: // hard mode
                side = DuckHunt.res.getAtlas("pack").findRegion("duck3Side").split(40, 33);
                angle = DuckHunt.res.getAtlas("pack").findRegion("duck3Angled").split(40, 34);
                dead = DuckHunt.res.getAtlas("pack").findRegion("duck3Fall").split(20, 31);
                shot = DuckHunt.res.getAtlas("pack").findRegion("duck3Shot");
                speed = 9;
                value = 1500;
                break;
        }

        for (int i = 0; i < 3; i++) {
            t1[i] = side[0][i];
            t2[i] = angle[0][i];
            if (i < 2) t3[i] = dead[0][i];
        }

        this.side = new Animation(0.1f, t1);
        this.angle = new Animation(0.1f, t2);
        this.dead = new Animation(0.1f, t3);

        this.side.setPlayMode(PlayMode.LOOP_PINGPONG);
        this.angle.setPlayMode(PlayMode.LOOP_PINGPONG);
        this.dead.setPlayMode(PlayMode.LOOP_PINGPONG);

        direction = rand.nextInt(2) + 1;

        int scalar = rand.nextInt(3) - 1;

        changeTime = fallTime = stateTime = timer = 0;

        float x = DuckHunt.WIDTH / 2 + scalar * 100;
        float y = 190;

        center = new Point(x, y);
        height = 50;
        width = 50;
        offScreen = isShot = isFalling = false;
        isClickable = true;
    }

    @Override
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

    @Override
    public void render(SpriteBatch sb) {
        float x = center.x;
        float y = center.y;

        if (isShot) sb.draw(shot, x - width / 2, y - height / 2, width, height);
        else if (isFalling) {
            currentFrame = dead.getKeyFrame(fallTime, true);
            sb.draw(currentFrame, x - width / 2, y - height / 2, width - 10, height);
        } else {
            switch (direction) {
                case 0:
                    currentFrame = side.getKeyFrame(stateTime, true);
                    if (!currentFrame.isFlipX()) currentFrame.flip(true, false);
                    sb.draw(currentFrame, x - width * 0.6f, y - height * 0.6f, width * 1.2f, height * 1.2f);
                    break;
                case 1:
                    currentFrame = angle.getKeyFrame(stateTime, true);
                    if (!currentFrame.isFlipX()) currentFrame.flip(true, false);
                    sb.draw(currentFrame, x - width * 0.6f, y - height * 0.6f, width * 1.2f, height * 1.2f);
                    break;
                case 2:
                    currentFrame = angle.getKeyFrame(stateTime, true);
                    if (currentFrame.isFlipX()) currentFrame.flip(true, false);
                    sb.draw(currentFrame, x - width * 0.6f, y - height * 0.6f, width * 1.2f, height * 1.2f);
                    break;
                default:
                    currentFrame = side.getKeyFrame(stateTime, true);
                    if (currentFrame.isFlipX()) currentFrame.flip(true, false);
                    sb.draw(currentFrame, x - width * 0.6f, y - height * 0.6f, width * 1.2f, height * 1.2f);
                    break;
            }
        }
    }

    @Override
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
