package model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.google.gson.JsonObject;
import commandline.utils.JsonUtility;


public class Pile {

    private LinkedList<Card> cardList = new LinkedList<Card>();

    public LinkedList<Card> getCards() {
        return cardList;
    }

    public void shuffle() {
        Collections.shuffle(cardList);
    }

    public Card peek() {
        return cardList.peek();
    }

    public Card pop() {
        return cardList.pollFirst();
    }

    public void add(Pile pile) {
        cardList.addAll(pile.getCards());
    }

    public void add(Card card) {
        cardList.add(card);
    }

    public int size() {
        return cardList.size();
    }



    // returns an Arraylist of all the piles to start the game with split equally to player piles
    // and an extra pile with remained cards
    public ArrayList<Pile> split(int numberOfPlayers) {
        // This is based on the number of players and cards
        int cards = cardList.size();
        int cardsPerPlayer = cards / numberOfPlayers;
        LinkedList<Card> cardListCopy = (LinkedList<Card>) cardList.clone();
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

    public static Pile reader(String jsonPath) throws IOException {

        // This should probably be dealt with outside of this class, not with hard coded
        // inline paths...

        // Load the config .json
        JsonObject config = JsonUtility.getJsonObjectFromFile(new File(jsonPath));

        // Load the deck.txt
        final String DECK_PATH = config.get("deckFile").getAsString();
        Scanner scanner = new Scanner(new FileReader(DECK_PATH));

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

    @Override
    public String toString() {
        String output = "-------- START OF PILE -------- \n";

        for (Card card : cardList) {
            output += card + "\n";
        }

        output += "-------- END OF PILE -------- \n";

        return output;
    }
}


