package game;

public class Game {
    private Maze maze;

    public Game() {
        this.maze = new MazeRepository().getLevel();
    }
}
