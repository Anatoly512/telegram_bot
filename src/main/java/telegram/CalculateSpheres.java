package telegram;

import java.util.Arrays;

public class CalculateSpheres {


    public synchronized int[] calculateResults (int[] array) {
        //  Вывод максимального и минимального числа в массиве
        //  методом собственной сортировки массива (слева направо, по убыванию)

        int minNumberOfArray = 0;
        int[] arrayCopy;           // в arrayCopy будет храниться уже отсортированный массив

        arrayCopy = Arrays.copyOf(mySortArray(array), array.length);    //  Теперь в массиве arrayCopy лежит отсортированный по убыванию массив


        //  Максимальный элемент в массиве
        System.out.println("Max number in array: " + arrayCopy[0]);    //   Максимальный элемент в массиве, так как массив уже отсортирован


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
        displayArray(arrayCopy);

        return arrayCopy;
    }


    private synchronized int[] mySortArray (int ...array) {
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




    private synchronized void displayArray (int ...array) {

        int i = 0;

        do
        {

            System.out.print(array[i] + " ");
            i++;
        }
        while (i < (array.length));

    }


}

