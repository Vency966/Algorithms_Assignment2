package task1;

import java.util.Scanner;

/**
 * Программа для перестановки букв в слове в обратном порядке
 * Пользователь вводит слово, программа выводит слово с буквами в обратном порядке
 */
public class WordReverser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Перестановка букв в слове ===");
        System.out.print("Введите слово: ");

        // Читаем введенное слово
        String word = scanner.nextLine();

        // Переворачиваем слово
        String reversedWord = reverseString(word);

        // Выводим результат
        System.out.println("Слово в обратном порядке: " + reversedWord);

        scanner.close();
    }

    /**
     * Метод для переворота строки
     * @param str исходная строка
     * @return перевернутая строка
     */
    public static String reverseString(String str) {
        // Используем StringBuilder для эффективного переворота строки
        return new StringBuilder(str).reverse().toString();

        // Альтернативный способ без StringBuilder:
        // char[] charArray = str.toCharArray();
        // String result = "";
        // for (int i = charArray.length - 1; i >= 0; i--) {
        //     result += charArray[i];
        // }
        // return result;
    }
}
