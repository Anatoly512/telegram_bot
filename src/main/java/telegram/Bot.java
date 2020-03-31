package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;

public class Bot extends TelegramLongPollingBot {

    String botUserName = "questSuper_Bot";
    String botToken = "904172873:AAG1PY5RZnAwAYFHB-McdI8ogWvjzcBNPto";

    Messages messageStrings = new Messages();

    List<String> listOfQuestions = new ArrayList<>();
    List<String> titleOfSpheres = new ArrayList<>();

    Map<Long, Map<String, Object>> users = new HashMap<>();   // общий список пользователей в формате :  [chatID пользователя] - [его результаты в виде HashMap]

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

    Bot() {
        keyboardMarkupTwoButtons();     //  Настройка в конструкторе пользовательской клавиатуры (на 2 кнопки "Старт" и "Помощь")

        listOfQuestions.add(messageStrings.SPHERE_1_CAREER);        //  Создание списка вопросов
        listOfQuestions.add(messageStrings.SPHERE_2_FAMILY);
        listOfQuestions.add(messageStrings.SPHERE_3_WEALTH);
        listOfQuestions.add(messageStrings.SPHERE_4_ENVIROMENT);
        listOfQuestions.add(messageStrings.SPHERE_5_DEVELOPMENT);
        listOfQuestions.add(messageStrings.SPHERE_6_RECREATION);
        listOfQuestions.add(messageStrings.SPHERE_7_TRAVELS);
        listOfQuestions.add(messageStrings.SPHERE_8_HEALTH);

        titleOfSpheres.add(messageStrings.SPHERE_1_CAREER_TITLE);       //  Готовится список названий сфер
        titleOfSpheres.add(messageStrings.SPHERE_2_FAMILY_TITLE);
        titleOfSpheres.add(messageStrings.SPHERE_3_WEALTH_TITLE);
        titleOfSpheres.add(messageStrings.SPHERE_4_ENVIROMENT_TITLE);
        titleOfSpheres.add(messageStrings.SPHERE_5_DEVELOPMENT_TITLE);
        titleOfSpheres.add(messageStrings.SPHERE_6_RECREATION_TITLE);
        titleOfSpheres.add(messageStrings.SPHERE_7_TRAVELS_TITLE);
        titleOfSpheres.add(messageStrings.SPHERE_8_HEALTH_TITLE);

    }


    @Override
    public void onUpdateReceived(Update update) {            // update содержит сообщение от пользователя

        synchronized (update) {

            String message = update.getMessage().getText();

            Long chatId = update.getMessage().getChatId();

            synchronized (users) {

                try {
                    //  Тестовая строка
                    System.out.println("\nChat id  :  " + chatId + "\n" + update);


                    if (!users.containsKey(chatId)) {     //  Если такого <chatId> еще нет в HashMap <users>

                        createNewUser(chatId);      //  Создание новой записи (пользователя) в HashMap <users>

                        keyboardMarkupTwoButtons();   //  Если пользователь новый, то ему нужна клавиатура на 2 кнопки ("Старт" и "Помощь")

                    }

                    //  Если это самое первое сообщение от пользователя
                    if (message != null && update.getMessage().hasText()) {
                        if ((boolean) (users.get(chatId)).get(messageStrings.IF_FIRST_MESSAGE)) {   //  проверяется переменная из вложенной HashMap <resultsForUser> для пользователя <chatId>

                            sendMsg(chatId, messageStrings.SMILE);                     //  Здесь идет блок, если подключился новый пользователь
                            sendMsg(chatId, messageStrings.THANK_YOU);
                            sendMsg(chatId, messageStrings.GREETENG_MESSAGE);

                            users.get(chatId).put(messageStrings.IF_FIRST_MESSAGE, false);    //  заносится во вложенную HashMap <resultsForUser> для пользователя <chatId>

                            message = "";    //  После разрыва связи нужно обнулить сообщение от пользователя, иначе будут обрабатываться предыдущие запросы
                            System.out.println("\nChat id  :  " + chatId + "\n" + update);
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

                        //  Если был обрав связи, то нужно еще раз обнулить основные переменные
                        users.get(chatId).put(messageStrings.NUMBER_OF_QUESTION, 0);      //  Установить номер вопроса на 0
                        users.get(chatId).put(messageStrings.POINTS_FOR_USER, 0);         //  Установить общие баллы за вопросы на 0

                        questionAsk(chatId, 0);   //  Так как это первый вопрос, то и баллы за предыдущий ответ пока не начислены

                    }

                    createWheel(message, chatId);     //  Составление колеса жизненного баланса  (начисление баллов за ответ)


                } catch (TelegramApiException e) {
                    System.out.println("Ошибка при приеме сообщения от пользователя : " + e.toString());
                } catch (NullPointerException ignore) {
                } catch (Exception n) {
                    System.out.println("Ошибка неустановленной природы при приеме сообщения : " + n.toString());
                }

            }
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


    private synchronized void questionAsk(Long chatId, int points) throws TelegramApiException {

        synchronized (users) {

        //  начисление общих баллов за предыдущий ответ  (0 если вопрос первый)

        users.get(chatId).put(messageStrings.POINTS_FOR_USER, (int) (users.get(chatId)).get(messageStrings.POINTS_FOR_USER) + points);

        //  занесение балла именно за конкретный вопрос (номер <NUMBER_OF_QUESTION>) в соответствующее поле в HashMap <resultsForUser>

        users.get(chatId).put((String.valueOf((int) ((users.get(chatId)).get(messageStrings.NUMBER_OF_QUESTION)))), points);


        if ((int) (users.get(chatId)).get(messageStrings.NUMBER_OF_QUESTION) >= messageStrings.AMOUNT_OF_SPHERES) {  //  Количество вопросов прописано в классе Messages, вместе со сферами
            //  Окончание опроса
            sendMsg(chatId, messageStrings.TEST_COMPLETE);

            //  Запуск анализа результатов для пользователя <chatId>
            analyseResults(chatId);
        }
        else {
            try {

                if ((int) (users.get(chatId)).get(messageStrings.NUMBER_OF_QUESTION) != 0) {      //  Если это вопрос не первый

                    keyboardMarkup.setKeyboard(keyboardMarkupNew());      //  Установка клавиатуры для ответов на 10 кнопок
                }

                //  Задается очередной вопрос
                sendMsg(chatId, listOfQuestions.get((int) (users.get(chatId)).get(messageStrings.NUMBER_OF_QUESTION)));

            } catch (IndexOutOfBoundsException e) {
                System.out.println("Выход за границы массива : " + e.toString());
            } catch (Exception n) {
                System.out.println("Ошибка неустановленной природы при отправке сообщения : " + n.toString());
            }
        }


        //  Готовим следующий вопрос для пользователя <chatId>             //  Номер записывается в соответствующее поле в HashMap <resultsForUser>

        users.get(chatId).put(messageStrings.NUMBER_OF_QUESTION, (int) (users.get(chatId)).get(messageStrings.NUMBER_OF_QUESTION) + 1);

        //  Тестовая строка
        System.out.println("\nОбщий пул пользователей  (новый вопрос подготовлен) :  " + users);
    }


}


    private synchronized void analyseResults(Long chatId) {

        //  Тестовая строка
        System.out.println("\nПользователь chatId : " + chatId + " -> РЕЗУЛЬТАТЫ  :  " + users.get(chatId));

        int points = (int) (users.get(chatId)).get(messageStrings.POINTS_FOR_USER);

        points = points % 80;
        if (points == 0)
            points = 100;   //  Нужно для корректного выведения результатов, если выбрано по 10 баллов на каждый вопрос


        String result = messageStrings.RESULT + String.valueOf(points) + "%";
        sendMsg(chatId, result);


        //  Обнуление всех переменных  (нужно для начала опроса заново по желанию пользователя)

        synchronized (users) {
            users.get(chatId).put(messageStrings.NUMBER_OF_QUESTION, 0);       //  Обнуление текущего номера вопроса (нужно для корректного подсчета сильных и слабых сфер)
            users.get(chatId).put(messageStrings.POINTS_FOR_USER, 0);
        }

        //  Вывод наименьших и наибольших значений развития сфер пользователя
        spheresMaxAndMinCalculate(chatId);


        //  Завершение опроса

        keyboardMarkupTwoButtons();   //  Переводим клавиатуру в изначальное состояние (на 2 кнопки -> "Старт" и "Помощь")

        sendMsg(chatId, messageStrings.SMILE);
        sendMsg(chatId, messageStrings.REPEAT);    //  Начинаем запуск бота с самого начала


        //  Сохранение результатов в файл
        ResultsSaver resultsSaver = new ResultsSaver();
        try {
            resultsSaver.saveResults(chatId, users.get(chatId));     //  метод параметризирован, 2-й параметр может быть любым (записывается в файл)
        } catch (IOException e) {
            System.out.println("\nОшибка записи в файл :  " + e.toString());
        }


/*  Можно сэкономить производительность, и просто сбросить на 0 основные переменные в HashMap <resultsForUser>

        //  Удаление юзера из HashMap      //   В принципе, это необязательно, сброс основных переменных в HashMap <resultsForUser> проще и быстрее)

        synchronized (users) {
            try {
         //       users.remove(chatId);

                System.out.println("\nПользователь с chatId :  " + chatId + "  завершил опрос и был удален из пула пользователей!");

                //  Тестовая строка
                System.out.println("\nОбщий пул пользователей (после удаления пользователя)  :  " + users);

            } catch (Exception e) {
                System.out.println("Ошибка при удалении объекта из HashMap :  " + e.toString());
            }
        }

*/


}


    private synchronized void spheresMaxAndMinCalculate(Long chatId) {

        int[] nums = new int[messageStrings.AMOUNT_OF_SPHERES];


        for (int i = 0; i < messageStrings.AMOUNT_OF_SPHERES; i++) {   //  получаем массив nums, состоящий из значений в HashMap <resultsForUser>  (баллы за вопросы)

            nums[i] = (int) (users.get(chatId)).get(String.valueOf(i + 1));        //  В HashMap <resultsForUser> нумерация вопросов начинается с 1

        }

        //  Тестовая строка
        System.out.println("\nМассив значений баллов за вопросы :  " + Arrays.toString(nums));


        CalculateSpheres calculateSpheres = new CalculateSpheres();

        nums = calculateSpheres.calculateResults(nums);     //  <nums> теперь отсортированный массив  (по убыванию)


        spheresMaxAndMinCreateAndShow(nums, chatId);

    }



    private synchronized void spheresMaxAndMinCreateAndShow(int[] nums, Long chatId) {

        List<Integer> maxSpheres = new ArrayList<>();
        List<Integer> minSpheres = new ArrayList<>();


        //  Вычисление наибольших и наименьших значений (развития сфер пользователя)
        //  и занесение результатов в коллекции <maxSpheres> и <minSpheres>

        boolean trigger = true;
        int i = 0;

        do {
            maxSpheres.add(nums[i]);      //   Заносится максимальный элемент в массиве  (в коллекцию <maxSpheres>)

            if (i == nums.length - 1)    //  Массив закончился
                break;

            if (!(nums[i] == (nums[i + 1])))    //  Если следующий элемент в массиве отличается, то выход из цикла
                trigger = false;
            else
                i++;                            //  иначе заносим в коллекцию <maxSpheres> следующий элемент
        }
        while (trigger);


        //  Тестовая строка
        System.out.println("\n\nНаибольшие элементы в коллекции  :  " + maxSpheres);


        trigger = true;
        i = nums.length - 1;

        boolean triggerIfZero = false;

        while (trigger) {
            trigger = false;

            if (nums[i] == 0) {
                nums[i] = 1;  //  Это чтобы избежать возможного будущего выхода за границы массива (если оставить 0)
                i--;
                trigger = true;    //  В массиве не должно быть нулей  (такого варианта ответов не предусмотрено логикой программы)
                triggerIfZero = true;
            }

            if (nums[i] != 0) minSpheres.add(nums[i]);    //   Заносится минимальный элемент в массиве (в коллекцию <minSpheres>)

            if (i == 0)    //  Массив закончился
                break;

            if ((nums[i] == (nums[i - 1]))) {    //  Другая логика :  если предыдущий элемент такой же, то продолжаем заносить в коллекцию <minSpheres>
                i--;
                trigger = true;
            }
        }


        //  Тестовая строка
        System.out.println("Наименьшие элементы в коллекции  :  " + minSpheres);


        //  Вывод на экран соответствующих ячеек из HashMap <resultsForUser>     //  поиск по ключам

        String stringSpheres = "";

        //  Вывод сильных сфер

        System.out.println("\nВаши сильные сферы  : ");

        sendMsg(chatId, "\nВаши сильные сферы  : ");

        Set<Map.Entry<String, Object>> entrySet = (users.get(chatId)).entrySet();

        Object desiredObject = new Object();  //  что хотим найти
        desiredObject = maxSpheres.get(0);    // <maxSpheres> сильные сферы

        for (Map.Entry<String, Object> pair : entrySet) {
            if (desiredObject.equals(pair.getValue())) {

                //    System.out.println("сильные сферы : " + pair.getKey());  // нашли наше значение и возвращаем ключ

                System.out.println("сильная сфера : " + titleOfSpheres.get((int) Integer.parseInt(pair.getKey()) - 1));

                stringSpheres = titleOfSpheres.get((int) Integer.parseInt(pair.getKey()) - 1);
                sendMsg(chatId, stringSpheres);



            }
        }


        //  Вывод слабых сфер

        if (!triggerIfZero) {

        System.out.println("\nВаши слабые сферы  : ");

        sendMsg(chatId, "\nВаши слабые сферы  : ");

        desiredObject = minSpheres.get(0);   //  <minSpheres>  слабые сферы

        if (desiredObject != null) {

            for (Map.Entry<String, Object> pair : entrySet) {
                if (desiredObject.equals(pair.getValue())) {

                    //   System.out.println("слабые сферы : " + pair.getKey());  // нашли наше значение и возвращаем ключ

                    System.out.println("слабая сфера : " + titleOfSpheres.get((int) Integer.parseInt(pair.getKey()) - 1));

                    stringSpheres = titleOfSpheres.get((int) Integer.parseInt(pair.getKey()) - 1);
                    sendMsg(chatId, stringSpheres);


                }
            }
        }
    }
        showResults(maxSpheres, minSpheres);
}


    private synchronized void showResults (List<Integer> maxSpheres, List<Integer> minSpheres) {

    }


    private synchronized void createNewUser(Long chatId) {

        Map<String, Object> resultsForUser = new HashMap<>();

        resultsForUser.put(messageStrings.BUTTON_1, 0);
        resultsForUser.put(messageStrings.BUTTON_2, 0);
        resultsForUser.put(messageStrings.BUTTON_3, 0);
        resultsForUser.put(messageStrings.BUTTON_4, 0);
        resultsForUser.put(messageStrings.BUTTON_5, 0);
        resultsForUser.put(messageStrings.BUTTON_6, 0);
        resultsForUser.put(messageStrings.BUTTON_7, 0);
        resultsForUser.put(messageStrings.BUTTON_8, 0);

        resultsForUser.put(messageStrings.POINTS_FOR_USER, 0);
        resultsForUser.put(messageStrings.NUMBER_OF_QUESTION, 0);

        resultsForUser.put(messageStrings.IF_FIRST_MESSAGE, true);

        users.put(chatId, resultsForUser);


        //  Тестовые строки
        System.out.println("\nСоздан новый пользователь  :  " + resultsForUser);
        System.out.println("\nОбщий пул пользователей  :  " + users);

    }


    private synchronized void createWheel(String message, Long chatId) throws TelegramApiException {

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


    private synchronized List<KeyboardRow> keyboardMarkupNew() {                   //  Настройка новой пользовательской клавиатуры на 10 кнопок

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


    private synchronized void keyboardMarkupTwoButtons() {        //  Настройка пользовательской клавиатуры на 2 кнопки ("Старт" и "Помощь")

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
