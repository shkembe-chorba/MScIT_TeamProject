package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Pile tests
 */
class PileTest {

    static final Card CARD_0 = new Card("1");
    static final Card CARD_1 = new Card("2");
    static final Card CARD_2 = new Card("3");
    static final Card CARD_3 = new Card("4");

    /**
     * Adds dummy cards to a pile
     *
     * @param pile     the pile
     * @param numCards the number of empty cards to be added
     */
    static void addCardHelper(Pile pile, int numCards) {
        for (int i = 0; i < numCards; i++) {
            pile.add(new Card("" + i));
        }
    }

    /*
     * Validation Tests
     */

    @DisplayName("Initialises with an empty pile")
    @Test
    void initialiseWithEmptyPile() {
        Pile pile = new Pile();
        assertEquals(0, pile.size());
    }

    @DisplayName("Shows correct size and tests the add method")
    @Test
    void sizeAndAdd() {
        Pile pile = new Pile();
        addCardHelper(pile, 40);
        assertEquals(40, pile.size());
    }

    @DisplayName("Shows correct card when peeked")
    @Test
    void peek() {
        Pile pile = new Pile();
        pile.add(CARD_0);
        assertEquals(CARD_0, pile.peek());
    }

    @DisplayName("Can peek the top card without removing it")
    @Test
    void peekGetsCardAndDoesNotRemove() {
        Pile pile = new Pile();
        addCardHelper(pile, 40);
        pile.peek();
        assertEquals(40, pile.size());

    }

    @DisplayName("Pop removes the top card")
    @Test
    void popRemovesTopCardAndReturns() {
        Pile pile = new Pile();
        addCardHelper(pile, 40);
        pile.pop();
        assertEquals(39, pile.size());
    }

    @DisplayName("Tests pile add method")
    @Test
    void popAddPile() {
        Pile pile = new Pile();
        Pile pile2 = new Pile();
        addCardHelper(pile, 40);
        addCardHelper(pile2, 40);
        pile.add(pile2);
        assertEquals(80, pile.size());
    }


    @DisplayName("Tests the cards split method for non equal cards between players")
    @Test
    public void splitWorkingNonEqualPlayerCards() {
        Pile pile = new Pile();
        addCardHelper(pile, 42);

        // Split the pile into 4
        ArrayList<Pile> list = pile.split(4);

        // The split should be the size of the split plus 1 (the remainder)
        assertEquals(5, list.size());

        // Check each of the splits is 42/4 (10)
        for (int i = 0; i < 4; i++) {
            assertEquals(10, list.get(i).size());
        }

        // Check the remainder is 42%4 (2)
        assertEquals(2, list.get(4).size());
    }

    @DisplayName("Tests the cards split method for equal cards between the players")
    @Test
    public void splitWorkingEqualPlayerCards() {
        Pile pile = new Pile();
        addCardHelper(pile, 40);
        ArrayList<Pile> list = pile.split(4);
        assertEquals(5, list.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(10, list.get(i).size());
        }
        assertEquals(0, list.get(4).size());
    }

    @DisplayName("Tests the toString")
    @Test
    public void toStringWorking() {
        Pile pile = new Pile();
        addCardHelper(pile, 10);
        String toString = pile.toString();
        assertTrue(toString.startsWith("-------- START OF PILE -------- \n")
                && toString.endsWith("-------- END OF PILE -------- \n"));
    }

    @DisplayName("Shuffle")
    @Test
    public void shuffleReturnsADifferentOrder() {
        Pile pile = new Pile();
        addCardHelper(pile, 5);
        // Clone a copy of the original order
        LinkedList<Card> initialOrder = new LinkedList<Card>(pile.getCards());
        pile.shuffle();
        // Check the new order doesn't equal the original
        assertNotEquals(initialOrder, pile.getCards());

    }

    @Nested
    @DisplayName("reader()")
    class Reader {

        final String RESOURCE_DIRECTORY_PATH = "src/test/resources/commandline/model";
        final String TEST_FILE = "testDeck.txt";

        @DisplayName("Returns a pile with the same properties as in the text file")
        @Test
        public void readerWorking() {
            File file = new File(RESOURCE_DIRECTORY_PATH, TEST_FILE);

            Pile pile = null;

            try {
                pile = Pile.reader(file.toString());
            } catch (IOException e) {
                fail("IOException in test");
            }

            Card card1 = new Card("350r");
            card1.add(new Attribute("Size", 1));
            card1.add(new Attribute("Speed", 9));
            card1.add(new Attribute("Range", 2));
            card1.add(new Attribute("Firepower", 3));
            card1.add(new Attribute("Cargo", 0));

            Card card2 = new Card("Avenger");
            card2.add(new Attribute("Size", 2));
            card2.add(new Attribute("Speed", 5));
            card2.add(new Attribute("Range", 4));
            card2.add(new Attribute("Firepower", 3));
            card2.add(new Attribute("Cargo", 2));


            assertEquals(2, pile.size(), "Pile should be 2 cards");

            List<Card> cardsInPile = pile.getCards();
            Card pileCard1 = cardsInPile.get(0);
            Card pileCard2 = cardsInPile.get(1);

            // Check the cards are the same, compare by string to avoid object clash
            assertEquals(card1.toString(), pileCard1.toString());
            assertEquals(card2.toString(), pileCard2.toString());

        }
    }
}


