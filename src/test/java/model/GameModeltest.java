package model;

import org.junit.jupiter.api.Test;

public class GameModeltest {
    Pile WholeDeck = new Pile();
    GameModel testModel = new GameModel(WholeDeck, 4);

    @Test
    public void returnsRandomPlayer() {
        
    }
}
