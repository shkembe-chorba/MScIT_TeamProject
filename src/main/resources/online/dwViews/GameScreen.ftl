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
	</body>

</html>