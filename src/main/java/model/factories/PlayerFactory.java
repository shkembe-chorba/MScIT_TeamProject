package model.factories;

import model.AIPlayer;
import model.Player;

public class PlayerFactory {

    public Player createHumanPlayer() {
        return new Player("USER");
    }

    public AIPlayer createAiPlayer(int aiIndex) {
        return new AIPlayer("AI" + aiIndex);
    }

}
