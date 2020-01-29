package model;

public class Attribute implements Comparable<Attribute> {
    protected String name;
    protected int value;

    public Attribute(String n, int v) {
        this.name = n;
        this.value = v;
    }

    public String getName(){
        return this.name;
    }

    public int getValue(){
        return this.value;
    }


    @Override
    public int compareTo(Attribute a) {
        if (!this.getName().equals(a.getName())){
            throw new IllegalArgumentException("Cannot compare different attributes");
        }
        return this.getValue()-a.getValue();
    }
}
