package model;
import java.util.ArrayList;
import java.util.Collections;

public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    /**
     * Chooses the attribute for the AI Player and returns it
     */
    public Attribute chooseAttribute() {
        ArrayList<Attribute> cardAttributes = peekCard().getAttributes();
        Attribute max = Collections.max(cardAttributes);
        return max;
    }
}
