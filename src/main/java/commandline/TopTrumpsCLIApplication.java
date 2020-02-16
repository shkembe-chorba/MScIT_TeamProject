package commandline;

import com.google.gson.JsonObject;

import commandline.controller.CliController;
import commandline.utils.JsonUtility;
import commandline.utils.Logger;
import commandline.view.TopTrumpsView;

import model.GameModel;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	private static final String JSON_CONFIG_NAME = "TopTrumps.json";
	private static final String DECK_READ_ERROR =
			"Could not load deck from file, please place in working directory.";
	private static final int DECK_READ_ERROR_CODE = 2;
	private static final int DATABASE_CONNECTION_ERROR_CODE = 3;

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

		// -------------
		// Logger Setup
		// -------------

		if (args[0].equalsIgnoreCase("true")) {
			writeGameLogsToFile = true; // Command line selection
		}

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

		// ----------
		// Get deck file from json config
		// ----------

		JsonObject jsonConfig = null;
		String deckFile = null;
		int numAIPlayers = 4;

		try {
			jsonConfig = JsonUtility.getJsonObjectFromFile(JSON_CONFIG_NAME);
			deckFile = jsonConfig.get("deckFile").getAsString();
			numAIPlayers = jsonConfig.get("numAIPlayers").getAsInt();

		} catch (IOException e) {
			System.err.println(DECK_READ_ERROR);
			System.exit(DECK_READ_ERROR_CODE);
		}

		// Setup MVC
		// ---------

		GameModel model = new GameModel(deckFile, numAIPlayers);
		CliController controller = new CliController(model);
		TopTrumpsView view = new TopTrumpsView(controller);
		controller.setView(view);

		// Initialise Application
		// ----------------------
		try {
			controller.run();
		} catch (SQLException e) {
			System.err.println("Database connection failure.");
			e.printStackTrace();
			System.exit(DATABASE_CONNECTION_ERROR_CODE);
		}

	}

	/**
	 * A helper method for displaying IO errors for the logger.
	 */
	private static void displayLoggerError(String message) {
		System.out.println(message);
		System.out.println("Please try again later or run without logging.");
	}

}
