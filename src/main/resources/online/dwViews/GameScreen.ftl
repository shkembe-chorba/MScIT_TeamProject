<html>

<#include "./assets/ftl-templates/Head.ftl">
	<#include "./assets/ftl-templates/Modal.ftl">

		<body onload="initalize()">

			<div class="container">
				<#include "./assets/ftl-templates/Nav.ftl">

					<div class="container pt-3">
						<div class="row">
							<div class="col">
								<h4>Game round X</h4>
							</div>
						</div>
						<div class="row justify-content-center">
							<div class="col-3">
								<div class="row">
									<button type="button" class="btn btn-lg">Play round</button>
								</div>
								<div class="row">
									<h6> Communal Pile Size: <span> 10 </span> </h6>
								</div>
							</div>
							<div class="col-9">
								<div class="card">
									<div class="card-body">
										<h5 class="card-title">The chosen attribute was <strong>strength.</strong></h5>
										<h5 class="card-title">The winner of the round is <strong>A6</strong>.</h5>
									</div>
								</div>
							</div>
						</div>
					</div>
					<br>

					<!-- The Player Card Deck Wrapper -->
					<div id="card-decks" class="card-deck">
					</div>
			</div>

			<!-- Import our Pseudo Player class and style sheet -->
			<link rel="stylesheet" href="assets/components/Player/player.css" />
			<script type="text/javascript" src="./assets/components/Player/player.js"> </script>

			<!-- Import API -->
			<script type="text/javascript" src="./assets/api/api.js"> </script>


			<script type="text/javascript">
				// Globals
				let PLAYERS;
				let USER;
				let AIS;

				// CALL ALL SETUP FUNCTIONS HERE
				function initalize() {
					// Setup event handlers
					setupNewGameModal();
					// Show the new game modal
					$(NEW_GAME_MODAL).modal('show');
				}

				// New Game Modal:
				const NEW_GAME_MODAL = "#newGameModal";
				const NEW_GAME_MODAL_PLAY = '#newGameModal-play';
				const NEW_GAME_MODAL_PLAYERS = '#newGameModal-players';
				const NEW_GAME_MODAL_CLOSERS = '#newGameModal-abort';
				const NEW_GAME_MODAL_SELECTION = 'input[name=newGameModal-aiPlayers]:checked';

				function setupNewGameModal() {
					// Redirect if the user aborts
					$(NEW_GAME_MODAL_CLOSERS).click(() => {
						window.location.href = "../toptrumps";
					})
					// Setup game on click
					$(NEW_GAME_MODAL_PLAY).click(() => {
						// Get value from the radio boxes
						let numAiPlayers = 0;
						numAiPlayers = $(NEW_GAME_MODAL_SELECTION).val();
						// Call the api
						apiInitGame(numAiPlayers, setupRound)
						$(NEW_GAME_MODAL).modal('hide'); // hide when selected number of players
					});
				}

				function setupRound() {
					apiInitRound((resultApi) => {
						// destroy all player cards in the wrapper
						$("#card-decks").empty();

						// Creates a variable by 'destructuring' the api object
						const {
							playersInGame
						} = resultApi;

						// Create player objects.
						PLAYERS = playersInGame.map(p => {
							return PlayerFactory(p);
						})

						// Get User
						USER = PLAYERS.filter(p => p.isUser())[0];
						// Get AI Player array
						AIS = PLAYERS.filter(p => !p.isUser());

						// Hide the ais cards.
						AIS.forEach(ai => ai.hideCard());

						// Attach the card to the screen.
						PLAYERS.forEach(p => p.attach("#card-decks"));
					})
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
			</script>
		</body>

</html>