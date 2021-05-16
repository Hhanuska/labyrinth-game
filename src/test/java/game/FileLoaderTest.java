package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileLoaderTest {

    @Test
    public void testLoadLevel() throws IOException {
        FileLoader.loadLevel();

        assertNotNull(FileLoader.getLevel());
    }

    @Test
    public void testLoadHighscores() throws IOException {
        FileLoader.loadHighScores();

        assertNotNull(FileLoader.getHighScores());
    }
}
