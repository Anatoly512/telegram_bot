package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    String botUserName = "questSuper_Bot";
    String botToken = "904172873:AAG1PY5RZnAwAYFHB-McdI8ogWvjzcBNPto";

    Long chatId;

    Messages messageStrings = new Messages();
    boolean ifFirstMessage = true;

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

Bot() {
    List<KeyboardRow> keyboard = new ArrayList<>();    //  Настройка в конструкторе пользовательской клавиатуры
    keyboardMarkup.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();

    row.add(messageStrings.BUTTON_1_CREATE_WHEEL);
    row.add(messageStrings.BUTTON_2_HELP);

    keyboard.add(row);
    keyboardMarkup.setKeyboard(keyboard);
}


    @Override
    public void onUpdateReceived(Update update) {            // update содержит сообщение от пользователя
        String message = update.getMessage().getText();

        chatId = update.getMessage().getChatId();

        try {
            sendMsg(chatId, message);     //  Отправляет сообщение назад (нужно для тестирования)

            //  Тестовая строка
            System.out.println("\nChat id  :  " + chatId + "\n" + update);


            //  Если это самое первое сообщение от пользователя
            if (message != null && update.getMessage().hasText()) {
                if (ifFirstMessage) {
                    sendMsg(chatId, messageStrings.SMILE);
                    sendMsg(chatId, messageStrings.THANK_YOU);
                    sendMsg(chatId, messageStrings.GREETENG_MESSAGE);

                    ifFirstMessage = false;
                }
            }


            if (message.equals("/start")) {      //  Возможность печатать команду с клавиатуры
                sendMsg(chatId, messageStrings.GREETENG_MESSAGE);
            }

            if (message.equals("/help") || (message.equals(messageStrings.BUTTON_2_HELP))) {
                sendMsg(chatId, messageStrings.HELP);
            }



            if (message.equals(messageStrings.BUTTON_1_CREATE_WHEEL)) {

                keyboardMarkup.setKeyboard(keyboardMarkupNew());

                sendMsg(chatId, messageStrings.START);

                //  Здесь идет логика составления колеса жизненного баланса



            }






        }
        catch (TelegramApiException e) {
            System.out.println("Ошибка при приеме сообщения от пользователя : " + e.toString());
        }
        catch (Exception n) {
            System.out.println("Ошибка неустановленной природы при приеме сообщения : " + n.toString());
        }
    }



    public synchronized void sendMsg(Long chatId, String messageString) throws TelegramApiException {
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



    private List<KeyboardRow> keyboardMarkupNew() {

        List<KeyboardRow> keyboardNew = new ArrayList<>();       //  Настройка новой пользовательской клавиатуры
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();

        firstRow.add(messageStrings.BUTTON_1);
        firstRow.add(messageStrings.BUTTON_2);
        firstRow.add(messageStrings.BUTTON_3);
        firstRow.add(messageStrings.BUTTON_4);
        firstRow.add(messageStrings.BUTTON_5);
        secondRow.add(messageStrings.BUTTON_6);
        secondRow.add(messageStrings.BUTTON_7);
        secondRow.add(messageStrings.BUTTON_8);
        secondRow.add(messageStrings.BUTTON_9);
        secondRow.add(messageStrings.BUTTON_10);

        keyboardNew.add(firstRow);
        keyboardNew.add(secondRow);

        keyboardMarkup.setResizeKeyboard(true);

        return keyboardNew;
    }



    @Override
    public String getBotUsername() {

        return botUserName;
    }


    @Override
    public String getBotToken() {

        return botToken;
    }


    public Long getChatId() {       //  Вдруг понадобится

        return chatId;
    }

}
