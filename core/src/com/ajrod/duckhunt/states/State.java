package com.ajrod.duckhunt.states;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {
    GSM gsm;
    private OrthographicCamera cam;
    private Vector3 mouse;

    State(GSM gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, DuckHunt.WIDTH, DuckHunt.HEIGHT);
        mouse = new Vector3();
    }

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);

    protected final void processMouse() {
        if (!Gdx.input.justTouched())
            return;

        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();

        cam.unproject(mouse);

        Point clickedPoint = new Point(mouse.x, mouse.y);

        onSuccessfulMouseClick(clickedPoint);
    }

    public abstract void onSuccessfulMouseClick(Point p);

}
