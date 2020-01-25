package commandline.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.function.Predicate;

public class CommandLineView {

    final public static String GLOBAL_COMMAND_DISPLAY_STRING = "Global Commands:";
    final public static String USER_PROMPT = ">> ";

    // A tree set which orders commands alphabetically.
    private Collection<GlobalCommand> globalCommands = new TreeSet<GlobalCommand>();

    private Scanner scanner;
    private PrintStream printStream;

    /**
     * An object which allows high level interfacing with the i/o streams.
     * 
     * @param inputStream
     * @param printStream
     */
    CommandLineView(InputStream inputStream, PrintStream printStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    /**
     * 
     * An object which allows high level interfacing with the i/o streams. Defaults to System.in and
     * System.out
     */
    CommandLineView() {
        this(System.in, System.out);
    }

    /**
     * Displays a message to the user. Always finishes with a new line.
     * 
     * @param message
     */
    public void displayMessage(String message) {
        printStream.println(message);
    }

    /**
     * Displays a bullet point list to the user.
     * 
     * <pre>
     * {@code
        public class Test {
            CommandLineView view = new CommandLineView();
            List<Integer> list = Arrays.asList(new Integer[] {1, 2, 3});
            view.displayBulletList(list);
        }
    
        // OUTPUTS:
        // > 1
        // > 2
        // > 3
     * }
     * </pre>
     * 
     * @param colllection
     */
    public <T> void displayBulletList(Collection<T> collection) {
        ListUtility listMessage = new ListUtility(collection);
        printStream.print(listMessage.getBulletList());
    }

    /**
     * Displays an indented list to the user.
     * 
     * <pre>
     * {@code
        public class Test {
            CommandLineView view = new CommandLineView();
            List<Integer> list = Arrays.asList(new Integer[] {1, 2, 3});
            view.displayMessage("See the list below");
            view.displayIndentedList(list);
        }
    
        // OUTPUTS:
        // See the list below:
        //     1
        //     2
        //     3
     * }
     * </pre>
     * 
     * @param col
     */
    public <T> void displayIndentedList(Collection<T> col) {
        ListUtility listMessage = new ListUtility(col);
        printStream.print(listMessage.getIndentedList());
    }

    /**
     * Prompts the user for input and returns it as a trimmed String.
     * 
     * @return The user input string
     */
    public String getUserInput() {
        printStream.print(USER_PROMPT);

        String input = scanner.nextLine().trim();

        GlobalCommand matchingGlobalCommand = getMatchingGlobalCommand(input);
        // If the input is a global command, notify any listeners.
        while (matchingGlobalCommand != null) {
            matchingGlobalCommand.notifyCommandListeners();
            input = getUserInput();
            matchingGlobalCommand = getMatchingGlobalCommand(input);
        }

        return input;
    }

    /**
     * Prompts the user for input, displaying the USER_PROMPT (>>). The user's input is passed to
     * the errorCheck function. If it passes, their input is returned. Otherwise, the user is
     * displayed an error message and prompted for input again.
     * 
     * @param errorCheck   A lambda function for acceptable input.
     * @param errorMessage The message displayed to the user
     * @return The user input.
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
     * Outputs an enumerated list of items to the user for selection.
     * 
     * @param list A List item
     * @return The list index from the user choice.
     */
    public <T> int getUserSelectionIndex(List<T> list) {
        int size = list.size();
        if (size < 1) {
            throw new IllegalArgumentException("List must have a size of at least 1");
        }
        ListUtility listUtility = new ListUtility(list);
        printStream.print(listUtility.getEnumeratedList());

        String indexString = getUserInput(str -> {
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

        }, "Enter a number between 1-" + size + ":");

        return Integer.parseInt(indexString) - 1;
    }

    /**
     * Outputs an enumerated list of items to the user for selection.
     * 
     * @param list a List item
     * @return the selected item
     */
    public <T> T getUserSelection(List<T> list) {
        return list.get(getUserSelectionIndex(list));
    }

    // Global Command Functionality

    /**
     * Add a GlobalCommand which can notify its GlobalCommandListeners when it is entered in the
     * command prompt, for short circuiting game flow.
     * 
     * @param command
     * @param description a description of the command's functionality
     * @return
     */
    public boolean addGlobalCommand(GlobalCommand gc) {
        return globalCommands.add(gc);
    }

    /**
     * Removes a GlobalCommand.
     * 
     * @param command
     * @param description a description of the command's functionality
     * @return
     */
    public boolean removeGlobalCommand(GlobalCommand gc) {
        return globalCommands.remove(gc);
    }

    /**
     * Outputs any global commands in a menu form for the user with their descriptions.
     */
    public Iterator<GlobalCommand> getGlobalCommandIterator() {
        return globalCommands.iterator();
    }

    /**
     * Searches the currently held global commands for a command and returns it if it exists,
     * otherwise it returns null.
     * 
     * @param command the string of the command to search for
     * @return the GlobalCommand object, otherwise null
     */
    private GlobalCommand getMatchingGlobalCommand(String command) {
        GlobalCommand glob = globalCommands.stream().filter(gc -> gc.getCommand().equals(command))
                .findAny().orElse(null);

        return glob;
    }

}
