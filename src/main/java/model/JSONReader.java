package model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JSONReader {
    ObjectMapper objectMapper = new ObjectMapper();

    File file = new File("MScIT_TeamProject\\TopTrumps.json");
            Pile pile;

    {
        try {
            pile = objectMapper.readValue(file, Pile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
