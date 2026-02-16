package task2;

import java.util.Scanner;
import java.util.Stack;

/**
 * Программа для проверки правильности расположения скобок в строке
 * Скобки: { } [ ]
 * На оценку 4: указывает на ошибки
 * На оценку 5: исправляет ошибки
 */
public class BracketChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Проверка расположения скобок ===");
        System.out.print("Введите строку со скобками: ");

        String input = scanner.nextLine();

        // Проверяем скобки
        checkBrackets(input);

        scanner.close();
    }

    /**
     * Проверяет правильность расположения скобок
     * @param text строка для проверки
     */
    public static void checkBrackets(String text) {
        Stack<Character> stack = new Stack<>();
        Stack<Integer> positions = new Stack<>();
        boolean hasError = false;

        System.out.println("\nАнализ строки: " + text);

        // Проходим по каждому символу
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            // Если открывающая скобка - помещаем в стек
            if (ch == '{' || ch == '[') {
                stack.push(ch);
                positions.push(i);
            }
            // Если закрывающая скобка - проверяем соответствие
            else if (ch == '}' || ch == ']') {
                if (stack.isEmpty()) {
                    System.out.println("Ошибка на позиции " + i + ": лишняя закрывающая скобка '" + ch + "'");
                    hasError = true;
                } else {
                    char lastOpen = stack.pop();
                    positions.pop();

                    // Проверяем соответствие типов скобок
                    if ((lastOpen == '{' && ch != '}') || (lastOpen == '[' && ch != ']')) {
                        System.out.println("Ошибка на позиции " + i + ": несоответствие типов скобок '" + lastOpen + "' и '" + ch + "'");
                        hasError = true;
                    }
                }
            }
        }

        // Проверяем, остались ли незакрытые скобки
        while (!stack.isEmpty()) {
            char unclosed = stack.pop();
            int pos = positions.pop();
            System.out.println("Ошибка на позиции " + pos + ": незакрытая скобка '" + unclosed + "'");
            hasError = true;
        }

        // Выводим результат для оценки 4
        if (!hasError) {
            System.out.println("✓ ВСЕ ВЕРНО: Все скобки расставлены правильно");
        } else {
            System.out.println("✗ ЕСТЬ ОШИБКИ: Необходимо исправить скобки");
        }

        // Дополнительно для оценки 5 - исправляем ошибки
        String correctedText = fixBrackets(text);
        System.out.println("\n=== ДЛЯ ОЦЕНКИ 5 ===");
        System.out.println("Исправленная строка: " + correctedText);
    }

    /**
     * Исправляет ошибки в расстановке скобок (для оценки 5)
     * @param text исходная строка с ошибками
     * @return исправленная строка
     */
    public static String fixBrackets(String text) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        // Сначала добавляем все символы, кроме скобок, и отслеживаем скобки
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '{' || ch == '[') {
                stack.push(ch);
                result.append(ch);
            } else if (ch == '}') {
                if (!stack.isEmpty() && stack.peek() == '{') {
                    stack.pop();
                    result.append(ch);
                } else if (!stack.isEmpty() && stack.peek() == '[') {
                    // Меняем тип скобки
                    stack.pop();
                    result.append('}');
                } else {
                    // Лишняя закрывающая - не добавляем
                    System.out.println("Удалена лишняя закрывающая скобка '}' на позиции " + i);
                }
            } else if (ch == ']') {
                if (!stack.isEmpty() && stack.peek() == '[') {
                    stack.pop();
                    result.append(ch);
                } else if (!stack.isEmpty() && stack.peek() == '{') {
                    // Меняем тип скобки
                    stack.pop();
                    result.append(']');
                } else {
                    // Лишняя закрывающая - не добавляем
                    System.out.println("Удалена лишняя закрывающая скобка ']' на позиции " + i);
                }
            } else {
                result.append(ch);
            }
        }

        // Добавляем недостающие закрывающие скобки
        while (!stack.isEmpty()) {
            char open = stack.pop();
            if (open == '{') {
                result.append('}');
            } else if (open == '[') {
                result.append(']');
            }
        }

        return result.toString();
    }
}