package model;

/**
 * Player.java
 * 
 * Example class to illustrate testing. 
 * TODO - Change for production
 */
public class Player {
    private String name;
    private int roundsWon;

    public int getRoundsWon() {
        return roundsWon;
    }

    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if ("".equals(name) || name.length() > 32) {
            throw new IllegalArgumentException("Invalid name, must be between 1 and 32 characters");
        }

        this.name = name;
    }

}
