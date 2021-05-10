package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Class responsible for loading mazes.
 */
public class MazeRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Maze level;

    /**
     * Creates a MazeRepository object.
     */
    public MazeRepository() {
        try {
            loadLevel();
            Logger.info("Level loaded");
        } catch (IOException e) {
            String errMsg = "Failed to load resource level.json";
            Logger.error(errMsg);
            throw new AssertionError(errMsg, e);
        }
    }

    private void loadLevel() throws IOException {
        level = OBJECT_MAPPER.readValue(
                new File("target/classes/game/level.json"), Maze.class
        );
    }

    /**
     * Returns the loaded information about the maze.
     *
     * @return The loaded information about the maze
     */
    public Maze getLevel() {
        return level;
    }
}
