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
        <script type="text/javascript" src="./assets/api/api.js"> </script>

        <script type="text/javascript">
            // Method that is called on page load and initializes the statistics displayed on screen
            function initalize() {
                apiGetStatistics((obj) => {
                    $("#gamesPlayed").text(obj.totGamesPlayed);
                    $("#humanWins").text(obj.userWins);
                    $("#drawNumber").text(obj.avgDraws);
                    $("#aiWins").text(obj.aiWins);
                    $("#maxGame").text(obj.maxRounds);
                })
            }
        </script>

    </body>

</html>