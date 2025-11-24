package com.tron.gamestate;

import javafx.scene.canvas.GraphicsContext;
import java.util.Stack;

public class GameStateManager {

    private static GameStateManager gsm;
    private Stack<GameState> states;

    private GameStateManager() {
        states = new Stack<>();
    }

    public static GameStateManager getInstance() {
        if (gsm == null) {
            gsm = new GameStateManager();
        }
        return gsm;
    }

    public void push(GameState state) {
        if (state == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        states.push(state);
        state.init();
    }

    public void pop() {
        if (!states.isEmpty()) {
            states.pop();
        }
    }

    public void setState(GameState state) {
        if (state == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        pop();
        push(state);
    }
    
    public void update(double dt) {
        if (!states.isEmpty()) {
            states.peek().update(dt);
        }
    }

    public void render(GraphicsContext gc) {
        if (!states.isEmpty()) {
            states.peek().render(gc);
        }
    }
    
    public void handleInput() {
        if (!states.isEmpty()) {
            states.peek().handleInput();
        }
    }

    // For testing purposes
    public int getStateCount() {
        return states.size();
    }

    // For testing purposes
    public GameState peekState() {
        return states.isEmpty() ? null : states.peek();
    }
}
