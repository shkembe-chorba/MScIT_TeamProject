package commandline.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * A utility for reading JSON files into managable POJOs.
 */
public class JsonUtility {

    /**
     * Returns a JsonObject from a file path.
     *
     * @param path the path to the .json file
     * @return a JsonObject
     */
    public static JsonObject getJsonObjectFromFile(File path) throws IOException {
        JsonReader jsonReader = new JsonReader(new FileReader(path));
        return JsonParser.parseReader(jsonReader).getAsJsonObject();
    }
}
