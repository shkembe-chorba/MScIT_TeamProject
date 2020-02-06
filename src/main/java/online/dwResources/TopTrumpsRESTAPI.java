package online.dwResources;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.*;
import online.configuration.TopTrumpsJSONConfiguration;

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
	private static final String DECK_READ_ERROR = "Could not load deck from file, please place in working directory.";
	private static final int DECK_READ_ERROR_CODE = 2;
	private static final int DATABASE_CONNECTION_ERROR_CODE = 3;
	private static final String CWD = System.getProperty("user.dir");
	private Database database = new Database();
	private GameModel model;
	private String deckFile;
	/**
	 * Contructor method for the REST API. This is called first. It provides a
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
	 * Initialises the game.
	 * @param numAiPlayers chosen number of AI players.
	 * @return "OK"
	 */
	@GET
	@Path("/initGame")
	public String initGame(@QueryParam("NumAiPlayers") String numAiPlayers) {
		model.reset(Integer.parseInt(numAiPlayers));
		return "OK";
	}

	/**
	 * Information needed to initialise a round.
	 * EXAMPLE:
	 * 	{
	 * 		"round": 1,
	 *		"communalPileSize": 4,
	 *		"chosenAttributeName": "strength"/"NA" , for an AI/USER
	 *		"playersInGame" : [
	 *			{
	 *				"name": "USER",
	 *				"isAI": false,
	 *				"isActive": true,
	 *				"deckSize": 10,
	 *				"topCard": {
	 *					"name": "TRex",
	 *					"attributes": [
	 *						{
	 *							"name": "strength",
	 *							"value": 5
	 *						}
	 *					]
	 *				}
	 *     		}
	 * 		]
	 * 	}
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
		if(activePlayer instanceof AIPlayer) {
			aiChosenAttribute = ((AIPlayer) activePlayer).chooseAttribute();
		}

		// uses playersInGameToMap() helper method that returns a List of Maps with info for all players in game.
		roundInfoMap.put("playersInGame", playersInGameToMap(playersInGame, activePlayer));
		roundInfoMap.put("round", roundNumber);
		roundInfoMap.put("communalPileSize", communalPileSize);
		if(aiChosenAttribute != null) {
			roundInfoMap.put("chosenAttributeName", aiChosenAttribute.getName());
		} else {
			roundInfoMap.put("chosenAttributeName", "NA");
		}
		// converts the map to a JSON string.
		String mapAsJSONString = oWriter.writeValueAsString(roundInfoMap);
		return mapAsJSONString;
	}

	/**
	 * Game statistics as a JSON string.
	 * Format :
	 * 	 	{
	 * 	  		"ai_wins": 5,
	 * 	  		"user_wins": 3,
	 * 	 		"avg_draws": 4,
	 * 	  		"tot_games_played": 7,
	 * 	  		"max_rounds": 8
	 * 	  	}
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/retrieveStats")
	public String retrieveStats() throws IOException {
		try {
			database.connect();
		} catch(SQLException e) {

		}
		RetrievedGameStatistics stats = database.retrieveGameStats();
		HashMap<String,Object> statsMap = new HashMap<>();
		statsMap.put("ai_wins",stats.getGamesWonByAi());
		statsMap.put("user_wins",stats.getGamesWonByUser());
		statsMap.put("avg_draws",stats.getAvgDraws());
		statsMap.put("tot_games_played", stats.getTotalGamesPlayed());
		statsMap.put("max_rounds", stats.getMaxRounds());

		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(statsMap);

		database.disconnect();

		return listAsJSONString;
	}

	/**
	 * Helper method. List of maps; Each map contains info about 1 player.
	 * Used in the initRound method.
	 *
	 * EXAMPLE with 1 player still in game, the list contains 1 map for the player: "USER".
	 * playersInGame : [
	 *		{
	 *			"name": "USER",
	 * 			"isAI": false,
	 *			"isActive": true,
	 *			"deckSize": 10,
	 *			"topCard": {
	 *				"name": "TRex",
	 *				"attributes": [
	 *					{
	 *						"name": "strength",
	 *						"value": 5
	 *					}
	 *				]
	 *			}
	 *		}
	 *	]
	 * @param playersInGame list of all players still in game (not eliminated)
	 * @param activePlayer player who will be playing in this round.
	 * @return a list of maps. Each map contains info about 1 player.
	 */
	private ArrayList<HashMap<String, Object>> playersInGameToMap(ArrayList<Player> playersInGame, Player activePlayer){
		ArrayList<HashMap<String, Object>> playersInGameMaps = new ArrayList<>();

		for(Player player: playersInGame) {
			HashMap<String, Object> playerMap = new HashMap<>();
			if(player instanceof AIPlayer) {
				playerMap.put("isAI", true);
			} else {
				playerMap.put("isAI", false);
			}
			if(player == activePlayer) {
				playerMap.put("isActive", true);
			} else {
				playerMap.put("isActive", false);
			}
			playerMap.put("name", player.toString());
			playerMap.put("deckSize", player.getDeckSize());
			playerMap.put("topCard", topCardToMap(player));

			playersInGameMaps.add(playerMap);
		}
		return playersInGameMaps;
	}

	/**
	 * Helper method. Takes a Player and returns a map for their topCard contents.
	 * Used in playersInGameToMap()
	 * EXAMPLE CONTENTS:
	 * topCard: {
	 * 	 		"name": "TRex",
	 * 	 		"attributes": [
	 * 	 					{
	 * 	 					"name": "strength",
	 * 	 					"value": 5
	 * 	 					}
	 * 	 				]
	 * 	 			}
	 * @param player
	 * @return
	 */
	private HashMap<String, Object> topCardToMap (Player player){
		HashMap<String, Object> topCardMap = new HashMap<>();
		topCardMap.put("name",player.peekCard().getName());

		ArrayList<HashMap<String, Object>> attributes = new ArrayList<>();
		for(Attribute attribute: player.peekCard().getAttributes()) {
			HashMap<String, Object> attributeMap = new HashMap<>();
			attributeMap.put("name", attribute.getName());
			attributeMap.put("value", attribute.getValue());
			attributes.add(attributeMap);
		}
		topCardMap.put("attributes", attributes);

		return topCardMap;
	}
}
