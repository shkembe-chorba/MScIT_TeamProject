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
	<script>
		vex.defaultOptions.className = 'vex-theme-os';
	</script>
	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex.css" />
	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex-theme-os.css" />
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

</head>

<body onload="initalize()">
	<!-- Call the initalize method when the page loads -->

	<div class="container">

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
				alert(responseText); // lets produce an alert
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
				alert(responseText); // lets produce an alert
			};

			// We have done everything we need to prepare the CORS request, so send it
			xhr.send();
		}
	</script>

</body>

</html>