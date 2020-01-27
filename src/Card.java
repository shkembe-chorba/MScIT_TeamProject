import java.util.ArrayList;

public class Card {
    protected String name;
    Attribute att[];
    ArrayList<Integer> a = new ArrayList<Integer>();

    public Card(String n) {
        this.name = n;
        this.att = new Attribute[5];
    }

    public int getValue(int i){
        return this.att[i].getValue();
    }

    public ArrayList<Integer> getCategoryValues(){
        for (int i=0; i<att.length; i++){
            a.add(this.att[i].getValue());
        }
        return a;
    }

    public void setName(String newName){
        this.name = newName;
    }


}
