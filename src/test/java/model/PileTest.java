package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PileTest {

    Card c0 = new Card("1");
    Card c1 = new Card("2");
    Card c2 = new Card("3");
    Card c3 = new Card("4");

    void addCardHelper(Pile pile) {
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

    }
}
