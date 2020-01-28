package model;

package model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AIPlayerTest {

    @Test
    public class chooseRightIndexofAttribute() {
        int [] attributeValues = {1, 2, 3, 4, 100 }; //This is what Card.getCategoryValues() method returns
        AIPlayer ai = new AIPlayer("AI1");
        int expected = ai.chooseAttribute();
        assertEquals(4, expected ); // Expected is that the method chooses index 4 corresponding to value 100
        }

    }
}