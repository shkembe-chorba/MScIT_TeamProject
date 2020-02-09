package model;

public class Attribute implements Comparable<Attribute> {
    protected String name;
    protected int value;

    public Attribute(String n, int v) {
        this.name = n;
        this.value = v;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }


    @Override
    public int compareTo(Attribute a) {
        return this.getValue() - a.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attribute) {
            Attribute a = (Attribute) obj;
            return this.getName().equals(a.getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %d", name, value);
    }
}
