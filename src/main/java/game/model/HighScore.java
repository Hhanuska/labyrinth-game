package game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing a high score.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighScore {
    private String name;
    private Long time;
}
