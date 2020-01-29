package model;

import java.util.ArrayList;

public class Card {
    protected String name;
    protected ArrayList<Attribute> cardList = new ArrayList<Attribute>();
    protected ArrayList<Integer> cardIntegers = new ArrayList<Integer>();

    public Card(String n) {
        if ("".equals(n)||(n==null))
        {
            throw new IllegalArgumentException("No empty names allowed");
        }
        this.name = n;
    }

    public void add(Attribute a){
        this.cardList.add(a);
    }
    public Attribute getAttribute(int i){
        return this.cardList.get(i);
    }

    public ArrayList<Attribute> getAttributes(){
        return this.cardList;
    }

    public ArrayList<Integer> getCategoryValues(){
        for (int i=0; i<cardList.size(); i++){
            cardIntegers.add(this.cardList.get(i).getValue());
        }
        return cardIntegers;
    }

    public String getName(){
        return this.name;
    }

}
