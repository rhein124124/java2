package com.tron.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PlayerAIModelTest {

    private PlayerAIModel playerAI;
    private List<PlayerModel> allPlayers;

    @BeforeEach
    void setUp() {
        playerAI = new PlayerAIModel(50, 50, 3, 0, 5, 5, PlayerModel.TronColor.RED);
        playerAI.setBounds(100, 100); // Small bounds for testing
        
        // Setup a dummy human player to be part of allPlayers list
        PlayerHumanModel humanPlayer = new PlayerHumanModel(10, 10, 0, 0, 5, 5, PlayerModel.TronColor.BLUE);
        allPlayers = new ArrayList<>(Arrays.asList(playerAI, humanPlayer));
        playerAI.setAllPlayers(allPlayers);
    }

    @Test
    void testInitialization() {
        assertNotNull(playerAI);
        assertEquals(50, playerAI.getX());
        assertEquals(50, playerAI.getY());
        assertEquals(3, playerAI.getVelocityX());
        assertEquals(0, playerAI.getVelocityY());
        assertTrue(playerAI.isAlive());
    }

    @Test
    void testSetAllPlayers() {
        assertEquals(2, playerAI.allPlayers.size()); // Accessing protected for testing
        assertSame(playerAI, playerAI.allPlayers.get(0));
    }

    @Test
    void testReactProximityWallAvoidance() {
        playerAI.setX(90); // Move close to right wall
        playerAI.setY(50);
        playerAI.setXVelocity(3); // Moving right

        // Force reactProximity to be called by setting time to 0
        playerAI.time = 0; 
        playerAI.reactProximity(); 
        
        // Expect AI to turn away from the wall
        // Given initial (3,0) moving right, it should try to turn UP or DOWN
        // If it was (90,50) and bounds (100,100), new vel should be (0, -3) or (0,3)
        assertTrue(playerAI.getVelocityY() != 0, "AI should change Y velocity to avoid wall");
        assertEquals(0, playerAI.getVelocityX(), "AI should stop moving in X direction");
    }
    
    @Test
    void testDetectCollisionRiskWithWall() {
        playerAI.setBounds(60, 60); // Smaller bounds
        // Test risk when moving right into wall
        assertTrue(playerAI.detectCollisionRisk(60, 30, 1), "Should detect collision risk with right wall");
        // Test risk when moving down into wall
        playerAI.setXVelocity(0);
        playerAI.setYVelocity(3);
        assertTrue(playerAI.detectCollisionRisk(30, 60, 1), "Should detect collision risk with bottom wall");
    }

    @Test
    void testDetectCollisionRiskWithPath() {
        // Setup human player to leave a path segment
        PlayerHumanModel human = (PlayerHumanModel) allPlayers.get(1);
        human.setX(50); human.setY(50); human.setXVelocity(0); human.setYVelocity(-3);
        human.move(); // Creates path segment (50,50) to (50,47)
        human.move(); // Creates path segment (50,47) to (50,44)
        
        playerAI.setX(48); playerAI.setY(46); // AI is near the path
        playerAI.setXVelocity(3); // AI moving right
        
        // Check if AI detects the human's path as a risk
        assertTrue(playerAI.detectCollisionRisk(51, 46, 1), "AI should detect collision risk with human's path");
    }
}
