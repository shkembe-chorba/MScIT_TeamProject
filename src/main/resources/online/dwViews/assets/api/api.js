/**
 * API.js
 *
 * A module that allows encapsulation of the API calls for consumption in
 * other javascript files. Allows any modifications to be isolated to this
 * file so backend / frontend devs can work from 'one source of truth'.
 */

// API PATHS
const URL_GET_STATISTICS = "http://localhost:7777/toptrumps/retrieveStats";
const URL_INIT_ROUND = "http://localhost:7777/toptrumps/initRound";
const URL_INIT_GAME = "http://localhost:7777/toptrumps/initGame";
const URL_GAME_OVER_SCORES = "http://localhost:7777/toptrumps/getGameOverScores";
const URL_PLAY_ROUND_WITH_ATTRIBUTE = "http://localhost:7777/toptrumps/playRoundWithAttribute";

// CORS REQUEST HELPER FUNCTIONS
// -----------------------------

function createCORSRequest(method, url) {
  let xhr = new XMLHttpRequest();
  if ("withCredentials" in xhr) {
    // Check if the XMLHttpRequest object has a "withCredentials" property.
    // "withCredentials" only exists on XMLHTTPRequest2 objects.
    xhr.open(method, url, true);
  } else if (typeof XDomainRequest != "undefined") {
    // Otherwise, check if XDomainRequest.
    // XDomainRequest only exists in IE, and is IE's way of making CORS requests.
    xhr = new XDomainRequest();
    xhr.open(method, url);
  } else {
    // Otherwise, CORS is not supported by the browser.
    xhr = null;
  }
  return xhr;
}

// Don't need to touch this, it's DRY :D
function apiGet(url, callback, paramDictionary) {
  // Add the contents of the parameter dictionary to the end of the URL
  let requestString = "";

  Object.keys(paramDictionary).forEach((key, index) => {
    // If it is the first item, append query character, otherwise &
    const queryPrefix = index === 0 ? "?" : "&";
    // Return "?key1=val1&key2=val2 ... "
    requestString += queryPrefix + key + "=" + paramDictionary[key];
  });

  // First create a CORS request, this is the message we are going to send (a get request in this case)
  let xhr = createCORSRequest("GET", url + requestString); // Request type and URL

  // Message is not sent yet, but we can check that the browser supports CORS
  if (!xhr) {
    alert("CORS not supported");
  }
  // CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
  // to do when the response arrives
  xhr.onload = function() {
    const obj = JSON.parse(xhr.response);
    callback(obj);
  };
  // We have done everything we need to prepare the CORS request, so send it
  xhr.send();
}

// API FUNCTIONS
// -------------

/**
 * apiGetStatistics
 * ----------------
 * Returns the game statistics as a JavaScript object/dictionary.
 *
 * Must be called when a player requests the game statistics.
 *
 * Format :
 * 	 	{
 * 	  		"aiWins": 5,
 * 	  		"userWins": 3,
 * 	 		"avgDraws": 4,
 * 	  		"totGamesPlayed": 7,
 * 	  		"maxRounds": 8
 * 	  	}
 */

function apiGetStatistics(callback) {
  apiGet(URL_GET_STATISTICS, callback, {});
}

/**
 * Makes the AI choose an attribute if it is active.
 * Returns the information needed to initialise a round
 * as a JavaScript object/dictionary.
 *
 * Must be called at the beginning of a round.
 *
 * chosenAttributeName corresponds to null
 * if the user is active and it corresponds to the
 * attribute that the AI chooses otherwise.
 *
 * EXAMPLE:
 * 	{
 * 		"round": 1,
 *		"communalPileSize": 4,
 *		"chosenAttributeName": "strength"/null,
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
 */
function apiInitRound(callback) {
  apiGet(URL_INIT_ROUND, callback, {});
}

/**
 * Initialises the game with the chosen number of AI players.
 * Returns {loaded: true}.
 *
 * Must be called before a game begins.
 * @param numAiPlayers chosen number of AI players
 */
function apiInitGame(numPlayers, callback) {
  apiGet(URL_INIT_GAME, callback, {
    NumAiPlayers: numPlayers,
  });
}
/**
 * Plays a round with the chosen attribute and auto completes the game if the user is eliminated
 * and there is no winner. If there is a winner in the round or the game is auto completed this
 * will be reflected in the gameWinnerName and gameAutoCompleted fields and the database will be
 * updated. Returns a JavaScript object/dictionary with information for playing a round with an attribute and
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
 * EXAMPLE: { "roundWinnerName": "USER"/null,
 *            "gameOver": true,
 *            "eliminatedPlayersNames": ["AI1", "AI2"] }
 */

function apiPlayRoundWithAttribute(attributeName, callback) {
  apiGet(URL_PLAY_ROUND_WITH_ATTRIBUTE, callback, {
  AttributeName: attributeName,
});
}

/**
 * Returns the won rounds for every player during the game, the game winner name and whether the
 * game auto completed.
 *
 * Must be called when a game has ended, i.e. there is a winner.
 *
 *
 *
 * EXAMPLE: { "playerScores": [ { "name": "USER", "score": 15}, { name: "AI1", "score": 10}, ...],
 *            "gameWinnerName": "USER",
 *            "gameAutoCompleted": true }
 */

function apiGetGameOverScores(callback) {
  apiGet(URL_GAME_OVER_SCORES, callback, {});
}
