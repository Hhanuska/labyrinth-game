package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Class responsible for loading mazes and high scores.
 */
public class FileLoader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static Maze level;

    private static HighScores highScores;

    /**
     * Loads the information about the maze.
     *
     * @throws IOException if the file doesn't exist
     */
    public static void loadLevel() throws IOException {
        level = OBJECT_MAPPER.readValue(
                new File("target/classes/game/level.json"), Maze.class
        );
    }

    /**
     * Loads the high scores.
     *
     * @throws IOException if the file doesn't exist
     */
    public static void loadHighScores() throws IOException {
        highScores = OBJECT_MAPPER.readValue(
                new File("target/classes/game/highscores.json"), HighScores.class
        );
    }

    /**
     * Returns the loaded information about the maze.
     *
     * @return The loaded information about the maze
     */
    public static Maze getLevel() {
        return level;
    }

    /**
     * Returns the loaded high scores.
     *
     * @return The loaded high scores
     */
    public static HighScores getHighScores() {
        return highScores;
    }
}
