package commandline.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import com.google.gson.JsonObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * JsonUtility Tests
 */
@DisplayName("JsonUtility")
public class JsonUtilityTest {

    private static final String TEST_RESOURCE_DIRECTORY = "src/test/resources/commandline/utils/";

    private static final String TEST_JSON_FILE = "test.json";
    private static final String TEST_TEXT_FILE = "test.txt";

    @DisplayName("getJsonObjectFromFile()")
    @Nested
    class GetJsonObjectFromFile {

        /*
         * Validation Tests
         */

        @DisplayName("Take a file path object and return a JsonObject")
        @Test
        void returnsJsonObject() {

            File testFilePath = new File(TEST_RESOURCE_DIRECTORY, TEST_JSON_FILE);

            JsonObject json = null;
            try {
                json = JsonUtility.getJsonObjectFromFile(testFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertTrue(json.get("test").getAsBoolean());
        }

        /*
         * Defect Tests
         */

        @DisplayName("Throw if file does not exist")
        @Test
        void throwIfFileDoesNotExist() {
            // Generate a garbage test file
            File nonExistentFile = new File(UUID.randomUUID().toString());

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    JsonUtility.getJsonObjectFromFile(nonExistentFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        @DisplayName("Throw if file is not json")
        @Test
        void throwIfFileIsNotJson() {
            // Pass a .txt file
            File testFile = new File(TEST_RESOURCE_DIRECTORY, TEST_TEXT_FILE);

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    JsonUtility.getJsonObjectFromFile(testFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
