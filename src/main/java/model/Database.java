package model;

import java.sql.*;


public class Database {

    static Connection connection = null;

    //Database Connection parameters.
    private static String URL = "jdbc:postgresql://localhost:5432/m_19_2175499m";
    private static String USER = "postgres";
    private static String PASS = "123456";

    //REMOVE
    public static void main(String[] args) {
        connect();

        // tests for uploading to database
        /*uploadGameStats(2, 3, "USER",
                new Player[]{
                        new Player("USER"),
                        new Player("AI1"),
                        new Player("AI2"),
                        new Player("AI3"),
                        new Player("AI4")
                }
        );*/
        // tests for retrieving stats from database
        final RetrievedGameStatistics statistics = retrieveGameStats();

        disconnect();
    }

    /**
     * This methods sets up a connection to the PostgreSQL database.
     * It needs to be run prior to any other methods.
     */
    public static void connect() {
        try {

            //Get connection.
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to database.");

        } catch (SQLException e) {
            System.out.println("Database connection failure.");
            e.printStackTrace();
        }
    }

    /**
     * This method closes the connection to the PostgreSQL database.
     * It needs to be run after a code-user has finished with the Database class.
     */
    public static void disconnect() {
        try {
            connection.close();
            System.out.println("Disconnected from database.");
        } catch (SQLException e) {
            System.out.println("Database disconnect failure.");
            e.printStackTrace();
        }
    }

    /**
     * This method uploads the persistent game statistics to the PostgreSQL database.
     *
     * @param draws   the number of draws in the game
     * @param rounds  the number of rounds in the game
     * @param winner  the winner of the game
     * @param players an array holding all of the players in the game
     */
    public static void uploadGameStats(int draws, int rounds, String winner, Player[] players) {
        try {
            //Create statement.
            Statement statement = connection.createStatement();

            //Get max gid up to now from the database
            //and make the gid variable larger by 1
            String sqlString = "SELECT max(gid) FROM game";
            ResultSet result = statement.executeQuery(sqlString);
            result.next();
            int gid = result.getInt("max") + 1;

            //Add a statement to update the game relation in the statement batch.
            sqlString = "INSERT INTO game(gid,draws,rounds,winner) VALUES (" + gid + "," + draws + "," + rounds + ",'" + winner + "'); ";
            statement.addBatch(sqlString);

            //Add statements to update the rounds_won relation in the statement batch.
            for (Player player : players) {
                sqlString = "INSERT INTO rounds_won(gid,player,r_won) VALUES (" + gid + ",'" + player.getName() + "'," + player.getRoundsWon() + "); ";
                statement.addBatch(sqlString);
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method creates an object that contains the retrieved overall game stats (5 metrics)
     * @return object containing stats
     */
    public static RetrievedGameStatistics retrieveGameStats() {
        RetrievedGameStatistics retrievedGameStatistics = null;
        try {
            int gamesPlayed, aiWins, userWins, maxRounds;
            double avgDraws;

            Statement statement = connection.createStatement();

            //Obtains the number of games played overall
            String sqlStringMaxGid = "SELECT max(gid) FROM game";
            ResultSet result = statement.executeQuery(sqlStringMaxGid);
            result.next();
            gamesPlayed = result.getInt("max");

            //Obtains the number of games won by AIs
            String sqlStringAIWins = "select count(winner) from game where winner!='USER' ";
            result = statement.executeQuery(sqlStringAIWins);
            result.next();
            aiWins = result.getInt("count");

            //Obtains the number of games won by the human user
            String sqlStringUserWins = "select count(winner) from game where winner='USER'";
            result = statement.executeQuery(sqlStringUserWins);
            result.next();
            userWins = result.getInt("count");

            //Obtains the average draw number
            String sqlStringAvgDraws = "select avg(draws) from game";
            result = statement.executeQuery(sqlStringAvgDraws);
            result.next();
            avgDraws = result.getDouble("avg");

            //Obtains the max number of rounds
            String sqlStringMaxRounds = "select max(rounds) from game";
            result = statement.executeQuery(sqlStringMaxRounds);
            result.next();
            maxRounds = result.getInt("max");

            retrievedGameStatistics = new RetrievedGameStatistics(gamesPlayed, aiWins, userWins, avgDraws, maxRounds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retrievedGameStatistics;
    }

}
