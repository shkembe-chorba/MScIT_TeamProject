package commandline.view;

import commandline.utils.ListUtility;

import java.io.InputStream;
import java.io.PrintStream;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * A class which encapsulates reading and writing from the IO streams and provides an API for simple
 * commandline user interfaces, such as displaying messages and fetching user input from a prompt.
 */
public class CommandLineView {

    // The prompt to display for user input.
    public static final String USER_PROMPT = ">> ";
    // The divider to print when displayDivider is called.
    public static final String DEFAULT_MESSAGE_DIVIDER = "---";

    // A set which includes any GlobalCommand objects. Used to implement an observer pattern where
    // the input of any global command will call its handler and 'short circuit' the usual flow of
    // control.
    private Collection<GlobalCommand> globalCommands = new HashSet<GlobalCommand>();

    private Scanner scanner;
    private PrintStream printStream;

    /**
     * Create a CommandLineView object which provides a high level api for displaying information to
     * the user and prompting them for input.
     *
     * @param inputStream
     * @param printStream
     */
    public CommandLineView(InputStream inputStream, PrintStream printStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    /**
     * As in {@link #CommandLineView(InputStream, PrintStream)}, but with the default System.in and
     * System.out IO streams.
     */
    public CommandLineView() {
        this(System.in, System.out);
    }

    /**
     * Outputs a message to the user. Always finishes with a new line.
     *
     * @param message The message to show the user.
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
     * Outputs an enumerated list of the .toString() method of each item in 'list'. Indicates the
     * selected index with an arrow: <code><--</code>
     *
     * @param list          the list of items whose .toString() methods will be displayed
     * @param selectedIndex the index of the selected item in the list
     */
    public <T> void displayEnumeratedSelection(List<T> list, int selectedIndex) {
        ListUtility lu = new ListUtility(list);
        printStream.print(lu.getEnumeratedList(selectedIndex));
    }

    /**
     * Outputs an enumerated list of the .toString() method of each item in 'list'.
     *
     * @param list the list of items whose .toString() methods will be displayed
     */
    public <T> void displayEnumeratedList(List<T> list) {
        displayEnumeratedSelection(list, -1);
    }

    /**
     * Outputs a bullet point list of the .toString() method of each item in 'list'. Indicates the
     * selected index with an arrow: <code><--</code>
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
     * selected index with an arrow: <code><--</code>
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
     * Prompts the user for input, displaying the USER_PROMPT (>>). If there are any global commands
     * added (using {@link #addGlobalCommand(GlobalCommand)}), then the input is checked to see if
     * it matches any of those commands. If it does, the GlobalCommand listeners are notified. The
     * user is then reprompted for new input.
     *
     * @return The user input, trimmed of whitespace
     */
    public String getUserInput() {
        printStream.print(USER_PROMPT);

        String input = scanner.nextLine().trim();

        // Check the input to see if a GlobalCommand has been entered (i.e. 'quit')
        GlobalCommand matchingGlobalCommand = getMatchingGlobalCommand(input);
        // If the input is a global command, notify any listeners.
        while (matchingGlobalCommand != null) {
            matchingGlobalCommand.notifyCommandListeners();
            // Reprompt for next input (which may be another global command...)
            input = getUserInput();
            matchingGlobalCommand = getMatchingGlobalCommand(input);
        }

        return input;
    }

    /**
     * Prompts the user for input, displaying the USER_PROMPT (>>). If there are any global commands
     * added (using {@link #addGlobalCommand(GlobalCommand)}), then the input is checked to see if
     * it matches any of those commands.
     *
     * The user's input is also passed to the errorCheck function. If it passes, their input is
     * returned. Otherwise, the user is displayed an error message and prompted for input again.
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
     * As with {@link #getUserInput(Predicate, String), but no error message is displayed to the
     * user.
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

        String userInstruction = "Enter a number between 1-" + size + ".";
        // Display list and prompt to the user
        displayEnumeratedList(list);
        displayMessage(userInstruction);

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
     * As with {@link #getUserSelectionIndex(List)}, but directly returns the indexed object rather
     * than the index.
     *
     * @param list The list of possible user choices
     * @return The list object selected
     */
    public <T> T getUserSelection(List<T> list) {
        return list.get(getUserSelectionIndex(list));
    }

    // GLOBAL COMMAND FUNCTIONALITY

    /**
     * Add a GlobalCommand which can notify its GlobalCommandListeners when it is entered in the
     * command prompt, for short circuiting game flow.
     *
     * @param gc The global command, whose listener will be notified when it is entered.
     * @throws IllegalArgumentException if the global command already exists
     */
    public void addGlobalCommand(GlobalCommand gc) {
        if (!globalCommands.add(gc)) {
            throw new IllegalArgumentException(
                    "Cannot have two equal global commands. Remove the existing command.");
        }
    }

    /**
     * Removes a GlobalCommand.
     *
     * @param gc The GlobalCommand object to be removed.
     * @return
     */
    public boolean removeGlobalCommand(GlobalCommand gc) {
        return globalCommands.remove(gc);
    }

    /**
     * Searches the currently held global commands for a command and returns it if it exists,
     * otherwise it returns null.
     *
     * @param command the string of the command to search for
     * @return the GlobalCommand object, otherwise null
     */
    private GlobalCommand getMatchingGlobalCommand(String command) {
        return globalCommands.stream().filter(gc -> gc.getCommand().equals(command)).findAny()
                .orElse(null);
    }

}
