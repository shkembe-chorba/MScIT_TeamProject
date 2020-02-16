<html>

<!-- HEAD -->
<#include "./assets/ftl-templates/Head.ftl">

<body onload="initialize()">

	<!-- HTML -->
	<div class="container">

		<!-- Include the modal HTML and the static Nav -->
		<#include "./assets/ftl-templates/Modal.ftl">
		<#include "./assets/ftl-templates/Nav.ftl">
		<#include "./assets/ftl-templates/GameOverModal.ftl">

		<!-- mt-3 = margin between nav and game-->
		<div class="container mt-3">
			<!-- ROUND DISPLAY -->
			<!-- #tt-round-number -->
			<h4>Game round <span id="tt-round-number"></span></h4>
			<!-- Responsive design for smaller screens (see row / col values) -->
			<div class="row justify-content-center align-items-center">
				<div class="col-sm-12 col-md-6 col-lg-3 text-center mb-3">
					<!-- PLAY BUTTON -->
					<!-- #tt-button-wrapper -->
					<div id="tt-button-wrapper"></div>
				</div>
				<div class="col-sm-12 col-md-6 col-lg-9 text-center">
					<!-- MESSAGE BOARD -->
					<div class="card ">
						<div class="tt-message-display card-body">
							<h4><div  id="tt-message-display"></div></h4>
							<!-- #tt-message-display -->
						</div>
					</div>
				</div>
			</div>

			<!-- COMMUNAL PILE DISPLAY-->
			<!-- #tt-communal-pile -->
			<div class="row justify-content-left align-items-left">
				<h5> <i class="fa fa-exchange" aria-hidden="true"></i> Communal deck: <span id="tt-communal-pile"></span> cards</h5>
			</div>

			<!-- PLAYER CARD DISPLAYS -->
			<!-- #tt-card-decks -->
			<div id="tt-card-decks" class="card-deck"></div>
		</div>
	</div>

	<!-- JS etc. IMPORTS -->

	<!-- Import our Pseudo Player class and style sheet -->
	<link rel="stylesheet" href="./assets/components/Player/Player.css" />
	<script type="text/javascript" src="./assets/components/Player/Player.js"> </script>
	<!-- Import our Pseudo PlayButton class -->
	<script type="text/javascript" src="./assets/components/PlayButton/PlayButton.js"> </script>
	<!-- Import API helper functions -->
	<script type="text/javascript" src="./assets/api/api.js"> </script>


	<!-- JS VIEW LOGIC -->
	<script type="text/javascript">

		// Global JS Components
		// --------------------
		// These change:
		let PLAYER_CARDS;
		let USER_CARD;
		let AI_CARDS;
		let CHOSEN_ATTRIBUTE;

		// This does not:
		const PLAY_BUTTON = PlayButtonFactory();

		// DOM Element ID References
		// -------------------------
		const DOM_CARD_WRAPPER = "#tt-card-decks";
		const DOM_BUTTON_WRAPPER = "#tt-button-wrapper";
		const DOM_MESSAGE_WRAPPER = "#tt-message-display"
		let  DOM_ROUND_NUMBER = "#tt-round-number";
		let DOM_COMMUNAL_PILE_SIZE = "#tt-communal-pile";
		// New Game Modal:
		const NEW_GAME_MODAL = "#newGameModal";
		const NEW_GAME_MODAL_PLAY = '#newGameModal-play';
		const NEW_GAME_MODAL_PLAYERS = '#newGameModal-players';
		const NEW_GAME_MODAL_CLOSERS = '#newGameModal-abort';
		const NEW_GAME_MODAL_SELECTION = 'input[name=newGameModal-aiPlayers]:checked';
		// Game Over Modal:
		const GAME_OVER_MODAL = "#gameOverModal";
		const GAME_OVER_AUTOCOMPLETE_TEXT = "#tt-auto-finish-text";
		const GAME_OVER_RESTART = "#tt-restartGame";
		const GAME_OVER_STATS = "#tt-showStatistics";
		const GAME_OVER_GAME_WINNER = "#winnerName";

		// INITIALISE GAME PHASE
		// ---------------------

		function initialize() {
			// Setup event handlers
			initialiseNewGameModal();
			initialisePlayButton();

			// Show the new game modal to begin
			$(NEW_GAME_MODAL).modal('show');
		}


		// Binds handlers to the NewGame modal
		function initialiseNewGameModal() {

			// Redirect if the user aborts
			$(NEW_GAME_MODAL_CLOSERS).click(() => {
				window.location.href = "../toptrumps";
			})

			// The modal does not close when clicked outside
			$(NEW_GAME_MODAL).modal({
				backdrop: 'static'
			});

			// Setup game on click
			$(NEW_GAME_MODAL_PLAY).click(() => {
				// Get value from the radio boxes
				let numAiPlayers = 0;
				numAiPlayers = $(NEW_GAME_MODAL_SELECTION).val();
				// Call the api to initialise a game
				apiInitGame(numAiPlayers, (response) => {
					if (response.loaded) {
						// If we get a good response, initialise the round
						apiInitRound(setupRound)
					}
				})

				// hide when selected number of players
				$(NEW_GAME_MODAL).modal('hide');
			});

		}

		// Set up click handlers on the main PLAY_BUTTON class
		// This class implements the PlayButton component found in PlayButtonFactory.js
		function initialisePlayButton() {
			// Add button to dom
			PLAY_BUTTON.attach(DOM_BUTTON_WRAPPER);
			// Attach callback if a dropdown attribute is chosen
			PLAY_BUTTON.onAttributeClick((attributeName) => {
				CHOSEN_ATTRIBUTE = attributeName;
				apiPlayRoundWithAttribute(attributeName, playRound);
			});
			// Attach callback if no dropdown attribute is availible (AI turn)
			PLAY_BUTTON.onPlayRoundClick(() => {
				apiPlayRoundWithAttribute(CHOSEN_ATTRIBUTE, playRound);
			});
			// Attach callback for next round click
			PLAY_BUTTON.onNextRoundClick(() => {
				apiInitRound(setupRound);
			});
			// Attach callback for game over click
			PLAY_BUTTON.onGameOverClick(() => {
				apiGetGameOverScores(gameOverScores);
			})
		}

		// Attach callbacks for GameOver Modal
		function initializeGameOverModal() {

			// Ensure clicking outside does not close the modal
			$(GAME_OVER_MODAL).modal({
				backdrop: 'static'
			});

			// Refresh the window if the user wants to play again
			$(GAME_OVER_RESTART).click(() => {
				window.location.href = "/toptrumps/game";
			})

			// Go to the stats page otherwise.
			$(GAME_OVER_STATS).click(() => {
				window.location.href = "/toptrumps/stats";
			})

			$(GAME_OVER_MODAL).modal('show');
		}


		// Setup a round.
		function setupRound(apiResponse) {

			const {
				playersInGame,
				chosenAttributeName,
				round,
				communalPileSize,
			} = apiResponse;

			const attributes = playersInGame[0].topCard.attributes;

			//Set the round number
			$(DOM_ROUND_NUMBER).text(round);

			// Set the chosen attribute (if an AI player has already called it)
			CHOSEN_ATTRIBUTE = chosenAttributeName;

			//Update the communal pile size
			$(DOM_COMMUNAL_PILE_SIZE).text(communalPileSize);

			// Create the player cards.
			setupPlayerCards(playersInGame);
			// Set the button to the correct type - human can choose only when they are the active player
			setupButtonView(chosenAttributeName, attributes);

			// Display the active player in the messageboard.
			$(DOM_MESSAGE_WRAPPER).text(playersInGame.filter(p => p.isActive)[0].name + " is the active player");
		}

		function setupPlayerCards(players) {
			// Clear previous cards from the wrapper
			$(DOM_CARD_WRAPPER).empty();
			// Build the player components (found in PlayerFactory.js) from the api call
			PLAYERS = players.map(p => {
				return PlayerFactory(p);
			})
			// Get User
			USER = PLAYERS.filter(p => p.isUser())[0];
			// Get AI Player array
			AIS = PLAYERS.filter(p => !p.isUser());
			// Hide the ais cards.
			AIS.forEach(ai => ai.hideCard());
			// Attach the cards to the screen.
			PLAYERS.forEach(p => p.attach(DOM_CARD_WRAPPER));
		}

		function setupButtonView(chosenAttributeName, attributes) {
			// If the AI returned a chosen attribute during the setup round api call,
			// then the next round button displayed as the user has no input.
			if (chosenAttributeName !== null) {
				PLAY_BUTTON.setPlayRoundButton();
			}
			// if it is human who needs to choose attribute
			else {
				PLAY_BUTTON.clearAttributes();
				PLAY_BUTTON.setAttributeButton();
				attributes.forEach(a => {
					PLAY_BUTTON.addAttribute(a.name);
				})
			}
		}

		// PLAY ROUND PHASE
		// ----------------

		function playRound(apiResponse) {

			// Get these variables out of apiResponse by destructuring js object
			const {
				eliminatedPlayersNames,
				roundWinnerName,
				gameOver,
			} = apiResponse;

			// CARDS
			// Show all players' cards
			PLAYERS.forEach(p => p.showCard());
			// Display all players in 'loser' state (for draw)
			PLAYERS.forEach(p => p.setLoser(CHOSEN_ATTRIBUTE));
			// Display winner if exists
			PLAYERS.filter(p => p.getName() === roundWinnerName).forEach(p => p.setWinner(CHOSEN_ATTRIBUTE));
			// Display eliminated players
			PLAYERS.filter(p => eliminatedPlayersNames.includes(p.getName())).forEach(p => p.eliminate());

			// DISPLAY WINNER MESSAGES
			if (roundWinnerName) {
				$(DOM_MESSAGE_WRAPPER).text("The round winner is " + roundWinnerName + " with attribute " + CHOSEN_ATTRIBUTE);
			}
			else {
				$(DOM_MESSAGE_WRAPPER).text("The round was a draw");
			}

			// SET BUTTON
			// ----------------
			if (gameOver) {
				PLAY_BUTTON.setGameOverButton();

			} else {
				PLAY_BUTTON.setNextRoundButton();
			}
		}

		// GAME OVER PHASE
		// ----------------
		function gameOverScores(apiResponse) {

			// Destructure apiResponse into variables
			const {
				gameWinnerName,
				gameAutoCompleted
			} = apiResponse;

			const didAutocompleteText = gameAutoCompleted ? "Game autocompleted without the user" : "";
			const winnerText = gameWinnerName === "USER" ? "YOU!!" : gameWinnerName;

			// Tell the user if the game autocompleted without the player.
			$(GAME_OVER_AUTOCOMPLETE_TEXT).text(didAutocompleteText);
			// Set the winning player text
			$(GAME_OVER_GAME_WINNER).text(winnerText);
			initializeGameOverModal();


		}
	</script>
</body>
</html>