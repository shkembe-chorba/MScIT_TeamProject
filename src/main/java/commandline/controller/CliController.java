package commandline.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import commandline.view.TopTrumpsView;

import model.AIPlayer;
import model.Attribute;
import model.Database;
import model.GameModel;
import model.Player;
import model.RetrievedGameStatistics;

/**
 * The Controller of the CLI version of the Top Trumps Game.
 */
public class CliController {

    // Fields.
    private final GameModel model;
    private TopTrumpsView view;
    private Database database;

    /**
     * Constructor.
     *
     * @param model the game model
     */
    public CliController(GameModel model) {
        this.model = model;
    }

    /**
     * Sets the game view.
     *
     * @param view
     */
    public void setView(TopTrumpsView view) {
        this.view = view;
    }

    /**
     * Runs the controller and begins the game.
     *
     * @throws SQLException when the database cannot be connected to
     */
    public void run() throws SQLException {

        // Initialize and connect to database.
        database = new Database();
        database.connect();

        // Infinite loop for main menu.
        while (true) {
            // Prompt for choice between statistics and a new game.
            int choice = view.displayMenu();
            switch (choice) {
                case 0:
                    // Display game statistics.
                    RetrievedGameStatistics statistics = database.retrieveGameStats();
                    view.displayStatistics(statistics);
                    break;

                case 1:
                    // Start new game.
                    setUpNewGame();
                    // Loop for the game rounds that ends when the game has a winner.
                    while (true) {
                        playRound();
                        Player gameWinner = model.checkForWinner();
                        if (gameWinner != null) {
                            // Display end of game.
                            view.displayGameOver(gameWinner.toString(), model.getPlayers());
                            // Upload statistics to the database.
                            database.uploadGameStats(model.getDraws(), model.getRoundNumber(),
                                    gameWinner.toString(), model.getPlayers());
                            break;
                        }
                    }
                    break;

                default:
                    continue;
            }
        }
    }

    /**
     * Sets up a new game by displaying the message for the beginning of a game and resetting the
     * game model.
     */
    private void setUpNewGame() {
        view.displayGameStartMessage();
        model.reset();
    }

    /**
     * Plays a round.
     */
    private void playRound() {
        Player activePlayer = model.getActivePlayer();

        view.displayRoundNumber(model.getRoundNumber());

        // Is human player still in game.
        if (model.userStillInGame()) {
            view.displayUserHand(model.getHumanPlayer());
        }

        // Display "Player X is the active player!".
        view.displayActivePlayer(activePlayer);

        Attribute selectedAttribute = null;
        // Category choice: AI or human.
        if (isPlayerAI(model.getActivePlayer())) {
            view.displayAiPlayerHand(activePlayer);
            // AI selects an attribute if it is active.
            selectedAttribute = ((AIPlayer) activePlayer).chooseAttribute();
        } else {
            // The User selects an attribute if he is active.
            selectedAttribute = view.getUserAttribute(activePlayer.peekCard().getAttributes());
        }
        view.displayChosenCategory(selectedAttribute);

        // Play a round and get the round winner.
        Player roundWinner = model.playRoundWithAttribute(selectedAttribute);
        if (roundWinner == null) {
            // Draw case
            view.displayDrawnRound(model.getRoundNumber(), model.getCommunalPileSize());
        } else {
            // Winner case
            view.displayRoundWinner(roundWinner.toString(), model.getRoundNumber());
            view.displayWinningCard(model.getWinningCard());
        }

        // Check for eliminations.
        ArrayList<Player> eliminatedPlayers = model.checkToEliminate();
        for (Player player : eliminatedPlayers) {
            view.displayEliminatedPlayer(player.toString());
        }
    }

    /**
     * Checks if a player is an AI player.
     *
     * @param player
     * @return whether a player is an AI
     */
    private boolean isPlayerAI(Player player) {
        return (player instanceof AIPlayer);
    }

    /**
     * Disconnects from the database and quits the game.
     */
    public void quit() {
        database.disconnect();
        System.exit(0);
    }
}
