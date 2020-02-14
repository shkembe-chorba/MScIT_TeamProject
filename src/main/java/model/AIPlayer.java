package model;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that represents the AI Players in the game.
 */
public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    /**
     * Chooses the attribute for the AI Player and returns it.
     * Uses the fact that attributes are comparable and returns the
     * one with highest value.
     * @return attribute
     */
    public Attribute chooseAttribute() {
        ArrayList<Attribute> cardAttributes = peekCard().getAttributes();
        Attribute max = Collections.max(cardAttributes);
        return max;
    }
}