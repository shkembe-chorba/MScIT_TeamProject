package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    @DisplayName("Check if the game statistics are uploaded correctly to the database.")
    void uploadGameStats() {
        try {
            //Connect to database.
            Database.connect();

            //Get the database connection.
            Connection conn = Database.connection;

            //Turn off autoCommit for test.
            conn.setAutoCommit(false);

            //Set a savepoint to roll back to after test.
            Savepoint savepoint1 = conn.setSavepoint("Savepoint1");

            //Create statement.
            Statement statement = conn.createStatement();

            //Empty rounds_won relation for test.
            String sqlString = "delete from rounds_won;";
            statement.addBatch(sqlString);

            //Empty game relation for test.
            sqlString = "delete from game;";
            statement.addBatch(sqlString);
            statement.executeBatch();

            //Set up players for test.
            Player user = new Player("USER");
            Player ai1 = new Player("AI1");
            Player ai2 = new Player("AI2");
            Player ai3 = new Player("AI3");
            Player ai4 = new Player("AI4");

            //Set up player array for test.
            Player[] players = {user, ai1, ai2, ai3, ai4};

            //Set the roundsWon for the players in the first game for the test.
            user.setRoundsWon(2);
            ai1.setRoundsWon(8);
            ai2.setRoundsWon(3);
            ai3.setRoundsWon(3);
            ai4.setRoundsWon(4);

            //Upload the first game to the database.
            Database.uploadGameStats(2,20,"AI1",players);

            //Set the roundsWon for the players in the second game for the test.
            user.setRoundsWon(5);
            ai1.setRoundsWon(3);
            ai2.setRoundsWon(2);
            ai3.setRoundsWon(3);
            ai4.setRoundsWon(2);

            //Upload the second game to the database.
            Database.uploadGameStats(3,15,"USER",players);

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

            //Roll back to savepoint1.
            conn.rollback(savepoint1);

            //Disconnect from database.
            Database.disconnect();

            //Make Strings for the expected data and the data from the database.
            String data = game + rounds_won;
            String data_expected = game_expected + rounds_won_expected;

            //Compare the data from the database to the expected data.
            assertEquals(data_expected, data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}