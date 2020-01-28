package commandline;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import model.Player;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	private static final String LOGGER_FILENAME = "toptrumps.log";
	private static final String LOGGER_DIRECTORY = "./";

	private static final int LOGGER_IO_ERROR_CODE = 1;
	private static final String LOGGER_IO_ERROR =
			"Sorry, there is a problem writing your log file to disk. "
					+ "Try again later or run without logging.";

	private static final int LOGGER_SECURITY_ERROR_CODE = 2;
	private static final String LOGGER_SECURITY_ERROR =
			"Sorry, the logger has created a security violation. "
					+ "Try again later or run without logging.";

	private static final String LOGGER_DIVIDER_STRING = "--------";

	private static final Logger LOGGER = Logger.getLogger("commandline");
	private static Handler loggerHandler;

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

		if (writeGameLogsToFile) {

		}

		try {
			setupLogger();
		} catch (SecurityException e) { // Handle security errors gracefully
			System.out.println(LOGGER_SECURITY_ERROR);
			System.exit(LOGGER_SECURITY_ERROR_CODE);
		} catch (IOException e) { // Handle io errors gracefully
			System.out.println(LOGGER_IO_ERROR);
			System.exit(LOGGER_IO_ERROR_CODE);
		}

		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the
											// application

		// Loop until the user wants to exit the game
		while (!userWantsToQuit) {
			Logger.getLogger("commandline").info("This is a test log message");
			// ----------------------------------------------------
			// Add your game logic here based on the requirements
			// ----------------------------------------------------
			new Player("Gareth");
			userWantsToQuit = true; // use this when the user wants to exit the game

		}
	}

	private static void setupLogger() throws SecurityException, IOException {
		// Remove default logging output to System.err
		LogManager.getLogManager().reset();
		// Create a Logger object in main scope (so it isn't garbage collected).
		loggerHandler = new FileHandler(LOGGER_DIRECTORY + LOGGER_FILENAME);
		// The default format for messages is to append LOGGER_DIVIDER_STRING
		loggerHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				return String.format("%s\n%s\n", record.getMessage(), LOGGER_DIVIDER_STRING);
			}
		});

		LOGGER.addHandler(loggerHandler);
	}


}
