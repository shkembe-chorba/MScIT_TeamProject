package model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
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

    @Nested
    public class CompareTo {
        @DisplayName("Attributes can be compared based on value")
        @Test
        public void comparableWorksAsExpected() {
            Attribute a1 = new Attribute("Strength", 10);
            Attribute a2 = new Attribute("Strength", 4);
            Attribute a3 = new Attribute("Strength", 10);
            Attribute a4 = new Attribute("Strength", 12);

            Attribute[] unorderedArray = {a1, a2, a3, a4};
            Attribute[] orderedArray = {a2, a1, a3, a4};

            Arrays.sort(unorderedArray);
            assertArrayEquals(orderedArray, unorderedArray);
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
