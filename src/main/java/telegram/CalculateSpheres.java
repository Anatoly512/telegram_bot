package telegram;

import java.util.Arrays;

public class CalculateSpheres {

    public int counter = 0;        //  хранит сколько есть всего повторяющихся чисел


    public void calculateResults (int[] array) {

        MySortArray(array);
    }


    private void MySortArray (int ...array) {
        //  Вывод максимального и минимального числа в массиве
        //  методом собственной сортировки массива (слева направо, по убыванию)

        int minNumberOfArray = 0;
        int[] arrayCopy = new int[array.length];    // в arrayCopy будет храниться уже отсортированный массив

        arrayCopy = Arrays.copyOf(MySortArrayDoing(array), array.length);


        //  Теперь в массиве arrayCopy лежит отсортированный по убыванию массив

        System.out.println("Max number in array: " + arrayCopy[0]);


        //  Теперь вычислим минимальный элемент в массиве

        boolean trigger = true;
        int c = arrayCopy.length-1;

            while (trigger) {
                trigger = false;
                minNumberOfArray = arrayCopy[c];
                if (minNumberOfArray == 0){
                    c--;
                    trigger = true;
                }
            }


        System.out.print("Min number in array: " + minNumberOfArray);

        System.out.print("\nSorted array by increased method : ");
        DisplayArray(arrayCopy);


    }

    private int[] MySortArrayDoing (int ...array) {
        //  Сортировки массива слева направо, по убыванию
        //  и возврат отсортированного массива в вызывающий метод

        boolean trigger = true;
        int n;

        int[] arraySort = new int[array.length];              //  в новый массив arraySort будет записываться
        arraySort = Arrays.copyOf(array, array.length);       //  результат сортировки


        while (trigger) {
            trigger = false;
            for (int i = 0; i < (array.length-1); i++) {
                if (arraySort[i] < arraySort[i + 1]) {
                    n = arraySort[i];
                    arraySort[i] = arraySort[i + 1];
                    arraySort[i + 1] = n;
                    trigger = true;
                }

            }

        }

        return arraySort;
    }


    private void PrintMaxMinNumbers (int... array) {
        //  Вывод максимального и минимального значения повторений чисел в массиве

        int sizeMaxMinValues = array.length;
        int[] aMaxMinValues = new int[] { 0, 0 };                   //  aMaxMinValues[0]  =  максимальное значение
        //  aMaxMinValues[1]  =  минимальное значение


        if (array.length <= 1) sizeMaxMinValues = 2;          //  Проверка, если пользователь ввел всего одну цифру

        aMaxMinValues = Arrays.copyOf(SearchMaxMinNumbers(array), sizeMaxMinValues);

        try {
            System.out.println("\nMax repeatings : " + aMaxMinValues[0]);
            System.out.println("Min repeatings : " + aMaxMinValues[1]);
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            System.out.println("\nПроизошел выход за пределы массива !\n");   //  Это чтобы не обрушить программу
            //  в случае выхода за границы массива
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }


    private int[] SearchMaxMinNumbers (int ...array) {
        //  Поиск максимального и минимального значения повторений чисел в массиве
        //  и возврат результата в виде массива из 2-х чисел

        int[] aMaxMinValues = new int[2];                           //  aMaxMinValues[0]  =  максимальное значение
        //  aMaxMinValues[1]  =  минимальное значение

        //  Создается двухмерный массив - размер в длину == array.length
        //  В первом измерении хранится число (только повторяющееся),
        //  а во втором измерении - сколько раз оно повторяется

        int[][] arrayResult = new int[2][(array.length)];        //  В этот массив будет производиться сортировка

        //  Этот вывод на экран нужен только для тестирования
        //  System.out.println("\n  My arrayCopy  : \n");
        //  DisplayArray(arrayCopy);
        //  System.out.println();


        boolean trigger;
        int counterNumber = 1;           //  сколько повторений одного числа

        for (int k = 0; k <= (array.length - 1); k++) {         //  Внешний цикл, по количеству элементов в массиве
            counterNumber = 1;
            trigger = true;

            for (int i = 0; i <= (array.length - 1); i++) {        //  Внутренний цикл, сравнение каждого элемента
                //  со всеми остальными на совпадение
                if (k == i) continue;

                if (array[k] == array[i]) {

                    if (trigger) {
                        arrayResult[0][counter] = array[i];        //  Записывается число, которое имеет повторения.
                        counter++;                                 //  counter хранит номер ячейки в arrayResult
                        trigger = false;
                    }


                    counterNumber++;
                    arrayResult[1][counter-1] = counterNumber;      //  Записывает сколько раз встретилось это число


                }

            }

        }


        //  Тестовая строка
        //  System.out.println("\n  My UNSORTED array  :  \n\n" + Arrays.deepToString(arrayResult));


        //  Сортировка двухмерного массива arrayResult[][]
        //  каждая строка сортируется отдельно, в своем цикле

        boolean trigger1 = true;
        int a;

        while (trigger1) {
            trigger1 = false;
            for (int i = 0; i < (array.length-1); i++) {
                if (arrayResult[0][i] < arrayResult[0][i + 1]) {
                    a = arrayResult[0][i];
                    arrayResult[0][i] = arrayResult[0][i + 1];
                    arrayResult[0][i + 1] = a;
                    trigger1 = true;
                }

            }

        }

        boolean trigger2 = true;
        a = 0;

        while (trigger2) {
            trigger2 = false;
            for (int i = 0; i < (array.length-1); i++) {
                if (arrayResult[1][i] < arrayResult[1][i + 1]) {
                    a = arrayResult[1][i];
                    arrayResult[1][i] = arrayResult[1][i + 1];
                    arrayResult[1][i + 1] = a;
                    trigger2 = true;
                }

            }

        }

        //  Тестовая строка
        //  System.out.println("\n  My new sorted array  :  \n\n" + Arrays.deepToString(arrayResult));


        aMaxMinValues[0] = arrayResult[1][0];       //  максимальное значение

        // aMaxMinValues[1]  =  минимальное значение

        //  Этот вывод на экран нужен для тестирования
        //  System.out.println("\n  Counter  =  " + counter);
        //  System.out.println("  CounterNumber  =  " + counterNumber);
        //  System.out.println();

        try {
            if (counter <= 0) counter = 1;

            aMaxMinValues[1] = arrayResult[1][counter-1];         //  В случае если повторений чисел нет вообще
                                                                  //  т.е. переменная (counter = 0), то происходил
                                                                  //  выход за границы массива в выражении [counter-1]
        }
        catch(ArrayIndexOutOfBoundsException ex){
            System.out.println("\nПроизошел выход за пределы массива !\n");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }


        return aMaxMinValues;

    }



    private void SortArray (int ...array) {
        //  стандартный метод сортировки масива (слева направо по возрастанию)

        int[] arrayStandartSorting = new int[array.length];
        arrayStandartSorting = Arrays.copyOf(array, array.length);

        Arrays.sort(arrayStandartSorting);

        System.out.println("\nSorted array by standart method :  " + Arrays.toString(arrayStandartSorting));

    }


    private void DisplayArray (int ...array) {

        int i = 0;

        do
        {

            System.out.print(array[i] + " ");
            i++;
        }
        while (i < (array.length));

    }


}

