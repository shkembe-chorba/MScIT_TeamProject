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

// CORS REQUEST HELPER FUNCTIONS
// -----------------------------

function createCORSRequest(method, url) {
  var xhr = new XMLHttpRequest();
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
 * chosenAttributeName corresponds to "NA"
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
 * Returns the String "OK".
 *
 * Must be called before a game begins.
 * @param numAiPlayers chosen number of AI players
 */
function apiInitGame(numPlayers, callback) {
  apiGet(URL_INIT_GAME, callback, {
    NumAiPlayers: numPlayers,
  });
}
