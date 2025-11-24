package com.tron.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameObjectModelTest {

    private TestGameObject gameObject;

    // Concrete implementation for testing abstract GameObjectModel
    private static class TestGameObject extends GameObjectModel {
        private boolean alive;

        public TestGameObject(int x, int y, int velocityX, int velocityY, int width, int height, boolean alive) {
            super(x, y, velocityX, velocityY, width, height);
            this.alive = alive;
        }

        @Override
        public void accelerate() {
            // Dummy implementation for testing purposes
            if (x < 0 || x > rightBound || y < 0 || y > bottomBound) {
                this.alive = false;
            }
        }

        @Override
        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }
    }

    @BeforeEach
    void setUp() {
        gameObject = new TestGameObject(10, 10, 1, 0, 5, 5, true);
        gameObject.setBounds(100, 100); // Max 100x100 bounds
    }

    @Test
    void testInitialization() {
        assertEquals(10, gameObject.getX());
        assertEquals(10, gameObject.getY());
        assertEquals(1, gameObject.getVelocityX());
        assertEquals(0, gameObject.getVelocityY());
        assertEquals(5, gameObject.getWidth());
        assertEquals(5, gameObject.getHeight());
        assertTrue(gameObject.isAlive());
    }

    @Test
    void testSetBounds() {
        gameObject.setBounds(200, 200);
        // rightBound = 200 - 5 = 195
        // bottomBound = 200 - 5 = 195
        gameObject.x = 196; // Out of bounds
        gameObject.y = 196; // Out of bounds
        gameObject.clip();
        assertEquals(195, gameObject.getX());
        assertEquals(195, gameObject.getY());
    }

    @Test
    void testSetVelocity() {
        gameObject.setXVelocity(5);
        assertEquals(5, gameObject.getVelocityX());
        gameObject.setYVelocity(2);
        assertEquals(2, gameObject.getVelocityY());

        // Should not change velocity if it opposes current motion
        gameObject.setXVelocity(-1); // Current velocityX is 5, opposes -1
        assertEquals(5, gameObject.getVelocityX());
    }

    @Test
    void testMoveAndClip() {
        gameObject.move(); // x=11, y=10
        assertEquals(11, gameObject.getX());
        assertEquals(10, gameObject.getY());

        gameObject.x = -5; // Manually set out of bounds
        gameObject.clip();
        assertEquals(0, gameObject.getX());

        gameObject.x = 100; // Manually set out of bounds
        gameObject.clip();
        assertEquals(95, gameObject.getX()); // Clipped to rightBound - width
    }

    @Test
    void testAccelerateSetsNotAliveOutOfBounds() {
        gameObject.setX(96); // Set position near boundary
        gameObject.setXVelocity(10); // Set velocity to go out of bounds
        gameObject.move(); // Move x to 106, which is > rightBound (95)
        
        assertFalse(gameObject.isAlive(), "GameObject should be not alive after moving out of bounds");
        assertEquals(0, gameObject.getVelocityX(), "VelocityX should be 0 after being not alive");
    }
}
