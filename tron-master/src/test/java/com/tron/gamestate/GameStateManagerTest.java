package com.tron.gamestate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateManagerTest {

    private GameStateManager gsm;
    private GameState dummyState1;
    private GameState dummyState2;

    @BeforeEach
    void setUp() {
        gsm = GameStateManager.getInstance();
        // Clear any previous states for clean test execution
        while (gsm.getStateCount() > 0) {
            gsm.pop();
        }

        dummyState1 = new GameState() {
            @Override public void init() {}
            @Override public void handleInput() {}
            @Override public void update(double dt) {}
            @Override public void render(javafx.scene.canvas.GraphicsContext gc) {}
        };
        dummyState2 = new GameState() {
            @Override public void init() {}
            @Override public void handleInput() {}
            @Override public void update(double dt) {}
            @Override public void render(javafx.scene.canvas.GraphicsContext gc) {}
        };
    }

    @Test
    void testSingleton() {
        GameStateManager gsm1 = GameStateManager.getInstance();
        GameStateManager gsm2 = GameStateManager.getInstance();
        assertSame(gsm1, gsm2, "GameStateManager should be a singleton");
    }

    @Test
    void testPushAndPopState() {
        assertEquals(0, gsm.getStateCount(), "State count should be 0 initially");
        gsm.push(dummyState1);
        assertEquals(1, gsm.getStateCount(), "State count should be 1 after push");
        assertSame(dummyState1, gsm.peekState(), "Peeked state should be dummyState1");

        gsm.push(dummyState2);
        assertEquals(2, gsm.getStateCount(), "State count should be 2 after second push");
        assertSame(dummyState2, gsm.peekState(), "Peeked state should be dummyState2");

        gsm.pop();
        assertEquals(1, gsm.getStateCount(), "State count should be 1 after pop");
        assertSame(dummyState1, gsm.peekState(), "Peeked state should be dummyState1");

        gsm.pop();
        assertEquals(0, gsm.getStateCount(), "State count should be 0 after second pop");
        assertNull(gsm.peekState(), "Peeked state should be null after all pops");
    }

    @Test
    void testSetState() {
        gsm.push(dummyState1);
        assertEquals(1, gsm.getStateCount());
        assertSame(dummyState1, gsm.peekState());

        gsm.setState(dummyState2);
        assertEquals(1, gsm.getStateCount(), "State count should be 1 after set");
        assertSame(dummyState2, gsm.peekState(), "Peeked state should be dummyState2");
    }

    @Test
    void testPushNullState() {
        assertThrows(IllegalArgumentException.class, () -> gsm.push(null), "Pushing null state should throw IllegalArgumentException");
    }

    @Test
    void testSetNullState() {
        assertThrows(IllegalArgumentException.class, () -> gsm.setState(null), "Setting null state should throw IllegalArgumentException");
    }
}
