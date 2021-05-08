package game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

@Data
public class Maze {
    private List<List<String>> level;

    private Position start;

    private Position finish;

    private boolean ready = false;

    private boolean finished = false;

    public boolean getFinished() {
        return finished;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Position {
        int x;
        int y;
    }

    private List<List<Byte>> cells;

    public void initialize() {
        Logger.info("Initializing maze...");

        fillCells();

        validate();

        addPositions();

        ready = true;
    }

    private void fillCells() {
        Logger.debug("Converting strings to bytes...");

        cells = new ArrayList<>();

        for (List<String> strings : level) {
            List<Byte> cellsHelper = new ArrayList<>();

            for (String string : strings) {
                cellsHelper.add(Byte.parseByte(string, 2));
            }

            cells.add(cellsHelper);
        }
    }

    private void validate() throws AssertionError {
        Logger.debug("Validating maze...");
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                ensureSurroundingWalls(i, j);

                ensureDoubleWalls(i, j);

                if (cells.get(i).get(j) > 15) {
                    String errMsg = "Invalid cell at: " + "x: " + j + " y: " + i;

                    Logger.error(errMsg);

                    throw new AssertionError(errMsg);
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

    private void addPositions() {
        Logger.debug("Adding start and finish positions...");

        cells.get(start.getY()).set(start.getX(), (byte) (cells.get(start.getY()).get(start.getX()) | 0b10000));

        cells.get(finish.getY()).set(finish.getX(), (byte) (cells.get(finish.getY()).get(finish.getX()) | 0b100000));
    }

    public Position findBall() throws AssertionError {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                if ((cells.get(i).get(j) & 0b10000) != 0) {
                    return new Position(j, i);
                }
            }
        }

        Logger.error("Ball not found");

        throw new AssertionError("Ball not found");
    }

    public void moveRecursive(Direction d) throws AssertionError {
        if (ready == false) {
            Logger.error("Error moving ball before initialization");

            throw new AssertionError("Maze not ready yet");
        }

        if (finished == true) {
            Logger.warn("Game already finished. No further movement allowed");

            return;
        }

        Movement m = getMovementDetails(d);

        Position p = findBall();

        if (isValidMove(p, m) == false) {
            Logger.debug("Movement stopped");

            finished = isFinished();

            if (finished) {
                Logger.info("Game finished");
            }

            return;
        }

        cells.get(p.getY()).set(p.getX(), (byte) (cells.get(p.getY()).get(p.getX()) & 0b101111));

        Position moveTo = new Position(
                m.axis == 'x' ? p.getX() + m.getMove() : p.getX(),
                m.axis == 'y' ? p.getY() + m.getMove() : p.getY()
        );

        cells.get(moveTo.getY()).set(moveTo.getX(), (byte) (cells.get(moveTo.getY()).get(moveTo.getX()) | 0b10000));

        moveRecursive(d);
    }

    private byte getMask(Direction d) {
        byte mask = 0;

        switch (d) {
            case UP -> mask = 0b1;
            case DOWN -> mask = 0b100;
            case LEFT -> mask = 0b1000;
            case RIGHT -> mask = 0b10;
        }

        return mask;
    }

    private char getAxis(Direction d) {
        return d == Direction.UP || d == Direction.DOWN ? 'y' : 'x';
    }

    private int getMove(Direction d) {
        return d == Direction.UP || d == Direction.LEFT ? -1 : 1;
    }

    @AllArgsConstructor
    @Data
    class Movement {
        int move;
        char axis;
        byte mask;
    }

    private Movement getMovementDetails(Direction d) {
        return new Movement(getMove(d), getAxis(d), getMask(d));
    }

    private boolean isValidMove(Position p, Movement m) {
        return (m.mask & cells.get(p.getY()).get(p.getX())) == 0;
    }

    public boolean isFinished() {
        Position p = findBall();

        return (cells.get(p.getY()).get(p.getX()) & 0b110000) == 0b110000;
    }
}
