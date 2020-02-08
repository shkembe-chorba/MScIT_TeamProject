<html>

<#include "./assets/ftl-templates/Head.ftl">

<body onload="initalize()">
<div class="container">
	<#include "./assets/ftl-templates/Modal.ftl">
	<#include "./assets/ftl-templates/Nav.ftl">

	<div class="container pt-3">
		<div class="row">
			<div class="col">
				<h4>Game round X</h4>
			</div>
		</div>
		<div class="row justify-content-md-center">

			<div id="tt-button-wrapper">
				<!-- Button is inserted here -->
			</div>

			<div class="card" style="width: 40rem;text-align:center">
				<div class="card-body">
					<h5 class="card-title">The chosen attribute was <strong>strength.</strong></h5>
					<h5 class="card-title">The winner of the round is <strong>A6</strong>.</h5>
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
		<script type="text/javascript" src="./assets/components/Player/Player.js"> </script>
		<script type="text/javascript" src="./assets/components/PlayButton/PlayButton.js"> </script>

		<!-- Import API -->
		<script type="text/javascript" src="./assets/api/api.js"> </script>





					<!-- Import our Pseudo Player class and style sheet -->
					<link rel="stylesheet" href="assets/components/Player/player.css" />
					<script type="text/javascript" src="./assets/components/Player/player.js"> </script>

					<!-- Import API -->
					<script type="text/javascript" src="./assets/api/api.js"> </script>


					<script type="text/javascript">
						// Global JS Components
						let PLAYER_CARDS;
						let USER_CARD;
						let AI_CARDS;
						let CHOSEN_ATTRIBUTE;
						let PLAY_BUTTON = PlayButtonFactory();

						// DOM Element ID References
						const DOM_CARD_WRAPPER = "#card-decks";

						// CALL ALL SETUP FUNCTIONS HERE
						function initalize() {
							// Setup event handlers
							setupNewGameModal();
							setupRoundButton();
							// setupPlayButton();
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
								apiInitGame(numAiPlayers, (response) => {
									if (response.loaded) {
										// If we get a good response, initialise the round
										apiInitRound(setupRound)
									}
								})
								$(NEW_GAME_MODAL).modal('hide'); // hide when selected number of players
							});
						}

						function setupRound(apiResponse) {
							// Destructure apiResponse fields into variables
							const {
								playersInGame,
								chosenAttributeName
							} = apiResponse;

							setupPlayerCards(playersInGame, apiResponse);

							if (chosenAttributeName !== null) {
								PLAY_BUTTON.setPlayRoundButton();
							}
							// if it is human who needs to choose attribute
							else {
								PLAY_BUTTON.setAttributeButton();
								apiResponse.playersInGame[0].topCard.attributes.forEach(a => {
									PLAY_BUTTON.addAttribute(a.name);
								})
							}
							// Empty
							// setupMessageBoard();

						}

						function setupRoundButton() {
							// PLAY BUTTON
							// -----------
							// Add button to dom
							PLAY_BUTTON.attach("#tt-button-wrapper");
							// Attach attribute click handler
							PLAY_BUTTON.onAttributeClick((attributeName) => {
								// returns null to api to play the round with attribute function
								// will call a function that is responsible for getting game logic
								//	playRoundWithAttribute(null) function - once it is done
							});
							// Attach 'play round' click when no attribute options are there
							PLAY_BUTTON.onPlayRoundClick(() => {
							});
							// Attach 'next round' click.
							PLAY_BUTTON.onNextRoundClick(setupRound);
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

							function playRound(apiResponse) {
								const {
									eliminatedPlayersNames,
									roundWinnerName
								} = apiResponse;

								// CARDS
								// -----
								// Show all players' cards
								PLAYERS.forEach(p => p.showCard());
								// Display all players in 'loser' state (for draw)
								PLAYERS.forEach(p => p.setLoser(CHOSEN_ATTRIBUTE));
								// Display winner if exists
								PLAYERS.filter(p => p.getName() === roundWinnerName).forEach(p => p.setWinner(CHOSEN_ATTRIBUTE));
								// Display eliminated players
								PLAYERS.filter(p => eliminatedPlayersNames.contains(p.getName())).forEach(p => p.eliminate());

								PLAY_BUTTON.setNextRoundButton();

								// DISPLAY WINNER MESSAGES
								// -----------------------

						}
			</script>

	</div>
							</body>
</html>