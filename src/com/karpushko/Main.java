package com.karpushko;

import java.util.*;
import java.util.stream.Collectors;

/*
 *Map - динамический массив.
 * Интерфейс Map<K, V> представляет отображение или иначе говоря словарь, где каждый элемент представляет пару "ключ-значение".
 * Ключ (key) - страна, уникальный элемент. Значение (value) - баллы, изменяемый элемент.
 * countries - первоначальный Map, в который заносятся страны и баллы, при запуске приложения.
 *  sortedMap = countries отсортированный для каждого тура индивидуалтно. Каждый метод взаимодействует с ним по разно.
 * Например, для отображения победителей тура, countries сортируется по убыванию баллов. Так же к уже отсортированному
 * Map - sortedMap добавляется метод .limit(3), который выводит только три первых значения Map.
 */

public class Main {
    static Map<String, Integer> countries = new HashMap<>();
    static Map<String, Integer> sortedMap;

    public static void main(String[] args) {
        //.put - метод, передающий в Map "countries" аргумениы: key- страна типа String, value - баллы типа Integer.
        countries.put("Россия", 62);
        countries.put("Болгария", 123);
        countries.put("Канада", 120);
        countries.put("Украина", 139);
        countries.put("Словакия", 80);
        countries.put("Турция", 76);
        countries.put("Сербия", 28);

        // Вызов метода getMenu();
        getMenu();
    }

    private static void getMenu() {
        // Задается предел, при котором начало первого тура невозмжно. Т.е. пока в таблице не будет минимум 10 стран,
        // подменю начала первого тура не будет отображаться, вместо него обратный счетчик.
        if (countries.size() < 10) {
            System.out.println();
            getCountries();
            System.out.println();
            System.out.printf("%-15s [%s]\n", "Добавить страну", "1");
            System.out.printf("%-15s [%s]\n", "Выход", "9");
            System.out.println("\nСтран до первого тура: " +
                    (10 - countries.size()));
            // getInt() - возвращает int, с проверкой на тип.
            switch (getInt()) {
                case 1:
                    addCountry();
                    break;
                case 9:
                    return;
                default:
                    System.err.println("Введите целое число из списка");
                    getMenu();
                    break;
            }
        } else {
            // Реализация меню, когда стран >= 10.
            System.out.println();
            // getCountries() метод вывода таблицы стран. Тип void;
            getCountries();
            System.out.println();
            System.out.printf("%-15s [%s]\n", "Добавить страну", "1");
            System.out.printf("%-15s [%s]\n", "Первый тур", "2");
            System.out.printf("%-15s [%s]\n", "Выход", "9");
            switch (getInt()) {
                case 1:
                    addCountry();
                    break;
                case 2:
                    tourOne();
                    break;
                case 9:
                    return;
                default:
                    System.err.println("Введите целое число из списка");
                    // Рекурсия метода, в случае ошибки, т.е. вызов getMenu(), для отображения сначала.
                    getMenu();
                    break;
            }
        }
    }

    // Метод возвращает сумму (sum) всех баллов от каждого судьи. Тип int.
    public static int getPoints() {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            System.out.print("Судья " + (i + 1) + ": ");
            sum += getInt();
        }
        return sum;
    }

    /*
     * Entry - обращение к Map. Метод позволяет получить от каждого элемента Map ключ (страна) и значени (баллы).
     * Stream - API: поток, позволяет работать со структурами данных, в частности с данными Map.
     * .sorted - метод сортировки.
     * Comparator - класс сравнивания. Из него мы берем comparingByValue - сравниваем значение (value) в Map.
     * Comparator.reverseOrder() - вывод в обратном порядке. Т.е. значение сортируются от большего к меньшему.
     * .limit() - ограничение.
     * .collect (Stream API) - получение данных в виде коллекции, в частности в Map (Collectors.toMap).
     * Map.Entry::getKey - ссылка на ключ (страна).
     * Map.Entry::getValue - ссылка на значени (баллы).
     * (e1, e2) -> e1, LinkedHashMap::new) - лямбда, т.е. для каждого значения (баллов) создается ссылка на
     * LinkedHashMap (новый обьект), после чего они сравниваются, с помощью comparing.
     *
     */
    public static void tourOne() {
        sortedMap = countries.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println("Победители первого тура: ");
        int count = 0;
        // Вывовд в форме [ count key value ] для каждого обьекта (entry) из sortedMap.
        // count - счетчик, номер позиции страны.
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            System.out.println(++count + ". " + entry.getKey() + " " + entry.getValue());
        }
        System.out.println();
        // Список стран.
        getCountries();
        tourTwo();
    }

    // Реализация второго тура.
    public static void tourTwo() {
        System.out.println();
        // Вывод списка стран.
        getCountries();
        System.out.println();
        System.out.printf("%-15s [%s]\n", "Добавить баллы", "1");
        System.out.printf("%-15s [%s]\n", "Второй тур", "2");
        System.out.printf("%-15s [%s]\n", "Выход", "9");

        switch (getInt()) {
            case 1:
                System.out.println("Введите название страны: ");
                String name = new Scanner(System.in).nextLine();
                // substring - метож разделение String.
                //.substring(0, 1).toUpperCase() - отсекаем первую букву (0, 1) , делаем ее заглавной.
                // .substring(1).toLowerCase() - последующие строчные.
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                // isCyrillic() - метод проверки слова на кириллицу. Возвращает true, если слово состоит только из
                // букв на кириллице, иначе возвращает false.
                if (isCyrillic(name)) {
                    // cnt - счетчик. Если, ни один entry не совпадает с name - ошибка: cnt = 1.
                    int cnt = 0;
                    for (Map.Entry<String, Integer> entry : countries.entrySet()) {
                        // .equals - сравнение строк. Т.е. сравнение key (страны) и строки, введенной пользователем.
                        if (entry.getKey().equals(name)) {
                            // setValue () - запись значени (баллов) в обьект entry, т.е. для каждого key (страны) из
                            // Map.
                            // entry.getValue() + getPoints() - сумма старого значения и суммы судейских баллов.
                            entry.setValue(entry.getValue() + getPoints());
                        } else cnt = 1;
                    }
                    if (cnt != 0) {
                        System.out.println("Такой страны нет в списке");
                    }
                } else {
                    System.err.println("Введите страну на кириллице");
                }
                tourTwo();
                break;
            case 2:
                System.out.println("Победители второго тура: ");
                /*
                 * Entry - обращение к Map. Метод позволяет получить от каждого элемента Map ключ (страна) и значени (баллы).
                 * Stream - API: поток, позволяет работать со структурами данных, в частности с данными Map.
                 * .sorted - метод сортировки.
                 * Comparator - класс сравнивания. Из него мы берем comparingByValue - сравниваем значение (value) в Map.
                 * Comparator.reverseOrder() - вывод в обратном порядке. Т.е. значение сортируются от большего к меньшему.
                 * .limit() - ограничение.
                 * .collect (Stream API) - получение данных в виде коллекции, в частности в Map (Collectors.toMap).
                 * Map.Entry::getKey - ссылка на ключ (страна).
                 * Map.Entry::getValue - ссылка на значени (баллы).
                 * (e1, e2) -> e1, LinkedHashMap::new) - лямбда, т.е. для каждого значения (баллов) создается ссылка на
                 * LinkedHashMap (новый обьект), после чего они сравниваются, с помощью comparing.
                 *
                 */
                sortedMap = countries.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(3)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
                int count = 0;
                for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                    // Вывовд в форме [ count key value ] для каждого обьекта (entry) из sortedMap.
                    // count - счетчик, номер позиции страны.
                    System.out.println(++count + ". " + entry.getKey() + " " + entry.getValue());
                }
                break;
            case 9:
                return;
            default:
                System.err.println("Введите целое число из списка");
                tourTwo();
                break;
        }

    }

    // Метод добавления страны.
    private static void addCountry() {
        System.out.println("Название: ");
        String name = new Scanner(System.in).nextLine();
        // substring - метож разделение String.
        //.substring(0, 1).toUpperCase() - отсекаем первую букву (0, 1) , делаем ее заглавной.
        // .substring(1).toLowerCase() - последующие строчные.
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        // isCyrillic() - метод проверки слова на кириллицу. Возвращает true, если слово состоит только из
        // букв на кириллице, иначе возвращает false.
        if (isCyrillic(name)) {
            for (Map.Entry<String, Integer> entry : countries.entrySet()) {
                if (entry.getKey().equals(name)) {
                    System.err.println("Такая страна уже существует");
                    addCountry();
                    return;
                }
            }
            System.out.println("Баллы: ");
            // countries.put() - добавление в значение (баллы) Map полученную строку
            // и сумму баллов из метода getPoints();
            countries.put(
                    name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(),
                    getPoints()
            );
        } else {
            System.err.println("Введите страну на кириллице");
            addCountry();
        }
        getMenu();
    }

    // Метод получения таблицы стран.
    public static void getCountries() {
        /*
         * Entry - обращение к Map. Метод позволяет получить от каждого элемента Map ключ (страна) и значени (баллы).
         * Stream - API: поток, позволяет работать со структурами данных, в частности с данными Map.
         * .sorted - метод сортировки.
         * Comparator - класс сравнивания. Из него мы берем comparingByValue - сравниваем значение (value) в Map.
         * Comparator.reverseOrder() - вывод в обратном порядке. Т.е. значение сортируются от большего к меньшему.
         * .collect (Stream API) - получение данных в виде коллекции, в частности в Map (Collectors.toMap).
         * Map.Entry::getKey - ссылка на ключ (страна).
         * Map.Entry::getValue - ссылка на значени (баллы).
         * (e1, e2) -> e1, LinkedHashMap::new) - лямбда, т.е. для каждого значения (баллов) создается ссылка на
         * LinkedHashMap (новый обьект), после чего они сравниваются, с помощью comparing.
         *
         */
        sortedMap = countries.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        // Подгонка под таблицу.
        System.out.printf("%-8s| %-2s\n--------+-------\n", "Страна", "Баллы");
        // Вовод элементов Map.
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet())
            System.out.printf("%-8s| %-2s\n", entry.getKey(), entry.getValue());
    }

    // Проверка строки на кириллицу.
    public static boolean isCyrillic(String str) {
        // Сравнение каждого символа в разбитой посимвольно строке (str.toCharArray())
        // С юникод : CYRILLIC.
        for (char ch : str.toCharArray()) {
            if (Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.CYRILLIC) {
                return false;
            }
        }
        return true;
    }

    // Проверка вводимого числа на int.
    public static int getInt() {
        int points = 0;
        // Пока значение не получено, цикл крутится
        while (true)
            // Обработка ошибки.
            try {
                points = new Scanner(System.in).nextInt();
                if (points >= 0 && points <= 12)
                    return points;
                else System.out.println("Введите число в диапазоне: [0:12]");
            } catch (Exception e) {
                System.err.println("Введите целое число");
            }
    }
}