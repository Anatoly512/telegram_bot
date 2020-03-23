package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    String botUserName = "questSuper_Bot";
    String botToken = "904172873:AAG1PY5RZnAwAYFHB-McdI8ogWvjzcBNPto";

    Long chat_id;
    String chat_id_String;

    boolean ifFirstMessage = true;

    private final String VARIANT_1 = "Variant 1";
    private final String VARIANT_2 = "Variant 2";
    private final String VARIANT_3 = "Variant 3";
    private final String VARIANT_4 = "Variant 4";

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();

    Bot() {
        keyboardMarkup.setResizeKeyboard(true);              //  Настройка в конструкторе пользовательской клавиатуры
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();

        firstRow.add(VARIANT_1);
        firstRow.add(VARIANT_2);
        secondRow.add(VARIANT_3);
        secondRow.add(VARIANT_4);

        keyboard.add(firstRow);
        keyboard.add(secondRow);

        keyboardMarkup.setKeyboard(keyboard);
    }


    @Override
    public void onUpdateReceived(Update update) {            // update содержит сообщение от пользователя
        String message = update.getMessage().getText();

        chat_id = update.getMessage().getChatId();
        chat_id_String = chat_id.toString();

        try {
            //  Тестовая строка
            sendMsg(chat_id_String, message);     //  Отправляет сообщение назад (нужно для тестирования)


            //  Если это самое первое сообщение от пользователя
            if (message != null && update.getMessage().hasText()) {
                if (ifFirstMessage) {
                    sendMsg(chat_id_String, "❤");
                    sendMsg(chat_id_String, "Спасибо, что подключились к нам !  \uD83D\uDE04 ");
                    ifFirstMessage = false;
                }
            }

            //  Тестовая строка
            System.out.println("\nChat id  :  " + chat_id + "\n" + update);



            if (message.equals("/start")) {
                sendMsg(chat_id_String, "Hello, world !!!  Starting conversation !!!");
            }

            if (message.equals("/help")) {
                sendMsg(chat_id_String, "Hello !   I'll try to help you !");
            }



            if (message.equals(VARIANT_1)) {
                sendMsg(chat_id_String, "Processing  Variant 1   !");
            }

            if (message.equals(VARIANT_2)) {
                sendMsg(chat_id_String, "Processing  Variant 2   !");
            }

            if (message.equals(VARIANT_3)) {
                sendMsg(chat_id_String, "Processing  Variant 3   !");
            }

            if (message.equals(VARIANT_4)) {
                sendMsg(chat_id_String, "Processing  Variant 4   !");
            }


        }
        catch (TelegramApiException e) {
            System.out.println("Ошибка при приеме сообщения от пользователя : " + e.toString());
        }
        catch (Exception n) {
            System.out.println("Ошибка неустановленной природы при приеме сообщения : " + n.toString());
        }
    }



    public synchronized void sendMsg(String chatId, String messageString) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageString);
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            System.out.println("Ошибка при отправке сообщения : " + e.toString());
        }
        catch (Exception n) {
            System.out.println("Ошибка неустановленной природы (при попытке отправить сообщение) : " + n.toString());
        }
    }





    @Override
    public String getBotUsername() {

        return botUserName;
    }


    @Override
    public String getBotToken() {

        return botToken;
    }


    public String getChat_id() {       //  Вдруг понадобится

        return chat_id_String;
    }

}
