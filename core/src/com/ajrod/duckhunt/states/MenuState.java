package com.ajrod.duckhunt.states;

import com.ajrod.duckhunt.DuckHunt;
import com.ajrod.duckhunt.objects.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuState extends State {
	
	private TextureRegion bg;
	private Button b1, b2;

	public MenuState(GSM gsm) {
		super(gsm);
		
		bg = new TextureRegion(new Texture(Gdx.files.internal("bg.png")));
		
		b1 = new Button(DuckHunt.WIDTH/2, DuckHunt.HEIGHT/2 - 50, 0);
		b2 = new Button(DuckHunt.WIDTH/2, DuckHunt.HEIGHT/2 - 150, 1);
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(bg, 0, 0, DuckHunt.WIDTH, DuckHunt.HEIGHT);
		b1.render(sb);
		b2.render(sb);
		DuckHunt.font.setScale(3.0f);;
		DuckHunt.font.draw(sb, "HUNTY", DuckHunt.WIDTH/2 - 170, DuckHunt.HEIGHT/2 + 250);
		DuckHunt.font.draw(sb, "DUCK", DuckHunt.WIDTH/2 - 130, DuckHunt.HEIGHT/2 + 150);
		DuckHunt.font.setScale(1.0f);
		sb.end();
	}

	@Override
	public void handleInput() {
		if(Gdx.input.justTouched()) {
			mouse.x = Gdx.input.getX();
			mouse.y = Gdx.input.getY();
			cam.unproject(mouse);
			if (b1.contains(mouse.x, mouse.y)) {
				gsm.set(new GameState(gsm));
			}
			else if (b2.contains(mouse.x, mouse.y)) {
				gsm.set(new LeaderboardState(gsm));
			}
		}
	}

}
