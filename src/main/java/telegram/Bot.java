package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    String botUserName = "questSuper_Bot";
    String botToken = "904172873:AAG1PY5RZnAwAYFHB-McdI8ogWvjzcBNPto";

    Messages messageStrings = new Messages();

    List<String> listOfQuestions = new ArrayList<>();

    Map<Long, Map<String, Object>> users = new HashMap<>();   // общий список пользователей в формате :  [chatID пользователя] - [его результаты в виде HashMap]
    Map<String, Object> resultsForUser = new HashMap<>();    //  встроенная в HashMap <users> еще одна HashMap для записи результатов для каждого пользователя

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
        Long chatId = update.getMessage().getChatId();

        try {
            //  Тестовая строка
            System.out.println("\nChat id  :  " + chatId + "\n" + update);


            if (!users.containsKey(chatId)) {     //  Если такого chatId еще нет в HashMap <users>

                createNewUser(chatId);      //  Создание новой записи (пользователя) в HashMap <users>
            }


            //  Если это самое первое сообщение от пользователя
            if (message != null && update.getMessage().hasText()) {
                if ((boolean) (users.get(chatId)).get(messageStrings.IF_FIRST_MESSAGE)) {   //  проверяется переменная из вложенной HashMap <resultsForUser> для пользователя <chatId>

                    sendMsg(chatId, messageStrings.SMILE);
                    sendMsg(chatId, messageStrings.THANK_YOU);
                    sendMsg(chatId, messageStrings.GREETENG_MESSAGE);

                    users.get(chatId).put(messageStrings.IF_FIRST_MESSAGE, false);    //  заносится во вложенную HashMap <resultsForUser> для пользователя <chatId>

                }
            }


            if (message.equals("/start")) {      //  Возможность печатать команду с клавиатуры
                sendMsg(chatId, messageStrings.GREETENG_MESSAGE);
            }

            if (message.equals("/help") || (message.equals(messageStrings.BUTTON_2_HELP))) {
                sendMsg(chatId, messageStrings.HELP);
            }


            if (message.equals(messageStrings.BUTTON_1_CREATE_WHEEL)) {      //  Самое начало создания колеса

                keyboardMarkup.setKeyboard(keyboardMarkupNew());      //  Установка клавиатуры для ответов на 10 кнопок

                sendMsg(chatId, messageStrings.START);

                //  Есть две логики создания "колеса" :
                //  1)  Посылать сообщения с собственной прикрепленной клавиатурой
                //  2)  Использовать одну клавиатуру для всех   <Пока что основной вариант, как самый простой и рациональный>

                questionAsk(chatId, 0);   //  Так как это первый вопрос, то и баллы за предыдущий ответ пока не начислены

            }

            createWheel(message, chatId);     //  Составление колеса жизненного баланса  (начисление баллов за ответ)


        } catch (TelegramApiException e) {
            System.out.println("Ошибка при приеме сообщения от пользователя : " + e.toString());
        } catch (Exception n) {
            System.out.println("Ошибка неустановленной природы при приеме сообщения : " + n.toString());
        }
    }


    public synchronized void sendMsg(Long chatId, String messageString) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageString);
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            System.out.println("Ошибка при отправке сообщения : " + e.toString());
        } catch (Exception n) {
            System.out.println("Ошибка неустановленной природы (при попытке отправить сообщение) : " + n.toString());
        }
    }



    private void questionAsk(Long chatId, int points) throws TelegramApiException {

        //  GET  users.get(chatId)).get(messageStrings.IF_FIRST_MESSAGE)
        //  PUT  users.get(chatId).put(messageStrings.IF_FIRST_MESSAGE, false);

        this.pointsForUser = this.pointsForUser + points;   //  начисление баллов за предыдущий ответ  (0 если вопрос первый)

        if (this.numberOfQuestion >= messageStrings.AMOUNT_OF_SPHERES) {       //  Количество вопросов прописано в классе Messages, вместе со сферами
            //  Окончание опроса
            sendMsg(chatId, messageStrings.TEST_COMPLETE);

            //  Запуск анализа результатов
            analyseResults(chatId, this.pointsForUser);
        }
        else {
            try {
                sendMsg(chatId, listOfQuestions.get(this.numberOfQuestion));     //  Задается очередной вопрос
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("Выход за границы массива : " + e.toString());
            } catch (Exception n) {
                System.out.println("Ошибка неустановленной природы при отправке сообщения : " + n.toString());
            }

            this.numberOfQuestion++;
        }

    }


    private void analyseResults(Long chatId, int points) {

        points = points % 80;
        if (points == 0)
            points = 100;   //  Нужно для корректного выведения результатов, если выбрано по 10 баллов на каждый вопрос

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


    private void createNewUser (Long chatId) {

        resultsForUser.put(messageStrings.POINTS, 0);
        resultsForUser.put(messageStrings.BUTTON_1, 0);
        resultsForUser.put(messageStrings.BUTTON_2, 0);
        resultsForUser.put(messageStrings.BUTTON_3, 0);
        resultsForUser.put(messageStrings.BUTTON_4, 0);
        resultsForUser.put(messageStrings.BUTTON_5, 0);
        resultsForUser.put(messageStrings.BUTTON_6, 0);
        resultsForUser.put(messageStrings.BUTTON_7, 0);
        resultsForUser.put(messageStrings.BUTTON_8, 0);

        resultsForUser.put(messageStrings.NUMBER_OF_QUESTION, 0);
        resultsForUser.put(messageStrings.IF_FIRST_MESSAGE, true);

        users.put(chatId, resultsForUser);


        //  Тестовые строки
        System.out.println("\nСоздан новый пользователь  :  " + resultsForUser);
        System.out.println("\nОбщий пул пользователей  :  " + users);

    }


    private void createWheel (String message, Long chatId) throws TelegramApiException {

        //  Составление колеса жизненного баланса  (начисление баллов за ответ)


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


    private List<KeyboardRow> keyboardMarkupNew() {                   //  Настройка новой пользовательской клавиатуры на 10 кнопок

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


    private void keyboardMarkupTwoButtons() {     //  Настройка пользовательской клавиатуры на 2 кнопки ("Старт" и "Помощь")

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();

        row.add(messageStrings.BUTTON_1_CREATE_WHEEL);
        row.add(messageStrings.BUTTON_2_HELP);

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

    }


    @Override
    public String getBotUsername() {

        return botUserName;
    }


    @Override
    public String getBotToken() {

        return botToken;
    }


}
