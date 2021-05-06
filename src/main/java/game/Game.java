package game;

import java.util.List;

public class Game {
    private Labyrinth labyrinth;

    public Game() {
        this.labyrinth = new LabyrinthRepository().getLevel();
    }
}
