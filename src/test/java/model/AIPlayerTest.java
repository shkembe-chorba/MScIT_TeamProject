package model;

package model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AIPlayerTest {
    Card topCardValues= {1, 2, 3, 4, 100 };
    AIPlayer ai = new AIPlayer("AI1");

    @Nested
    @Test
    public class chooseRightAttribute() {

        int expected = ai.chooseAttribute();
        assertEquals(4, expected );
    }
    @Test
    public class chooseRightAttribute() {

        int expected = ai.chooseAttribute();
        assertEquals(4, expected );
    }
}