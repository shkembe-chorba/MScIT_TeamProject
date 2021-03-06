package commandline.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * A utility for reading JSON files into managable POJOs.
 */
public class JsonUtility {

    /**
     * Returns a JsonObject from a file path.
     *
     * @param file the path to the .json file
     * @throws IllegalArgumentException if the file is not a .json file
     * @return a JsonObject
     */
    public static JsonObject getJsonObjectFromFile(String path) throws IOException {

        File file = new File(path);
        // Check if is valid file, otherwise throw
        if (file.isFile() && getExtension(file).equals("json")) {
            FileReader fileReader = new FileReader(file);
            JsonReader jsonReader = new JsonReader(fileReader);
            return JsonParser.parseReader(jsonReader).getAsJsonObject();
        } else {
            throw new IllegalArgumentException("File is not a valid .json file");
        }
    }

    private static String getExtension(File path) {
        String extension = "";
        String pathString = path.toString();

        int extensionIndex = pathString.indexOf('.');
        if (extensionIndex > 0) {
            extension = pathString.substring(extensionIndex + 1);
        }
        return extension;
    }
}
