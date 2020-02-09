package commandline.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import commandline.view.IOStreamTest;

/**
 * Logger tests
 */
public class LoggerTest {

    /*
     * Setup
     */

    // Creates a temporary test log in the current directory
    private static final String TEST_FILE_PATH_STRING = "./testLogger.log";

    // A helper function for getting the contents of the test log
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

    // Delete the temporary test log after each test
    @AfterEach
    private void removeFilesCreated() {
        File testFile = new File(TEST_FILE_PATH_STRING);
        testFile.delete();
    }

    @Nested
    @DisplayName("When the logger has been enabled")
    class Enabled {

        /*
         * Validation tests
         */

        @DisplayName("A test log file is created when enabled")
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

        @DisplayName("Logger can be used statically when enabled and in app scope")
        @Test
        void canBeUsedStatically() {
            // Create in scope
            Logger logger = new Logger(TEST_FILE_PATH_STRING);
            Logger.log("Test");
        }


        @DisplayName("Each entry creates the message with a divider after it")
        @Test
        void logEntryCorrectFormat() {

            // Expect that the divider string is given after each log.
            String expectedOutput = "Test\n" + Logger.LOGGER_DIVIDER_STRING + "\n";

            // Create in scope
            Logger logger = new Logger(TEST_FILE_PATH_STRING);

            try {
                logger.enable();
            } catch (IOException e) {
                fail("I/O error during test");
            }

            Logger.log("Test");

            assertEquals(expectedOutput, getTestFileContents());
        }

        @DisplayName("Can provide a header to each log easily")
        @Test
        void logEntryCorrectFormatWithHeader() {

            // Expect that a header that is one space above the message can be provided
            String expectedOutput = "Header\n\n" + "Test\n" + Logger.LOGGER_DIVIDER_STRING + "\n";

            // Create in scope
            Logger logger = new Logger(TEST_FILE_PATH_STRING);

            try {
                logger.enable();
            } catch (IOException e) {
                fail("I/O error during test");
            }

            Logger.log("Header", "Test");

            assertEquals(expectedOutput, getTestFileContents());
        }
    }

    // This class extends IOStreamTest to allow access to streams
    @Nested
    @DisplayName("When the logger is not enabled")
    class NotEnabled extends IOStreamTest {

        /*
         * Defect tests
         */

        @DisplayName("Logger does not throw when used statically but disabled")
        @Test
        void loggerIsSilentWhenNotEnabled() {
            // Create in scope
            Logger logger = new Logger(TEST_FILE_PATH_STRING);
            Logger.log("This should not throw");
        }

        @DisplayName("No default behaviour to System.err")
        @Test
        void noOutputToSystemDotOut() {
            // Create in scope
            Logger logger = new Logger(TEST_FILE_PATH_STRING);
            Logger.log("Test");
            assertEquals("", getErr().toString());
        }

        @DisplayName("No file is made when logger is not enabled")
        @Test
        void noFileUntilEnabled() {
            // Create in scope
            Logger logger = new Logger(TEST_FILE_PATH_STRING);
            File outputFile = new File(TEST_FILE_PATH_STRING);

            assertFalse(outputFile.exists());
        }
    }
}
