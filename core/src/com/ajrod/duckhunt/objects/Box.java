package com.ajrod.duckhunt.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Box {

    protected Point center;
    protected float width, height;

    public Box(Point p, float w, float h) {
        center = p;
        width = w;
        height = h;
    }

    public final boolean contains(Point p) {
        boolean containsX = valuesAreWithinRadius(center.x, p.x, width / 2);
        boolean containsY = valuesAreWithinRadius(center.y, p.y, height / 2);

        return containsX && containsY;
    }

    public Point getLowerLeftCorner() {
        return new Point(getLowX(), getLowY());
    }

    private float getLowX() {
        return center.x - width / 2;
    }

    private float getLowY() {
        return center.y - height / 2;
    }

    private static boolean valuesAreWithinRadius(float a, float b, float radius) {
        return Math.abs(b - a) < radius;
    }

    abstract public void update(float dt);

    abstract public void render(SpriteBatch sb);

    abstract public void onClick();
}
