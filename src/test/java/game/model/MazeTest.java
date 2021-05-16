package game.model;

import static org.junit.jupiter.api.Assertions.*;

import game.model.Direction;
import game.model.Maze;
import game.model.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MazeTest {
    private Maze exampleMaze(Position start, Position finish) {
        List<List<String>> level = new ArrayList<>();

        List<String> innerList1 = new ArrayList<>();
        List<String> innerList2 = new ArrayList<>();
        List<String> innerList3 = new ArrayList<>();

        innerList1.add("1001");
        innerList1.add("0111");
        innerList1.add("0000");

        innerList2.add("0100");
        innerList2.add("0000");
        innerList2.add("0000");

        innerList3.add("1100");
        innerList3.add("0100");
        innerList3.add("0110");

        level.add(innerList1);
        level.add(innerList2);
        level.add(innerList3);

        return new Maze(level, start, finish);
    }

    private Maze invalidExampleMaze(Position start, Position finish) {
        List<List<String>> level = new ArrayList<>();

        List<String> innerList1 = new ArrayList<>();
        List<String> innerList2 = new ArrayList<>();
        List<String> innerList3 = new ArrayList<>();

        innerList1.add("1001");
        innerList1.add("10111");
        innerList1.add("0000");

        innerList2.add("0100");
        innerList2.add("0000");
        innerList2.add("0000");

        innerList3.add("1100");
        innerList3.add("100100");
        innerList3.add("0110");

        level.add(innerList1);
        level.add(innerList2);
        level.add(innerList3);

        return new Maze(level, start, finish);
    }

    @Test
    public void testInit() {
        Maze maze = exampleMaze(new Position(0, 0), new Position(2, 2));
        System.out.println(maze.getLevel());
        maze.initialize();

        assertTrue(maze.getCells()[0][0].get() > 0b10000);
        assertTrue(maze.getCells()[2][2].get() > 0b100000);

        assertEquals(maze.getCells()[0][2].get(), 0b1011);
        assertEquals(maze.getCells()[1][0].get(), 0b1100);
        assertEquals(maze.getCells()[1][1].get(), 0b0001);
        assertEquals(maze.getCells()[1][2].get(), 0b0010);
        assertEquals(maze.getCells()[2][0].get(), 0b1101);
        assertEquals(maze.getCells()[2][1].get(), 0b0100);
        assertEquals(maze.getCells()[2][2].get(), 0b100110);

        maze = invalidExampleMaze(new Position(0, 0), new Position(2, 2));
        assertThrows(AssertionError.class, maze::initialize);
    }

    @Test
    public void testFindBall() {
        Maze maze = exampleMaze(new Position(0, 0), new Position(2, 2));
        maze.initialize();

        assertEquals(maze.findBall(), new Position(0, 0));

        maze.getCells()[0][0].set(maze.getCells()[0][0].get() & 0b1111);
        assertThrows(AssertionError.class, maze::findBall);
    }

    @Test
    public void testMovement() {
        Maze maze = exampleMaze(new Position(0, 0), new Position(2, 2));
        assertThrows(AssertionError.class, () -> maze.moveRecursive(Direction.RIGHT));
        maze.initialize();

        maze.moveRecursive(Direction.RIGHT);
        assertEquals(maze.findBall(), new Position(1, 0));
        maze.moveRecursive(Direction.DOWN);
        assertEquals(maze.findBall(), new Position(1, 0));
        maze.moveRecursive(Direction.LEFT);
        assertEquals(maze.findBall(), new Position(0, 0));
        maze.moveRecursive(Direction.DOWN);
        assertEquals(maze.findBall(), new Position(0, 1));
        maze.moveRecursive(Direction.RIGHT);
        assertEquals(maze.findBall(), new Position(2, 1));
        maze.moveRecursive(Direction.UP);
        assertEquals(maze.findBall(), new Position(2, 0));
    }

    @Test
    public void testFinish() {
        Maze maze = exampleMaze(new Position(0, 0), new Position(2, 2));
        maze.initialize();

        maze.moveRecursive(Direction.DOWN);
        maze.moveRecursive(Direction.RIGHT);
        maze.moveRecursive(Direction.DOWN);

        assertTrue(maze.isFinished());

        maze.moveRecursive(Direction.UP);
        assertFalse(maze.getCells()[0][2].get() >= 0b10000);
        assertEquals(maze.findBall(), new Position(2, 2));
    }

    @Test
    public void testRestart() {
        Maze maze = exampleMaze(new Position(0, 0), new Position(2, 2));
        maze.initialize();

        maze.moveRecursive(Direction.DOWN);
        maze.restart();
        assertThrows(AssertionError.class, maze::findBall);

        maze.initialize();
        maze.moveRecursive(Direction.DOWN);
        maze.moveRecursive(Direction.RIGHT);
        maze.moveRecursive(Direction.DOWN);

        assertTrue(maze.isFinished());

        maze.restart();
        maze.initialize();
        assertFalse(maze.isFinished());
        assertDoesNotThrow(() -> maze.moveRecursive(Direction.DOWN));
    }
}
