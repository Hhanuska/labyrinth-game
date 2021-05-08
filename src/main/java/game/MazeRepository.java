package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class MazeRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Maze level;

    public MazeRepository() {
        try {
            loadLevel();
            Logger.info("Level loaded");
        } catch(IOException e) {
            String errMsg = "Failed to load resource level.json";
            Logger.error(errMsg);
            throw new AssertionError(errMsg, e);
        }
    }

    private void loadLevel() throws IOException {
        level = OBJECT_MAPPER.readValue(new File("target/classes/game/level.json"), Maze.class);
    }

    public Maze getLevel() {
        return level;
    }
}
