package telegram;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ResultsJsonSaver {

private Messages msg = new Messages();

    ResultsJsonSaver() {
    }


    public synchronized <S, T> void saveResults(Long chatId, Map<S, T> resultsOfUser) throws Exception {

        String pathToFile = msg.PATH_TO_FILES + String.valueOf(chatId) + msg.JSON_FILE;

        Gson gson = new Gson();

        String jsonResults = gson.toJson(resultsOfUser);

        try {
            FileWriter writer = new FileWriter(pathToFile);
            writer.write(jsonResults);
            writer.close();
        } catch (IOException e) {
            System.out.println("Что-то с записью в файл " + pathToFile + " пошло не так !");
        }

    }

}
