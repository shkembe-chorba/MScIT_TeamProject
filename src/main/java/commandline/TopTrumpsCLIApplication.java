package commandline;

import java.io.IOException;
import commandline.utils.Logger;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	private static final String LOGGER_DIRECTORY = "./";
	private static final String LOGGER_FILENAME = "TopTrumps.log";
	private static final String LOGGER_SECURITY_ERROR =
			"There is a security error with the logger.";
	private static final int LOGGER_SECURITY_ERROR_CODE = 1;
	private static final String LOGGER_IO_ERROR = "There is a file system error with the logger.";
	private static final int LOGGER_IO_ERROR_CODE = 1;

	/**
	 * This main method is called by TopTrumps.java when the user specifies that they want to run in
	 * command line mode. The contents of args[0] is whether we should write game logs to a file.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		boolean writeGameLogsToFile = false; // Should we write game logs to file?

		if (args[0].equalsIgnoreCase("true")) {
			writeGameLogsToFile = true; // Command line selection
		}

		// -------------
		// Logger Setup
		// -------------

		Logger logger = new Logger(LOGGER_DIRECTORY + LOGGER_FILENAME);

		if (writeGameLogsToFile) {
			try {
				// Creates a file handler and attaches it to the logger
				logger.enable();
			} catch (SecurityException e) { // Exit and Handle security errors gracefully
				displayLoggerError(LOGGER_SECURITY_ERROR);
				System.exit(LOGGER_SECURITY_ERROR_CODE);
			} catch (IOException e) { // Exit and Handle io errors gracefully
				displayLoggerError(LOGGER_IO_ERROR);
				System.exit(LOGGER_IO_ERROR_CODE);
			}
		}

		// End Logger Setup
		// ----------------

		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the
											// application

		// Loop until the user wants to exit the game
		while (!userWantsToQuit) {

			// ----------------------------------------------------
			// Add your game logic here based on the requirements
			// ----------------------------------------------------
			userWantsToQuit = true; // use this when the user wants to exit the game

		}
	}

	private static void displayLoggerError(String message) {
		System.out.println(message);
		System.out.println("Please try again later or run without logging.");
	}

}
