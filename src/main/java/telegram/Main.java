package telegram;

import com.google.gson.Gson;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws TelegramApiException {

        System.out.println("Start !\n");

/*      //  Проверка JSON
        ArrayList<String> myList = new ArrayList<>();

        myList.add("Hello, ");
        myList.add("world !");

        Gson gson = new Gson();

        String JSON = gson.toJson(myList);

        System.out.println(JSON);
        System.out.println(gson.fromJson(JSON, ArrayList.class));

*/


        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Bot myBot = new Bot();


        try {
            telegramBotsApi.registerBot(myBot);
        }
        catch (TelegramApiRequestException e) {
            System.out.println("Ошибка при регистрации бота : " + e.toString());
        }
        catch (Exception n) {
            System.out.println("Ошибка неустановленной природы при запросе к боту : " + n.toString());
        }


        //  Тестовый блок
        System.out.println("\nBot name :  " + myBot.getBotUsername());
        System.out.println("Bot token :  " + myBot.getBotToken());


        System.out.println("\nПодключитесь к боту Telegram  " + myBot.getBotUsername() + "  и напишите ему что-нибудь (например, просто отправьте смайлик) !");


    }


}
