package commandline.controller;

import commandline.view.TopTrumpsView;
import model.*;

import java.util.ArrayList;

public class CliController implements TopTrumpsControllerInterface {

    private final GameModel model;
    private TopTrumpsView view;
    private Database database;

    public CliController(GameModel model) {
        this.model = model;
    }

    public void setView(TopTrumpsView view) {
        this.view = view;
    }

    /**
     * 1. Display option to view stats or start game
     * 2. Take user input
     * 3. Act based on input;
     */
    @Override
    public void run(){
        database = new Database();
        database.connect();

        while(true) {
            //Prompt for statistics choice
            int choice = view.displayMenu();
            switch (choice) {
                case 1:
                    RetrievedGameStatistics statistics = database.retrieveGameStats();
                    view.displayStatistics(statistics);
                    break;

                case 2:
                    setUpNewGame();
                    while (true) {
                        playRound();
                        Player winner = model.checkForWinner();
                        if (winner != null) {
                            //display game end
                            view.displayGameOver(winner.toString(), model.getPlayers());
                            //upload statistics to database
                            database.uploadGameStats(model.getDraws(),model.getRoundNumber(),winner.toString(),model.getPlayers());
                            break;
                        }
                    }
                    break;
            }



        }
    }

    private void setUpNewGame(){
        view.displayGameStartMessage();
        model.reset(4);
    }

    private void playRound(){
        Player activePlayer = model.getActivePlayer();

        view.displayRoundNumber(model.getRoundNumber());

        // Is human player still in game
        if(model.userStillInGame()){
            view.displayUserHand(model.getHumanPlayer());
        }

        // Player X is the active player!
        view.displayActivePlayer(activePlayer);

        Attribute selectedAttribute = null;
        // Category choice: AI or human
        if(isPlayerAI(model.getActivePlayer())) {
            view.displayAiPlayerHand(activePlayer);
            // ai needs to select  getAttribute(chooseIndexOfAttribute)
            selectedAttribute = (AIPlayer) (activePlayer).chooseAttribute();
        } else {
            selectedAttribute = view.getUserAttribute(activePlayer.peekCard().getAttributes());
        }
        view.displayChosenCategory(selectedAttribute);

        Player winningPlayer = playRoundWithAttribute(selectedAttribute);
        if (winningPlayer == null){
            //Draw case
            view.displayDrawnRound(model.getRoundNumber(), model.getCommunalPileSize());
        } else {
            //Winner case
            view.displayRoundWinner(winningPlayer.toString(), model.getRoundNumber());
            view.displayWinningCard(model.getWinningCard());
        }

        //Check for eliminations
        ArrayList<Player> eliminatedPlayers = model.checktoEliminate();
        for(Player player: eliminatedPlayers) {
            view.displayEliminatedPlayer(player.toString());
        }
    }

    private boolean isPlayerAI(Player player) {
        return (player instanceof AIPlayer);
    }

    @Override
    public void quit() {
        database.disconnect();
        System.exit(0);
    }
}
