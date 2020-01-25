package commandline.view;

import java.util.ArrayList;
import java.util.Collection;

public class GlobalCommand implements Comparable<GlobalCommand> {

    private Collection<GlobalCommandListener> listeners = new ArrayList<GlobalCommandListener>();

    private String command;
    private String description;

    public GlobalCommand(String command) {
        this(command, "");
    }

    public GlobalCommand(String command, String description) {
        if (!command.matches("^\\w+$")) {
            throw new IllegalArgumentException("Global commands must be one word strings");
        }
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlobalCommand) {
            GlobalCommand gc = (GlobalCommand) obj;
            return command.equals(gc.getCommand());
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(GlobalCommand gc) {
        return command.compareTo(gc.getCommand());
    }

    @Override
    public String toString() {
        String output = command;
        if (!description.equals("")) {
            output += " - " + description;
        }
        return output;
    }

    /**
     * Registers a listener which is notified when a global command is entered.
     * 
     * @param gcl
     */
    public void addCommandListener(GlobalCommandListener gcl) {
        listeners.add(gcl);
    }

    /**
     * Removes a global command listener.
     * 
     * @param gcl
     */
    public void removeCommandListener(GlobalCommandListener gcl) {
        listeners.remove(gcl);
    }

    // This notifies any global command listeners when a global command is entered
    public void notifyCommandListeners() {
        listeners.forEach(gcl -> gcl.globalCommandReceived(command));
    }

}


