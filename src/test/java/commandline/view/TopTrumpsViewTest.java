package commandline.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import commandline.controller.CliController;
import commandline.controller.TopTrumpsControllerInterface;
import model.Card;
import model.GameModel;
import model.Pile;
import model.Player;

public class TopTrumpsViewTest extends IOStreamTest {

    // Anonymous dummy controller for testing view
    private TopTrumpsControllerInterface mockController = new TopTrumpsControllerInterface() {

        @Override
        public void run() throws SQLException {
        }

        @Override
        public void quit() {
        }
    };

    private final String TEST_PLAYER_NAME = "Test Player";
    private final String TEST_CARD_NAME = "Test Card";

    @BeforeEach
    void setupTestItems() {

    }

    @DisplayName("Display functionality")
    @Nested
    class Display {

        @DisplayName("Displays a game start message")
        @Test
        void displaysGameStart() {

            TopTrumpsView view = new TopTrumpsView(mockController);
            view.displayGameStartMessage();

            assertEquals("Game Start\n", getOutput());
        }

        @DisplayName("Displays the round number")
        @ParameterizedTest
        @ValueSource(ints = {1, 4, 6, 2})
        void displaysRoundNumber(int val) {
            TopTrumpsView view = new TopTrumpsView(mockController);
            view.displayRoundNumber(val);

            String expectedOutput = "Round " + val + "\nPlayers have drawn their cards\n"
                    + CommandLineView.DEFAULT_MESSAGE_DIVIDER + "\n";

            assertEquals(expectedOutput, getOutput());
        }

        @DisplayName("Displays the player's name and their current card")
        @Test
        void displaysPlayerAndTheirCard() {

            // testPlayer.addToDeck(addedPile);


            // TopTrumpsView view = new TopTrumpsView(mockController);
            // view.displayTopCard(val);
        }


        @DisplayName("Logo is shown with quit command instructions")
        @Test
        void displayLogoTest() {

            TopTrumpsView view = new TopTrumpsView(mockController);
            view.displayLogo();

            String expectedOutput = "\n\n\n--------------------\n" + "--- Top Trumps   ---\n"
                    + "--------------------\n\n" + "To quit type in \"quit\" at any prompt.\n\n";

            assertEquals(expectedOutput, getOutput());
        }

        @DisplayName("Displays statistics from the database")
        void displayStatistics() {

        }

    }
}
