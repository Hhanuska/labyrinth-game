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

    public static void loadLevel() throws IOException {
        level = OBJECT_MAPPER.readValue(
                new File("target/classes/game/level.json"), Maze.class
        );
    }

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

    public static HighScores getHighScores() {
        return highScores;
    }
}
