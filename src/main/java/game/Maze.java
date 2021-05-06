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

        validate();

        addPositions();
    }

    private void addPositions() {
        cells.get(start.getY()).set(start.getX(), (byte) (cells.get(start.getY()).get(start.getX()) | 0b10000));

        cells.get(finish.getY()).set(finish.getX(), (byte) (cells.get(finish.getY()).get(finish.getX()) | 0b100000));
    }

    private void validate() throws AssertionError {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                ensureSurroundingWalls(i, j);

                ensureDoubleWalls(i, j);

                if (cells.get(i).get(j) > 15) {
                    throw new AssertionError("Invalid cell at: " + "x: " + j + " y: " + i);
                }
            }
        }
    }

    private void ensureSurroundingWalls(int row, int col) {
        if (row == 0) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | 0b1));
        }

        if (row == cells.size() - 1) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | 0b100));
        }

        if (col == 0) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | 0b1000));
        }

        if (col == cells.get(row).size() - 1) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | 0b10));
        }
    }

    private void ensureDoubleWalls(int row, int col) {
        if (cells.size() - 1 > row) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | ((0b1 & cells.get(row + 1).get(col)) << 2)));
        }
        if (row != 0) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | ((0b100 & cells.get(row - 1).get(col)) >> 2)));
        }

        if (cells.get(row).size() - 1 > col) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | ((0b1000 & cells.get(row).get(col + 1)) >> 2)));
        }
        if (col != 0) {
            cells.get(row).set(col, (byte) (cells.get(row).get(col) | ((0b10 & cells.get(row).get(col - 1)) << 2)));
        }
    }
}
