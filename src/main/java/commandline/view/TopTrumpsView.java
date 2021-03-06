package commandline.view;

import commandline.controller.CliController;

import model.Attribute;
import model.Card;
import model.Player;
import model.RetrievedGameStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * The view component of the app. Contains the high level functionality required by the game and
 * interfaces with lower level components which take care of I/O and general message formatting.
 */
public class TopTrumpsView {

    // Constants for GlobalCommands
    public static final String QUIT_COMMAND = "quit";
    public static final String QUIT_COMMAND_DESCRIPTION = "quits the program";

    private CliController controller;

    // A CommandLineView which takes care of low level IO
    private CommandLineView cli = new CommandLineView();

    /**
     * Creates the application view component.
     *
     * @param controller A controller which implements the TopTrumpsControllerInterface interface.
     */
    public TopTrumpsView(CliController controller) {
        this.controller = controller;
        // Add the 'quit' command.
        setupQuitCommand();
    }

    /**
     * Adds a 'quit' command, in a pattern similar to to adding a 'button' to a Jframe.
     */
    private void setupQuitCommand() {
        GlobalCommand quitCommand = new GlobalCommand(QUIT_COMMAND, QUIT_COMMAND_DESCRIPTION);
        quitCommand.addCommandListener(new GlobalCommandListener() {
            // Calls the controller's quit method when the user types the QUIT_COMMAND
            @Override
            public void globalCommandReceived(String globalCommand) {
                controller.quit();
            }
        });

        cli.addGlobalCommand(quitCommand);
    }

    /**
     * Displays Game Start
     */
    public void displayGameStartMessage() {
        cli.displayMessage("Game Start");
    }

    /**
     * Displays the round number
     */
    public void displayRoundNumber(int roundNumber) {
        cli.displayMessage(String.format("Round %d\nPlayers have drawn their cards", roundNumber));
        cli.displayDivider();
    }

    /**
     * Displays the player's name and their current card.
     *
     * @param player
     */
    public void displayTopCard(Player player) {
        Card topCard = player.peekCard();
        cli.displayMessage(String.format("%s drew '%s':", player.toString(), topCard.getName()));
        cli.displayBulletList(topCard.getAttributes());
    }

    /**
     * Displays the player's name and their remaining cards.
     *
     * @param player
     */
    public void displayRemainingCardCount(Player player) {
        cli.displayMessage(String.format("%s has %d cards remaining in their deck.",
                player.toString(), player.getRemainingDeckSize()));
    }

    /**
     * Displays the users current hand and their remaining cards.
     *
     * @param user a Player which is user controlled
     */
    public void displayUserHand(Player user) {
        displayTopCard(user);
        displayRemainingCardCount(user);
        cli.displayDivider();
    }

    /**
     * Displays the main menu, a welcome graphic, and how to quit the app.
     *
     * @return
     */
    public int displayMenu() {
        displayWelcomeMessage();
        cli.displayMessage("Do you want to see past results or play a game?");
        List<String> options = Arrays.asList(new String[] {"Print Game Statistics", "Play game"});
        return cli.getUserSelectionIndex(options);
    }

    /**
     * Display a welcome message and how to quit.
     */
    private void displayWelcomeMessage() {
        cli.displayMessage("--- MAIN MENU ---\n");
        cli.displayMessage("To quit type in \"quit\" at any prompt.\n");
    }

    /**
     * Displays the active player's name.
     *
     * @param player
     */
    public void displayActivePlayer(Player player) {
        cli.displayMessage(player.toString() + " is the active player!");
    }

    /**
     * Displays the game statistics.
     *
     * @param stats A bean which contains the retrieved game statistics.
     */
    public void displayStatistics(RetrievedGameStatistics stats) {

        List<String> statsList = new LinkedList<>();
        statsList.add(String.format("Total games played: %d", stats.getTotalGamesPlayed()));
        statsList.add(String.format("Games won by AI: %d", stats.getGamesWonByAi()));
        statsList.add(String.format("Games won by user: %d", stats.getGamesWonByUser()));
        statsList.add(String.format("Average number of draws: %.2f", stats.getAvgDraws()));
        statsList.add(String.format("Maximum rounds in a game: %s", stats.getMaxRounds()));

        cli.displayMessage("Here are the current statistics:");
        cli.displayBulletList(statsList);
        cli.displayDivider();
    }

    /**
     * Prompts the player for their attribute selection.
     *
     * @param attributes
     * @return The selected attribute object
     */
    public Attribute getUserAttribute(List<Attribute> attributes) {
        cli.displayMessage("Select your category:");
        return cli.getUserSelection(attributes);
    }

    /**
     * Displays The category chosen is + category attribute name
     *
     * @param attribute
     */
    public void displayChosenCategory(Attribute attribute) {
        cli.displayMessage("The category chosen is: " + attribute.getName());
    }

    /**
     * Displays the winner of the round.
     *
     * @param playerName
     * @param roundNumber
     */
    public void displayRoundWinner(String playerName, int roundNumber) {
        cli.displayMessage(String.format("%s won round %d!", playerName, roundNumber));
    }

    public void displayAiPlayerHand(Player ai) {
        displayTopCard(ai);
        cli.displayDivider();
    }

    /**
     * Displays that the round was a draw and the communal pile count.
     *
     * @param roundNumber
     * @param communalPileCount
     */
    public void displayDrawnRound(int roundNumber, int communalPileCount) {
        cli.displayMessage(String.format("Round %d was a draw.\nThe communal pile now has %d cards",
                roundNumber, communalPileCount));
    }

    /**
     * Displays the winning card of the round.
     *
     * @param card the winning card
     */
    public void displayWinningCard(Card card) {
        List<Attribute> attributes = card.getAttributes();
        cli.displayMessage(String.format("The winning card was '%s':", card.getName()));
        cli.displayBulletList(attributes);
        cli.displayDivider();
    }

    /**
     * Displays the eliminated player.
     *
     * @param playerName
     */
    public void displayEliminatedPlayer(String playerName) {
        cli.displayMessage(playerName + " has been eliminated!");
        cli.displayDivider();
    }

    /**
     * Displays the game over
     */
    public void displayGameOver(String winningPlayerName, Player[] players) {

        List<String> scores = new ArrayList<>();
        for (Player player : players) {
            scores.add(player.toString() + ": " + player.getRoundsWon());
        }

        cli.displayMessage("Game Over\nThe overall winner was " + winningPlayerName);
        cli.displayMessage("Scores:");
        cli.displayIndentedList(scores);
        cli.displayDivider();
    }

}
