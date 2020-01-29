package commandline.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LoggerTest {

    private static final String TEST_FILE_PATH_STRING = "./testLogger.log";

    @AfterEach
    private void removeFilesCreated() {
        File testFile = new File(TEST_FILE_PATH_STRING);
        testFile.delete();
    }

    @Nested
    public class Enabled {
        @DisplayName("Correct new file is created when enabled")
        @Test
        public void newFileWhenEnabled() {

            File outputFile = new File(TEST_FILE_PATH_STRING);
            Logger logger = new Logger(TEST_FILE_PATH_STRING);

            assertFalse(outputFile.exists());

            try {
                logger.enable();
            } catch (IOException e) {
                fail("I/O error during test");
            }

            assertTrue(outputFile.exists());
        }

        @DisplayName("Logger can be used statically when enabled")
        @Test
        void canBeUsedStatically() {
            Logger logger = new Logger(TEST_FILE_PATH_STRING);

            Logger.log("Test");
        }


        @DisplayName("Each entry creates the message with a divider after it")
        @Test
        void logEntryCorrectFormat() {
            Logger logger = new Logger(TEST_FILE_PATH_STRING);

            try {
                logger.enable();
            } catch (IOException e) {
                fail("I/O error during test");
            }

            String expectedOutput = "Test\n" + Logger.LOGGER_DIVIDER_STRING + "\n";

            String fileContent = "";
            try {
                fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH_STRING)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Logger.log("Test");

            assertEquals(expectedOutput, fileContent);
        }

    }


    // @Nested
    // private class Disabled {
    // @DisplayName("No file is made when logger is disabled")
    // @Test

    // @DisplayName("Logger is silent when used statically but disabled")
    // @Test
    // }



}
