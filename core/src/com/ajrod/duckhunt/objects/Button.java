package com.ajrod.duckhunt.objects;

import com.ajrod.duckhunt.DuckHunt;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button extends Box {

    private TextureRegion button;
    private String text;
    private float xPadding, yPadding;

    public Button(Point p, final int state) {
        super(p, 500, 50);

        xPadding = 10;
        yPadding = 15;

        switch (state) {
            case 0:
                text = "Play";
                width = 100;
                break;
            case 1:
                text = "Leaderboards";
                width = 300;
                break;
            default:
                text = "Back";
                width = 110;
                break;
        }

        button = new TextureRegion(new Texture(Gdx.files.internal("button.png")));
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {
        Point p = getLowerLeftCorner();
        sb.draw(button, p.x, p.y, width, height);
        DuckHunt.font.draw(sb, text, p.x + xPadding, center.y + yPadding);
    }

    @Override
    public void onClick() {
    }

}
