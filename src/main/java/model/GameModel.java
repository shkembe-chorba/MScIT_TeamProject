package model;
import java.util.Random;
/**
 * Top Trumps game - MSc IT+ Masters Team Project
 *
 * Contributors:
 * 2175499m: Filip Marinov
 * 2504299a:Ventsislav Antov
 * 2172605d:Nadezhda Dimitrova
 * 2200528b: Tereza Buckova
 * 2493194s:Gareth Sears
 *
 * Class that represents the game model,
 * initiliazes the game with players
 * and distributing the deck between players and communal pile.
 */

public class GameModel {
    private GameState gameState;
    private int roundNumber;
    private Player[] players;
    private Player[] playersInGame; // players still left in the game
    private Player activePlayer; // active player that chooses the attribute
    private Pile CommunalPile;

    /**
     * Initialization of the game - needs the whole deck and number of AI players as a parameter
     * sets up a players array based on the number of players
     * and creates and puts players both human and AI into a players array
     * randomly selects first player
     */
    public GameModel(Pile WholeDeck, int numAIPlayers) {
        Player[] players = new Player[numAIPlayers + 1];
        this.players = players;
        createHumanPlayer();
        createAIPlayers(numAIPlayers);
        WholeDeck.shuffle();
        assignCards(WholeDeck, players);
        this.activePlayer = randomlySelectFirstPlayer(players);
        this.playersInGame = players;
    }


    public void createHumanPlayer() {
        Player HumanPlayer = new Player("USER");
        players[0] = HumanPlayer;
    }

    public void createAIPlayers(int numOfPlayers) {
        for (int i = 1; i < numOfPlayers; i++) {
            players[i] = new AIPlayer("AI" + i);
        }
    }

    public void assignCards(Pile wholeDeck, Player[] players) {
        Pile split = wholeDeck.split();
        for (int i = 0; i < 4; i++) {
            players[i].setPlayerHand(split[i]);
            //TODO: need to see what part of pile the person gets
        }
    }

    public void playRoundwithAtrribute(Attribute attribute) {
        //TODO: play round with attribute - compares attributes of player,  takes in attribute or index
        selects a winner, sets a next active player
    }

    public Player randomlySelectFirstPlayer(Player [] players) {
        Random rand = new Random();
        Player firstPlayer = players[rand.nextInt(players.length)];
        return firstPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Player getActivePlayer() { return activePlayer; }

    public void setActivePlayer(Player player) { Player player = this.activePlayer; }

    public Player[] getPlayers() {
        return players;
    }



}




