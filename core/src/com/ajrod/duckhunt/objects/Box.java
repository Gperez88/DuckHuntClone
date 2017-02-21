package com.ajrod.duckhunt.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Box {
	
	protected float x, y, width, height, closeness;
	
	public boolean contains(float x, float y) {
		return x > this.x - width/2 &&
				x < this.x + width/2 &&
				y > this.y - height/2 &&
				y < this.y + height/2;
	}
	
	public float getCloseness() {
		return closeness;
	}
	
	abstract public void update(float dt);
	abstract public void render(SpriteBatch sb);
	abstract public void onClick();
}
