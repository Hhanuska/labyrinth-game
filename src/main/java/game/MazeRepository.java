package game;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class MazeRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Maze level;

    public MazeRepository() {
        try {
            loadLevel();
        } catch(IOException e) {
            throw new AssertionError("Failed to load resource level.json", e);
        }
    }

    private void loadLevel() throws IOException {
        level = OBJECT_MAPPER.readValue(new File("src/main/resources/game/level.json"), Maze.class);
    }

    public Maze getLevel() {
        return level;
    }
}
