package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
