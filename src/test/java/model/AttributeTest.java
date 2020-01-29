package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

public class AttributeTest {

    @Nested
    private class Constructor {
        @DisplayName("Constructor does not accept empty attribute names")
        @Test
        public void doesNotAcceptEmptyAttributeNames() {
            assertThrows(IllegalArgumentException.class, () -> new Attribute("", 1));
        }

        @DisplayName("Does not accept attribute values less than 0")
        @Test
        public void doesNotAcceptValuesLessThanZero() {
            assertThrows(IllegalArgumentException.class, () -> new Attribute("Test", -1));
        }
    }


    @Test
    public void testGetAttributeName() {
        Attribute testAttribute = new Attribute("Strength", 1);
        String expectedString = "Strength";
        String actualString = testAttribute.getName();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetAttributeValue() {
        Attribute testAttribute = new Attribute("Strength", 1);
        int expectedInt = 1;
        int actualInt = testAttribute.getValue();
        assertEquals(expectedInt, actualInt);
    }

    @Test
    public void testSetAttributeName() {
        Attribute testAttribute = new Attribute("Strength", 1);
        testAttribute.setName("Magic");
        String expectedString = "Magic";
        String actualString = testAttribute.getName();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testSetAttributeValue() {
        Attribute testAttribute = new Attribute("Strength", 1);
        testAttribute.setValue(2);
        int expectedInt = 2;
        int actualInt = testAttribute.getValue();
        assertEquals(expectedInt, actualInt);
    }

    // @Nested
    // private class CompareTo {
    // @DisplayName
    // @Test


    // }
}
