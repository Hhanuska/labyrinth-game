package game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * X and Y coordinates.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    private int x;
    private int y;
}
