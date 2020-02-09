package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * AIPlayer tests
 */
public class AIPlayerTest {

    // Define potential attributes
    final static Attribute ATTRIBUTE_LOW_VALUE = new Attribute("Strength", 1);
    final static Attribute ATTRIBUTE_HIGH_VALUE = new Attribute("Stamina", 20);
    final static Attribute ATTRIBUTE_MID_VALUE = new Attribute("Money", 4);

    final static Attribute[] ATTRIBUTE_ARRAY =
            {ATTRIBUTE_HIGH_VALUE, ATTRIBUTE_MID_VALUE, ATTRIBUTE_LOW_VALUE};

    // The pile for testing
    static Pile testPile;

    /**
     * Sets up the TEST_PILE
     */
    @BeforeAll
    static void setupTestItems() {
        // Create a test card
        Card card = new Card("Test");

        for (Attribute attribute : ATTRIBUTE_ARRAY) {
            card.add(attribute);
        }

        // Create a test pile with the card
        Pile pile = new Pile();
        pile.add(card);

        // Set the test pile
        testPile = pile;
    }


    @Nested
    @DisplayName("chooseAttribute()")
    class ChooseAttribute {

        /*
         * Validation Tests
         */

        @Test
        @DisplayName("AIPlayer makes the best choice and returns attribute")
        public void chooseCorrectAttribute() {

            // creates new AIPlayer
            AIPlayer testAIPlayer = new AIPlayer("ai");

            // adds the card to pile of the AI Player
            testAIPlayer.addToDeck(testPile);

            // expects attribute with highest associated value
            assertEquals(ATTRIBUTE_HIGH_VALUE, testAIPlayer.chooseAttribute());

        }
    }

}
