package model;

import commandline.utils.ListUtility;

import java.util.ArrayList;

/**
 * Represents the cards in the game.
 */
public class Card {

    //Fields.
    protected String name;
    protected ArrayList<Attribute> attributeList = new ArrayList<Attribute>();

    /**
     * Constructor.
     * @param name name of card
     */
    public Card(String name) {
        if ("".equals(name) || (name == null)) {
            throw new IllegalArgumentException("No empty names allowed");
        }
        this.name = name;
    }

    /**
     * Adds an attribute to the card.
     * @param attribute
     */
    public void add(Attribute attribute) {
        this.attributeList.add(attribute);
    }

    /**
     * toString
     * @return Card name: name
     *         bullet list of attributes
     */
    @Override
    public String toString() {
        String attributeString = new ListUtility(attributeList).getBulletList();
        return String.format("Card name: %s\n%s", name, attributeString);
    }

    /**
     * Return the value of the attribute in the card
     * with the same name as the name of the passed attribute.
     * @param givenAttribute
     * @return value of attribute of the same type
     */
    public int getValue(Attribute givenAttribute) {
        String givenAttributeName = givenAttribute.getName();
        for (Attribute attribute : attributeList) {
            if (attribute.getName().equals(givenAttributeName)) {
                return attribute.getValue();
            }
        }
        return 0;
    }

    /**
     * Return the i'th attribute
     * @param i
     * @return
     */
    public Attribute getAttribute(int i) {
        return this.attributeList.get(i);
    }

    //Getters.

    public ArrayList<Attribute> getAttributes() {
        return this.attributeList;
    }

    public String getName() {
        return this.name;
    }

}
