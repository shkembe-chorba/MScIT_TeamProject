package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AttributeTest {

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

    @Nested
    private class CompareTo {
        @DisplayName("Attribute with higher value is treated as greater")
        @Test
        public void attributeWithHigherValueIsGreater() {
            Attribute a1 = new Attribute("Strength", 10);
            Attribute a2 = new Attribute("Strength", 4);
            Attribute a3 = new Attribute("Strength", 10);
            Attribute a4 = new Attribute("Strength", 12);

            List<Attribute> list = Arrays.asList(new Attribute[] {a1, a2, a3, a4});

            assertTrue(a1.compareTo(a2) > 0);
            assertFalse(a2.compareTo(a1) < 0);
            assertTrue(a1.compareTo(a3) == 0);
            assertEquals(a4, Collections.max(list));
        }

        @DisplayName("Attributes with different names cannot be compared")
        @Test
        public void attributesWithDifferentNamesCannotBeCompared() {

            Attribute a1 = new Attribute("Strength", 10);
            Attribute a2 = new Attribute("Stamina", 4);

            assertThrows(IllegalArgumentException.class, () -> a1.compareTo(a2));
        }

    }
}
