package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for gameState - only testing the String method and set up of the enum
 */
public class GameStateTest {

@Test
    public void testGameStatetoStringmethod() {
        assertEquals("ACTIVE", GameState.ACTIVE);
    }

}