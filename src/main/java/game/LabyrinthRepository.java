package game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LabyrinthRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Labyrinth level;

    public LabyrinthRepository() {
        try {
            loadLevel();
        } catch(IOException e) {
            throw new AssertionError("Failed to load resource level.json", e);
        }
    }

    private void loadLevel() throws IOException {
        level = OBJECT_MAPPER.readValue(new File("src/main/resources/game/level.json"), Labyrinth.class);
    }

    public Labyrinth getLevel() {
        return level;
    }
}
