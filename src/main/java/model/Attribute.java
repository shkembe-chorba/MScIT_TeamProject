package model;

/**
 * Represents the attributes of the cards in the game.
 */
public class Attribute implements Comparable<Attribute> {
    //Fields.
    protected String name;
    protected int value;

    /**
     * Constructor.
     * @param name name
     * @param value value
     */
    public Attribute(String name, int value) {
        this.name = name;
        this.value = value;
    }


    //Getters.
    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }


    /**
     * compareTo
     * @param other other attribute
     * @return attribute comparison integer
     */
    @Override
    public int compareTo(Attribute other) {
        return this.getValue() - other.getValue();
    }

    /**
     * toString
     * @return name: value
     */
    @Override
    public String toString() {
        return String.format("%s: %d", name, value);
    }
}
