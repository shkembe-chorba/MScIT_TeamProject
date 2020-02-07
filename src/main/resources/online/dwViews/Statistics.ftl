<html>

<head>
    <title>Top Trumps</title>

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

<body onload="initalize() ">

<div class="container">
    <div class="pb-2 mt-4 mb-10 border-bottom">
        <a class="btn btn-outline-primary btn-lg" href="/toptrumps/game" role="button">Home</a>
    </div>

    <div class="jumbotron jumbotron-fluid">
        <div class="container">
            <div class="row justify-content-md-center">
                <div class="p-3">
                    <h1> Game Statistics </h1>
                </div>
            </div>
            <div class="row justify-content-md-center"><br>
                <h3 > Games played:&nbsp</h3> <h3 id="gamesPlayed">  </h3> </div>
            </div><br>
            <div class="row justify-content-md-center">
                <h3> Won by human:&nbsp </h3><h3 id=humanWins></h3>  </h3>
            </div><br>
            <div class="row justify-content-md-center">

                    <h3> Won by AI:&nbsp</h3> <h3 id="aiWins"></h3> </h3>
                </div><br>
            <div class="row justify-content-md-center">
                <br>
                    <h3> Average number of draws in game:&nbsp
                        <h3 id="drawNumber"></h3> </h3>
            </div><br>
            <div class="row justify-content-md-center">
                    <h3> Longest game played:&nbsp <h3 id="maxGame"></h3> </h3>
                </div>
            </div>


        </div>
    </div>

</div>

<script type="text/javascript">
    // Method that is called on page load and initializes the statistics displayed on screen
    function initalize() { retrieveStatistics()}
</script>

<script type="text/javascript">
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





    /**
     * This function returns the game statistics as a javascript object/dictionary.
     * Format :
     * {
     * "ai_wins": 5,
     * "user_wins": 3,
     * "avg_draws": 4,
     * "tot_games_played": 7,
     * "max_rounds": 8
     * }
     */
    function retrieveStatistics() {

        // First create a CORS request, this is the message we are going to send (a get request in this case)
        let xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/retrieveStats"); // Request type and URL

        // Message is not sent yet, but we can check that the browser supports CORS
        if (!xhr) {
            alert("CORS not supported");
        }
        // CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
        // to do when the response arrives
        xhr.onload = function(e) {
            var responseText = JSON.parse(xhr.response); // the text of the response
            $("#gamesPlayed").text(responseText.tot_games_played);
            $("#humanWins").text(responseText.user_wins);
            $("#drawNumber").text(responseText.avg_draws);
            $("#aiWins").text(responseText.ai_wins);
            $("#maxGame").text(responseText.max_rounds);

        };
        // We have done everything we need to prepare the CORS request, so send it
        xhr.send();
    }

</script>

</body>

</html>
