package model;

public class Player {
    private String name;

    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.equals("") || name.length() > 32) {
            throw new IllegalArgumentException("Invalid name, must be between 1 and 32 characters");
        }

        this.name = name;
    }

}
