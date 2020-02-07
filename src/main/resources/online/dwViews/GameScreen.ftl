<html>

<#include "./assets/ftl-templates/Head.ftl">

	<body onload="initalize()">

		<div class="container">
			<#include "./assets/ftl-templates/Nav.ftl">

				<div class="container pt-3">
					<div class="row">
						<div class="col">
							<h4>Game round X</h4>
						</div>
					</div>
					<div class="row justify-content-md-center">
						<button type="button" class="btn btn-lg">Play round</button>
						<div class="card" style="width: 40rem;text-align:center">
							<div class="card-body">
								<h5 class="card-title">The chosen attribute was <strong>strength.</strong></h5>
								<h5 class="card-title">The winner of the round is <strong>A6</strong>.</h5>

							</div>
						</div>
					</div>
				</div>

				<!-- The Player Card Deck Wrapper -->
				<div id="card-decks" class="card-deck">
				</div>

		</div>

		<!-- Import our Pseudo Player class and style sheet -->
		<link rel="stylesheet" href="assets/components/Player/player.css" />
		<script type="text/javascript" src="./assets/components/Player/player.js"> </script>

		<script type="text/javascript">
			// Create an array of all the players with our pseudo Player class
			const players = TEST_JSON.players.map(p => {
				return PlayerFactory(p);
			})

			// Get just the user
			const user = players.filter(p => p.isUser())[0];
			// Get the ais in an array
			const ais = players.filter(p => !p.isUser());

			// Hide all the AI cards from view
			ais.forEach(ai => {
				ai.hideCard();
			});

			// Add every player to the card decks element
			players.forEach(p => p.attach("#card-decks"));

			// Method that is called on page load
			function initalize() {

				// --------------------------------------------------------------------------
				// You can call other methods you want to run when the page first loads here
				// --------------------------------------------------------------------------



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
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/playRoundWithAttribute?AttributeName=" +
					attributeName); // Request type and URL+parameters

				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives
				xhr.onload = function (e) {
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
				xhr.onload = function (e) {
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
				xhr.onload = function (e) {
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
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/initGame?NumAiPlayers=" +
					numAiPlayers); // Request type and URL+parameters

				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives
				xhr.onload = function (e) {
					var responseText = xhr.response; // the text of the response
					return responseText;
				};

				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();
			}
		</script>
	</body>

</html>