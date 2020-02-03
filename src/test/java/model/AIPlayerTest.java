package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AIPlayerTest {
    @Test
    @DisplayName("AIPlayer makes the best choice and returns attribute")
    public void chooseCorrectAttribute() {

        // initiates new pile with one card with three attributes
        Attribute a0 = new Attribute("Strength", 1);
        Attribute a1 = new Attribute("Stamina", 20);
        Attribute a2 = new Attribute("Money", 4);
        Card testCard = new Card("Test");
        testCard.add(a0);
        testCard.add(a1);
        testCard.add(a2);
        Pile testPile = new Pile();
        testPile.add(testCard);

        //creates new AIPlayer
        AIPlayer testAIPlayer = new AIPlayer("ai");
        //adds the card to pile of the AI Player
        testAIPlayer.addToDeck(testPile);

        //expects attribute with highest associated value
        Attribute expectedAttribute = a1;
        Attribute actualAttribute = testAIPlayer.chooseAttribute();
        assertEquals(expectedAttribute, actualAttribute);

    }

}