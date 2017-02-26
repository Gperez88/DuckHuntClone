package com.ajrod.duckhunt.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GSM {
    private Stack<State> states;

    public GSM() {
        states = new Stack<State>();
    }

    public void push(State s) {
        states.push(s);
    }

    private void pop() {
        states.pop();
    }

    void set(State s) {
        pop();
        states.push(s);
    }

    public void update(float dt) { //if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }
}
