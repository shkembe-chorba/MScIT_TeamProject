<html>

	<head>
		<!-- Web page title -->
    	<title>Top Trumps</title>
    	
    	<!-- Import JQuery, as it provides functions you will probably find useful (see https://jquery.com/) -->
    	<script src="https://code.jquery.com/jquery-2.1.1.js"></script>
    	<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
    	<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.1/themes/flick/jquery-ui.css">

		<!-- Optional Styling of the Website, for the demo I used Bootstrap (see https://getbootstrap.com/docs/4.0/getting-started/introduction/) -->
		<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/TREC_IS/bootstrap.min.css">
    	<script src="http://dcs.gla.ac.uk/~richardm/vex.combined.min.js"></script>
    	<script>vex.defaultOptions.className = 'vex-theme-os';</script>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex.css"/>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex-theme-os.css"/>
    	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

	</head>

    <body onload="initalize()"> <!-- Call the initalize method when the page loads -->
    	
    	<div class="container">

			<!-- Add your HTML Here -->
		
		</div>
		
		<script type="text/javascript">
		
			// Method that is called on page load
			function initalize() {
			
				// --------------------------------------------------------------------------
				// You can call other methods you want to run when the page first loads here
				// --------------------------------------------------------------------------
				
				// For example, lets call our sample methods
				helloJSONList();
				helloWord("Student");
				
			}
			
			// -----------------------------------------
			// Add your other Javascript methods Here
			// -----------------------------------------
		
			// This is a reusable method for creating a CORS request. Do not edit this.
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
		
		</script>
		
		<!-- Here are examples of how to call REST API Methods -->
		<script type="text/javascript">
		
			/**
			 * Plays a round with the chosen attribute and auto completes the game if the
			 * user is eliminated and there is no winner. If there is a winner in the round
			 * or the game is auto completed this will be reflected in gameWinnerName and
			 * the database will be updated.
			 * Returns a JavaScript object/dictionary with information for playing a round with an attribute
			 * and possible game over information.
			 *
			 * Must be called after initRound().
			 *
			 * If the game is auto completed roundWinnerName and eliminatedPlayersNames correspond to information
			 * about the last round before the autocompletion, not the last round overall.
			 *
			 * userEliminated corresponding to true does not always mean that the game was
			 * auto completed, the check for that must be done via gameAutoCompleted.
			 *
			 * roundWinnerName corresponds to null if there was a draw and it
			 * corresponds to the name of the round winner otherwise.
			 *
			 * gameWinnerName corresponds to null if there was no game winner and it
			 * corresponds to the name of the winner otherwise.
			 *
			 * gameWinnerName not being null does not necessarily mean that the game ended
			 * in the current round, the game could have been auto completed.
			 * That must be checked via gameAutoCompleted.
			 *
			 * EXAMPLE:
			 * 	{
			 * 	    "roundWinnerName": "USER"/null,
			 * 	    "userEliminated": true,
			 * 	    "gameWinnerName": "USER"/null,
			 * 	    "gameAutoCompleted": false,
			 * 	    "eliminatedPlayersNames": [ "AI1", "AI2"]
			 * 	}
			 */
			function playRoundWithAttribute(attributeName) {

				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/playRoundWithAttribute?AttributeName="+attributeName); // Request type and URL+parameters

				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives
				xhr.onload = function(e) {
					var responseText = xhr.response; // the text of the response
					return JSON.parse(responseText);
				};

				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();
			}

			/**
			 * Returns the won rounds for every player during the game
			 * as a JavaScript array of objects/dictionaries.
			 *
			 * Must be called when a game has ended, i.e. there is a winner.
			 *
			 * EXAMPLE:
			 * [
			 *     {
			 *     	"name": "USER",
			 *      "score": 15,
			 *      },
			 *      {
			 *      name: "AI1",
			 *      "score", 10
			 *      }
			 *      ...
			 * ]
			 */
			function getGameOverScores() {

				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/getGameOverScores"); // Request type and URL

				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives
				xhr.onload = function(e) {
					var responseText = xhr.response; // the text of the response
					return JSON.parse(responseText);
				};

				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();
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
			 *		"chosenAttributeName": "strength"/"NA",
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
			function initRound() {

				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/initRound"); // Request type and URL
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
 					var responseText = xhr.response; // the text of the response
					return JSON.parse(responseText);
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();		
			}

			/**
			 * Initialises the game with the chosen number of AI players.
			 * Returns the String "OK".
			 *
			 * Must be called before a game begins.
			 * @param numAiPlayers chosen number of AI players
			 */
			function initGame(numAiPlayers) {
			
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/initGame?NumAiPlayers="+numAiPlayers); // Request type and URL+parameters
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives
				xhr.onload = function(e) {
					var responseText = xhr.response; // the text of the response
					return responseText;
				};

				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();		
			}

		</script>
		
		</body>
</html>