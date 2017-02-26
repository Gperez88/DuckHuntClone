package com.ajrod.duckhunt.states;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Button.BackButton;
import com.ajrod.duckhunt.objects.Button.Button;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LeaderboardState extends State {

    private Button backButton;
    private TextureRegion bg;

    LeaderboardState(GSM gsm) {
        super(gsm);
        backButton = new BackButton(new Point(DuckHunt.WIDTH / 2, 100));
        bg = new TextureRegion(new Texture(Gdx.files.internal("bg.png")));
    }

    @Override
    public void update(float dt) {
        super.processMouse();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, DuckHunt.WIDTH, DuckHunt.HEIGHT);
        DuckHunt.font.setScale(0.75f);
        for (int i = 0; i < 10; i++)
            DuckHunt.font.draw(sb, (i + 1) + ".	.	.	.	" + DuckHunt.scores[9 - i], 300, (DuckHunt.HEIGHT - 50) - 30 * i);
        DuckHunt.font.setScale(1f);
        backButton.render(sb);
        sb.end();
    }

    @Override
    public void onSuccessfulMouseClick(Point clickedPoint) {
        if (backButton.contains(clickedPoint))
            gsm.set(new MenuState(gsm));
    }
}
