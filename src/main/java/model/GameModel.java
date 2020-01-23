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
 * initiliazes
 */

public class GameModel {
    private GameState gameState;
    private int roundNumber;
    private Player[] players;
    private Player[] playersInGame;
    private Player activePlayer;
    private Pile CommunalPile;

    public GameModel(Pile WholeDeck, int numAIPlayers) {
        Player[] players = new Player[numAIPlayers + 1];
        this.players = players;
        createHumanPlayer();
        createAIPlayers(numAIPlayers);
        WholeDeck.shuffle();
        assignCards(WholeDeck, players);
        activePlayer = randomlySelectFirstPlayer(players);
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

    public Player randomlySelectFirstPlayer(Player [] players) {
        Random rand = new Random();
        Player firstPlayer = players[rand.nextInt(players.length)];
        return firstPlayer;
    }

//    public void removePlayerinGame(int ) {
//
//        }

    public GameState getGameState() {
        return gameState;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Player getActivePlayer() {
        //TODO: return next active player - needs to be person who won last
        return activePlayer;
    }

    public void playRoundwithAtrribute() {
        //TODO: play round with attribute
    }
    public Player[] getPlayers() {
        return players;
    }

    public String playersinGame() {
            return s;
        }

}




