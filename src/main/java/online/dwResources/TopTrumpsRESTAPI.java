package online.dwResources;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.Database;
import model.GameModel;
import model.RetrievedGameStatistics;
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

	@GET
	@Path("/resetGameModel")
	/**
	 * This method resets the game model with the given number of AI players.
	 * It returns the string "OK".
	 */
	public String resetGameModel(@QueryParam("NumAiPlayers") String numAiPlayers) {
		model.reset(Integer.parseInt(numAiPlayers));
		return "OK";
	}

	@GET
	@Path("/retrieveStats")
	/**
	 * This method returns the game statistics as a JSON string.
	 * Format :
	 * {
	 * "ai_wins": 5,
	 * "user_wins": 3,
	 * "avg_draws": 4,
	 * "tot_games_played": 7,
	 * "max_rounds": 8
	 * }
	 */
	public String retrieveStats() throws IOException {

		try {
			database.connect();
		} catch(SQLException e) {

		}
		RetrievedGameStatistics stats = database.retrieveGameStats();
		HashMap<String,Object> map = new HashMap<>();
		map.put("ai_wins",stats.getGamesWonByAi());
		map.put("user_wins",stats.getGamesWonByUser());
		map.put("avg_draws",stats.getAvgDraws());
		map.put("tot_games_played", stats.getTotalGamesPlayed());
		map.put("max_rounds", stats.getMaxRounds());

		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(map);

		database.disconnect();

		return listAsJSONString;
	}

	@GET
	@Path("/helloWord")
	/**
	 * Here is an example of how to read parameters provided in an HTML Get request.
	 * 
	 * @param Word - A word
	 * @return - A String
	 * @throws IOException
	 */
	public String helloWord(@QueryParam("Work") String Word) throws IOException {
		return "Hello " + Word;
	}

}
