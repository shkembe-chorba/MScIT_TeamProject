package commandline.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private String getTestFileContents() {
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH_STRING)));
            System.out.println(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    @Nested
    class Enabled {

        @DisplayName("Correct new file is created when enabled")
        @Test
        public void newFileWhenEnabled() {

            File outputFile = new File(TEST_FILE_PATH_STRING);
            Logger logger = new Logger(TEST_FILE_PATH_STRING);

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
            String expectedOutput = "Test\n" + Logger.LOGGER_DIVIDER_STRING + "\n";

            Logger logger = new Logger(TEST_FILE_PATH_STRING);

            try {
                logger.enable();
            } catch (IOException e) {
                fail("I/O error during test");
            }

            Logger.log("Test");

            assertEquals(expectedOutput, getTestFileContents());
        }

    }


    @Nested
    class NotEnabled {
        private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        private final PrintStream originalErr = System.err;

        @BeforeEach
        public void setUpStreams() {
            System.setErr(new PrintStream(errContent));
        }

        @AfterEach
        public void restoreStreams() {
            System.setErr(originalErr);
        }

        @DisplayName("Logger does not throw when used statically but disabled")
        @Test
        void loggerIsSilentWhenNotEnabled() {
            Logger logger = new Logger(TEST_FILE_PATH_STRING);
            Logger.log("This should not throw");
        }

        @DisplayName("No default behaviour to System.err")
        @Test
        void noOutputToSystemDotOut() {
            Logger logger = new Logger(TEST_FILE_PATH_STRING);
            Logger.log("Test");
            assertEquals("", errContent.toString());
        }

        @DisplayName("No file is made when logger is not enabled")
        @Test
        void noFileUntilEnabled() {

            Logger logger = new Logger(TEST_FILE_PATH_STRING);
            File outputFile = new File(TEST_FILE_PATH_STRING);

            assertFalse(outputFile.exists());
        }
    }
}
