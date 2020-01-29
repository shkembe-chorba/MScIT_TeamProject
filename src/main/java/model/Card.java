package model;

import java.util.ArrayList;

public class Card {
    protected String name;
    protected ArrayList<Attribute> card = new ArrayList<Attribute>();
    protected ArrayList<Integer> cardIntegers = new ArrayList<Integer>();

    public Card(String n) {
        this.name = n;
        this.card = new ArrayList<Attribute>();
    }

    public void add(Attribute a){
        this.card.add(a);
    }
    public Attribute getAttribute(int i){
        return this.card.get(i);
    }

    public ArrayList<Attribute> getAttributes(){
        return this.card;
    }

    public ArrayList<Integer> getCategoryValues(){
        for (int i=0; i<card.size(); i++){
            cardIntegers.add(this.card.get(i).getValue());
        }
        return cardIntegers;
    }

    public String getName(){
        return this.name;
    }

}
