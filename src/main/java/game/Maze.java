package game;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinylog.Logger;

import java.util.List;

/**
 * Class for interacting with the maze.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Maze {
    private List<List<String>> level;

    private Position start;

    private Position finish;

    private boolean ready = false;

    private boolean finished = false;

    /**
     * Creates a Maze object.
     *
     * @param level 2D list of binary strings
     * @param start starting point
     * @param finish finish point
     */
    public Maze(List<List<String>> level, Position start, Position finish) {
        this.level = level;
        this.start = start;
        this.finish = finish;
    }

    private ReadOnlyIntegerWrapper[][] cells;

    /**
     * Initialize the maze.
     *
     * Must be called before using any other method.
     */
    public void initialize() {
        Logger.info("Initializing maze...");

        fillCells();

        validate();

        addPositions();

        ready = true;
    }

    private void fillCells() {
        Logger.debug("Converting strings to bytes...");

        cells = new ReadOnlyIntegerWrapper[level.size()][level.get(0).size()];

        for (int i = 0; i < level.size(); i++) {
            for (int j = 0; j < level.get(i).size(); j++) {
                String binaryString = level.get(i).get(j);

                cells[i][j] = new ReadOnlyIntegerWrapper(Integer.parseInt(binaryString, 2));
            }
        }
    }

    private void validate() throws AssertionError {
        Logger.debug("Validating maze...");
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                ensureSurroundingWalls(i, j);

                ensureDoubleWalls(i, j);

                if (cells[i][j].get() > 15) {
                    String errMsg =
                            "Invalid cell at: " + "x: " + j + " y: " + i;

                    Logger.error(errMsg);

                    throw new AssertionError(errMsg);
                }
            }
        }
    }

    private void ensureSurroundingWalls(final int row, final int col) {
        if (row == 0) {
            cells[row][col].set((cells[row][col].get() | 0b1));
        }

        if (row == cells.length - 1) {
            cells[row][col].set((cells[row][col].get() | 0b100));
        }

        if (col == 0) {
            cells[row][col].set((cells[row][col].get() | 0b1000));
        }

        if (col == cells[row].length - 1) {
            cells[row][col].set((cells[row][col].get() | 0b10));
        }
    }

    private void ensureDoubleWalls(final int row, final int col) {
        if (cells.length - 1 > row) {
            cells[row][col].set((cells[row][col].get() | ((0b1 & cells[row + 1][col].get()) << 2)));
        }
        if (row != 0) {
            cells[row][col].set((cells[row][col].get() | ((0b100 & cells[row - 1][col].get()) >> 2)));
        }

        if (cells[row].length - 1 > col) {
            cells[row][col].set((cells[row][col].get() | ((0b1000 & cells[row][col + 1].get()) >> 2)));
        }
        if (col != 0) {
            cells[row][col].set((cells[row][col].get() | ((0b10 & cells[row][col - 1].get()) << 2)));
        }
    }

    private void addPositions() {
        Logger.debug("Adding start and finish positions...");

        cells[start.getY()][start.getX()].set((cells[start.getY()][start.getX()].get() | 0b10000));

        cells[finish.getY()][finish.getX()].set((cells[finish.getY()][finish.getX()].get() | 0b100000));
    }

    /**
     * Remove the ball and the finish from the board.
     */
    private void removePositions() {
        Logger.debug("Removing ball and finish position...");

        for (ReadOnlyIntegerWrapper[] cell : cells) {
            for (ReadOnlyIntegerWrapper readOnlyIntegerWrapper : cell) {
                readOnlyIntegerWrapper.set(readOnlyIntegerWrapper.get() & 0b1111);
            }
        }
    }

    /**
     * Restart the game.
     * Reinitialization required
     */
    public void restart() {
        if (!ready) {
            return;
        }

        removePositions();

        finished = false;
    }

    /**
     * Returns the current position of the ball.
     *
     * @return The current position of the ball
     * @throws AssertionError if the ball doesn't exist
     */
    public Position findBall() throws AssertionError {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if ((cells[i][j].get() & 0b10000) != 0) {
                    return new Position(j, i);
                }
            }
        }

        Logger.error("Ball not found");

        throw new AssertionError("Ball not found");
    }

    /**
     * Move the ball in the specified direction.
     *
     * @param d {@link Direction}
     * @throws AssertionError if called before initialization
     */
    public void moveRecursive(final Direction d) throws AssertionError {
        if (!ready) {
            Logger.error("Error moving ball before initialization");

            throw new AssertionError("Maze not ready yet");
        }

        if (finished) {
            Logger.warn("Game already finished. No further movement allowed");

            return;
        }

        Movement m = getMovementDetails(d);

        Position p = findBall();

        if (!isValidMove(p, m)) {
            Logger.debug("Movement stopped");

            finished = isFinished();

            if (finished) {
                Logger.info("Game finished");
            }

            return;
        }

        cells[p.getY()][p.getX()].set((cells[p.getY()][p.getX()].get() & 0b101111));

        Position moveTo = new Position(
                m.getAxis() == 'x' ? p.getX() + m.getMove() : p.getX(),
                m.getAxis() == 'y' ? p.getY() + m.getMove() : p.getY()
        );

        cells[moveTo.getY()][moveTo.getX()].set((cells[moveTo.getY()][moveTo.getX()].get() | 0b10000));

        moveRecursive(d);
    }

    private int getMask(final Direction d) {
        return switch (d) {
            case UP -> 0b1;
            case DOWN -> 0b100;
            case LEFT -> 0b1000;
            case RIGHT -> 0b10;
        };
    }

    private char getAxis(final Direction d) {
        return d == Direction.UP || d == Direction.DOWN ? 'y' : 'x';
    }

    private int getMove(final Direction d) {
        return d == Direction.UP || d == Direction.LEFT ? -1 : 1;
    }

    private Movement getMovementDetails(final Direction d) {
        return new Movement(getMove(d), getAxis(d), getMask(d));
    }

    private boolean isValidMove(final Position p, final Movement m) {
        return (m.getMask() & cells[p.getY()][p.getX()].get()) == 0;
    }

    /**
     * Returns true if the ball reached the finish.
     *
     * @return True if the ball reached the finish
     */
    public boolean isFinished() {
        Position p = findBall();

        return (cells[p.getY()][p.getX()].get() & 0b110000) == 0b110000;
    }
}
