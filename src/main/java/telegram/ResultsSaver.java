package telegram;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ResultsSaver {


    public <S, T> void saveResults(Long chatID, Map<S, T> resultsOfUser) throws IOException {

        String pathToFile ="src/main/java/telegram/json_results/user_" + String.valueOf(chatID) + ".json";

        Gson gson = new Gson();

        String jsonResults = gson.toJson(resultsOfUser);

        try {
            FileWriter writer = new FileWriter(pathToFile);
            writer.write(jsonResults);
            writer.close();
        }
        catch (Exception ex) {
            System.out.println("Что-то с записью в файл " + pathToFile + " пошло не так !");
        }

    }

}
