<html>

<!-- HEAD -->
<#include "./assets/ftl-templates/Head.ftl">

	<body onload="initialize()">

		<!-- HTML -->
		<div class="container">
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
											<!-- #tt-message-display -->
											<h5 class="card-title">The chosen attribute was <strong>strength.</strong>
											</h5>
											<h5 class="card-title">The winner of the round is <strong>A6</strong>.</h5>
										</div>
									</div>
								</div>
							</div>
						</div>

						<!-- PLAYER CARD DISPLAYS -->
						<!-- #tt-card-decks -->
						<div id="tt-card-decks" class="card-deck"></div>
		</div>


		<!-- JS etc. IMPORTS -->

		<!-- Import our Pseudo Player class and style sheet -->
		<link rel="stylesheet" href="assets/components/Player/player.css" />
		<script type="text/javascript" src="./assets/components/Player/Player.js"> </script>
		<!-- Import our Pseudo PlayButton class -->
		<script type="text/javascript" src="./assets/components/PlayButton/PlayButton.js"> </script>
		<!-- Import API -->
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
			let DOM_ROUND_NUMBER = "#tt-round-number";

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

				// Show the new game modal
				$(NEW_GAME_MODAL).modal('show');
			}

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
					// Call the api
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
				PLAY_BUTTON.onGameOverClick(() => {
					apiGetGameOverScores(gameOverScores);
				})
			}

			function initializeGameOverModal() {

				$(GAME_OVER_MODAL).modal({
					backdrop: 'static'
				});

				$(GAME_OVER_RESTART).click(() => {
					window.location.href = "/toptrumps/game";
				})

				$(GAME_OVER_STATS).click(() => {
					window.location.href = "/toptrumps/stats";
				})
				// Display only after we have set it up
				$(GAME_OVER_MODAL).modal('show');

			}

			// NEW ROUND PHASE
			// ---------------

			function setupRound(apiResponse) {
				// Destructure apiResponse fields into variables
				const {
					playersInGame,
					chosenAttributeName,
					round
				} = apiResponse;
				const attributes = playersInGame[0].topCard.attributes;

				//Set the round number
				$(DOM_ROUND_NUMBER).text(round);

				// Set the chosen attribute (if an AI player has already called it)
				CHOSEN_ATTRIBUTE = chosenAttributeName;

				setupPlayerCards(playersInGame);
				// Set the button to the correct type - human can choose only when they are the active player
				setupButtonView(chosenAttributeName, attributes);


				// Empty
				// setupMessageBoard();
			}

			function setupPlayerCards(players) {
				// Clear the cards from the wrapper
				$(DOM_CARD_WRAPPER).empty();
				// Create player objects.
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

			// If the AI returned a chosen attribute, there is the next round button displayed

			function setupButtonView(chosenAttributeName, attributes) {
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
				// ----------------

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