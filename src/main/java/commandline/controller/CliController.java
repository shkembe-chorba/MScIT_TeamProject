package commandline.controller;

import commandline.view.TopTrumpsView;
import model.GameModel;
import model.Player;

public class CliController implements TopTrumpsControllerInterface {

    private final GameModel model;
    private TopTrumpsView view;

    public CliController(GameModel model) {
        this.model = model;
    }

    public void setView(TopTrumpsView view) {
        this.view = view;
    }

    /**
     * 1. Display option to view stats or start game
     * 2. Take user input
     * 3. Act based on input;
     */
    @Override
    public void run(){ }




    /**
     * 1. Print "Game Started"
     * 2. Initialise
     *      2.1 Create players
     *      2.2 Initialise deck from file
     *      2.3 Distribute cards evenly
     *      2.4 Any cards left after 2.3 go at communal pile
     * 3. Select active player at random
     * 4. Start round.
     */
    private void startNewGame(){
        view.displayGameStartMessage();
    }






    /**
     * 1. Display current round number
     * 2. Display "Players have drawn their cards"
     * 3. if (human player is active) --> display top card (+ attributes )
     * 4. Display active player
     * 5.a) if (human player is active) -->
     *          Display categories
     *          Take user input on which category gets played
     * 5.b) if (ai player is active) -->
     *          Display "Player X drew ..." and its stats
     * 6. Display the chosen category
     * 7.a) (if the round has a winner) -->
     *          Display who won the round
     *          Display the winning card
     *          Player who won takes all cards (including own) + communal cards and places them at the back of the deck
     * 7.b) (if the round is a draw) -->
     *          All 5 cards go into communal pile
     *          startNewRound( currentPlayer ) ( with the same player selected )
     *
     * 8. Check if any player has been eliminated. If true: display it
     * 9. If game finished --> gameEnded(); else -> startNewRound( randomPlayer )
     */
    private void startNewRound(Player activePlayer){ }

    /**
     * Display winner and game ended stats
     * Add game data to database
     */
    private void gameEnded() { }

    /**
     * 1. Display Stats
     * 2. Go back to run.
     */
    private void displayStats(){}

    
    @Override
    public void quit() {

    }
}
