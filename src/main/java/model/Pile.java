package model;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Represents a pile of cards in the game, e.g. deck, player deck, communal pile.
 */
public class Pile {

    private LinkedList<Card> cardList = new LinkedList<Card>();

    /**
     * Shuffle the pile.
     */
    public void shuffle() {
        Collections.shuffle(cardList);
    }

    /**
     * Return the top card of the pile.
     *
     * @return top card
     */
    public Card peek() {
        return cardList.peek();
    }

    /**
     * Remove the top card from the pile and return it.
     *
     * @return top card
     */
    public Card pop() {
        return cardList.pollFirst();
    }

    /**
     * Add another pile to the current's bottom.
     *
     * @param anotherPile
     */
    public void add(Pile anotherPile) {
        cardList.addAll(anotherPile.getCards());
    }

    /**
     * Add card to top of pile.
     *
     * @param card
     */
    public void add(Card card) {
        cardList.add(card);
    }

    /**
     * Return size of pile.
     *
     * @return size of pile
     */
    public int size() {
        return cardList.size();
    }

    // returns an Arraylist of all the piles to start the game with split equally to player piles
    // and an extra pile with remained cards

    /**
     * Returns an ArrayList of piles: the last one will become the communal pile and the rest will
     * become player decks at the beginning of the game
     *
     * @param numberOfPlayers
     * @return list of card piles
     */
    public ArrayList<Pile> split(int numberOfPlayers) {
        // This is based on the number of players and cards
        int cards = cardList.size();
        int cardsPerPlayer = cards / numberOfPlayers;
        LinkedList<Card> cardListCopy = new LinkedList<Card>(cardList);
        Pile[] decks = new Pile[numberOfPlayers];
        Pile communalPile = new Pile();
        int startingIndex = 0;
        for (int i = 0; i < decks.length; i++) {
            decks[i] = new Pile();
            decks[i].cardList = new LinkedList<Card>(
                    cardListCopy.subList(startingIndex, startingIndex + cardsPerPlayer));
            startingIndex += cardsPerPlayer;
        }
        communalPile.cardList =
                new LinkedList<Card>(cardListCopy.subList(startingIndex, cardListCopy.size()));
        ArrayList<Pile> setOfDecks = new ArrayList<Pile>(Arrays.asList(decks));
        setOfDecks.add(communalPile);
        return setOfDecks;
    }

    /**
     * Returns a pile of cards from a deck file with the given path(deckPath).
     *
     * @param deckPath
     * @return
     * @throws IOException
     */
    public static Pile reader(String deckPath) throws IOException {

        Scanner scanner = new Scanner(new FileReader(deckPath));
        String[] headers = scanner.nextLine().split(" ");
        Pile pile = new Pile();

        while (scanner.hasNextLine()) {
            String[] values = scanner.nextLine().split(" ");
            Card card = new Card(values[0]);

            for (int i = 1; i < values.length; i++) {
                Attribute a = new Attribute(headers[i], Integer.parseInt(values[i]));
                card.add(a);
            }

            pile.add(card);
        }

        scanner.close();
        return pile;
    }

    /**
     * Returns the toString() version of the cards in the pile, separated by new lines, starting
     * with:
     *
     * <code>-------- START OF PILE --------</code>
     *
     * and ending with:
     *
     * <code>-------- END OF PILE --------</code>
     *
     * @return the full pile with dividers
     */
    @Override
    public String toString() {
        String output = "-------- START OF PILE -------- \n";

        for (Card card : cardList) {
            output += card + "\n";
        }

        output += "-------- END OF PILE -------- \n";

        return output;
    }

    /**
     * Returns the current card list.
     * 
     * @return the card list
     */
    public LinkedList<Card> getCards() {
        return cardList;
    }
}


