package ua.edu.sumdu.j2se.bovkun.tasks.functional;

import java.time.LocalDateTime;
import java.util.Scanner;

public class DataFormatter {
    public static boolean checkIntValue(int a, int interval1, int interval2)
    {
        return a >= interval1 && a <= interval2;
    }

    private static boolean validate(int d, int m, int year){
        String day = Integer.toString(d);
        String month = Integer.toString(m);

        if (day.equals("31") &&
                (month.equals("4") || month.equals("6") || month.equals("9") ||
                        month.equals("11") || month.equals("04") || month .equals("06") ||
                        month.equals("09"))) {
            return false;
        }

        else if (month.equals("2") || month.equals("02")) {
            if(year % 4==0){
                return !day.equals("30") && !day.equals("31");
            }
            else{

                return !day.equals("29") && !day.equals("30") && !day.equals("31");
            }
        }

        else{
            return true;
        }
    }

    private static String validateDateForCreate(int partOfDate)
    {
        String value = Integer.toString(partOfDate);
        if(value.length() == 1) {
            value = "0" + value;
        }
        return value;
    }

    public static String getTimeEvent() {
        Scanner input = new Scanner(System.in);
        System.out.println("В каком часу (от 0 до 23):");
        int hour = input.nextInt();
        while (!checkIntValue(hour, 0, 23))
        {
            System.out.println("Введено неверное значение! Повторите ещё раз! ");
            System.out.println("В каком часу (от 0 до 23):");
            hour = input.nextInt();
        }

        System.out.println("В какой минуте (от 0 до 59):");
        int minutes = input.nextInt();
        while (!checkIntValue(minutes, 0, 59))
        {
            System.out.println("Введено неверное значение! Повторите ещё раз! ");
            System.out.println("В какой минуте (от 0 до 59):");
            minutes = input.nextInt();
        }

        System.out.println("В какой секунде (от 0 до 59):");
        int second = input.nextInt();
        while (!checkIntValue(second, 0, 59))
        {
            System.out.println("Введено неверное значение! Повторите ещё раз! ");
            System.out.println("В какой секунде (от 0 до 59):");
            second = input.nextInt();
        }

        int day, month, year;

        do {
            System.out.println("Введите день месяца (от 1 до 31):");
            day = input.nextInt();
            while (!checkIntValue(day, 1, 31)) {
                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                System.out.println("Введите день месяца (от 1 до 31):");
                day = input.nextInt();
            }

            System.out.println("Введите месяц (от 1 до 12):");
            month = input.nextInt();
            while (!checkIntValue(month, 1, 12)) {
                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                System.out.println("Введите месяц (от 1 до 12):");
                month = input.nextInt();
            }

            System.out.println("Введите год (от "+ LocalDateTime.now().getYear() + " до 9999):");
            year = input.nextInt();
            while (!checkIntValue(year, LocalDateTime.now().getYear(), 9999)) {
                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                System.out.println("Введите год (от "+ LocalDateTime.now().getYear() + " до 9999):");
                year = input.nextInt();
            }

            if(!validate(day, month, year))
            {
                System.out.println("Данная дата не является валидной!");
            }
        } while (!validate(day, month, year));
        return year + "-" + validateDateForCreate(month) + "-" + validateDateForCreate(day) + "T" + validateDateForCreate(hour) + ":" + validateDateForCreate(minutes) + ":" + validateDateForCreate(second);
    }
}
