package commandline.view;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandLineViewTest {

    // Setup I/O for each test
    // ------------------------

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // Helper to provide mock input
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }
    // --- End Setup ----

    @DisplayName("display...() methods")
    @Nested
    public class DisplayMethods {

        @DisplayName("displayMessage outputs string with new line")
        @Test
        public void displayMessageOutputsNewLine() {
            CommandLineView view = new CommandLineView();
            view.displayMessage("Output message");
            assertEquals("Output message\n", outContent.toString());
        }

        @DisplayName("display...List finishes with just one new line")
        @Test
        public void displayListOutputsWithOnlyOneNewLine() {
            CommandLineView view = new CommandLineView();

            List<String> testList = Arrays.asList(new String[] {"a", "b", "c"});

            view.displayBulletList(testList);
            assertFalse(outContent.toString().endsWith("\n\n"));

            view.displayIndentedList(testList);
            assertFalse(outContent.toString().endsWith("\n\n"));

        }

        @DisplayName("displayDivider seperates messages with a new line")
        @Test
        public void displayDividerSeperatesWithNewLine() {
            String expectedOutput =
                    "first\n" + CommandLineView.DEFAULT_MESSAGE_DIVIDER + "\nsecond\n";
            CommandLineView view = new CommandLineView();
            view.displayMessage("first");
            view.displayDivider();
            view.displayMessage("second");
            assertEquals(expectedOutput, outContent.toString());
        }
    }

    @DisplayName("getUserInput()")
    @Nested
    public class GetUserInput {

        @DisplayName("Returns input without new line")
        @Test
        public void returnsInputString() {
            provideInput("This is the user input\n");

            CommandLineView view = new CommandLineView();
            assertEquals("This is the user input", view.getUserInput());
        }

        @DisplayName("Trims input of white space")
        @Test
        public void trimsWhitespace() {
            provideInput(" Test   \n");

            CommandLineView view = new CommandLineView();
            assertEquals("Test", view.getUserInput());
        }

        @DisplayName("Returns input which passes acceptance function")
        @Test
        public void passesAcceptanceFunction() {
            provideInput("test\n");

            CommandLineView view = new CommandLineView();
            // Lambda function checks for 'one word'.
            assertEquals("test", view.getUserInput(x -> x.matches("^\\w+$")));
        }

        @DisplayName("Reprompts user when input fails acceptance function")
        @Test
        public void repromptsWhenFailsAcceptanceFunction() {
            provideInput("test test\n21\n\n");

            CommandLineView view = new CommandLineView();
            // Lambda function checks for 'empty'
            assertEquals("", view.getUserInput(x -> "".matches(x)));
        }

        @DisplayName("Displays provided error message if it fails error condition")
        @Test
        public void displaysMessageIfInputFailsErrorCondition() {
            provideInput("test test\ntest\n");

            CommandLineView view = new CommandLineView();
            // Lambda function checks for 'one word'
            view.getUserInput(x -> x.matches("^\\w+$"), "Does not match one word.");
            assertTrue(outContent.toString().contains("Does not match one word.\n"));
        }
    }
    @DisplayName("getUserSelection() / getUserSelectionIndex()")
    @Nested
    public class GetUserSelection {

        @DisplayName("Fails when passed an empty list")
        @Test
        public void failsWhenPassedEmptyList() {
            List<String> list = Arrays.asList(new String[] {});
            CommandLineView view = new CommandLineView();
            assertThrows(IllegalArgumentException.class, () -> view.getUserSelectionIndex(list));
        }

        @DisplayName("Returns list index from user selection")
        @Test
        public void returnsListIndexFromUserSelection() {
            provideInput("3\n");

            List<Integer> list = Arrays.asList(new Integer[] {1, 2, 3, 4});
            CommandLineView view = new CommandLineView();
            // Note, user selection is i+1
            assertEquals(2, view.getUserSelectionIndex(list));
        }

        @DisplayName("Repeats prompt until valid range index is given")
        @ParameterizedTest
        @ValueSource(strings = {"5\n", "\n", "cow\n", "0\n", "-1\n"})
        public void repeatsIfGivenInvalidRange(String invalidInput) {
            provideInput(invalidInput + "1\n"); // Add valid input for 2nd prompt

            List<Integer> list = Arrays.asList(new Integer[] {1, 2, 3, 4});
            CommandLineView view = new CommandLineView();
            assertEquals(1, view.getUserSelection(list));
        }

        @DisplayName("Displays error message on invalid user input")
        @ParameterizedTest
        // This test gets its provider from the source below. This allows different arrays to be
        // tested (to check the length in the message) is correct.
        @MethodSource("commandline.view.ArrayProviderTest#stringArrayProvider")
        public void displaysErrorOnInvalidInput(String[] input) {

            provideInput("wrong\n" + "1\n"); // 1 is always a valid value
            String expectedErrorMessage = "Enter a number between 1-" + input.length + ".\n";

            CommandLineView view = new CommandLineView();
            view.getUserSelectionIndex(Arrays.asList(input)); // Convert array to list.
            assertTrue(outContent.toString().contains(expectedErrorMessage));
        }
    }

    @DisplayName("Global commands")
    @Nested
    public class GlobalCommands {

        @DisplayName("Throws for repeated global commands")
        @Test
        public void doesNotAcceptRepeatedGlobalCommands() {

            GlobalCommand gc1 = new GlobalCommand("quit");
            GlobalCommand gc2 = new GlobalCommand("quit", "I should throw");

            CommandLineView view = new CommandLineView();
            view.addGlobalCommand(gc1);

            assertThrows(IllegalArgumentException.class, () -> view.addGlobalCommand(gc2));
        }

        // Private class for test below

        @DisplayName("Inputing a global command notifies listeners")
        @Test
        public void globalCommandInputNotifiesListeners() {
            provideInput("quit\n\n");

            GlobalCommandListenerTest gcl = new GlobalCommandListenerTest();
            GlobalCommand gc = new GlobalCommand("quit");
            gc.addCommandListener(gcl);

            CommandLineView view = new CommandLineView();
            view.addGlobalCommand(gc);
            view.getUserInput();

            assertTrue(gcl.triggered);
        }

        @DisplayName("Not inputting a global command does not notify listeners")
        @Test
        public void noGlobalCommandDoesNotAffectListeners() {
            provideInput("x\n\n");

            GlobalCommandListenerTest gcl = new GlobalCommandListenerTest();
            GlobalCommand gc = new GlobalCommand("quit");
            gc.addCommandListener(gcl);

            CommandLineView view = new CommandLineView();
            view.addGlobalCommand(gc);
            view.getUserInput();

            assertFalse(gcl.triggered);
        }

    }
}
