package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

/**
 * Top Trumps game - MSc IT+ Masters Team Project
 *
 * Contributors: 2175499m: Filip Marinov 2504299a:Ventsislav Antov 2172605d:Nadezhda Dimitrova
 * 2200528b: Tereza Buckova 2493194s:Gareth Sears
 *
 * Class that represents the game model, initiliazes the game with players and distributing the deck
 * between players and communal pile.
 */

public class GameModel {
    private GameState gameState;
    private int roundNumber;
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
     * Reads the pile from the reader and initializes it
     */
    public GameModel(String jsonConfigFile) {
        try {
            wholeDeck = Pile.reader(jsonConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets and initializes the game with setting up players sets the round number, winning card
     * and roundWinner to null
     *
     * @param numAIPlayers
     */
    public void reset(int numAIPlayers) {

        players = new Player[numAIPlayers + 1];
        createHumanPlayer();
        createAIPlayers(numAIPlayers);
        wholeDeck.shuffle();
        assignCards(wholeDeck, players);
        activePlayer = randomlySelectFirstPlayer(players);
        playersInGame = new ArrayList<>(Arrays.asList(players));
        winningCard = null;
        roundWinner = null;

        roundNumber = 0;
        drawRound = 0;
    }

    /**
     * Creates human player always called 'USER'
     */
    public void createHumanPlayer() {
        humanPlayer = new Player("USER");
        players[0] = humanPlayer;
    }

    /**
     * Creates expected number of AIPlayers and adds them to players in the game with the correct
     * name
     */
    public void createAIPlayers(int numOfAIPlayers) {
        for (int i = 1; i <= numOfAIPlayers; i++) { // starts with 1 because HumanPlayer is in index
                                                    // 0
            players[i] = new AIPlayer("AI" + i);
        }
    }

    /**
     * Splits the whole deck of 40 cards and assigns it to players it assigns the reminder of cards
     * to communal pile
     */

    public void assignCards(Pile wholeDeck, Player[] players) {
        ArrayList<Pile> setOfDecks = wholeDeck.split(players.length, 40);
        for (int i = 0; i < players.length; i++) {
            players[i].addToDeck(setOfDecks.get(i));
        }
        communalPile = setOfDecks.get(players.length);
    }

    /**
     * Takes in attribute and compares values of the peek card from all players on the chosen
     * attribute
     */

    public Player playRoundWithAttribute(Attribute chosenAttribute) {
        int maxValue = 0;
        int drawMaxValue = 0;
        // increases round number
        roundNumber++;

        for (int i = 0; i < playersInGame.size(); i++) {
            // assigns top card for a player that is still in game as activeCard
            Card activeCard = playersInGame.get(i).peekCard();
            int playersAttributeValue = activeCard.getValue(chosenAttribute);

            if (maxValue < playersAttributeValue) {
                maxValue = playersAttributeValue;
                roundWinner = playersInGame.get(i);

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

            drawRound++;
            // resets the roundWinner
            roundWinner = null;
            // returns null to controller
            return null;

        } else {
            // increases the won round
            roundWinner.wonRound();
            winningCard = roundWinner.peekCard();

            // waits for the popping of the card and adds it to the communal pile
            addCardsToCommunalPile();
            // shuffles the communalPile
            communalPile.shuffle();
            // transfers all cards from communal pile to roundWinner
            transferCommunalPile(roundWinner);

            setActivePlayer(roundWinner);

            // returns winner to the controller
            return roundWinner;
        }
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
     * Checks whether human player is still in game
     */
    public boolean userStillInGame() {
        if (playersInGame.contains(humanPlayer)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks for the game winner by checking if there is only one player left in the game
     */
    public Player checkForWinner() {
        if (playersInGame.size() == 1) {
            return playersInGame.get(0);
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
    public GameState getGameState() {
        return gameState;
    }

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
}
