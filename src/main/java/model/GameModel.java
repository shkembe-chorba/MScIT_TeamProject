package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

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
    private List <Player> playersInGame ; // players still left in the game
    private Player activePlayer; // active player that chooses the attribute
    private Pile CommunalPile;
    private Player roundWinner;
    private Card winningCard;

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
        WholeDeck.Shuffle();
        assignCards(WholeDeck, players);

        this.activePlayer = randomlySelectFirstPlayer(players);
        List <Player> playersInGame = Arrays.asList(players);
        int roundNumber = 0;
    }

    public void createHumanPlayer() {
        Player HumanPlayer = new Player("USER");
        players[0] = HumanPlayer;
    }

    public void createAIPlayers(int numOfPlayers) {
        for (int i = 0; i < numOfPlayers-1; i++) {
            players[i] = new AIPlayer("AI" + i);
        }
    }

    public void assignCards(Pile wholeDeck, Player[] players) {
        ArrayList<Pile> split = wholeDeck.split(players.length, 40);
        for (int i = 0; i < players.length; i++) {
            players[i].addtoHand(split.get(i));
        }
        this.CommunalPile = split.get(players.length + 1);
    }

    public void playRoundwithAtrributeIndex(int chosenAttribute) {
        int maxValue = 0;
        Card winningCard = null;
        for (int i = 0; i < playersInGame.size(); i++) {
           Card activeCard = playersInGame.get(i).peekCard();
           int playersAttributeValue = activeCard.getValue(chosenAttribute);

           if (maxValue < playersAttributeValue) {
               roundWinner = playersInGame.get(i);
               maxValue = playersAttributeValue;
               winningCard = activeCard;
           }
            setActivePlayer(roundWinner);
            roundWinner.wonRound();

        }

    }

    public Player randomlySelectFirstPlayer(Player [] players) {
        Random rand = new Random();
        Player firstPlayer = players[rand.nextInt(players.length)];
        return firstPlayer;
    }


    public void eliminatePlayer(Player eliminated) {
        playersInGame.remove(eliminated);
    }

    public Card getWinningCard() {
        return winningCard;
    }

    public void checktoEliminate() {
        for (int i = 0; i < playersInGame.size(); i++) {
            if (playersInGame.get(i).peekCard() == null){
                eliminatePlayer(playersInGame.get(i));
            }
        }

    

    }
    public GameState getGameState() {
        return gameState;
    }

    public String displayRoundNumber() {
        String s = "";
        s = s+ roundNumber;
        return s;
    }

    public void increaseRoundNumber() {
        roundNumber++;
    }

    public Player getActivePlayer() { return activePlayer; }

    public Player getRoundWinner() {return roundWinner; }

    public void setActivePlayer(Player playerActive) { this.activePlayer = playerActive; }

    public Player[] getPlayers() { return players; }

    //array of players that were in the game in the beginning
    public String toString(Player [] players) {
        String s = "";
        for (int i = 0; i < players.length; i++) {
            s = players[i].toString();
        }
        return s;
    }


}




