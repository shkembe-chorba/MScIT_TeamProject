package commandline.controller;

/**
 * An interface which designates the methods that the controller needs to implement for
 * functionality with other components.
 */
public interface TopTrumpsControllerInterface {
    /**
     * Runs the main application loop when called.
     */
    public void run();

    /**
     * Quits the application when called.
     */
    public void quit();
}
