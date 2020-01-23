package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameModelTest {


    Pile wholeDeck = new Pile();
    GameModel gameModelTest = new GameModel(wholeDeck, 4);
    Player[] players = gameModelTest.getPlayers();

    @Test
    @DisplayName("Players in Game")

    public void checkPlayersinGame() {
        String s = players.toString();
        String expectedPlayers = "USER, A1, A2, A3, A4";
        assertEquals(expectedPlayers, s);
}



    }

