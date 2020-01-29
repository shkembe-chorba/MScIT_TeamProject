package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class CardTest {

    private static final Attribute a0 = new Attribute("Strength", 1);
    private static final Attribute a1 = new Attribute("Stamina", 11);
    private static final Attribute a2 = new Attribute("Money", 4);

    private static void addAttributesToCard(Card card) {
        card.add(a0);
        card.add(a1);
        card.add(a2);
    }

    @Nested
    private class Constructor {

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

    }

    @Nested
    private class Add {
        @Test
        public void canAddAttribute() {

        }
    }

    @Nested
    private class GetAttribute {

    }

    @Nested
    private class GetAttributes {
        @DisplayName("Returns an attribute list")
        @Test
        void getsAttributeList() {

        }
    }



    @Test
    public void canGetAttributeByIndex() {
        Card testCard = new Card("Spaceship");
        addAttributesToCard(testCard);
        assertEquals(a0, testCard.getAttribute(0));
        assertEquals(a2, testCard.getAttribute(2));
    }

}
