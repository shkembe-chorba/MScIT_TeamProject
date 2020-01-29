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

    public int playerSplit(int players, int cards){
        return cards%players;
    }

    //returns an Arraylist of all the piles to start the game with split equally to player piles and an extra pile with remained cards
    public ArrayList<Pile> split(int players, int cards) {
        //This is based on the number of players and cards

        int playerCards = cards%players;
        int otherCards = cards-playerCards*players;
        ArrayList<Pile> p = new ArrayList<Pile>();
        int j =0;

        while (j<players) {
            Pile a = new Pile();
            for (int i = 0; i<playerCards; i++) {
                a.add(cardList.peek());
                cardList.remove();
                }
            p.add(a);
            j++;
        }
        Pile b = new Pile();
        for (int i = 0; i<otherCards; i++) {
            b.add(cardList.peek());
            cardList.remove();
        }
        p.add(b);
        return p;
    }

    public static String getdeckFile(){
        return this.getdeckFile();
    }

    public static Pile reader(){
        Pile cardPile = new Pile();

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
                cardPile.add(a);
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
        return cardPile;
    }
}




