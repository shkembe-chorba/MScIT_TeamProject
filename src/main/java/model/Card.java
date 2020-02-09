package model;

import commandline.utils.ListUtility;

import java.util.ArrayList;

public class Card {
    protected String name;
    protected ArrayList<Attribute> cardList = new ArrayList<Attribute>();
    protected ArrayList<Integer> cardIntegers = new ArrayList<Integer>();

    public Card(String n) {
        if ("".equals(n) || (n == null)) {
            throw new IllegalArgumentException("No empty names allowed");
        }
        this.name = n;
    }

    public String toString() {
        String attributeString = new ListUtility(cardList).getBulletList();
        return String.format("Card name: %s\n%s", name, attributeString);
    }



    public void add(Attribute a) {
        this.cardList.add(a);
    }

    /**
     * Gets the attribute from the card which has the same attribute name as the one passed to it.
     * 
     * @param givenAttribute
     * @return
     */
    public Attribute getAttribute(Attribute givenAttribute) {
        for (Attribute attribute : cardList) {
            if (attribute.equals(givenAttribute)) {
                return attribute;
            }
        }
        return null;
    }

    public Attribute getAttribute(int i) {
        return this.cardList.get(i);
    }

    public ArrayList<Attribute> getAttributes() {
        return this.cardList;
    }

    public String getName() {
        return this.name;
    }

}
