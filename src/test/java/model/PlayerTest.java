package model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Example test - Note: Not confirmed for production 
 * TODO: Update
 */
public class PlayerTest {

    @Nested
    @DisplayName("Unsupported Names")
    public class UnsupportedNames {

        final public String emptyName = "";
        final public String longName = "ThisIsSuchACrazyNameIAmSureItWillNotWork";
        private Player testPlayer;

        @BeforeEach
        public void init() {
            testPlayer = new Player("Gonzo");
        }

        @Test
        @DisplayName("A player must have a name which isn't an empty string.")
        public void playerIsPassedAnEmptyString() {

            assertThrows(IllegalArgumentException.class, () -> {
                new Player(emptyName);
            }, "Constructor fails");

            assertThrows(IllegalArgumentException.class, () -> {
                testPlayer.setName(emptyName);
            }, "setName fails");
        }

        @Test
        @DisplayName("The name must be below 32 characters for database storage")
        public void nameLengthGreaterThan32() {

            assertThrows(IllegalArgumentException.class, () -> {
                new Player(longName);
            }, "Constructor fails");

            assertThrows(IllegalArgumentException.class, () -> {
                testPlayer.setName(longName);
            }, "setName fails");
        }

    }
}
