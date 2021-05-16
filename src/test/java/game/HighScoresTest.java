package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HighScoresTest {

    @Test
    public void testHighScores() {
        var hs = new HighScores();

        hs.addScore(new HighScore("test1", (long) 1111));
        assertEquals(hs.getScores()[0].getName(), "test1");
        assertEquals(hs.getScores()[0].getTime(), 1111);
        assertNull(hs.getScores()[1]);

        hs.addScore(new HighScore("test2", (long) 2222));
        assertEquals(hs.getScores()[0].getName(), "test1");
        assertEquals(hs.getScores()[0].getTime(), 1111);
        assertEquals(hs.getScores()[1].getName(), "test2");
        assertEquals(hs.getScores()[1].getTime(), 2222);

        hs.addScore(new HighScore("test3", (long) 1500));
        assertEquals(hs.getScores()[0].getName(), "test1");
        assertEquals(hs.getScores()[0].getTime(), 1111);
        assertEquals(hs.getScores()[1].getName(), "test3");
        assertEquals(hs.getScores()[1].getTime(), 1500);
        assertEquals(hs.getScores()[2].getName(), "test2");
        assertEquals(hs.getScores()[2].getTime(), 2222);

        hs.addScore(new HighScore("test3", (long) 1000));
        assertEquals(hs.getScores()[1].getName(), "test1");
        assertEquals(hs.getScores()[1].getTime(), 1111);
        assertEquals(hs.getScores()[0].getName(), "test3");
        assertEquals(hs.getScores()[0].getTime(), 1000);

        hs.addScore(new HighScore("test4", (long) 4444));
        hs.addScore(new HighScore("test5", (long) 5555));
        hs.addScore(new HighScore("test6", (long) 4000));
        assertEquals(hs.getScores()[1].getName(), "test1");
        assertEquals(hs.getScores()[1].getTime(), 1111);
        assertEquals(hs.getScores()[0].getName(), "test3");
        assertEquals(hs.getScores()[0].getTime(), 1000);
        assertEquals(hs.getScores()[2].getName(), "test2");
        assertEquals(hs.getScores()[2].getTime(), 2222);
        assertEquals(hs.getScores()[3].getName(), "test6");
        assertEquals(hs.getScores()[3].getTime(), 4000);
        assertEquals(hs.getScores()[4].getName(), "test4");
        assertEquals(hs.getScores()[4].getTime(), 4444);
    }
}
