public class Card {
    String name;
    Attribute att[];

    public Card(String n) {
        this.name = n;
        this.att = new Attribute[5];
    }

    public int getValue(int i){
        return this.att[i].getValue();
    }

    public void setName(String newName){
        this.name = newName;
    }


}
