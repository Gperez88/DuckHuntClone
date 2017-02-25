package com.ajrod.duckhunt;

import com.ajrod.duckhunt.handler.Content;
import com.ajrod.duckhunt.states.GSM;
import com.ajrod.duckhunt.states.MenuState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Arrays;

public class DuckHunt extends ApplicationAdapter {

    public static final String TITLE = "Duck Hunt";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static BitmapFont font;
    public static Content res;
    public static Preferences prefs;
    public static int[] scores = new int[10];

    private GSM gsm;
    private SpriteBatch sb;

    @Override
    public void create() {
        gsm = new GSM();
        sb = new SpriteBatch();
        res = new Content();
        font = new BitmapFont(Gdx.files.internal("flipps.fnt"), false);
        prefs = Gdx.app.getPreferences("DuckHunt");

        for (int i = 0; i < 10; i++) {
            if (prefs.contains("score" + i))
                scores[i] = prefs.getInteger("score" + i);
            else
                scores[i] = 0;
        }

        Arrays.sort(scores);

        res.loadAtlas("pack.pack", "pack");
        gsm.push(new MenuState(gsm));

        Gdx.gl.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(sb);
    }

    public void dispose() {
        super.dispose();
    }
}
