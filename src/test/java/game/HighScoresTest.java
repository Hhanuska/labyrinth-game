package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HighScoresTest {
    @Test
    private void testHighScores() {
        var hs = new HighScores();

        hs.addScore(new HighScore("test1", (long) 1111));
        assertEquals(hs.getScores()[0].getName(), "test1");
        assertEquals(hs.getScores()[0].getTime(), 1111);
        assertNull(hs.getScores()[1]);
    }
}
