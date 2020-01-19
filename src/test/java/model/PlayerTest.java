package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Unit test for simple App.
 */
public class PlayerTest {

    @Nested
    @DisplayName("Unsupported Names")
    class UnsupportedNames {

        final public String emptyName = "";
        final public String longName = "ThisIsSuchACrazyNameIAmSureItWillNotWork";
        private Player testPlayer;

        @BeforeEach
        void init() {
            testPlayer = new Player("Gonzo");
        }

        @Test
        @DisplayName("A player must have a name which isn't an empty string.")
        void playerIsPassedAnEmptyString() {

            assertThrows(IllegalArgumentException.class, () -> {
                new Player(emptyName);
            }, "Constructor fails");

            assertThrows(IllegalArgumentException.class, () -> {
                testPlayer.setName(emptyName);
            }, "setName fails");
        }

        @Test
        @DisplayName("The name must be below 32 characters for database storage")
        void nameLengthGreaterThan32() {

            assertThrows(IllegalArgumentException.class, () -> {
                new Player(longName);
            }, "Constructor fails");

            assertThrows(IllegalArgumentException.class, () -> {
                testPlayer.setName(longName);
            }, "setName fails");
        }

    }
}
