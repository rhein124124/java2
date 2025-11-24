package com.tron.gamestate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuStateTest {

    private GameStateManager gsm;
    private MenuState menuState;

    @BeforeEach
    void setUp() {
        gsm = GameStateManager.getInstance();
        menuState = new MenuState(gsm);
        gsm.push(menuState);
    }

    @Test
    void testInitialState() {
        assertNotNull(menuState, "MenuState should be created");
        assertEquals(menuState, gsm.peekState(), "MenuState should be the current state");
    }

}
