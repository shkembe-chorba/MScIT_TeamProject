package commandline;

import java.io.File;
import commandline.controller.CliController;
import commandline.view.TopTrumpsView;
import model.GameModel;
import model.factories.PlayerFactory;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	private static final String JSON_CONFIG_NAME = "TopTrumps.json";

	/**
	 * This main method is called by TopTrumps.java when the user specifies that they want to run in
	 * command line mode. The contents of args[0] is whether we should write game logs to a file.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		final File path = new File(System.getProperty("user.dir"), JSON_CONFIG_NAME);


		boolean writeGameLogsToFile = false; // Should we write game logs to file?
		if (args[0].equalsIgnoreCase("true"))
			writeGameLogsToFile = true; // Command line selection

		GameModel model = new GameModel(path.toString(), new PlayerFactory());
		CliController controller = new CliController(model);
		TopTrumpsView view = new TopTrumpsView(controller);
		controller.setView(view);
		controller.run();

		// THE BELOW LOOP HAS BEEN MOVED TO THE CONTROLLER

		// // This will be moved to the controller.
		// // // Loop until the user wants to exit the game
		// while (true) {

		// // ----------------------------------------------------
		// // Add your game logic here based on the requirements
		// // ----------------------------------------------------

		// }



	}

}
