package commandline.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import commandline.utils.ListUtility;

public class CommandLineView {

    public static final String USER_PROMPT = ">> ";
    public static final String DEFAULT_MESSAGE_DIVIDER = "---";

    private Scanner scanner;
    private PrintStream printStream;

    /**
     * An object which allows high level interfacing with the i/o streams.
     * 
     * @param inputStream
     * @param printStream
     */
    public CommandLineView(InputStream inputStream, PrintStream printStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    public CommandLineView() {
        this(System.in, System.out);
    }

    /**
     * Outputs a message to the user. Always finishes with a new line.
     * 
     * @param message
     */
    public void displayMessage(String message) {
        printStream.println(message);
    }

    /**
     * Outputs a divider for seperating message blocks.
     */
    public void displayDivider() {
        displayMessage(DEFAULT_MESSAGE_DIVIDER);
    }

    /**
     * Outputs a bullet point list of the .toString() method of each item in 'list'. Indicates the
     * selected index with an arrow: <--
     * 
     * @param list          the list of items whose .toString() methods will be displayed
     * @param selectedIndex the index of the selected item in the list
     */
    public <T> void displayBulletSelection(List<T> list, int selectedIndex) {
        ListUtility lu = new ListUtility(list);
        printStream.print(lu.getBulletList(selectedIndex));
    }

    /**
     * Outputs a bullet point list of the .toString() method of each item in 'list'.
     * 
     * @param list the list of items whose .toString() methods will be displayed
     */
    public <T> void displayBulletList(List<T> list) {
        displayBulletSelection(list, -1);
    }

    /**
     * Outputs an indented list of the .toString() method of each item in 'list'. Indicates the
     * selected index with an arrow: <--
     * 
     * @param list          the list of items whose .toString() methods will be displayed
     * @param selectedIndex the index of the selected item in the list
     */
    public <T> void displayIndentedSelection(List<T> list, int selectedIndex) {
        ListUtility lu = new ListUtility(list);
        printStream.print(lu.getIndentedList(selectedIndex));
    }

    /**
     * Outputs an indented point list of the .toString() method of each item in 'list'.
     * 
     * @param list the list of items whose .toString() methods will be displayed
     */
    public <T> void displayIndentedList(List<T> list) {
        displayIndentedSelection(list, -1);
    }

    /**
     * Prompts the user for input and returns it as a trimmed String.
     * 
     * @return The user input, trimmed of whitespace
     */
    public String getUserInput() {
        printStream.print(USER_PROMPT);
        return scanner.nextLine().trim();
    }

    /**
     * Prompts the user for input, displaying the USER_PROMPT (>>). The user's input is passed to
     * the errorCheck function. If it passes, their input is returned. Otherwise, the user is
     * displayed an error message and prompted for input again.
     * 
     * @param errorCheck   A lambda function for acceptable input.
     * @param errorMessage The error message displayed to the user
     * @return The user input, trimmed of whitespace
     * 
     */
    public String getUserInput(Predicate<String> errorCheck, String errorMessage) {
        String input = getUserInput();

        // Invalid input
        while (!errorCheck.test(input)) {
            if (errorMessage != null) {
                displayMessage(errorMessage);
            }
            input = getUserInput();
        }
        return input;
    }

    /**
     * Prompts the user for input, displaying the USER_PROMPT (>>). The user's input is passed to
     * the errorCheck function. If it passes, their input is returned. Otherwise, the user is
     * prompted for input again.
     * 
     * @param errorCheck
     * @return The user input.
     */
    public String getUserInput(Predicate<String> errorCheck) {
        return getUserInput(errorCheck, null);
    }

    /**
     * Displays an enumerated list of items to the user for selection, prompting them for a choice.
     * 
     * @param list The list of possible user choices
     * @return The list index from the user choice
     */
    public <T> int getUserSelectionIndex(List<T> list) {
        int size = list.size();
        // Check for valid list
        if (size < 1) {
            throw new IllegalArgumentException("List must have a size of at least 1");
        }
        String userInstruction = "Enter a number between 1-" + size + ":";
        String choiceList = new ListUtility(list).getEnumeratedList();

        // Display options to user with an instruction
        printStream.print(choiceList);
        printStream.println(userInstruction);

        String indexString = getUserInput(str -> {
            // Check the input to see if it's a valid range.
            try {
                int index = Integer.parseInt(str);
                if (index > 0 && index <= size) {
                    return true;
                } else {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
            // Reprint userInstruction and reprompt user if this fails.
        }, userInstruction);

        // Return the index (the user selection - 1)
        return Integer.parseInt(indexString) - 1;
    }

    /**
     * Displays an enumerated list of items to the user for selection, prompting them for a choice.
     * A convenience method for directly returning the object
     * 
     * @param list The list of possible user choices
     * @return The list object selected
     */
    public <T> T getUserSelection(List<T> list) {
        return list.get(getUserSelectionIndex(list));
    }

}
