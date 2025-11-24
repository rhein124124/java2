package com.tron.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class ScoreModelTest {

    @TempDir
    Path tempDir; // JUnit 5 provides temporary directory

    private File tempScoresFile;

    @BeforeEach
    void setUp() throws IOException {
        tempScoresFile = tempDir.resolve("test_high_scores.txt").toFile();
    }

    private void writeScoresToFile(int... scores) throws IOException {
        List<String> scoreStrings = new ArrayList<>();
        for (int score : scores) {
            scoreStrings.add(String.valueOf(score));
        }
        Files.write(tempScoresFile.toPath(), scoreStrings);
    }

    @Test
    void testInitializationWithEmptyFile() {
        ScoreModel scoreModel = new ScoreModel(tempScoresFile.getAbsolutePath());
        assertNotNull(scoreModel.getHighScores());
        assertTrue(scoreModel.getHighScores().isEmpty());
    }

    @Test
    void testInitializationWithExistingScores() throws IOException {
        writeScoresToFile(100, 90, 80);
        ScoreModel scoreModel = new ScoreModel(tempScoresFile.getAbsolutePath());
        List<Integer> expected = Arrays.asList(100, 90, 80);
        assertEquals(expected, scoreModel.getHighScores());
    }

    @Test
    void testAddHighScore() throws IOException {
        writeScoresToFile(100, 90, 80);
        ScoreModel scoreModel = new ScoreModel(tempScoresFile.getAbsolutePath());

        scoreModel.addHighScore(110);
        List<Integer> expected1 = Arrays.asList(110, 100, 90, 80);
        assertEquals(expected1, scoreModel.getHighScores());

        scoreModel.addHighScore(85);
        List<Integer> expected2 = Arrays.asList(110, 100, 90, 85, 80);
        assertEquals(expected2, scoreModel.getHighScores());
    }

    @Test
    void testAddHighScoreExceedsTen() throws IOException {
        writeScoresToFile(100, 90, 80, 70, 60, 50, 40, 30, 20, 10); // 10 scores
        ScoreModel scoreModel = new ScoreModel(tempScoresFile.getAbsolutePath());

        scoreModel.addHighScore(105); // New top score
        List<Integer> expected1 = Arrays.asList(105, 100, 90, 80, 70, 60, 50, 40, 30, 20); // Top 10
        assertEquals(expected1, scoreModel.getHighScores());
        assertEquals(10, scoreModel.getHighScores().size());

        scoreModel.addHighScore(5); // New low score, should not be added
        assertEquals(expected1, scoreModel.getHighScores()); // Should remain the same top 10
        assertEquals(10, scoreModel.getHighScores().size());
    }

    @Test
    void testSaveHighScores() throws IOException {
        ScoreModel scoreModel = new ScoreModel(tempScoresFile.getAbsolutePath());
        scoreModel.addHighScore(75);
        scoreModel.addHighScore(120);

        // Re-read file to verify content
        List<String> lines = Files.readAllLines(tempScoresFile.toPath());
        List<String> expectedLines = Arrays.asList("120", "75");
        assertEquals(expectedLines, lines);
    }
}
