package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class CardTest {

    protected static final Attribute a0 = new Attribute("Strength", 1);
    protected static final Attribute a1 = new Attribute("Stamina", 11);
    protected static final Attribute a2 = new Attribute("Money", 4);

    public static void addAttributesToCard(Card card) {
        card.add(a0);
        card.add(a1);
        card.add(a2);
    }

    @Nested
    class Constructor {

        @DisplayName("Throws if passed no name")
        @Test
        void throwsIfNameEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Card("");
            });
        }
    }

    @Test
    public void canGetName() {
        Card testCard = new Card("Strength");
        String expectedString = "Strength";
        String actualString = testCard.getName();
        assertEquals(expectedString, actualString);
    }

    @Nested
    class AddAndGet {
        @Test
        public void canAddAttribute() {
            Card testCard = new Card("Test");
            CardTest.addAttributesToCard(testCard);
            Attribute expectedAttribute = testCard.getAttribute(0);
            Attribute actualAttribute = CardTest.a0;
            assertEquals(actualAttribute, expectedAttribute);
        }
    }


    @Nested
    class GetAttributes {
        @DisplayName("Returns an attribute list")
        @Test
        public void getsAttributeList() {
            Card testCard = new Card("Test");
            CardTest.addAttributesToCard(testCard);

            ArrayList<Attribute> expectedAttributeList = testCard.getAttributes();
            ArrayList<Attribute> actualAttributeList = new ArrayList<Attribute>();
            actualAttributeList.add(CardTest.a0);
            actualAttributeList.add(CardTest.a1);
            actualAttributeList.add(CardTest.a2);
            assertEquals(expectedAttributeList, actualAttributeList);
        }

        @Test
        public void canGetAttributeByIndex() {
            Card testCard = new Card("Spaceship");
            CardTest.addAttributesToCard(testCard);
            assertEquals(CardTest.a0, testCard.getAttribute(0));
            assertEquals(CardTest.a2, testCard.getAttribute(2));
        }
    }

}


