package com.tron.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class TronMapModelTest {

    private TestTronMapModel tronMap;

    // Concrete implementation for testing abstract TronMapModel
    private static class TestTronMapModel extends TronMapModel {
        public TestTronMapModel(int numPlayers) {
            super(numPlayers);
        }

        @Override
        public void tick(double dt) {
            // Dummy implementation for testing
        }

        @Override
        public void reset() {
            // Dummy implementation for testing
            this.gameRunning = true; // Set to running for some tests
            this.scorePlayer1 = 0;
            this.scorePlayer2 = 0;
            // Clear and add players if needed for specific tests
            if (players == null) {
                players = new ArrayList<>();
            } else {
                players.clear();
            }
        }

        @Override
        public void updateScores() {
            // Dummy implementation
        }

        @Override
        public void addScore(int playerIndex, int scoreToAdd) {
            // Dummy implementation
        }
    }

    @BeforeEach
    void setUp() {
        tronMap = new TestTronMapModel(1); // Test with 1 player initially
    }

    @Test
    void testInitialization() {
        assertNotNull(tronMap.availableColors);
        assertFalse(tronMap.availableColors.isEmpty());
        assertEquals(1, tronMap.players.size()); // Initially sized list, but no actual players
        assertFalse(tronMap.isGameRunning()); // Should not be running initially
        assertEquals(0, tronMap.getScorePlayer1());
        assertEquals(0, tronMap.getScorePlayer2());
    }

    @Test
    void testSetPlayers() {
        List<PlayerModel> newPlayers = new ArrayList<>();
        newPlayers.add(new PlayerHumanModel(0,0,0,0,5,5, PlayerModel.TronColor.BLUE));
        tronMap.setPlayers(newPlayers);
        assertEquals(1, tronMap.getPlayers().size());
        assertNotNull(tronMap.getPlayers().get(0));
    }

    @Test
    void testGetRandomStart() {
        int[] start = tronMap.getRandomStart();
        assertNotNull(start);
        assertEquals(4, start.length); // x, y, velX, velY
        assertTrue(start[0] >= 50 && start[0] <= 450); // Within 50-450 bounds
        assertTrue(start[1] >= 50 && start[1] <= 450); // Within 50-450 bounds
        
        // Check if either velX or velY is non-zero and has correct magnitude
        assertTrue(Math.abs(start[2]) == TronMapModel.VELOCITY || Math.abs(start[3]) == TronMapModel.VELOCITY);
        assertTrue(start[2] == 0 || start[3] == 0); // Only one velocity component should be non-zero
    }

    @Test
    void testReset() {
        tronMap.players.add(new PlayerHumanModel(0,0,0,0,5,5, PlayerModel.TronColor.BLUE)); // Add dummy player
        tronMap.reset();
        assertTrue(tronMap.isGameRunning());
        assertEquals(0, tronMap.getScorePlayer1());
        assertEquals(0, tronMap.getScorePlayer2());
        assertTrue(tronMap.players.isEmpty()); // Should clear players on reset
    }
}
