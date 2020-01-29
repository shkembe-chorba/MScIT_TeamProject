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

    public void setName(String newName){
        this.name = newName;
    }

    public void setValue(int newValue){
        this.value=newValue;
    }

    @Override
    public int compareTo(Attribute a) {
        return this.getValue()-a.getValue();
    }
}
