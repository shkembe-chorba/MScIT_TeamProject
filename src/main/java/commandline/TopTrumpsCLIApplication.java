package commandline;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	/**
	 * This main method is called by TopTrumps.java when the user specifies that they want to run in
	 * command line mode. The contents of args[0] is whether we should write game logs to a file.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		boolean writeGameLogsToFile = false; // Should we write game logs to file?
		if (args[0].equalsIgnoreCase("true"))
			writeGameLogsToFile = true; // Command line selection

		TopTrumpsModel model = new TopTrumpsModel();
		TopTrumpsController controller = new TopTrumpsController(model, writeGameLogsToFile);
		TopTrumpsView view = new TopTrumpsView(controller);
		controller.addView(view);

		conroller.run();

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
