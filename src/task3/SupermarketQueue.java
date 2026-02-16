package task3;

import java.util.*;

/**
 * Программа для моделирования очередей покупателей в супермаркете
 * Покупатели добавляются нажатием клавиши, обслуживаются случайное время
 */
public class SupermarketQueue {
    // Количество касс (очередей)
    private static final int NUMBER_OF_CASHIERS = 3;
    // Максимальное количество товаров у покупателя
    private static final int MAX_ITEMS = 20;
    // Время обслуживания одного товара (в условных единицах)
    private static final int TIME_PER_ITEM = 2;

    // Очереди покупателей для каждой кассы
    private static List<Queue<Customer>> queues = new ArrayList<>();
    // Таймеры обслуживания для каждой кассы
    private static int[] serviceTimers;
    // Генератор случайных чисел
    private static Random random = new Random();
    // Счетчик покупателей
    private static int customerId = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Инициализация очередей
        for (int i = 0; i < NUMBER_OF_CASHIERS; i++) {
            queues.add(new LinkedList<>());
        }

        serviceTimers = new int[NUMBER_OF_CASHIERS];

        System.out.println("=== МОДЕЛИРОВАНИЕ ОЧЕРЕДЕЙ В СУПЕРМАРКЕТЕ ===");
        System.out.println("Команды:");
        System.out.println("  n - новый покупатель");
        System.out.println("  t - промотать время (обслужить одного покупателя)");
        System.out.println("  q - выход");
        System.out.println("================================================");

        displayQueues();

        String command;
        do {
            System.out.print("\nВведите команду: ");
            command = scanner.nextLine().toLowerCase();

            switch (command) {
                case "n":
                    addNewCustomer();
                    break;
                case "t":
                    processTime();
                    break;
                case "q":
                    System.out.println("Программа завершена.");
                    break;
                default:
                    System.out.println("Неизвестная команда. Используйте n, t или q.");
            }

            displayQueues();

        } while (!command.equals("q"));

        scanner.close();
    }

    /**
     * Добавление нового покупателя
     */
    private static void addNewCustomer() {
        // Генерируем случайное количество товаров (от 1 до MAX_ITEMS)
        int itemCount = random.nextInt(MAX_ITEMS) + 1;
        // Время обслуживания = количество товаров * время на товар
        int serviceTime = itemCount * TIME_PER_ITEM;

        Customer customer = new Customer(customerId++, itemCount, serviceTime);

        // Выбираем очередь с наименьшим количеством людей
        int shortestQueueIndex = findShortestQueue();
        queues.get(shortestQueueIndex).add(customer);

        System.out.println("Новый покупатель #" + customer.getId() +
                " с " + itemCount + " товарами (время обслуживания: " +
                serviceTime + ") добавлен в очередь " + (shortestQueueIndex + 1));
    }

    /**
     * Находит индекс самой короткой очереди
     */
    private static int findShortestQueue() {
        int minSize = Integer.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < queues.size(); i++) {
            int size = queues.get(i).size();
            if (size < minSize) {
                minSize = size;
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * Обработка единицы времени
     */
    private static void processTime() {
        boolean customerServed = false;

        // Обрабатываем каждую кассу
        for (int i = 0; i < NUMBER_OF_CASHIERS; i++) {
            Queue<Customer> queue = queues.get(i);

            if (!queue.isEmpty()) {
                Customer currentCustomer = queue.peek();

                // Уменьшаем оставшееся время обслуживания
                currentCustomer.decreaseRemainingTime();

                // Если покупатель обслужен
                if (currentCustomer.getRemainingTime() <= 0) {
                    Customer served = queue.poll();
                    System.out.println("Покупатель #" + served.getId() +
                            " обслужен в кассе " + (i + 1));
                    customerServed = true;
                }
            }
        }

        if (!customerServed) {
            System.out.println("Никто не был обслужен за это время.");
        }
    }

    /**
     * Отображение состояния всех очередей
     */
    private static void displayQueues() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│         СОСТОЯНИЕ ОЧЕРЕДЕЙ          │");
        System.out.println("├─────────────────────────────────────┤");

        for (int i = 0; i < NUMBER_OF_CASHIERS; i++) {
            System.out.print("│ Касса " + (i + 1) + ": ");

            Queue<Customer> queue = queues.get(i);
            if (queue.isEmpty()) {
                System.out.print("пусто");
            } else {
                for (Customer customer : queue) {
                    System.out.print("#" + customer.getId() +
                            "(" + customer.getRemainingTime() + ") ");
                }
            }

            // Выравнивание для красивого вывода
            int spaces = 30 - (queue.isEmpty() ? 4 : queue.toString().length());
            for (int j = 0; j < spaces; j++) {
                System.out.print(" ");
            }
            System.out.println("│");
        }

        System.out.println("└─────────────────────────────────────┘");
    }

    /**
     * Внутренний класс для представления покупателя
     */
    static class Customer {
        private int id;
        private int itemCount;
        private int totalServiceTime;
        private int remainingTime;

        public Customer(int id, int itemCount, int totalServiceTime) {
            this.id = id;
            this.itemCount = itemCount;
            this.totalServiceTime = totalServiceTime;
            this.remainingTime = totalServiceTime;
        }

        public int getId() {
            return id;
        }

        public int getItemCount() {
            return itemCount;
        }

        public int getTotalServiceTime() {
            return totalServiceTime;
        }

        public int getRemainingTime() {
            return remainingTime;
        }

        public void decreaseRemainingTime() {
            if (remainingTime > 0) {
                remainingTime--;
            }
        }

        @Override
        public String toString() {
            return "#" + id + "(" + remainingTime + ")";
        }
    }
}