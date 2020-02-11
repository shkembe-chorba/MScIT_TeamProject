package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

/**
 * Pile tests
 */
@Nested
class GameModelTest {

    private static final String TEST_RESOURCE_DIRECTORY = "src/test/resources/commandline/model";

    // Our test deck is 2 cards
    private static final String CARD_DECK_2 =
            new File(TEST_RESOURCE_DIRECTORY, "cardDeckTwo.txt").toString();


    private static final String CARD_DECK_10 =
            new File(TEST_RESOURCE_DIRECTORY, "cardDeckTen.txt").toString();

    private static final String CARD_DECK_10_SAME_CARDS =
            new File(TEST_RESOURCE_DIRECTORY, "cardDeckTenDuplicates.txt").toString();

    private static final String CARD_DECK_40 =
            new File(TEST_RESOURCE_DIRECTORY, "cardDeckForty.txt").toString();


    @Test
    void test() {

    }

    /**
     * Validation Tests
     */
    @DisplayName("Constructor and Reset")
    @Nested
    public class ConstructorAndReset {
        @DisplayName("Reads in a deck and assigns cards to players and communal pile")
        @Test
        public void assignsCardsCorrectly() {
            int numAIPlayers = 2;
            GameModel model = new GameModel(CARD_DECK_10, numAIPlayers);
            List<Player> listPlayer = model.getPlayersInGame();

            for (Player player : listPlayer) {
                int totalHandSize = player.getRemainingDeckSize() + 1;
                assertEquals(3, totalHandSize, "Player deck sizes not as expected");
            }

            assertEquals(1, model.getCommunalPileSize(), "Communal pile size not as expected");
        }

        @DisplayName("Players assigned different cards")
        @Test
        public void assignsDifferentCards() {
            int numAIPlayers = 1;
            GameModel model = new GameModel(CARD_DECK_2, numAIPlayers);
            List<Player> listPlayer = model.getPlayersInGame();

            Player p1 = listPlayer.get(0);
            Player p2 = listPlayer.get(1);

            assertNotEquals(p1.peekCard().toString(), p2.peekCard().toString());
        }
    }

    @Nested
    public class CommunalPile {
        @DisplayName("Communal Pile increments after a draw")
        @Test
        void communalPileIncrementsAfterDraw() {
            int numAIPlayers = 1;
            GameModel model = new GameModel(CARD_DECK_10_SAME_CARDS, numAIPlayers);
            Attribute chosenAttribute = model.getActivePlayer().peekCard().getAttribute(0);
            model.playRoundWithAttribute(chosenAttribute);

            // Player plus AI card
            assertEquals(2, model.getCommunalPileSize());

            model.playRoundWithAttribute(chosenAttribute);
            // Player plus AI card x2
            assertEquals(4, model.getCommunalPileSize());
        }

        @DisplayName("Communal Pile does not increment when no draw is possible")
        @Test
        void communalPileDoesNotIncreaseAfterWin() {
            int numAIPlayers = 1;
            // Deck cards are guaranteed to have different attributes.
            GameModel model = new GameModel(CARD_DECK_2, numAIPlayers);
            Attribute chosenAttribute = model.getActivePlayer().peekCard().getAttribute(0);
            model.playRoundWithAttribute(chosenAttribute);
            assertEquals(0, model.getCommunalPileSize());
        }

        @DisplayName("Communal Pile empties after a draw.")
        @Test
        void communalPileResetAfterWin() {
            int numAIPlayers = 4;
            GameModel model = new GameModel(CARD_DECK_40, numAIPlayers);

            // PLAY THE GAME OVER AND OVER UNTIL A DRAW OCCURS
            Player activePlayer;
            Attribute chosenAttribute;

            // Continue playing until there is a draw
            while (model.getDraws() != 1) {
                activePlayer = model.getActivePlayer();
                chosenAttribute = activePlayer.peekCard().getAttribute(0);
                model.playRoundWithAttribute(chosenAttribute);

                if (model.checkForWinner() != null) {
                    model.reset();
                }
            }

            // After a draw, the communal pile should have cards.
            assertTrue(model.getCommunalPileSize() > 0);

            // Play until there is a win.
            do {
                activePlayer = model.getActivePlayer();
                chosenAttribute = activePlayer.peekCard().getAttribute(0);
                model.playRoundWithAttribute(chosenAttribute);
            } while (model.getDraws() != 1);

            // THEN ASSERT THAT THE COMMUNAL PILE SIZE IS 0
            assertEquals(0, model.getCommunalPileSize());
        }
    }

    @Nested
    public class Eliminations {


    }

}
