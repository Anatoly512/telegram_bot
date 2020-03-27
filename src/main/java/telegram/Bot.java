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

    Long chatId;

    Messages messageStrings = new Messages();
    boolean ifFirstMessage = true;

    int numberOfQuestion = 0;
    int pointsForUser = 0;

    List<String> listOfQuestions = new ArrayList<>();

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

Bot() {
    keyboardMarkupTwoButtons();     //  Настройка в конструкторе пользовательской клавиатуры

    listOfQuestions.add(messageStrings.SPHERE_1_CAREER);        //  Создание списка вопросов
    listOfQuestions.add(messageStrings.SPHERE_2_FAMILY);
    listOfQuestions.add(messageStrings.SPHERE_3_WEALTH);
    listOfQuestions.add(messageStrings.SPHERE_4_ENVIROMENT);
    listOfQuestions.add(messageStrings.SPHERE_5_DEVELOPMENT);
    listOfQuestions.add(messageStrings.SPHERE_6_RECREATION);
    listOfQuestions.add(messageStrings.SPHERE_7_TRAVELS);
    listOfQuestions.add(messageStrings.SPHERE_8_HEALTH);
}


    @Override
    public void onUpdateReceived(Update update) {            // update содержит сообщение от пользователя
        String message = update.getMessage().getText();

        chatId = update.getMessage().getChatId();

        try {
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

                wheelCreate(chatId);   //  Запуск метода создания "колеса" для пользователя <chatId>
                                       //  Это вариант для логики сообщений с собственной прикрепленной клавиатурой

                //  Есть две логики создания "колеса" :
                //  1)  Посылать сообщения с собственной прикрепленной клавиатурой
                //  2)  Использовать одну клавиатуру для всех   <Пока что основной вариант, как самый простой и рациональный>


                questionAsk(chatId, 0);   //  Так как это первый вопрос, то и баллы за предыдущий ответ пока не начислены


            }


            //  Здесь идет логика составления колеса жизненного баланса  (начисление баллов за ответ)

            if (message.equals(messageStrings.BUTTON_1)) {

                sendMsg(chatId, messageStrings.POINTS_1);

                questionAsk(chatId, 1);

            }

            if (message.equals(messageStrings.BUTTON_2)) {

                sendMsg(chatId, messageStrings.POINTS_2);

                questionAsk(chatId, 2);

            }

            if (message.equals(messageStrings.BUTTON_3)) {

                sendMsg(chatId, messageStrings.POINTS_3);

                questionAsk(chatId, 3);

            }

            if (message.equals(messageStrings.BUTTON_4)) {

                sendMsg(chatId, messageStrings.POINTS_4);

                questionAsk(chatId, 4);

            }

            if (message.equals(messageStrings.BUTTON_5)) {

                sendMsg(chatId, messageStrings.POINTS_5);

                questionAsk(chatId, 5);

            }

            if (message.equals(messageStrings.BUTTON_6)) {

                sendMsg(chatId, messageStrings.POINTS_6);

                questionAsk(chatId, 6);

            }

            if (message.equals(messageStrings.BUTTON_7)) {

                sendMsg(chatId, messageStrings.POINTS_7);

                questionAsk(chatId, 7);

            }

            if (message.equals(messageStrings.BUTTON_8)) {

                sendMsg(chatId, messageStrings.POINTS_8);

                questionAsk(chatId, 8);

            }

            if (message.equals(messageStrings.BUTTON_9)) {

                sendMsg(chatId, messageStrings.POINTS_9);

                questionAsk(chatId, 9);

            }

            if (message.equals(messageStrings.BUTTON_10)) {

                sendMsg(chatId, messageStrings.POINTS_10);

                questionAsk(chatId, 10);

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



    private List<KeyboardRow> keyboardMarkupNew() {                   //  Настройка новой пользовательской клавиатуры на 10 кнопок  (points)

        List<KeyboardRow> keyboardNew = new ArrayList<>();
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



    private void keyboardMarkupTwoButtons() {     //  Настройка пользовательской клавиатуры на 2 кнопки (Старт и Помощь)

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();

        row.add(messageStrings.BUTTON_1_CREATE_WHEEL);
        row.add(messageStrings.BUTTON_2_HELP);

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

    }


    private void wheelCreate(Long chatId) {     //  Пока что тестовый метод   (для сообщений с собственной прикрепленной клавиатурой)

        System.out.println("\nСоздаем колесо жизненного баланса для пользователя  chatId :  " + chatId);

    }



    private void questionAsk (Long chatId, int points) throws TelegramApiException {

        this.pointsForUser = this.pointsForUser + points;   //  начисление баллов за предыдущий ответ  (0 если вопрос первый)

        if (this.numberOfQuestion >= 8) {
            //  Окончание опроса
            sendMsg(chatId, messageStrings.TEST_COMPLETE);

            //  Запуск анализа результатов
            analyseResults(this.pointsForUser);
        }
        else {
            try {
                sendMsg(chatId, listOfQuestions.get(this.numberOfQuestion));     //  Задается очередной вопрос
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("Выход за границы массива : " + e.toString());
            }
            catch (Exception n) {
                System.out.println("Ошибка неустановленной природы при отправке сообщения : " + n.toString());
            }

            this.numberOfQuestion++;
        }

    }


    private void analyseResults (int points) throws TelegramApiException {


        points = points % 80;
        if (points == 0) points = 100;   //  Нужно для корректного выведения результатов, если выбрано по 10 баллов на каждый вопрос

        String result = String.valueOf(points) + "%";

        sendMsg(chatId, messageStrings.RESULT);
        sendMsg(chatId, result);


        //  Здесь надо как-то завершить опрос, иначе получится бесконечный цикл

        //  Обнуление всех переменных  (нужно для начала опроса заново по желанию пользователя)

        this.numberOfQuestion = 0;
        this.pointsForUser = 0;

        keyboardMarkupTwoButtons();   //  Переводим клавиатуру в изначальное состояние (на 2 кнопки)

        sendMsg(chatId, messageStrings.SMILE);
        sendMsg(chatId, messageStrings.REPEAT);    //  Пробуем запуск бота с самого начала



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
