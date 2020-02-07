<html>

<#include "./assets/ftl-templates/Head.ftl">

    <body onload="initalize() ">

        <div class="container">
            <#include "./assets/ftl-templates/Nav.ftl">

                <div class="jumbotron jumbotron-fluid">
                    <div class="container">
                        <div class="row justify-content-md-center">
                            <div class="p-3">
                                <h1> Game Statistics </h1>
                            </div>
                        </div>
                        <div class="row justify-content-md-center"><br>
                            <h3> Games played:&nbsp</h3>
                            <h3 id="gamesPlayed"> </h3>
                        </div>
                    </div><br>
                    <div class="row justify-content-md-center">
                        <h3> Won by human:&nbsp </h3>
                        <h3 id=humanWins></h3>
                        </h3>
                    </div><br>
                    <div class="row justify-content-md-center">

                        <h3> Won by AI:&nbsp</h3>
                        <h3 id="aiWins"></h3>
                        </h3>
                    </div><br>
                    <div class="row justify-content-md-center">
                        <br>
                        <h3> Average number of draws in game:&nbsp
                            <h3 id="drawNumber"></h3>
                        </h3>
                    </div><br>
                    <div class="row justify-content-md-center">
                        <h3> Longest game played:&nbsp <h3 id="maxGame"></h3>
                        </h3>
                    </div>
                </div>
        </div>

        <!-- Test API -->
        <script type="text/javascript" src="./assets/api/api-test.js"> </script>

        <script type="text/javascript">
            // Method that is called on page load and initializes the statistics displayed on screen
            function initalize() {
                apiGetStatistics((obj) => {
                    $("#gamesPlayed").text(obj.tot_games_played);
                    $("#humanWins").text(obj.user_wins);
                    $("#drawNumber").text(obj.avg_draws);
                    $("#aiWins").text(obj.ai_wins);
                    $("#maxGame").text(obj.max_rounds);
                })
            }
        </script>


        <!-- calls for REST API Methods -->
        <script type="text/javascript">
            /**
             * Returns the game statistics as a JavaScript object/dictionary.
             *
             * Must be called when a player requests the game statistics.
             *
             * Format :
             * 	 	{
             * 	  		"aiWins": 5,
             * 	  		"userWins": 3,
             * 	 		"avgDraws": 4,
             * 	  		"totGamesPlayed": 7,
             * 	  		"maxRounds": 8
             * 	  	}
             */
            function retrieveStats() {

                // First create a CORS request, this is the message we are going to send (a get request in this case)
                var xhr = createCORSRequest('GET',
                "http://localhost:7777/toptrumps/retrieveStats"); // Request type and URL

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