package model;

import java.sql.*;

/**
 * This class holds the database functionality for the TopTrumpsGame.
 */
public class Database {

    // Default database Connection parameters.
    private static final String URL = "jdbc:postgresql://localhost:5432/m_19_2175499m";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    private Connection connection = null;

    // Database Connection parameters.
    private String url;
    private String user;
    private String pass;

    /**
     * Database constructor.
     *
     * @param url  database url
     * @param user database username
     * @param pass database password
     */
    public Database(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    /**
     * Default database constructor for TopTrumps.
     */
    public Database() {
        this(URL, USER, PASS);
    }

    /**
     * Getter for the database connection.
     *
     * @return the database connection
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * This methods sets up a connection to the PostgreSQL database. It needs to be run prior to any
     * other methods.
     */
    public void connect() throws SQLException {

        // Get connection.
        connection = DriverManager.getConnection(url, user, pass);
        // System.out.println("Connected to database.");

    }

    /**
     * This method closes the connection to the PostgreSQL database. It needs to be run after a
     * code-user has finished with the Database class.
     */
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
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
    public void uploadGameStats(int draws, int rounds, String winner, Player[] players) {
        try {
            // Create statement.
            Statement statement = connection.createStatement();

            // Default gid for first game.
            int gid = 1;

            // Get max gid up to now from the database
            // and make the gid variable larger by 1
            String sqlString = "SELECT max(gid) FROM game";
            ResultSet result = statement.executeQuery(sqlString);

            if (result.next()) {
                gid = result.getInt("max") + 1;
            }
            // Add a statement to update the game relation in the statement batch.
            sqlString = "INSERT INTO game(gid,draws,rounds,winner) VALUES (" + gid + "," + draws
                    + "," + rounds + ",'" + winner + "'); ";
            statement.addBatch(sqlString);

            // Add statements to update the rounds_won relation in the statement batch.
            for (Player player : players) {
                sqlString = "INSERT INTO rounds_won(gid,player,r_won) VALUES (" + gid + ",'"
                        + player.toString() + "'," + player.getRoundsWon() + "); ";
                statement.addBatch(sqlString);
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method creates an object that contains the retrieved overall game stats (5 metrics)
     *
     * @return object containing stats
     */
    public RetrievedGameStatistics retrieveGameStats() {
        RetrievedGameStatistics retrievedGameStatistics = null;
        try {
            int gamesPlayed = 0;
            int aiWins = 0;
            int userWins = 0;
            int maxRounds = 0;
            double avgDraws = 0;

            Statement statement = connection.createStatement();

            // Obtains the number of games played overall
            String sqlStringMaxGid = "SELECT max(gid) FROM game";
            ResultSet result = statement.executeQuery(sqlStringMaxGid);
            if (result.next()) {
                gamesPlayed = result.getInt("max");
            }

            // Obtains the number of games won by AIs
            String sqlStringAIWins = "select count(winner) from game where winner!='USER' ";
            result = statement.executeQuery(sqlStringAIWins);
            if (result.next()) {
                aiWins = result.getInt("count");
            }

            // Obtains the number of games won by the human user
            String sqlStringUserWins = "select count(winner) from game where winner='USER'";
            result = statement.executeQuery(sqlStringUserWins);
            if (result.next()) {
                userWins = result.getInt("count");
            }

            // Obtains the average draw number
            String sqlStringAvgDraws = "select avg(draws) from game";
            result = statement.executeQuery(sqlStringAvgDraws);
            if (result.next()) {
                avgDraws = result.getDouble("avg");
            }

            // Obtains the max number of rounds
            String sqlStringMaxRounds = "select max(rounds) from game";
            result = statement.executeQuery(sqlStringMaxRounds);
            if (result.next()) {
                maxRounds = result.getInt("max");
            }

            retrievedGameStatistics =
                    new RetrievedGameStatistics(gamesPlayed, aiWins, userWins, avgDraws, maxRounds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retrievedGameStatistics;
    }

}
