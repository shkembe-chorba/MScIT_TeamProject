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
        for(int i = 0; i < 40; i++) {
            pile.add(new Card(""+i));
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



    @DisplayName("Tests the cards split method for player 1")
    @Test
    public void splitWorking() {
        Pile pile = new Pile();
        int expectedInt = 10;
        int actualInt = pile.split(4).get(0).size();
        assertEquals(expectedInt, actualInt);
    }

    @DisplayName("Tests the cards split method for equal cards between the players")
    @Test
    public void splitWorkingEqualPlayerCards() {
        Pile pile = new Pile();
        addCardHelper(pile);
        ArrayList<Pile> list = pile.split(4);
        assertEquals(5, list.size());
        for(int i = 0; i < 4; i++) {
            assertEquals(10, list.get(i).size());
        }
        assertEquals(0, list.get(4).size());
    }

    @DisplayName("Tests the cards split method for equal cards between the players")
    @Test
    public void splitWorkingEqualExtraCards() {
        Pile pile = new Pile();
        int expectedInt = 2;
        int actualInt = pile.split(5).get(5).size();
        assertEquals(expectedInt, actualInt);
    }
}
