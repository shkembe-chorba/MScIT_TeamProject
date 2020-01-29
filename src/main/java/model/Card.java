package model;

import java.util.ArrayList;

public class Card {
    protected String name;
    protected Attribute att[];
    protected ArrayList<Integer> a = new ArrayList<Integer>();

    public Card(String n) {
        this.name = n;
        this.att = new Attribute[5];
    }


    public int getValue(int i){
        return this.att[i].getValue();
    }

    public String getAttribute(int i){
        return this.att[i].getName();
    }

    public ArrayList<Integer> getCategoryValues(){
        for (int i=0; i<att.length; i++){
            a.add(this.att[i].getValue());
        }
        return a;
    }

    public String getName(){
        return this.name;
    }

}
