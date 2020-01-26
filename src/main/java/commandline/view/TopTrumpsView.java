package commandline.view;

import java.util.Collection;
import java.util.List;

public class TopTrumpsView {

    TopTrumpsController controller;
    CommandLineView cli = new CommandLineView();

    public TopTrumpsView(TopTrumpsController controller) {
        this.controller = controller;
    }

    private void setupQuitCommand() {
        GlobalCommand quitCommand = new GlobalCommand("quit", "quits the program");
        quitCommand.addCommandListener(new GlobalCommandListener() {
            @Override
            public void globalCommandReceived(String globalCommand) {
                // CONTROLLER CONTROLS QUITING and GAME LOGIC
                controller.quit();
            }
        });

        cli.addGlobalCommand(quitCommand);
    }

    public void displayRoundNumber(int roundNumber) {
        cli.displayMessage(String.format("Round %d\nPlayers have drawn their cards", roundNumber));
    }

    public void displayHand(String playerName, String cardName, List<Attribute> attributes) {
        cli.displayMessage(String.format("%s drew '%s':", playerName, cardName));
        cli.displayBulletList(attributes);
    }

    public void displayRemainingCardCount(String playerName, int cardsRemaining) {
        cli.displayMessage(String.format("%s has %d cards remaining."));
    }

    public void displayActivePlayer(String playerName) {
        cli.displayMessage(playerName + " is the active player!");
    }

    public Attribute getUserAttribute(List<Attribute> attributes) {
        cli.displayMessage("Select your category:");
        return cli.getUserSelection(attributes);
    }

    public void displayRoundWinner(String playerName, int roundNumber) {
        cli.displayMessage(String.format("%s won round %d!", playerName, roundNumber));
    }

    public void displayDrawnRound(int roundNumber, int communalPileCount) {
        cli.displayMessage(String.format("Round %d was a draw.\nThe communal pile now has %d cards",
                roundNumber, communalPileCount));
    }

    public void displayWinningCard(String cardName, List<Attribute> attributes) {
        cli.displayMessage(String.format("The winning card was '%s':", cardName));
    }

    public void displayEliminatedPlayer(String playerName) {
        cli.displayMessage(playerName + " has been eliminated!");
    }

    public void displayGameOver(String winningPlayerName, List<ScoreCard> scores) {
        cli.displayMessage("Game Over\nThe overall winner was " + winningPlayerName);
        cli.displayMessage("Scores:");
        cli.displayIndentedList(scores);
    }

}
