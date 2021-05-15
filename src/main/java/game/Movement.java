package game;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Movement {
    private int move;
    private char axis;
    private int mask;
}
