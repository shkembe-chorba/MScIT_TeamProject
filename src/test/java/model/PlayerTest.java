package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    public void testGetName() {
        Player testPlayer = new Player("USER");
        String expectedString = "USER";
        String actualString = testPlayer.toString();
        assertEquals(expectedString, actualString);
    }

     @Test
    public void testIncrementingRounds() {
        Player testPlayer = new Player("USER");
        testPlayer.wonRound();
        int expectedNumber = 1;
        int actualNumber = testPlayer.getRoundsWon();
        assertEquals(expectedNumber, actualNumber);
    }

    @DisplayName("No cards should be returned when initialising with a blank deck")
    @Test
    void emptyDeckAfterInitialisation() {
        Player player = new Player("Gareth");
        assertEquals(null, player.peekNextCard());
        assertEquals(null, player.removeNextCard());
    }

}