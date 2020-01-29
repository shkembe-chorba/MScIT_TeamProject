package model;

public class Attribute {
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
}
