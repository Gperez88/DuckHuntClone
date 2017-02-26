package com.ajrod.duckhunt.states;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Duck.Duck;
import com.ajrod.duckhunt.objects.Duck.EasyDuck;
import com.ajrod.duckhunt.objects.Duck.HardDuck;
import com.ajrod.duckhunt.objects.Duck.MediumDuck;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState extends State {

    private final int MAX_SUBROUNDS = 5;
    private TextureRegion grass, tree, bg, roundBox, bulletBox, scoreBox, statusBox, roundCount;
    private TextureRegion[] bullet, birdIcon, greenNum, whiteNum, scoreCount;
    private List<Duck> ducks;
    private int round, subRound, score, duckHit, shots;
    private final static int NUM_BULLETS = 3;
    private final static int NUM_DUCKS = 2;

    GameState(GSM gsm) {
        super(gsm);

        duckHit = 0;
        score = 0;
        round = 1;
        subRound = 1;
        shots = 3;

        bullet = new TextureRegion[NUM_BULLETS];
        birdIcon = new TextureRegion[10];
        greenNum = new TextureRegion[10];
        whiteNum = new TextureRegion[10];
        scoreCount = new TextureRegion[6];

        ducks = new ArrayList<Duck>();

        for (int i = 0; i < NUM_DUCKS; i++)
            ducks.add(new EasyDuck());

        grass = DuckHunt.res.getAtlas("pack").findRegion("grass");
        tree = DuckHunt.res.getAtlas("pack").findRegion("tree");
        bg = DuckHunt.res.getAtlas("pack").findRegion("bg");

        roundBox = DuckHunt.res.getAtlas("pack").findRegion("roundCount");
        bulletBox = DuckHunt.res.getAtlas("pack").findRegion("shot");
        scoreBox = DuckHunt.res.getAtlas("pack").findRegion("scoreBox");
        statusBox = DuckHunt.res.getAtlas("pack").findRegion("hitBox");

        TextureRegion[][] tmp1 = DuckHunt.res.getAtlas("pack").findRegion("numGreen").split(7, 7);
        TextureRegion[][] tmp2 = DuckHunt.res.getAtlas("pack").findRegion("numWhite").split(7, 7);

        for (int i = 0; i < 10; i++) {
            birdIcon[i] = DuckHunt.res.getAtlas("pack").findRegion("notHit");
            greenNum[i] = tmp1[i / 6][i % 6];
            whiteNum[i] = tmp2[i / 6][i % 6];
            if (i < NUM_BULLETS) bullet[i] = DuckHunt.res.getAtlas("pack").findRegion("bullet");
            if (i < 6) scoreCount[i] = whiteNum[score / (10 * (i + 1))];
        }

        roundCount = greenNum[1];

    }

    @Override
    public void update(float dt) {
        handleInput();
        for (Duck duck : ducks)
            duck.update(dt);


        if (allDucksAreGone()) {
            subRound++;
            shots = 3;
            if (subRound > MAX_SUBROUNDS) {
                endRound();
            }
            resetDucks(dt);
        }
    }

    private boolean allDucksAreGone() {
        for(Duck duck : ducks)
            if(!duck.isGone())
                return false;

        return true;
    }

    private void endRound() {
        round++;
        if (round < 10) roundCount = greenNum[round];
        subRound = 1;
        duckHit = 0;
        for (int i = 0; i < 10; i++) {
            birdIcon[i] = DuckHunt.res.getAtlas("pack").findRegion("notHit");
        }
        if (round == 10) {
            if (score >= DuckHunt.scores[0]) DuckHunt.scores[0] = score;
            Arrays.sort(DuckHunt.scores);
            for (int i = 0; i < 10; i++)
                DuckHunt.prefs.putInteger("score" + i, DuckHunt.scores[i]);
            DuckHunt.prefs.flush();
            gsm.set(new MenuState(gsm));
        }
    }

    private void resetDucks(float dt) {
        ducks = new ArrayList<Duck>();

        // easy
        if (round < 4) {
            ducks.add(new EasyDuck());
            ducks.add(subRound < 4 ? new EasyDuck() : new MediumDuck());
        }

        // medium
        else if (round < 7) {
            ducks.add(new MediumDuck());
            ducks.add(subRound < 4 ? new MediumDuck() : new HardDuck());
        }

        // hard
        else {
            ducks.add(new HardDuck());
            ducks.add(new HardDuck());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, DuckHunt.WIDTH, DuckHunt.HEIGHT);
        sb.draw(bulletBox, 71, 42, bulletBox.getRegionWidth() * 3.125f, bulletBox.getRegionHeight() * 2.5f);
        sb.draw(roundBox, 85, 100, roundBox.getRegionWidth() * 3.125f, roundBox.getRegionHeight() * 2.5f);
        sb.draw(scoreBox, DuckHunt.WIDTH - 200, 42, scoreBox.getRegionWidth() * 3.125f, scoreBox.getRegionHeight() * 2.5f);
        sb.draw(statusBox, 200, 40, statusBox.getRegionWidth() * 3.125f, statusBox.getRegionHeight() * 2.5f);
        sb.draw(roundCount, 135, 102, roundCount.getRegionWidth() * 3.125f, roundCount.getRegionHeight() * 2.5f);
        for (int i = 0; i < 10; i++) {
            sb.draw(birdIcon[i], 290 + (birdIcon[0].getRegionWidth() * 3.125f + 3) * i, 62,
                    birdIcon[0].getRegionWidth() * 3.125f, birdIcon[0].getRegionHeight() * 2.5f);
            if (i < shots) sb.draw(bullet[i], 83 + (bullet[0].getRegionWidth() * 3.125f + 10) * i, 65,
                    bullet[0].getRegionWidth() * 3.125f, bullet[0].getRegionHeight() * 2.5f);
            if (i < 6)
                sb.draw(scoreCount[i], (DuckHunt.WIDTH - 75) - (scoreCount[0].getRegionWidth() * 3.125f + 3) * i, 64,
                        scoreCount[0].getRegionWidth() * 3.125f, scoreCount[0].getRegionHeight() * 2.5f);
        }

        for (Duck duck : ducks)
            duck.render(sb);

        sb.draw(tree, 20, 213, tree.getRegionWidth() * 2 + 75, tree.getRegionHeight() * 2 + 60);
        sb.draw(grass, 0, 145, DuckHunt.WIDTH, grass.getRegionHeight() * 2 + 15);
        sb.end();
    }

    @Override
    public void handleInput() {
        if (!Gdx.input.justTouched())
            return;

        if (--shots < 0)
            return;

        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();

        cam.unproject(mouse);

        Point clickedPoint = new Point(mouse.x, mouse.y);

        for (Duck duck : ducks) {
            if (duck.isAlive() && duck.contains(clickedPoint)) {
                duckHit++;
                duck.onClick();
                updateScore(duck.getValue());
                birdIcon[duckHit - 1] = DuckHunt.res.getAtlas("pack").findRegion("hit");
            }
        }
    }

    private void updateScore(int value) {
        score += value;

        for (int j = 0; j < 6; j++)
            scoreCount[j] = whiteNum[(score / (int) Math.pow(10, j)) % 10];
    }

}
