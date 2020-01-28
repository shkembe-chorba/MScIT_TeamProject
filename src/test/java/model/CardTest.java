package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {
    @Test
    public void testCardValue() {
    Card testCard = new Card("Spaceship");
    testCard.att[0] .setValue(1);
    int expectedInt = 1;
    int actualInt = testCard.getValue(0);
    assertEquals(expectedInt, actualInt);
}

    @Test
    public void testCardAttribute() {
        Card testCard = new Card("Spaceship");
        testCard.att[0].setName("Spade");
        String expectedString = "Spade";
        String actualString = testCard.getAttribute(0);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCardSetName() {
        Card testCard = new Card("Spaceship");
        testCard.setName("Rocket");
        String expectedString = "Rocket";
        String actualString = testCard.getName();
        assertEquals(expectedString, actualString);
    }

    @DisplayName("Tests the ArrayList")
    @Test
    public void testGetCategoryValues() {
        Card testCard = new Card("Spaceship");
        ArrayList<Integer> a = new ArrayList<Integer>();
        testCard.att[0].setValue(1);
        testCard.att[1].setValue(3);
        testCard.att[2].setValue(5);
        testCard.att[3].setValue(7);
        testCard.att[4].setValue(2);
        a.add(1);
        a.add(3);
        a.add(5);
        a.add(7);
        a.add(2);

        ArrayList<Integer> expected = a;
        ArrayList<Integer> actual = testCard.getCategoryValues();
        assertEquals(expected, actual);
    }

}
