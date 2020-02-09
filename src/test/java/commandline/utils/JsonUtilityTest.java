package commandline.utils;

import java.io.File;
import java.io.IOException;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilityTest {


    @DisplayName("getJsonObjectFromFile()")
    @Nested
    class GetJsonObjectFromFile {

        private static final String TEST_RESOURCE_DIRECTORY =
                "/src/test/resources/commandline/utils/";
        private static final String TEST_RESOURCE_FILE = "test.json";

        @DisplayName("Take a file path object and return a JsonObject")
        @Test
        void returnsJsonObject() {

            // Load test file
            File path = new File(TEST_RESOURCE_DIRECTORY, TEST_RESOURCE_FILE);

            JsonObject json = null;

            try {
                json = JsonUtility.getJsonObjectFromFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertTrue(json.get("test").getAsBoolean());

        }
    }



}
