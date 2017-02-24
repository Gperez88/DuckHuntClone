package com.ajrod.duckhunt.objects;

import com.ajrod.duckhunt.DuckHunt;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button extends Box {

    private TextureRegion button;
    private String text;
    private int state;

    public Button(int x, int y, int s) {
        height = 50;
        this.x = x;
        this.y = y;
        state = s;

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
        sb.draw(button, x - width / 2, y - height / 2, width, height);
        DuckHunt.font.draw(sb, text, x - width / 2 + 10, y + 15);
    }

    @Override
    public void onClick() {
    }

}
