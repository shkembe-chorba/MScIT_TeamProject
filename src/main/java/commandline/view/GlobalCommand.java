package commandline.view;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class acts like a 'component' which can be added to a CommandLineView, much like a Button
 * can be added to a Swing Frame. Like a Button, you can attach listeners to it which are notified
 * when the command is triggered (by typing the command in the CommandLineView prompt).
 *
 * This allows certain commands to bypass usual program flow. For example, 'quit' terminating the
 * application.
 */
public class GlobalCommand implements Comparable<GlobalCommand> {

    private Collection<GlobalCommandListener> listeners = new ArrayList<GlobalCommandListener>();

    private String command;
    private String description;

    /**
     * Creates a global command.
     *
     * @param command     the String which activates a global command
     * @param description a description of the global command (potentially for help menus etc.)
     */
    public GlobalCommand(String command, String description) {
        if (!command.matches("^\\w+$")) {
            throw new IllegalArgumentException("Global commands must be one word strings");
        }
        this.command = command;
        this.description = description;
    }

    public GlobalCommand(String command) {
        this(command, "");
    }

    /**
     * @return the global command String
     */
    public String getCommand() {
        return command;
    }

    /**
     * Compares global commands by the command string.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlobalCommand) {
            GlobalCommand gc = (GlobalCommand) obj;
            return command.equals(gc.getCommand());
        } else {
            return false;
        }
    }

    /**
     * Compares global commands by the command string.
     */
    @Override
    public int compareTo(GlobalCommand gc) {
        return command.compareTo(gc.getCommand());
    }

    /**
     * Returns the global command and its description.
     */
    @Override
    public String toString() {
        String output = command;
        if (!"".equals(description)) {
            output += " - " + description;
        }
        return output;
    }

    /**
     * Registers a listener which is notified when a global command is entered.
     *
     * @param gcl a GlobalCommandListener
     */
    public void addCommandListener(GlobalCommandListener gcl) {
        listeners.add(gcl);
    }

    /**
     * Removes a global command listener.
     *
     * @param gcl a GlobalCommandListener
     */
    public void removeCommandListener(GlobalCommandListener gcl) {
        listeners.remove(gcl);
    }

    /**
     * Notify any listeners that a global command has been triggered.
     */
    public void notifyCommandListeners() {
        listeners.forEach(gcl -> gcl.globalCommandReceived(command));
    }

}


