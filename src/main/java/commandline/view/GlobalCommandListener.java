package commandline.view;

/**
 * An interface for a GlobalCommand listener which runs when the global command is entered in
 * CommandLineView
 */
public interface GlobalCommandListener {
    public void globalCommandReceived(String globalCommand);
}
