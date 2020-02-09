package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Attribute tests
 */
public class AttributeTest {

    /*
     * Validation Tests
     */

    @DisplayName("getName()")
    @Test
    public void testGetAttributeName() {
        Attribute testAttribute = new Attribute("Strength", 1);
        String expectedString = "Strength";
        String actualString = testAttribute.getName();
        assertEquals(expectedString, actualString);
    }

    @DisplayName("getValue()")
    @Test
    public void testGetAttributeValue() {
        Attribute testAttribute = new Attribute("Strength", 1);
        int expectedInt = 1;
        int actualInt = testAttribute.getValue();
        assertEquals(expectedInt, actualInt);
    }

    @DisplayName("compareTo()")
    @Nested
    public class CompareTo {

        /*
         * Validation Tests
         */

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
    }
}
