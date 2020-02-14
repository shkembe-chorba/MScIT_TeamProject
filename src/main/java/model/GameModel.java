package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import commandline.utils.Logger;

/**
 * Top Trumps game - MSc IT+ Masters Team Project
 *
 * Contributors: 2175499m: Filip Marinov 2504299a:Ventsislav Antov 2172605d:Nadezhda Dimitrova
 * 2200528b: Tereza Buckova 2493194s:Gareth Sears
 *
 * Class that represents the game model, initiliazes the game with players, distributes the deck
 * between players and communal pile and handles the progression of each round.
 */

public class GameModel {
    private int roundNumber = 1;
    private Player[] players;
    private ArrayList<Player> playersInGame = null; // players still left in the game
    private Player activePlayer; // active player that chooses the attribute
    private Pile communalPile;
    private Player roundWinner;
    private int drawRound;
    private Card winningCard;
    private Pile wholeDeck;
    private Player humanPlayer;

    /**
     *  Constructor that reads the deck from the jsonConfigFile.
     * @param jsonConfigFile
     */
    public GameModel(String jsonConfigFile) {
        try {
            wholeDeck = Pile.reader(jsonConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // --- DEBUG LOG ---
        // The contents of the complete deck once it has been read in and constructed
        Logger.log("COMPLETE GAME DECK AFTER LOAD:", wholeDeck.toString());
    }

    /**
     * Resets and initializes the game by setting up players,
     * setting the round number to 1 and winning card
     * and roundWinner to null.
     *
     * @param numAIPlayers number of AI players in game
     */
    public void reset(int numAIPlayers) {

        players = new Player[numAIPlayers + 1];
        createHumanPlayer();
        createAIPlayers(numAIPlayers);
        wholeDeck.shuffle();

        // --- DEBUG LOG ---
        // The contents of the complete deck after it has been shuffled
        Logger.log("COMPLETE GAME DECK AFTER SHUFFLE:", wholeDeck.toString());

        assignCards(wholeDeck, players);
        activePlayer = randomlySelectFirstPlayer(players);
        playersInGame = new ArrayList<>(Arrays.asList(players));
        winningCard = null;
        roundWinner = null;

        roundNumber = 1;
        drawRound = 0;
    }

    /**
     * Creates human player always called 'USER'.
     */
    public void createHumanPlayer() {
        humanPlayer = new Player("USER");
        players[0] = humanPlayer;
    }

    /**
     * Creates expected number of AI Players and adds them to players in the game with the correct
     * name.
     */
    public void createAIPlayers(int numOfAIPlayers) {
        // starts with 1 because HumanPlayer is in index 0
        for (int i = 1; i <= numOfAIPlayers; i++) {
            players[i] = new AIPlayer("AI" + i);
        }
    }

    /**
     * Splits the whole deck of 40 cards and assigns it to players. It assigns the remainder of cards
     * to the communal pile.
     */

    public void assignCards(Pile wholeDeck, Player[] players) {
        ArrayList<Pile> setOfDecks = wholeDeck.split(players.length);
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            Pile playerDeck = setOfDecks.get(i);
            player.addToDeck(playerDeck);

            // --- DEBUG LOG ---
            // The contents of the user’s deck and the computer’s deck(s) once they have been
            // allocated.
            String isAILabel = player instanceof AIPlayer ? "AI" : "USER";
            Logger.log(String.format("(%s) %s's DECK AFTER ALLOCATION:", isAILabel, player),
                    playerDeck.toString());

        }
        communalPile = setOfDecks.get(players.length);

        // --- DEBUG LOG ---
        // Initial communal deck contents.
        Logger.log("INITIAL COMMUNAL DECK CONTENTS", communalPile.toString());
    }


    /**
     * Takes in attribute and compares the values of the top card of all players in the given
     * attribute to play a round.
     * @param chosenAttribute
     * @return the round winner or null if there is a draw.
     */
    public Player playRoundWithAttribute(Attribute chosenAttribute) {

        // --- DEBUG LOG ---
        // The contents of the current cards in play
        String playerCardStrings = playersInGame.stream()
                .map(p -> String.format("%s's CARD:\n\n%s\n\n", p, p.peekCard()))
                .collect(Collectors.joining());
        Logger.log("CARDS IN PLAY AT ROUND " + roundNumber + " START", playerCardStrings);

        // --- DEBUG LOG ---
        // The category selected and corresponding values when a user or computer selects a category
        Logger.log(String.format("%s's ATTRIBUTE FOR THE ROUND:", activePlayer),
                chosenAttribute.toString());

        int maxValue = 0;
        int drawMaxValue = 0;

        for (Player player : playersInGame) {
            // assigns top card for a player that is still in game as activeCard
            Card activeCard = player.peekCard();
            int playersAttributeValue = activeCard.getValue(chosenAttribute);

            if (maxValue < playersAttributeValue) {
                maxValue = playersAttributeValue;
                roundWinner = player;
                // if there is a draw, it stores it in the temporary draw value
            } else if (maxValue == playersAttributeValue) {
                drawMaxValue = maxValue;
            }
        }

        // if maxValue is also the drawMaxValue after going through all the values, it means that
        // there is no higher value
        if (maxValue == drawMaxValue) {

            // pops the card from all the players and transfers them to communal pile
            addCardsToCommunalPile();

            // --- DEBUG LOG ---
            // The contents of the communal pile when cards are added or removed from it
            Logger.log("ROUND WAS DRAWN - COMMUNAL PILE CONTENTS:", communalPile.toString());

            drawRound++;
            // resets the roundWinner
            roundWinner = null;
            // returns null to controller

        } else {
            // increases the won round
            roundWinner.wonRound();
            winningCard = roundWinner.peekCard();

            // waits for the popping of the card and adds it to the communal pile
            addCardsToCommunalPile();

            // --- DEBUG LOG ---
            // The contents of the communal pile when cards are added to it
            Logger.log("ROUND WAS WON - COMMUNAL PILE CONTENTS TO BE TRANSFERED:",
                    communalPile.toString());

            // shuffles the communalPile
            communalPile.shuffle();
            // transfers all cards from communal pile to roundWinner
            transferCommunalPile(roundWinner);

            // --- DEBUG LOG ---
            // The contents of the communal pile when cards are removed from it
            Logger.log("COMMUNAL PILE AFTER TRANSFER:", communalPile.toString());

            setActivePlayer(roundWinner);

        }

        // --- DEBUG LOG ---
        // The contents of each deck after a round
        String playerDeckStrings = playersInGame.stream()
                .map(p -> String.format("%s's DECK:\n\n%s\n\n", p, p.getDeck()))
                .collect(Collectors.joining());
        Logger.log("PLAYERS DECKS AT END OF THE ROUND: ", playerDeckStrings);


        // increases round number
        roundNumber++;

        // returns winner to the controller
        return roundWinner;
    }

    /**
     * Randomly selects first player from the players array
     */
    public Player randomlySelectFirstPlayer(Player[] players) {
        Random rand = new Random();
        Player firstPlayer = players[rand.nextInt(players.length)];
        return firstPlayer;
    }

    /**
     * Checks whether the human player is still in game
     */
    public boolean userStillInGame() {
        return playersInGame.contains(humanPlayer);
    }

    /**
     * Checks for the game winner by checking if there is only one player left in the game
     */
    public Player checkForWinner() {
        if (playersInGame.size() == 1) {
            Player winner = playersInGame.get(0);

            // --- DEBUG LOG ---
            // The winner of the game
            Logger.log("WINNING PLAYER:", winner.toString());

            return winner;
        } else {
            return null;
        }
    }

    /**
     * Checks whether the player has another card and if not eliminates them from players in game
     */
    public ArrayList<Player> checkToEliminate() {
        ArrayList<Player> eliminated = new ArrayList<Player>();

        // Check which players are to be eliminated
        for (Player player : playersInGame) {
            if (player.peekCard() == null) {
                eliminated.add(player);
            }
        }

        // Eliminate players from model
        for (Player eliminatedPlayer : eliminated) {
            playersInGame.remove(eliminatedPlayer);
        }

        return eliminated;
    }

    /**
     * Transfers cards to communal pile from all players
     */
    public void addCardsToCommunalPile() {
        for (int i = 0; i < playersInGame.size(); i++) {
            Player playerToPopCard = playersInGame.get(i);
            communalPile.add(playerToPopCard.popCard());
        }
    }

    /**
     * Transfers communal pile to winner of the round
     */
    public void transferCommunalPile(Player roundWinner) {
        this.roundWinner.addToDeck(communalPile);
        communalPile = new Pile();
    }


    // getters and setters
    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getDraws() {
        return drawRound;
    }

    public void increaseRoundNumber() {
        roundNumber++;
    }

    public int getCommunalPileSize() {
        return communalPile.size();
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Player getRoundWinner() {
        return roundWinner;
    }

    public Card getWinningCard() {
        return winningCard;
    }

    public void setActivePlayer(Player playerActive) {
        this.activePlayer = playerActive;
    }

    public Player[] getPlayers() {
        return players;
    }

    public ArrayList<Player> getPlayersInGame() {
        return playersInGame;
    }

}
