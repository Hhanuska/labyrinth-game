package game;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Maze {
    private List<List<String>> level;

    private Position start;

    private Position finish;

    @Data
    static class Position {
        int x;
        int y;
    }

    private List<List<Byte>> cells;

    public void initialize() {
        cells = new ArrayList<>();

        for (List<String> strings : level) {
            List<Byte> cellsHelper = new ArrayList<>();

            for (String string : strings) {
                cellsHelper.add(Byte.parseByte(string, 2));
            }

            cells.add(cellsHelper);
        }

        System.out.println(cells);

        addPositions();
    }

    private void addPositions() {
        cells.get(start.getY()).set(start.getX(), (byte) (cells.get(start.getY()).get(start.getX()) | 0b10000));

        cells.get(finish.getY()).set(finish.getX(), (byte) (cells.get(finish.getY()).get(finish.getX()) | 0b100000));
    }
}
