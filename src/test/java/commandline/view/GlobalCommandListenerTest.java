package commandline.view;

public class GlobalCommandListenerTest implements GlobalCommandListener {
    public boolean triggered = false;

    @Override
    public void globalCommandReceived(String globalCommand) {
        triggered = true;

    }
}
