<html>

<head>
	<!-- Web page title -->
	<title>Top Trumps</title>

	<!-- Update imports for latest bootstrap and FA -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
		integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous">
	</script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
		integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous">
	</script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
		integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous">
	</script>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

</head>

<body onload="initalize()">
	<!-- Call the initalize method when the page loads -->

	<div class="container">

		<!-- New Game Modal -->
		<!-- #newGameModal -->
		<div class="modal fade" id="newGameModal" tabindex="-1" role="dialog" aria-labelledby="newGameModal"
			aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title">New Top Trumps Game</h5>
						<!-- .newGameModal-abort -->
						<button type="button" class="close newGameModal-abort" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">

						<h4>Choose how many AI players </h4>

						<label><input type="radio" name="newGameModal-aiPlayers" value="1">1</label>
						<label><input type="radio" name="newGameModal-aiPlayers" value="2">2</label>
						<label><input type="radio" name="newGameModal-aiPlayers" value="3">3</label>
						<label><input type="radio" name="newGameModal-aiPlayers" value="4">4</label>
					</div>
					<div class="modal-footer">
						<!-- .newGameModal-abort -->
						<button type="button" class="btn btn-secondary newGameModal-abort"
							data-dismiss="modal">Close</button>
						<!-- #newGameModal-play -->
						<button id="newGameModal-play" type="button" class="btn btn-primary"
							data-dismiss="modal">Play</button>
					</div>
				</div>
			</div>
		</div>
		<!-- END New Game Modal -->

	</div>

	<script type="text/javascript">
		// JQuery Selectors
		// ----------------

		// New Game Modal:
		const NEW_GAME_MODAL = '#newGameModal';
		const NEW_GAME_MODAL_PLAY = '#newGameModal-play';
		const NEW_GAME_MODAL_PLAYERS = '#newGameModal-players';
		const NEW_GAME_MODAL_CLOSERS = '.newGameModal-abort';
		const NEW_GAME_MODAL_SELECTION = 'input[name=newGameModal-aiPlayers]:checked';
		// ---------------

		// Setup Functions
		// ---------------
		// These functions create event handlers for clicks etc.

		// New Game Modal:
		function setupNewGameModal() {
			// Redirect if the user aborts
			$(NEW_GAME_MODAL_CLOSERS).click(() => {
				window.location.href = "../toptrumps";
			})

			// Setup game on click
			$(NEW_GAME_MODAL_PLAY).click(() => {
				// Get value from the radio boxes
				const numAiPlayers = $(NEW_GAME_MODAL_SELECTION).val();
				// Call the api
				setupGame(numAiPlayers);
			});
		}

		// CALL ALL SETUP FUNCTIONS HERE
		function initalize() {
			// Setup event handlers
			setupNewGameModal();

			// Show the new game modal
			$(NEW_GAME_MODAL).modal('show');
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
		function setupGame(numAiPlayers) {
			alert('Game started with ' + numAiPlayers + ' players');
		}


		function setupRound() {

		}

		function playRound() {

		}



		// This calls the helloJSONList REST method from TopTrumpsRESTAPI
		function helloJSONList() {

			// First create a CORS request, this is the message we are going to send (a get request in this case)
			var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/helloJSONList"); // Request type and URL

			// Message is not sent yet, but we can check that the browser supports CORS
			if (!xhr) {
				alert("CORS not supported");
			}

			// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
			// to do when the response arrives
			xhr.onload = function (e) {
				var responseText = xhr.response; // the text of the response

			};

			// We have done everything we need to prepare the CORS request, so send it
			xhr.send();
		}

		// This calls the helloJSONList REST method from TopTrumpsRESTAPI
		function helloWord(word) {

			// First create a CORS request, this is the message we are going to send (a get request in this case)
			var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/helloWord?Word=" +
				word); // Request type and URL+parameters

			// Message is not sent yet, but we can check that the browser supports CORS
			if (!xhr) {
				alert("CORS not supported");
			}

			// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
			// to do when the response arrives
			xhr.onload = function (e) {
				var responseText = xhr.response; // the text of the response

			};

			// We have done everything we need to prepare the CORS request, so send it
			xhr.send();
		}
	</script>

</body>

</html>