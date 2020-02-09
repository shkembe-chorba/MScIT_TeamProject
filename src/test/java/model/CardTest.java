package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Card tests
 */
public class CardTest {

    protected static final Attribute ATTRIBUTE_STRENGTH_1 = new Attribute("Strength", 1);
    protected static final Attribute ATTRIBUTE_STAMINA_11 = new Attribute("Stamina", 11);
    protected static final Attribute ATTRIBUTE_MONEY_4 = new Attribute("Money", 4);

    @Nested
    @DisplayName("Card Constructor")
    class Constructor {

        /*
         * Defect Test
         */

        @DisplayName("Throws if passed no name")
        @Test
        void throwsIfNameEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Card("");
            });
        }
    }

    /*
     * Validation Tests
     */

    @DisplayName("getName()")
    @Test
    public void canGetName() {
        Card testCard = new Card("Strength");
        assertEquals("Strength", testCard.getName());
    }

    @DisplayName("add() and getAttribute()")
    @Test
    public void canAddAttribute() {
        Card testCard = new Card("Test");
        testCard.add(ATTRIBUTE_STRENGTH_1);
        assertEquals(ATTRIBUTE_STRENGTH_1, testCard.getAttribute(0));
    }


    @Nested
    @DisplayName("getAttributes()")
    class GetAttributes {
        @DisplayName("Returns an attribute list")
        @Test
        public void getsAttributeList() {
            Card testCard = new Card("Test");

            testCard.add(ATTRIBUTE_STRENGTH_1);
            testCard.add(ATTRIBUTE_STAMINA_11);
            testCard.add(ATTRIBUTE_MONEY_4);

            ArrayList<Attribute> expectedAttributeList = new ArrayList<Attribute>();
            expectedAttributeList.add(ATTRIBUTE_STRENGTH_1);
            expectedAttributeList.add(ATTRIBUTE_STAMINA_11);
            expectedAttributeList.add(ATTRIBUTE_MONEY_4);

            assertEquals(expectedAttributeList, testCard.getAttributes());
        }

        @DisplayName("Can get attribute by index")
        @Test
        public void canGetAttributeByIndex() {
            Card testCard = new Card("Test");

            testCard.add(ATTRIBUTE_STRENGTH_1);
            testCard.add(ATTRIBUTE_STAMINA_11);
            testCard.add(ATTRIBUTE_MONEY_4);

            assertEquals(CardTest.ATTRIBUTE_STRENGTH_1, testCard.getAttribute(0));
            assertEquals(CardTest.ATTRIBUTE_MONEY_4, testCard.getAttribute(2));
        }
    }

}


