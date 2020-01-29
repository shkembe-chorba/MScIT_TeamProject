package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Pile {

    private LinkedList<Card> pile;

    public Pile() {
        pile = new LinkedList<Card>();
    }

    public void shuffle() {
        Collections.shuffle(pile);
    }

    public Card peek() {
        return pile.peek();
    }

    public Card pop() {
        return pile.pollFirst();
    }

    public void add(LinkedList a) {
        pile.addAll(a);
    }

    public void add(Card a) {
        pile.add(a);
    }

    public int size() {
        return pile.size();
    }

    //returns an Arraylist of all the piles to start the game with split equally to player piles and an extra pile with remained cards
    public ArrayList<Pile> split(int numberOfPlayers, int cards) {
        //This is based on the number of players and cards

        int cardsPerPlayer = cards/numberOfPlayers;
        int otherCards = cards%numberOfPlayers;

        ArrayList<Pile> setOfDecks = new ArrayList<Pile>();

        // Player Decks
        int j =0;
        while (j<numberOfPlayers) {
            Pile playerDeck = new Pile();
            for (int i = 0; i<cardsPerPlayer; i++) {
                playerDeck.add(pile.pop());
            }
            setOfDecks.add(playerDeck);
            j++;
        }
        //Communal Pile
        Pile communalPile = new Pile();
        for (int i = 0; i<otherCards; i++) {
            communalPile.add(pile.pop());
        }
        setOfDecks.add(communalPile);
        return setOfDecks;
    }

    public String getdeckFile(){
        return this.getdeckFile();
    }

    public static Pile reader() {
        Pile c = new Pile();

        //using the JSONReader - not sure if this is what it should look like
        String fileName = "MScIT_TeamProject\\"+getdeckFile()+".txt";

        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            Scanner s = new Scanner(fr);
            String line = s.nextLine();
            String[] categories = line.split(" ", 5);
            while(s.hasNextLine()) {
                String lineCard = s.nextLine();
                String[] card = line.split(" ", 5);
                Card a = new Card(card[0]);
                a.att[0].setName(categories[1]);
                a.att[0].setValue(Integer.parseInt(card[1]));
                a.att[1].setName(categories[2]);
                a.att[1].setValue(Integer.parseInt(card[2]));
                a.att[2].setName(categories[3]);
                a.att[2].setValue(Integer.parseInt(card[3]));
                a.att[3].setName(categories[4]);
                a.att[3].setValue(Integer.parseInt(card[4]));
                a.att[4].setName(categories[5]);
                a.att[4].setValue(Integer.parseInt(card[5]));
                c.add(a);
            }
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(fr != null) {
                try {
                    fr.close();
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return c;
    }
}