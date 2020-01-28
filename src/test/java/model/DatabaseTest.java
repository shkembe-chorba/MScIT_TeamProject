package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseTest {

    private Connection conn;
    private Savepoint savepoint;

    private Player user;
    private Player ai1;
    private Player ai2;
    private Player ai3;
    private Player ai4;

    private Player[] players;

    @BeforeEach
    void setUp() throws Exception {
        //Connect to database.
        Database.connect();

        //Get the database connection.
        conn = Database.getConnection();

        //Turn off autoCommit for test.
        conn.setAutoCommit(false);

        //Set a savepoint to roll back to after test.
        savepoint = conn.setSavepoint("Savepoint1");

        //Create statement.
        Statement statement = conn.createStatement();

        //Empty rounds_won relation for test.
        String sqlString = "delete from rounds_won;";
        statement.addBatch(sqlString);

        //Empty game relation for test.
        sqlString = "delete from game;";
        statement.addBatch(sqlString);
        statement.executeBatch();

        //Set up mock players for test.
        user = mock(Player.class);
        ai1 = mock(Player.class);
        ai2 = mock(Player.class);
        ai3 = mock(Player.class);
        ai4 = mock(Player.class);

        //Set up player array for test.
        players = new Player[] {user, ai1, ai2, ai3, ai4};

        //Set up names of players.
        when(user.toString()).thenReturn("USER");
        when(ai1.toString()).thenReturn("AI1");
        when(ai2.toString()).thenReturn("AI2");
        when(ai3.toString()).thenReturn("AI3");
        when(ai4.toString()).thenReturn("AI4");

        //Set the roundsWon for the players in the first game for the test.
        when(user.getRoundsWon()).thenReturn(2);
        when(ai1.getRoundsWon()).thenReturn(8);
        when(ai2.getRoundsWon()).thenReturn(3);
        when(ai3.getRoundsWon()).thenReturn(3);
        when(ai4.getRoundsWon()).thenReturn(4);

        //Upload the first game to the database.
        Database.uploadGameStats(2, 20, "AI1", players);

        //Set the roundsWon for the players in the second game for the test.
        when(user.getRoundsWon()).thenReturn(5);
        when(ai1.getRoundsWon()).thenReturn(3);
        when(ai2.getRoundsWon()).thenReturn(2);
        when(ai3.getRoundsWon()).thenReturn(3);
        when(ai4.getRoundsWon()).thenReturn(2);

        //Upload the second game to the database.
        Database.uploadGameStats(3, 15, "USER", players);
    }

    @AfterEach
    void tearDown() throws Exception {
        //Roll back to savepoint1.
        conn.rollback(savepoint);

        //Disconnect from database.
        Database.disconnect();
    }

    /**
     * @throws Exception this is done because if we do a try-catch block our test reports a false positive when an exception is caught.
     *                   An alternative to the throws exception would be an Assert.fail() in the catch block.
     */
    @Test
    @DisplayName("Check if the game statistics are uploaded correctly to the database.")
    void uploadGameStats() throws Exception {
        //Create statement.
        Statement statement = conn.createStatement();

        //Set up the expected game relation as a String.
        String game_expected = "1 2 20 AI1\n" +
                "2 3 15 USER\n";
        //Set up the expected rounds_won relation as a String.
        String rounds_won_expected = "1 USER 2\n" +
                "1 AI1 8\n" +
                "1 AI2 3\n" +
                "1 AI3 3\n" +
                "1 AI4 4\n" +
                "2 USER 5\n" +
                "2 AI1 3\n" +
                "2 AI2 2\n" +
                "2 AI3 3\n" +
                "2 AI4 2\n";

        //Get the game relation from the database as a String.
        String game = "";
        ResultSet resultSet = statement.executeQuery("SELECT * FROM game");
        while (resultSet.next()) {
            game += resultSet.getString("gid") + " " + resultSet.getString("draws") + " " +
                    resultSet.getString("rounds") + " " + resultSet.getString("winner") + "\n";
        }

        //Get the rounds_won relation from the database as a String.
        String rounds_won = "";
        resultSet = statement.executeQuery("SELECT * FROM rounds_won");
        while (resultSet.next()) {
            rounds_won += resultSet.getString("gid") + " " + resultSet.getString("player") + " " +
                    resultSet.getString("r_won") + "\n";
        }

        //Make Strings for the expected data and the data from the database.
        String data = game + rounds_won;
        String data_expected = game_expected + rounds_won_expected;

        //Compare the data from the database to the expected data.
        assertEquals(data_expected, data);
    }

    /**
     * @throws Exception this is done because if we do a try-catch block our test reports a false positive when an exception is caught.
     *                   An alternative to the throws exception would be an Assert.fail() in the catch block.
     */
    @Test
    @DisplayName("Check if the returned game statistics are as expected.")
    void checkReturnedGameStats() throws Exception {
        //Set the roundsWon for the players in the second game for the test.
        when(user.getRoundsWon()).thenReturn(7);
        when(ai1.getRoundsWon()).thenReturn(1);
        when(ai2.getRoundsWon()).thenReturn(2);
        when(ai3.getRoundsWon()).thenReturn(3);
        when(ai4.getRoundsWon()).thenReturn(0);

        //Upload the third game to the database.
        Database.uploadGameStats(5, 13, "AI3", players);

        final RetrievedGameStatistics expectedStatistics =
                new RetrievedGameStatistics(3, 2, 1, 3.333, 20);
        final RetrievedGameStatistics returnedStatistics = Database.retrieveGameStats();

        assertEquals(expectedStatistics.getAvgDraws(), returnedStatistics.getAvgDraws(), 0.01); // 0.01 is the error margin
        assertEquals(expectedStatistics.getGamesWonByAi(), returnedStatistics.getGamesWonByAi());
        assertEquals(expectedStatistics.getGamesWonByUser(), returnedStatistics.getGamesWonByUser());
        assertEquals(expectedStatistics.getMaxRounds(), returnedStatistics.getMaxRounds());
        assertEquals(expectedStatistics.getTotalGamesPlayed(), returnedStatistics.getTotalGamesPlayed());
    }
}