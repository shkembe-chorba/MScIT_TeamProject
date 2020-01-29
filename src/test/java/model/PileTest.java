package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PileTest {

    static Card c0 = new Card("1");
    static Card c1 = new Card("2");
    static Card c2 = new Card("3");
    static Card c3 = new Card("4");

    static void addCardHelper(Pile pile) {
        pile.add(c0);
        pile.add(c1);
        pile.add(c2);
        pile.add(c3);
    }

    @DisplayName("Initialises with an empty pile")
    @Test
    void initialiseWithEmptyPile() {
        Pile pile = new Pile();
        assertEquals(0, pile.size());
    }

    @DisplayName("Shows correct size and tests the add method")
    @Test
    void sizeAndAdd(){
        Pile pile = new Pile();
        addCardHelper(pile);
        assertEquals(4, pile.size());
    }

    //No need to test shuffle method as this is built in

    @DisplayName("Shows correct card when peeked")
    @Test
    void peek(){
        Pile pile = new Pile();
        PileTest.addCardHelper(pile);
        assertEquals(c0, pile.peek());
    }

    @DisplayName("Can peek the top card without removing it")
    @Test
    void peekGetsCardAndDoesNotRemove() {
        Pile pile = new Pile();
        addCardHelper(pile);
        pile.peek();
        assertEquals(4, pile.size());

    }

    @DisplayName("Pop removes the top card")
    @Test
    void popRemovesTopCardAndReturns() {
        Pile pile = new Pile();
        addCardHelper(pile);
        pile.pop();
        assertEquals(3, pile.size());
    }
}
