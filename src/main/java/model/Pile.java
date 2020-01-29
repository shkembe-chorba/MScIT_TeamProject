package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Pile {

    private LinkedList<Card> cardList;

    public Pile() {
        cardList = new LinkedList<Card>();
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

    public void add(LinkedList a) {
        cardList.addAll(a);
    }

    public void add(Card a) {
        cardList.add(a);
    }

    public int size() {
        return cardList.size();
    }

    public int playerSplit(int players, int cards) {
        return cards % players;
    }

    // returns an Arraylist of all the piles to start the game with split equally to player piles
    // and an extra pile with remained cards
    public ArrayList<Pile> split(int players, int cards) {
        // This is based on the number of players and cards

        int playerCards = cards % players;
        int otherCards = cards - playerCards * players;
        ArrayList<Pile> p = new ArrayList<Pile>();
        int j = 0;

        while (j < players) {
            Pile a = new Pile();
            for (int i = 0; i < playerCards; i++) {
                a.add(cardList.peek());
                cardList.remove();
            }
            p.add(a);
            j++;
        }
        Pile b = new Pile();
        for (int i = 0; i < otherCards; i++) {
            b.add(cardList.peek());
            cardList.remove();
        }
        p.add(b);
        return p;
    }

    public static String getdeckFile() {
        return getdeckFile();
    }

    public static Pile reader() throws IOException {

        // TODO: Move hard coding of this path up a level

        FileReader fileReader = new FileReader("./" +getdeckFile()+".txt");
        Scanner scanner = new Scanner(fileReader);
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

}


