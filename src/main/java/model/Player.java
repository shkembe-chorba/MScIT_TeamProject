package model;
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
 * Player class that represents human player
 * and is a superclass to AIPlayer class.
 * It allows assigning Player a pile of cards,
 * creating new playersand retrieving them
 */

public class Player {
    private String name;
    private int roundsWon;
    private Pile playerHand;

    public Player(String name) {
        setName(name);
        this.name = name;
    }

    /** Sets the name*/
    public void setName(String name) {
        this.name = name;
    }

    /** Used in the beginning of a game to assign a pile to a player
     @param playerHand pile of cards   */
    public void setPlayerHand(Pile playerHand) {
        this.playerHand = playerHand;
    }

    /**
     * Method that returns number of rounds that the player won
     * @return roundsWon
     * */
    public int getRoundsWon() {
        return this.roundsWon;
    }

    /**
     * Increase counter of rounds for player
     * Used when player wins a round */
    public void wonRound() {
        this.roundsWon++;
    }

    /**
     * Method that takes the following card in the player's hand
     * @return nextCard
     */
    public Card getNextCard() {
        Card nextCard = playerHand.peek();
        return nextCard;
    }

    public String toString() {
        return name;}
        
    }



