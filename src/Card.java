public class Card {
    String name;
    Attribute a[];

    public Card(String n) {
        this.a = new Attribute[5];
    }

    public int getValue(int i){
        return this.a[i].value;
    }
}
