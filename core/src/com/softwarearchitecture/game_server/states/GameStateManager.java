package com.softwarearchitecture.game_server.states;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStateManager {
    /**
     * Keeps track of the current state of the game
     * 
     * 
     */
    private Stack<State> states;

    public GameStateManager() {
        states = new Stack<State>();
    }

    public void push(State state) {
        if (!states.isEmpty()) {
            states.peek().dispose();
        }
        states.push(state);
    }

    public void setOverlapping(State state) {
        states.push(state);
    }

    public State peek() {
        try {
            return states.lastElement();

        } catch (Exception e) {
            System.out.println("could not peek: " + e);
        }
        return null;
    }

    public void pop() {
        states.pop().dispose();
    }

    public State popKeepState() {
        State state = peek();
        states.pop();
        return state;
    }

    public void update(float deltaTime) {
        states.peek().update(deltaTime);
    }

    public void render(SpriteBatch spriteBatch) {
        states.peek().render(spriteBatch);
    }

}
