package commandline.view;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import commandline.controller.TopTrumpsControllerInterface;

import model.Player;
import model.Card;


/**
 * The view component of the app. Contains the high level functionality required by the game and
 * interfaces with lower level components which take care of I/O and general message formatting.
 */
public class TopTrumpsView {

    public static final String QUIT_COMMAND = "quit";
    public static final String QUIT_COMMAND_DESCRIPTION = "quits the program";

    TopTrumpsControllerInterface controller;
    CommandLineView cli = new CommandLineView();

    /**
     * Creates the application view component.
     * 
     * @param controller A controller which implements the TopTrumpsControllerInterface interface.
     */
    public TopTrumpsView(TopTrumpsControllerInterface controller) {
        this.controller = controller;
        setupQuitCommand();
    }

    // Adds the quit command, much like adding a Button in Swing.
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
    public void displayHand(Player player) {
        Card hand = player.peekCard();
        cli.displayMessage(String.format("%s drew '%s':", player.toString(), hand.getName()));
        cli.displayBulletList(hand.getAttributes());
    }

    /**
     * Displays the player's name and their remaining cards.
     * 
     * @param player
     */
    public void displayRemainingCardCount(Player player) {
        cli.displayMessage(String.format("%s has %d cards remaining.", player.toString(),
                player.getCardCount()));
    }

    /**
     * Displays the users current hand and their remaining cards.
     * 
     * @param user a Player which is user controlled
     */
    public void displayUserHand(PlayerInterface user) {
        displayHand(user);
        displayRemainingCardCount(user);
        cli.displayDivider();
    }

    /**
     * Displays the active player's name.
     * 
     * @param playerName
     */
    public void displayActivePlayer(String playerName) {
        cli.displayMessage(playerName + " is the active player!");
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
     * Displays the winner of the round.
     * 
     * @param playerName
     * @param roundNumber
     */
    public void displayRoundWinner(String playerName, int roundNumber) {
        cli.displayMessage(String.format("%s won round %d!", playerName, roundNumber));
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
     * @param card             the winning card
     * @param winningAttribute the winning attribute to highligh
     */
    public void displayWinningCard(CardInterface card, Attribute winningAttribute) {
        List<Attribute> attributes = card.getAttributes();
        int winningIndex = attributes.indexOf(winningAttribute);
        cli.displayBulletSelection(String.format("The winning card was '%s':", card.getName()),
                winningIndex);
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
    public void displayGameOver(String winningPlayerName, List<ScoreCard> scores) {
        cli.displayMessage("Game Over\nThe overall winner was " + winningPlayerName);
        cli.displayMessage("Scores:");
        cli.displayIndentedList(scores);
        cli.displayDivider();
    }

}
