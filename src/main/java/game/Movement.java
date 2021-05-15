package game;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for describing the movement of the ball.
 */
@AllArgsConstructor
@Data
public class Movement {
    private int move;
    private char axis;
    private int mask;
}
