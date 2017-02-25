package com.ajrod.duckhunt.states;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Button;
import com.ajrod.duckhunt.objects.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LeaderboardState extends State {

    private Button b;
    private TextureRegion bg;

    public LeaderboardState(GSM gsm) {
        super(gsm);
        b = new Button(new Point(DuckHunt.WIDTH / 2, 100), 2);
        bg = new TextureRegion(new Texture(Gdx.files.internal("bg.png")));
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, DuckHunt.WIDTH, DuckHunt.HEIGHT);
        DuckHunt.font.setScale(0.75f);
        for (int i = 0; i < 10; i++)
            DuckHunt.font.draw(sb, (i + 1) + ".	.	.	.	" + DuckHunt.scores[9 - i], 300, (DuckHunt.HEIGHT - 50) - 30 * i);
        DuckHunt.font.setScale(1f);
        b.render(sb);
        sb.end();
    }

    @Override
    public void handleInput() {
        if (!Gdx.input.justTouched())
            return;

        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();

        cam.unproject(mouse);

        Point clickedPoint = new Point(mouse.x, mouse.y);

        if (b.contains(clickedPoint))
            gsm.set(new MenuState(gsm));
    }
}
