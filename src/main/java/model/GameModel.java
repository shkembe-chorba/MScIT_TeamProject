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
    private List <Player> playersInGame = null ; // players still left in the game
    private Player activePlayer; // active player that chooses the attribute
    private Pile communalPile;
    private Player roundWinner;
    private int draws;
    private Card winningCard;
    private Pile wholeDeck;
    private Player humanPlayer;

    /**
     * Reads the pile from the reader and initializes it
     */
    public GameModel() {
        wholeDeck = Pile.reader();
    }

    /**
     * Resets and initializes the game with setting up players
     * sets the round number, winning card and roundWinner to null
     * @param numAIPlayers
     */
    public void reset(int numAIPlayers) {

        players = new Player[numAIPlayers + 1];
        createHumanPlayer();
        createAIPlayers(numAIPlayers);
        wholeDeck.shuffle();
        assignCards(wholeDeck, players);
        activePlayer = randomlySelectFirstPlayer(players);
        playersInGame = Arrays.asList(players);
        winningCard = null;
        roundWinner = null;

        roundNumber = 0;
        draws = 0;
    }


    public void createHumanPlayer() {
        humanPlayer = new Player("USER");
        players[0] = humanPlayer;
    }

    public void createAIPlayers(int numOfAIPlayers) {
        for (int i = 1; i < numOfAIPlayers; i++) {
            players[i] = new AIPlayer("AI" + i);
        }
    }

    public void assignCards(Pile wholeDeck, Player[] players) {
        ArrayList<Pile> split = wholeDeck.split(players.length, 40);
        for (int i = 0; i < players.length; i++) {
            players[i].addtoDeck(split.get(i));
        }
        this.communalPile = split.get(players.length + 1);
    }

    // index of the attribute that the round is going to played with
    public Player playRoundwithAtrribute(Attribute chosenAttribute) {
        int maxValue = 0;
        int drawValue = 0;
        roundNumber++;

        for (int i = 0; i < playersInGame.size(); i++) {
            Card activeCard = playersInGame.get(i).peekCard();
            int playersAttributeValue = activeCard.chosenAttribute.getValue();

            if (maxValue < playersAttributeValue) {
                maxValue = playersAttributeValue;
                roundWinner = playersInGame.get(i);

            } else if (maxValue == playersAttributeValue) {
                drawValue = maxValue;
            }
            else {}
        }


        // if maxValue is also the drawValue after going through all the values, it means that there is no higher value
        if (maxValue == drawValue) {
            // pops the card from all the players and transfers them to communal pile
            addCardstoCommunalPile();
            draws++;
            // resets the roundWinner
            roundWinner = null;
            // returns null to controller
            return null;

        } else {
            setActivePlayer(roundWinner);
            roundWinner.wonRound();
            winningCard = roundWinner.peekCard();

            // waits for the popping of the card and adds it to the communal pile
            addCardstoCommunalPile();
            // transfers all cards from communal pile to roundWinner
            receiveCommunalPile(roundWinner);

            // returns winner to the controller
            return roundWinner;
        }
    }

    public Player randomlySelectFirstPlayer(Player [] players) {
        Random rand = new Random();
        Player firstPlayer = players[rand.nextInt(players.length)];
        return firstPlayer;
    }

    //removes player from players in game
    public void eliminatePlayer(Player eliminated) {
        playersInGame.remove(eliminated);
    }

    // checks whether humanPlayer is still in game
    public boolean userSttillInGame() {
    if (playersInGame.contains(humanPlayer)) { return true; }
        else { return false; }
    }

    //
    public Player checkforWinner() {
        if (playersInGame.size = 1) {
            return playersInGame.get(0);
        }
        else (return null;)
    }

    public void checktoEliminate() {
        for (int i = 0; i < playersInGame.size(); i++) {
            if (playersInGame.get(i).peekCard() == null){
                eliminatePlayer(playersInGame.get(i));
            }
        }
    }

    public void addCardstoCommunalPile() {
        for (int i = 0; i < playersInGame.size(); i++) {
            Player playerToPopCard = playersInGame.get(i);
            communalPile.add(playerToPopCard.popCard());
        }
    }

    public void receiveCommunalPile(Player roundWinner) { this.roundWinner.addtoDeck(communalPile); }

    public GameState getGameState() { return gameState; }

    public Player getHumanPlayer() {return humanPlayer; }

    public int getRoundNumber() { return roundNumber;
    }
    public void increaseRoundNumber() { roundNumber++; }

    public Player getActivePlayer() { return activePlayer; }

    public Player getRoundWinner() {return roundWinner; }

    public Card getWinningCard() {return winningCard; }

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




