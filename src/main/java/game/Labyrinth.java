package game;

import lombok.Data;

import java.util.List;

@Data
public class Labyrinth {
    private List<List<String>> level;

    private Position start;

    private Position finish;

    @Data
    class Position {
        int x;
        int y;
    }

    private List<List<Byte>> cells;

    public void initialize() {
        for (int i = 0; i < level.size(); i++) {
            for (int j = 0; j < level.get(i).size(); j++) {
                cells.get(i).set(j, Byte.parseByte(level.get(i).get(j), 2));
            }
        }
    }
}
