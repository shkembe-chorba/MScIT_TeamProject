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
						<div class="row justify-content-md-center">
							<button type="button" id="buttonRound" class="btn btn-primary">
							</button>

							<div class="dropdown">
								<button class="btn btn-primary dropdown-toggle" type="button" id="buttonAttribute" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-hidden="true">
								</button>
								<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
									<a class="dropdown-item" id="att1">Attribute 1</a>
									<a class="dropdown-item" id="att2">Attribute 2</a>
									<a class="dropdown-item" id="att3">Attribute 3</a>
									<a class="dropdown-item" id="att4">Attribute 4</a>
									<a class="dropdown-item" id="att5">Attribute 5</a>
								</div>
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
			<script type="text/javascript" src="./assets/components/Player/player.js"> </script>

			<!-- Import API -->
			<script type="text/javascript" src="./assets/api/api.js"> </script>


			<script type="text/javascript">
				// CALL ALL SETUP FUNCTIONS HERE
				function initalize() {
					// Setup event handlers
					setupNewGameModal();
					setUpRoundButton();
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
						const players = playersInGame.map(p => {
							return PlayerFactory(p);
						})

						// Get User
						const user = players.filter(p => p.isUser())[0];
						// Get AI Player array
						const ais = players.filter(p => !p.isUser());

						// Hide the ais cards.
						ais.forEach(ai => ai.hideCard());

						// Attach the card to the screen.
						players.forEach(p => p.attach("#card-decks"));

						// If it is the AI who plays next
						if (resultApi.chosenAttributeName !== null) {
							setUpRoundButton();
						}
						// if it is human who needs to choose attribute
						else { setupAttributeButton();
							$("#att1").text();
							$("#att2").text();
							$("#att3").text();
							$("#att4").text();
							$("#att5").text();
						}
					})
				}

				// Sets round button to display text and
				const ROUND_BUTTON = "#buttonRound";
				const ATTRIBUTE_BUTTON = "#buttonAttribute";
				function setUpRoundButton() {
						$(ATTRIBUTE_BUTTON).hide();
						$(ROUND_BUTTON).show();
						$(ROUND_BUTTON).text("Play round");
						$(ROUND_BUTTON).click(() => {
							// returns null to api to play the round with attribute function
							// will call a function that is responsible for getting game logic
						//	playRoundWithAttribute(null) function - once it is done
						})
					}

				function setupAttributeButton() {
					$(ROUND_BUTTON).hide();
					$(ATTRIBUTE_BUTTON).text("Play round with attribute");
					$(ATTRIBUTE_BUTTON).show();
				}
			</script>

		</body>

</html>