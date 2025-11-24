package com.tron.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {

    private PlayerHumanModel player;

    @BeforeEach
    void setUp() {
        // Use PlayerHumanModel as a concrete implementation for testing
        player = new PlayerHumanModel(100, 100, 3, 0, 5, 5, PlayerModel.TronColor.BLUE);
        player.setBounds(500, 500); // Set map bounds for the player
    }

    @Test
    void testPlayerInitialization() {
        assertTrue(player.isAlive(), "Player should be alive upon initialization");
        assertEquals(100, player.getX(), "Player should have correct initial X coordinate");
        assertEquals(100, player.getY(), "Player should have correct initial Y coordinate");
        assertEquals(3, player.getBoostsLeft(), "Player should start with 3 boosts");
    }

    @Test
    void testPlayerMovement() {
        int initialX = player.getX();
        player.move();
        assertEquals(initialX + 3, player.getX(), "Player should move in the X direction based on its velocity");
    }

    @Test
    void testSetDirection() {
        // Change direction to UP
        player.setDirection(PlayerModel.Direction.UP);
        player.move();
        assertEquals(100, player.getX(), "X should not change when moving UP");
        assertEquals(100 - 3, player.getY(), "Y should decrease when moving UP");
    }

    @Test
    void testPlayerBoost() {
        player.startBoost();
        assertTrue(player.booster, "Player's booster flag should be true after starting boost");
        assertEquals(2, player.getBoostsLeft(), "Player should have one less boost after using it");
        
        player.updateBoost(System.nanoTime()); // Apply boost
        
        // Velocity should be VELBOOST
        assertEquals(PlayerModel.VELBOOST, player.getVelocityX(), "Player velocity should increase during boost");
    }

    @Test
    void testPlayerCollisionWithBounds() {
        // Move player close to the right boundary
        player.setX(498); 
        player.move(); // This move should make it hit the boundary (498 + 3 = 501, clipped to 495)
        player.accelerate(); // This checks bounds and sets alive to false
        
        assertFalse(player.isAlive(), "Player should not be alive after hitting a boundary");
    }
}
