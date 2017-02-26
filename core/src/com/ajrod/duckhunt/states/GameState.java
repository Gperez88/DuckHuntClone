package com.ajrod.duckhunt.states;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Duck.Duck;
import com.ajrod.duckhunt.objects.Duck.EasyDuck;
import com.ajrod.duckhunt.objects.Duck.HardDuck;
import com.ajrod.duckhunt.objects.Duck.MediumDuck;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState extends State {

    private final static int NUM_BULLETS = 3;
    private final static int NUM_DUCKS = 2;
    private final static int MAX_SUBROUNDS = 5;
    private final static int NUM_BIRD_ICONS = 10;
    private final static int NUM_SCORE_DIGITS = 6;
    private final static List<TextureRegion> greenDigits, whiteDigits;

    static {
        TextureRegion[][] greenNums = DuckHunt.res.getAtlas("pack").findRegion("numGreen").split(7, 7);
        TextureRegion[][] whiteNums = DuckHunt.res.getAtlas("pack").findRegion("numWhite").split(7, 7);

        greenDigits = new ArrayList<TextureRegion>(10);
        whiteDigits = new ArrayList<TextureRegion>(10);

        for (int i = 0; i < 10; ++i) {
            greenDigits.add(greenNums[i / 6][i % 6]);
            whiteDigits.add(whiteNums[i / 6][i % 6]);
        }
    }

    private TextureRegion grass, tree, bg, roundBox, bulletBox, scoreBox, statusBox, roundCount;
    private List<TextureRegion> bullets, birdIcons, scoreCounts;
    private List<Duck> ducks;
    private int round, subRound, score, ducksShot, shotsAvailable;

    GameState(GSM gsm) {
        super(gsm);

        ducksShot = 0;
        score = 0;
        round = 1;
        subRound = 1;
        shotsAvailable = 3;

        bullets = new ArrayList<TextureRegion>(NUM_BULLETS);
        birdIcons = new ArrayList<TextureRegion>(10);
        scoreCounts = new ArrayList<TextureRegion>(6);

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


        for (int i = 0; i < NUM_BIRD_ICONS; i++)
            birdIcons.add(DuckHunt.res.getAtlas("pack").findRegion("notHit"));

        for (int i = 0; i < NUM_BULLETS; ++i)
            bullets.add(DuckHunt.res.getAtlas("pack").findRegion("bullet"));

        for (int i = 0; i < NUM_SCORE_DIGITS; ++i)
            scoreCounts.add(whiteDigits.get(score / (10 * (i + 1))));

        roundCount = greenDigits.get(1);

    }

    @Override
    public void update(float dt) {
        super.processMouse();

        for (Duck duck : ducks)
            duck.update(dt);

        if (allDucksAreGone()) {
            subRound++;

            if (subRound > MAX_SUBROUNDS)
                endRound();

            shotsAvailable = 3;
            resetDucks(dt);
        }
    }

    private boolean allDucksAreGone() {
        for (Duck duck : ducks)
            if (!duck.isGone())
                return false;

        return true;
    }

    private void endRound() {
        round++;
        
        if(round >= 10) {
            endGame();
            return;
        }
            
        roundCount = greenDigits.get(round);
        
        subRound = 1;
        ducksShot = 0;
        
        for (int i = 0; i < NUM_BIRD_ICONS; i++)
            birdIcons.set(i, DuckHunt.res.getAtlas("pack").findRegion("notHit"));
    }
    
    private void endGame() {
        if (score >= DuckHunt.scores[0])
            DuckHunt.scores[0] = score;
        
        Arrays.sort(DuckHunt.scores);
        
        for (int i = 0; i < 10; i++)
            DuckHunt.prefs.putInteger("score" + i, DuckHunt.scores[i]);
        
        DuckHunt.prefs.flush();
        
        gsm.set(new MenuState(gsm));
    }

    private void resetDucks(float dt) {
        ducks.clear();

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

        for (int i = 0; i < NUM_BIRD_ICONS; i++) {
            TextureRegion birdIcon = birdIcons.get(i);
         
            sb.draw(birdIcon, 290 + (birdIcon.getRegionWidth() * 3.125f + 3) * i, 62,
                    birdIcon.getRegionWidth() * 3.125f, birdIcon.getRegionHeight() * 2.5f);
        }

        for (int i = 0; i < shotsAvailable; ++i) {
            TextureRegion bullet = bullets.get(i);
            
            sb.draw(bullet, 83 + (bullet.getRegionWidth() * 3.125f + 10) * i, 65,
                    bullet.getRegionWidth() * 3.125f, bullet.getRegionHeight() * 2.5f);
        }

        for (int i = 0; i < NUM_SCORE_DIGITS; ++i) {
            TextureRegion scoreDigit = scoreCounts.get(i);

            sb.draw(scoreDigit, (DuckHunt.WIDTH - 75) - (scoreDigit.getRegionWidth() * 3.125f + 3) * i, 64,
                    scoreDigit.getRegionWidth() * 3.125f, scoreDigit.getRegionHeight() * 2.5f);
        }

        for (Duck duck : ducks)
            duck.render(sb);

        sb.draw(tree, 20, 213, tree.getRegionWidth() * 2 + 75, tree.getRegionHeight() * 2 + 60);
        sb.draw(grass, 0, 145, DuckHunt.WIDTH, grass.getRegionHeight() * 2 + 15);
        sb.end();
    }

    @Override
    public void onSuccessfulMouseClick(Point clickedPoint) {
        if (--shotsAvailable < 0)
            return;

        for (Duck duck : ducks) {
            if (duck.isAlive() && duck.contains(clickedPoint)) {
                ducksShot++;
                duck.onClick();
                updateScore(duck.getValue());
                birdIcons.set(ducksShot - 1, DuckHunt.res.getAtlas("pack").findRegion("hit"));
            }
        }
    }

    private void updateScore(int value) {
        score += value;

        for (int i = 0; i < NUM_SCORE_DIGITS; i++)
            scoreCounts.set(i, whiteDigits.get((score / (int) Math.pow(10, i)) % 10));
    }

}
