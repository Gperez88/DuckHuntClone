package com.ajrod.duckhunt.objects.Button;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Box;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Button extends Box {

    private final static float xPadding = 10, yPadding = 15;
    protected String text;
    private TextureRegion button;

    public Button(Point p) {
        super(p, 100, 50);  // default values
        button = new TextureRegion(new Texture(Gdx.files.internal("button.png")));
    }

    public void render(SpriteBatch sb) {
        Point p = getLowerLeftCorner();
        sb.draw(button, p.x, p.y, width, height);
        DuckHunt.font.draw(sb, text, p.x + xPadding, center.y + yPadding);
    }

}
