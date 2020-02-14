package online.dwResources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.*;
import online.configuration.TopTrumpsJSONConfiguration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/toptrumps") // Resources specified here should be hosted at http://localhost:7777/toptrumps
@Produces(MediaType.APPLICATION_JSON) // This resource returns JSON content
@Consumes(MediaType.APPLICATION_JSON) // This resource can take JSON content as input
/**
 * This is a Dropwizard Resource that specifies what to provide when a user requests a particular
 * URL. In this case, the URLs are associated to the different REST API methods that you will need
 * to expose the game commands to the Web page.
 *
 * Below are provided some sample methods that illustrate how to create REST API methods in
 * Dropwizard. You will need to replace these with methods that allow a TopTrumps game to be
 * controled from a Web page.
 */
public class TopTrumpsRESTAPI {

	/**
	 * A Jackson Object writer. It allows us to turn Java objects into JSON strings easily.
	 */
	private ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
	private static final String CWD = System.getProperty("user.dir");
	private Database database = new Database();
	private GameModel model;
	private String gameWinnerName = null;
	private boolean gameAutoCompleted = false;

	/**
	 * Constructor method for the REST API. This is called first. It provides a
	 * TopTrumpsJSONConfiguration from which you can get the location of the deck file and the
	 * number of AI players.
	 *
	 * @param conf
	 */
	public TopTrumpsRESTAPI(TopTrumpsJSONConfiguration conf) {
		String deckFileName = conf.getDeckFile();
		File deckFile = new File(CWD, deckFileName);
		model = new GameModel(deckFile.toString());
	}

	// ----------------------------------------------------
	// API methods
	// ----------------------------------------------------

	/**
	 * Initialises the game with the chosen number of AI players. Returns {loaded: true}.
	 *
	 * Must be called before a game begins.
	 *
	 * @param numAiPlayers chosen number of AI players.
	 * @return {loaded: true}
	 */
	@GET
	@Path("/initGame")
	public String initGame(@QueryParam("NumAiPlayers") String numAiPlayers) {
		model.reset(Integer.parseInt(numAiPlayers));
		return "{\"loaded\":true}";
	}

	/**
	 * Plays a round with the chosen attribute and auto completes the game if the user is eliminated
	 * and there is no winner. If there is a winner in the round or the game is auto completed this
	 * will be reflected in the gameWinnerName and gameAutoCompleted fields and the database will be
	 * updated. Returns a JSON string with information for playing a round with an attribute and
	 * possible game over information.
	 *
	 * Must be called after initRound().
	 *
	 * If the game is auto completed roundWinnerName and eliminatedPlayersNames correspond to
	 * information about the last round before the autocompletion, not the last round overall.
	 *
	 * roundWinnerName corresponds to null if there was a draw and it corresponds to the name of the
	 * round winner otherwise.
	 *
	 * gameOver being true does not necessarily mean that the game ended in the current round, the
	 * game could have been auto completed. That must be checked via gameAutoCompleted in
	 * getGameOverScores().
	 *
	 * EXAMPLE: { "roundWinnerName": "USER"/null, "gameOver": true, "eliminatedPlayersNames": [
	 * "AI1", "AI2"] }
	 *
	 * @param attributeName
	 * @return
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/playRoundWithAttribute")
	public String playRoundWithAttribute(@QueryParam("AttributeName") String attributeName)
			throws JsonProcessingException {
		// Initialize map that will be returned as a JSON file.
		HashMap<String, Object> map = new HashMap<>();
		Attribute selectedAttribute = new Attribute(attributeName, 0);
		Player roundWinner = model.playRoundWithAttribute(selectedAttribute);

		if (roundWinner != null) {
			map.put("roundWinnerName", roundWinner.toString());
		} else {
			map.put("roundWinnerName", null);
		}


		ArrayList<Player> eliminatedPlayers = model.checkToEliminate();
		ArrayList<String> eliminatedPlayersNames = new ArrayList<>();
		for (Player player : eliminatedPlayers) {
			eliminatedPlayersNames.add(player.toString());
		}
		map.put("eliminatedPlayersNames", eliminatedPlayersNames);

		boolean userInGame = model.userStillInGame();

		Player gameWinner = model.checkForWinner();
		if (gameWinner == null) {
			if (userInGame) {
				map.put("gameOver", false);
			} else {
				gameWinner = autoCompleteGame();
				gameOver(map, gameWinner);
				gameAutoCompleted = true;
			}
		} else {
			gameOver(map, gameWinner);
			gameAutoCompleted = false;
		}
		String mapAsJSONString = oWriter.writeValueAsString(map);
		return mapAsJSONString;
	}

	/**
	 * Returns the won rounds for every player during the game, the game winner name and whether the
	 * game auto completed.
	 *
	 * Must be called when a game has ended, i.e. there is a winner.
	 *
	 *
	 * 
	 * EXAMPLE: { "playerScores": [ { "name": "USER", "score": 15}, { name: "AI1", "score": 10}, ...
	 * ], "gameWinnerName": "USER", "gameAutoCompleted": true }
	 * 
	 * @return json string with playerScores, gameWinnerName and gameAutoCompleted as shown in the
	 *         example
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/getGameOverScores")
	public String getGameOverScores() throws JsonProcessingException {
		Player[] players = model.getPlayers();
		ArrayList<HashMap<String, Object>> playerList = new ArrayList<>();
		for (Player player : players) {
			HashMap<String, Object> playerMap = new HashMap<>();
			playerMap.put("name", player.toString());
			playerMap.put("score", player.getRoundsWon());
			playerList.add(playerMap);
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("playerScores", playerList);
		map.put("gameWinnerName", gameWinnerName);
		map.put("gameAutoCompleted", gameAutoCompleted);
		String mapAsJSONString = oWriter.writeValueAsString(map);
		return mapAsJSONString;
	}

	/**
	 * Makes the AI choose an attribute if it is active. Returns the information needed to
	 * initialise a round.
	 *
	 * Must be called at the beginning of a round.
	 *
	 * chosenAttributeName corresponds to null if the user is active and it corresponds to the
	 * attribute that the AI chooses otherwise.
	 *
	 * EXAMPLE: { "round": 1, "communalPileSize": 4, "chosenAttributeName": "strength"/null,
	 * "playersInGame" : [ { "name": "USER", "isAI": false, "isActive": true, "deckSize": 10,
	 * "topCard": { "name": "TRex", "attributes": [ { "name": "strength", "value": 5 } ] } } ] }
	 *
	 * @return JSON string, containing all info needed to initialise a round.
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/initRound")
	public String initRound() throws JsonProcessingException {
		// Map that will contain all relevant info to initialise a round.
		HashMap<String, Object> roundInfoMap = new HashMap<>();

		int roundNumber = model.getRoundNumber();
		int communalPileSize = model.getCommunalPileSize();
		Player activePlayer = model.getActivePlayer();
		ArrayList<Player> playersInGame = model.getPlayersInGame();
		Attribute aiChosenAttribute = null;
		if (activePlayer instanceof AIPlayer) {
			aiChosenAttribute = ((AIPlayer) activePlayer).chooseAttribute();
		}

		// uses playersInGameToMap() helper method that returns a List of Maps with info for all
		// players in game.
		roundInfoMap.put("playersInGame", playersInGameToMap(playersInGame, activePlayer));
		roundInfoMap.put("round", roundNumber);
		roundInfoMap.put("communalPileSize", communalPileSize);
		if (aiChosenAttribute != null) {
			roundInfoMap.put("chosenAttributeName", aiChosenAttribute.getName());
		} else {
			roundInfoMap.put("chosenAttributeName", null);
		}
		// converts the map to a JSON string.
		String mapAsJSONString = oWriter.writeValueAsString(roundInfoMap);
		return mapAsJSONString;
	}

	/**
	 * Returns the game statistics as a JSON string.
	 *
	 * Must be called when a player requests the game statistics.
	 *
	 * Format : { "aiWins": 5, "userWins": 3, "avgDraws": 4, "totGamesPlayed": 7, "maxRounds": 8 }
	 *
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/retrieveStats")
	public String retrieveStats() throws IOException {
		try {
			database.connect();
		} catch (SQLException e) {

		}
		RetrievedGameStatistics stats = database.retrieveGameStats();
		HashMap<String, Object> statsMap = new HashMap<>();
		statsMap.put("aiWins", stats.getGamesWonByAi());
		statsMap.put("userWins", stats.getGamesWonByUser());
		statsMap.put("avgDraws", stats.getAvgDraws());
		statsMap.put("totGamesPlayed", stats.getTotalGamesPlayed());
		statsMap.put("maxRounds", stats.getMaxRounds());

		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(statsMap);

		database.disconnect();

		return listAsJSONString;
	}

	/**
	 * Helper method. Returns an list of maps; Each map contains info about 1 player. Used in the
	 * initRound method.
	 *
	 * EXAMPLE with 1 player still in game, the list contains 1 map for the player: "USER".
	 * playersInGame : [ { "name": "USER", "isAI": false, "isActive": true, "deckSize": 10,
	 * "topCard": { "name": "TRex", "attributes": [ { "name": "strength", "value": 5 } ] } } ]
	 *
	 * @param playersInGame list of all players still in game (not eliminated)
	 * @param activePlayer  player who will be playing in this round.
	 * @return a list of maps. Each map contains info about 1 player.
	 */
	private ArrayList<HashMap<String, Object>> playersInGameToMap(ArrayList<Player> playersInGame,
			Player activePlayer) {
		ArrayList<HashMap<String, Object>> playersInGameMaps = new ArrayList<>();

		for (Player player : playersInGame) {
			HashMap<String, Object> playerMap = new HashMap<>();
			if (player instanceof AIPlayer) {
				playerMap.put("isAI", true);
			} else {
				playerMap.put("isAI", false);
			}
			if (player == activePlayer) {
				playerMap.put("isActive", true);
			} else {
				playerMap.put("isActive", false);
			}
			playerMap.put("name", player.toString());
			playerMap.put("deckSize", player.getRemainingDeckSize());
			playerMap.put("topCard", topCardToMap(player));

			playersInGameMaps.add(playerMap);
		}
		return playersInGameMaps;
	}

	/**
	 * Plays the game between AIs until there is a winner.
	 *
	 * @return game winner
	 */
	private Player autoCompleteGame() {
		Player gameWinner = null;
		while (gameWinner == null) {
			Player activePlayer = model.getActivePlayer();
			Attribute selectedAttribute = ((AIPlayer) activePlayer).chooseAttribute();
			model.playRoundWithAttribute(selectedAttribute);
			model.checkToEliminate();
			gameWinner = model.checkForWinner();
		}
		return gameWinner;
	}

	/**
	 * Helper method. Takes a map and adds to it {"gameOver": true}, uploads Game Statistics to the
	 * database and sets the gameWinnerName field to the name of the game winner.
	 *
	 * @param map
	 * @param gameWinner
	 */
	private void gameOver(HashMap<String, Object> map, Player gameWinner) {
		map.put("gameOver", true);
		gameWinnerName = gameWinner.toString();
		uploadGameStats(gameWinner);
	}

	/**
	 * Helper Method. Uploads Game Statistics to the database.
	 *
	 * @param gameWinner
	 */
	private void uploadGameStats(Player gameWinner) {
		try {
			database.connect();
		} catch (SQLException e) {

		}
		database.uploadGameStats(model.getDraws(), model.getRoundNumber(), gameWinner.toString(),
				model.getPlayers());
		database.disconnect();
	}

	/**
	 * Helper method. Takes a Player and returns a map for their topCard contents. Used in
	 * playersInGameToMap() EXAMPLE CONTENTS: topCard: { "name": "TRex", "attributes": [ { "name":
	 * "strength", "value": 5 } ] }
	 *
	 * @param player
	 * @return
	 */
	private HashMap<String, Object> topCardToMap(Player player) {
		HashMap<String, Object> topCardMap = new HashMap<>();
		topCardMap.put("name", player.peekCard().getName());

		ArrayList<HashMap<String, Object>> attributes = new ArrayList<>();
		for (Attribute attribute : player.peekCard().getAttributes()) {
			HashMap<String, Object> attributeMap = new HashMap<>();
			attributeMap.put("name", attribute.getName());
			attributeMap.put("value", attribute.getValue());
			attributes.add(attributeMap);
		}
		topCardMap.put("attributes", attributes);

		return topCardMap;
	}
}
