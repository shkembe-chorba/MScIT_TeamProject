import java.util.ArrayList;
import java.util.Collections;

public class Pile {
    ArrayList<Card> c;

    public Pile() {
        this.c = new ArrayList<Card>();
    }

    public void Shuffle() {
        Collections.shuffle(c);
    }

    public Card peek(int i) {
        return c.get(i);
    }

    public void remove(int i) {
        c.remove(i);
    }

    public void add(ArrayList a) {
        c.addAll(a);
    }
}


