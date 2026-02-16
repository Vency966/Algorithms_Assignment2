package task4;

import java.util.*;

/**
 * Программа для моделирования автобусных маршрутов
 * Маршрут проходит через города, образуя цепочку остановок
 */
public class BusRoute {
    private List<Stop> route;
    private String routeName;

    public BusRoute(String routeName) {
        this.routeName = routeName;
        this.route = new LinkedList<>();
    }

    /**
     * Внутренний класс для представления остановки (города)
     */
    static class Stop {
        private String cityName;
        private int arrivalTime; // время прибытия в минутах от начала маршрута

        public Stop(String cityName, int arrivalTime) {
            this.cityName = cityName;
            this.arrivalTime = arrivalTime;
        }

        public String getCityName() {
            return cityName;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(int arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        @Override
        public String toString() {
            return cityName + " (прибытие: " + formatTime(arrivalTime) + ")";
        }
    }

    /**
     * Добавление остановки в конец маршрута
     */
    public void addStop(String cityName, int travelTimeFromPrevious) {
        if (route.isEmpty()) {
            // Первая остановка
            route.add(new Stop(cityName, 0));
            System.out.println("Добавлена начальная остановка: " + cityName);
        } else {
            // Вычисляем время прибытия на новую остановку
            Stop lastStop = route.get(route.size() - 1);
            int newArrivalTime = lastStop.getArrivalTime() + travelTimeFromPrevious;
            route.add(new Stop(cityName, newArrivalTime));
            System.out.println("Добавлена остановка: " + cityName +
                    " (время в пути от предыдущей: " + travelTimeFromPrevious + " мин)");
        }
    }

    /**
     * Вставка остановки в середину маршрута
     */
    public void insertStop(int position, String cityName, int travelTimeFromPrevious) {
        if (position < 0 || position > route.size()) {
            System.out.println("Ошибка: недопустимая позиция для вставки");
            return;
        }

        if (position == 0) {
            // Вставка в начало маршрута
            Stop newStop = new Stop(cityName, 0);
            // Корректируем время для всех последующих остановок
            for (int i = 0; i < route.size(); i++) {
                Stop stop = route.get(i);
                stop.setArrivalTime(stop.getArrivalTime() + travelTimeFromPrevious);
            }
            route.add(0, newStop);
        } else {
            // Вставка между остановками
            Stop previousStop = route.get(position - 1);
            int newArrivalTime = previousStop.getArrivalTime() + travelTimeFromPrevious;
            Stop newStop = new Stop(cityName, newArrivalTime);

            // Корректируем время для всех последующих остановок
            int timeDifference = travelTimeFromPrevious;
            for (int i = position; i < route.size(); i++) {
                Stop stop = route.get(i);
                stop.setArrivalTime(stop.getArrivalTime() + timeDifference);
            }

            route.add(position, newStop);
        }

        System.out.println("Вставлена остановка: " + cityName + " на позицию " + position);
    }

    /**
     * Удаление остановки из маршрута
     */
    public void removeStop(int position) {
        if (position < 0 || position >= route.size()) {
            System.out.println("Ошибка: недопустимая позиция для удаления");
            return;
        }

        Stop removed = route.remove(position);

        // Корректируем время для последующих остановок, если удалена не последняя
        if (position < route.size()) {
            int timeAdjustment;
            if (position == 0) {
                // Если удалена первая остановка, следующая становится начальной
                timeAdjustment = route.get(0).getArrivalTime();
                for (Stop stop : route) {
                    stop.setArrivalTime(stop.getArrivalTime() - timeAdjustment);
                }
            } else {
                // Если удалена промежуточная остановка
                Stop previousStop = route.get(position - 1);
                timeAdjustment = removed.getArrivalTime() - previousStop.getArrivalTime();

                for (int i = position; i < route.size(); i++) {
                    Stop stop = route.get(i);
                    stop.setArrivalTime(stop.getArrivalTime() - timeAdjustment);
                }
            }
        }

        System.out.println("Удалена остановка: " + removed.getCityName());
    }

    /**
     * Поиск общего времени движения по маршруту
     */
    public int getTotalTravelTime() {
        if (route.isEmpty()) {
            return 0;
        }
        return route.get(route.size() - 1).getArrivalTime();
    }

    /**
     * Отображение всего маршрута
     */
    public void displayRoute() {
        System.out.println("\n=== МАРШРУТ: " + routeName + " ===");
        if (route.isEmpty()) {
            System.out.println("Маршрут пуст");
            return;
        }

        for (int i = 0; i < route.size(); i++) {
            Stop stop = route.get(i);
            System.out.println((i + 1) + ". " + stop);
        }

        System.out.println("Общее время в пути: " + formatTime(getTotalTravelTime()));
    }

    /**
     * Форматирование времени в часы и минуты
     */
    private static String formatTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== АВТОБУСНЫЕ МАРШРУТЫ ===");
        System.out.print("Введите название маршрута: ");
        String routeName = scanner.nextLine();

        BusRoute route = new BusRoute(routeName);

        boolean running = true;
        while (running) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить остановку в конец");
            System.out.println("2. Вставить остановку");
            System.out.println("3. Удалить остановку");
            System.out.println("4. Показать маршрут");
            System.out.println("5. Общее время в пути");
            System.out.println("6. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Название города: ");
                    String city = scanner.nextLine();
                    if (route.route.isEmpty()) {
                        route.addStop(city, 0);
                    } else {
                        System.out.print("Время в пути от предыдущей остановки (мин): ");
                        int travelTime = Integer.parseInt(scanner.nextLine());
                        route.addStop(city, travelTime);
                    }
                    break;

                case "2":
                    if (route.route.isEmpty()) {
                        System.out.println("Сначала добавьте начальную остановку");
                        break;
                    }

                    System.out.print("Позиция для вставки (1-" + (route.route.size() + 1) + "): ");
                    int position = Integer.parseInt(scanner.nextLine()) - 1;

                    System.out.print("Название города: ");
                    String newCity = scanner.nextLine();

                    System.out.print("Время в пути от предыдущей остановки (мин): ");
                    int travelTimeToNew = Integer.parseInt(scanner.nextLine());

                    route.insertStop(position, newCity, travelTimeToNew);
                    break;

                case "3":
                    if (route.route.isEmpty()) {
                        System.out.println("Маршрут пуст");
                        break;
                    }

                    System.out.print("Позиция для удаления (1-" + route.route.size() + "): ");
                    int removePos = Integer.parseInt(scanner.nextLine()) - 1;
                    route.removeStop(removePos);
                    break;

                case "4":
                    route.displayRoute();
                    break;

                case "5":
                    System.out.println("Общее время в пути: " +
                            formatTime(route.getTotalTravelTime()));
                    break;

                case "6":
                    running = false;
                    System.out.println("Программа завершена.");
                    break;

                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        scanner.close();
    }
}