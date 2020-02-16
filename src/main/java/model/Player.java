package model;

/**
 * Player class that represents the human player and is a superclass of the AIPlayer class. It
 * allows assigning a pile of cards to the Player, creating new players and retrieving them.
 */

public class Player {
    private String name;
    private int roundsWon;
    private Pile playerDeck = new Pile();

    /**
     * Create a new Player.
     * 
     * @param name The player name.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Used in the beginning of a game to assign a pile to a player in the game used to transfer
     * cards from community pile to Player
     *
     * @param addedPile pile of cards
     */
    public void addToDeck(Pile addedPile) {
        playerDeck.add(addedPile);
    }

    /**
     * Method that returns number of rounds that the player won
     *
     * @return roundsWon
     */
    public int getRoundsWon() {
        return this.roundsWon;
    }

    /**
     * Increase counter of rounds for player Used when player wins a round
     */
    public void wonRound() {
        this.roundsWon++;
    }

    /**
     * Method that returns the top card of the player
     *
     * @return topCard
     */
    public Card peekCard() {
        Card topCard = playerDeck.peek();
        return topCard;
    }

    /**
     * Method that returns the top card of the player and removes it from their deck.
     *
     * @return top card of player
     */
    public Card popCard() {
        return playerDeck.pop();
    }

    /**
     * Returns name of a player
     *
     * @return name of the player
     */
    public String toString() {
        return name;
    }

    /**
     * Returns the size of the deck of the player without their top card.
     *
     * @return
     */
    public int getRemainingDeckSize() {
        return playerDeck.size() - 1;
    }

    // Getter.
    public Pile getDeck() {
        return playerDeck;
    }
}

