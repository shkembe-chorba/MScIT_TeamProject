package commandline.controller;

import java.sql.SQLException;

/**
 * An interface which can be implemented for dummy testing.
 */
public interface CliControllerInterface {
    public void run() throws SQLException;

    public void quit();
}