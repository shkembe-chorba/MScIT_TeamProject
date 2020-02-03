package commandline.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

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
}
