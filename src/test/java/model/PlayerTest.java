package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

        Player testPlayer = new Player("USER");

        @Test
public void testGetName() {
        String expectedString = "USER";
        String actualString = testPlayer.toString();
        assertEquals(expectedString, actualString);
    }

        Player testPlayer = new Player("USER");

        @Test
public void testIncrementingRounds() {
        testPlayer.wonRound();
        int expectedNumber = 1;
        int actualNumber = testPlayer.getRoundsWon();
        assertEquals(expectedNumber, actualNumber);
}

}