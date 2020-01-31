package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PileTest {

    static Card c0 = new Card("1");
    static Card c1 = new Card("2");
    static Card c2 = new Card("3");
    static Card c3 = new Card("4");

    static void addCardHelper(Pile pile) {
        for (int i = 0; i < 40; i++) {
            pile.add(new Card("" + i));
        }
    }

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
        addCardHelper(pile);
        assertEquals(40, pile.size());
    }

    // No need to test shuffle method as this is built in

    @DisplayName("Shows correct card when peeked")
    @Test
    void peek() {
        Pile pile = new Pile();
        pile.add(c0);
        assertEquals(c0, pile.peek());
    }

    @DisplayName("Can peek the top card without removing it")
    @Test
    void peekGetsCardAndDoesNotRemove() {
        Pile pile = new Pile();
        addCardHelper(pile);
        pile.peek();
        assertEquals(40, pile.size());

    }

    @DisplayName("Pop removes the top card")
    @Test
    void popRemovesTopCardAndReturns() {
        Pile pile = new Pile();
        addCardHelper(pile);
        pile.pop();
        assertEquals(39, pile.size());
    }

    @DisplayName("Tests pile add method")
    @Test
    void popAddPile() {
        Pile pile = new Pile();
        Pile pile2 = new Pile();
        addCardHelper(pile);
        addCardHelper(pile2);
        pile.add(pile2);
        assertEquals(80, pile.size());
    }


    @DisplayName("Tests the cards split method for non equal cards between players")
    @Test
    public void splitWorkingNonEqualPlayerCards() {
        Pile pile = new Pile();
        addCardHelper(pile);
        pile.add(c0);
        pile.add(c1);
        ArrayList<Pile> list = pile.split(4);
        assertEquals(5, list.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(10, list.get(i).size());
        }
        assertEquals(2, list.get(4).size());
    }

    @DisplayName("Tests the cards split method for equal cards between the players")
    @Test
    public void splitWorkingEqualPlayerCards() {
        Pile pile = new Pile();
        addCardHelper(pile);
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
        addCardHelper(pile);
        String toString = pile.toString();
        assertTrue(toString.startsWith("-------- START OF PILE -------- \n")
                && toString.endsWith("-------- END OF PILE -------- \n"));
    }
}


