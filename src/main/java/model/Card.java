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

    public String toString() {
        String attributeString = new ListUtility(cardList).getBulletList();
        return String.format("Card name: %s\n%s", name, attributeString);
    }

    public int getValue(Attribute givenAttribute) {
        String givenAttributeName = givenAttribute.getName();
        for(Attribute attribute: cardList) {
            if(attribute.getName().equals(givenAttributeName)) {
                return attribute.getValue();
            }
        }
        return 0;
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