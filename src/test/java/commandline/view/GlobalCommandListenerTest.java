package commandline.view;

/**
 * A helper class that can be used to test for GlobalCommand notifications by exposing a 'triggered'
 * variable which displays true if .globalCommandReceived is called.
 */
public class GlobalCommandListenerTest implements GlobalCommandListener {
    public boolean triggered = false;

    @Override
    public void globalCommandReceived(String globalCommand) {
        triggered = true;

    }
}
