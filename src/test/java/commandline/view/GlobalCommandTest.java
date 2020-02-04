package commandline.view;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class GlobalCommandTest {

    @DisplayName("Constructor")
    @Nested
    public class Constructor {

        @DisplayName("Throws if not one word")
        @ParameterizedTest
        @ValueSource(strings = {"", "test test"})
        public void throwsIfInvalidCommand(String command) {
            assertThrows(IllegalArgumentException.class, () -> {
                new GlobalCommand(command);
            });
        }

        @DisplayName("Accepts one word commands")
        @Test
        public void acceptsValidCommand() {
            new GlobalCommand("test");
        }
    }

    @DisplayName("notifyCommandListeners()")
    @Nested
    public class Listeners {
        @DisplayName("Listener notified")
        @Test
        public void globalCommandInputNotifiesListeners() {

            GlobalCommandListenerTest gcl = new GlobalCommandListenerTest();
            GlobalCommand gc = new GlobalCommand("Test");

            assertFalse(gcl.triggered);

            gc.addCommandListener(gcl);
            gc.notifyCommandListeners();

            assertTrue(gcl.triggered);
        }
    }

    @DisplayName("equals()")
    @Nested
    public class Equals {
        @DisplayName("Equals is true for same command")
        @Test
        public void equalsWhenCommandIsSame() {
            GlobalCommand gc1 = new GlobalCommand("Test");
            GlobalCommand gc2 = new GlobalCommand("Test", "But with a description");

            assertTrue(gc1.equals(gc2));
        }

        @DisplayName("Equals is false for different commands")
        @Test
        public void notEqualsWhenCommandIsDifferent() {
            GlobalCommand gc1 = new GlobalCommand("Test", "description equal");
            GlobalCommand gc2 = new GlobalCommand("Test2", "description equal");

            assertFalse(gc1.equals(gc2));
        }
    }
}
