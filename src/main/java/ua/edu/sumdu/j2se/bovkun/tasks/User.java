package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class User implements Observer {
    String name;
    String password;

    private static final Logger log = Logger.getLogger(Observer.class);

    public User() {
        makeWorkDirectoryEvent();
    }

    private void makeWorkDirectoryEvent()
    {
        String localDir = System.getProperty("user.dir");
        localDir += "\\src/main/java/ua/edu/sumdu/j2se/bovkun/tasks/file/";
        File file = new File(localDir);
        if (file.mkdirs())
        {
            log.info("Рабочая директория создана!!");
        }
    }

    private Task searchTaskEvent(AbstractTaskList abstractTaskList)
    {
        int i = 1;
        System.out.println("Обнаружено - " + abstractTaskList.size() + " задач с таким именем.");
        Task goalTask;
        for (Task task: abstractTaskList) {
            System.out.println( "Задача № " + i + " :\n" + task );
            i++;
        }
        System.out.println( "Выберите номер задачи которую необходимо изменить ( допустимые индексы задач от 1 до " + abstractTaskList.size() + " )" );
        Scanner input = new Scanner(System.in);
        int index = input.nextInt();
        while (!checkIntValue(index, 1, abstractTaskList.size())) {
            System.out.println("Введено неверное значение! Попробуйте ещё раз!");
            System.out.println("Выберите номер задачи которую необходимо изменить ( допустимые индекси задач от 1 до " + abstractTaskList.size() + " )");
            index = input.nextInt();
        }
        goalTask = abstractTaskList.getTask(index - 1);
        return goalTask;
    }

    private AbstractTaskList searchEvent(AbstractTaskList abstractTaskList, String action)
    {
        boolean success = false;
        AbstractTaskList searchAbstractTaskList = TaskListFactory.createTaskList(ListTypes.getTypeList(abstractTaskList));
        try {
            if (abstractTaskList == null) throw new IllegalArgumentException();

            System.out.println("Введите название задачи (полностью или частично) которую нужно " + action + ":");
            Scanner input = new Scanner(System.in);
            String title = input.nextLine();
            log.info("Пользователь " + getName() + " хочет " + action + " задачу под названием " + title);

            for (Task task : abstractTaskList) {
                if (task.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    searchAbstractTaskList.add(task);
                    success = true;
                }
            }
            log.info("Пользователь " + getName() + (success ? "" : "не") + " обнаружил задачу под названием " + title);
            if(!success)
            {
                throw new IllegalArgumentException();
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Данная задача не обнаружена!");
        }
        return searchAbstractTaskList;
    }

    private void customEditMenuEvent(Task task)
    {
        log.info("Пользователь " + getName() + " изменяет задачу:\n" + task);
        System.out.println("------------Меню редактирования----------------");
        System.out.println("--------1.Изменить название задачи------------");
        System.out.println("-----2.Активировать/деактивировать задачу-----");
        System.out.println("-------3.Повторить/не повторять задачу-------");
        if (task.isRepeated()) {
            System.out.println("-4.Редактировать дату начала выполнения задачи-");
            System.out.println("-5.Редактировать дату конца выполнения задачи-");
            System.out.println("-----6.Редактировать интервал выполнения-----");
        }
        else
        {
            System.out.println("----4.Редактировать дату выполнения задачи----");
        }
        System.out.println("------------------0.Выход-------------------");
    }

    private boolean validate(int d, int m, int year){
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

    public void printTaskEvent(AbstractTaskList abstractTaskList)
    {
        AbstractTaskList searchAbstractTaskList = searchEvent(abstractTaskList, "посмотреть");
        if(searchAbstractTaskList.size() != 0) {
            int i = 1;
            System.out.println("Обнаружено - " + searchAbstractTaskList.size() + " задач с таким именем.");
            for (Task task : searchAbstractTaskList) {
                System.out.println( "Задача № " + i + " :\n" + task );
                i++;
            }
        }
    }

    public void deleteTaskEvent(AbstractTaskList abstractTaskList)
    {
        AbstractTaskList searchAbstractTaskList = searchEvent(abstractTaskList, "удалить");

        if(searchAbstractTaskList.size() != 0) {
            if(abstractTaskList.remove(searchTaskEvent(searchAbstractTaskList)))
            {
                System.out.println( "Удаление произошло успешно!" );
            }
            else
            {
                System.out.println( "Неудача при удалении!" );
            }
        }
    }

    public void editTaskEvent(AbstractTaskList abstractTaskList)
    {
        AbstractTaskList searchAbstractTaskList = searchEvent(abstractTaskList, "редактировать");

        if(searchAbstractTaskList.size() != 0)
        {
            Task searchTask = searchTaskEvent(searchAbstractTaskList);
            while (edit (abstractTaskList, searchTask));
        }
    }

    private boolean checkIntValue(int a, int interval1, int interval2)
    {
        return a >= interval1 && a <= interval2;
    }

    private String validateDateForCreate(int partOfDate)
    {
        String value = Integer.toString(partOfDate);
        if(value.length() == 1) {
            value = "0" + value;
        }
        return value;
    }

    private String getTimeEvent() {
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

    @Override
    public void customFileEvent(AbstractTaskList abstractTaskList) throws IOException
    {
        String localDir = System.getProperty("user.dir");
        localDir += "\\src/main/java/ua/edu/sumdu/j2se/bovkun/tasks/file/TaskManager" + getName() + ".out";
        File file = new File(localDir);
        if(!file.exists() || file.length() == 0) {
            if (file.createNewFile())
            {
                log.info("Файл пользователя " + getName() + " создан");
            }
        }
        else
        {
            TaskIO.readBinary(abstractTaskList, file);
        }
        log.info("Пользователь " + getName() + " вошел в систему");
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPassword(String passwd) {
        this.password = passwd;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public void startMenuEvent()
    {
        System.out.println("----------Добро пожаловать!-----------");
        System.out.println("----------Выберите действие------------");
        System.out.println("-----------1.Авторизация--------------");
        System.out.println("-----------2.Регистрация-------------");
        System.out.println("--------------0.Выход---------------");
    }

    @Override
    public void customMenuEvent()
    {
        System.out.println("-----------Добро пожаловать!-----------");
        System.out.println("----------------Меню-------------------");
        System.out.println("--------1.Список всех задач------------");
        System.out.println("--------2.Задачи на неделю------------");
        System.out.println("----------3.Новая задача--------------");
        System.out.println("------4.Информация про задачу--------");
        System.out.println("------5.Редактировать задачу--------");
        System.out.println("---------6.Удалить задачу-----------");
        System.out.println("-------7.Сохранить изменения-------");
        System.out.println("--------------8.Выход---------------");
    }

    @Override
    public void printEvent (AbstractTaskList abstractTaskList, int time) {
        log.info("Пользователь " + getName() + " выводит " + ((time == 0) ? " все задачи " : "задачи за "+ time + " дней"));
        try {
            if (abstractTaskList == null || abstractTaskList.size() == 0) throw new IllegalArgumentException();
            if (time == 0)
            {
                System.out.println("------ЗАДАЧИ В ТЕКУЩЕМ СПИСКЕ-------");
                AbstractTaskList resultTaskList = TaskListFactory.createTaskList(ListTypes.getTypeList(abstractTaskList));
                for (Task task:abstractTaskList) {
                    resultTaskList.add(task);
                }
                System.out.println(resultTaskList);
                if (resultTaskList.size() == 0){
                    throw new IllegalArgumentException();
                }
            }
            else
            {
                System.out.println("-СПИСОК ИСПОЛНЯЕМЫХ ЗАДАЧ НА " + time + " ДНЕЙ-");
                SortedMap<LocalDateTime, Set<Task>> resultTaskList = Tasks.calendar(abstractTaskList, LocalDateTime.now(), LocalDateTime.now().plusDays(time));
                for (var el : resultTaskList.entrySet()) {
                    System.out.println(el.getKey().format(DateTimeFormatter.ofPattern("dd.MM.yyyy => HH:mm:ss")
                            .withZone(ZoneId.systemDefault())) + ": "
                            + el.getValue().stream().map(Task::getTitle).map(list -> String.join("\n", list))
                            .collect(Collectors.joining(", "))
                    );
                }
                if (resultTaskList.size() == 0){
                    throw new IllegalArgumentException();
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Задачи не обнаружены!");
            log.info("Пользователь " + getName() + " вывел пустой список задач");
        }
    }

    @Override
    public void addTaskEvent (AbstractTaskList abstractTaskList)
    {
        log.info("Пользователь " + getName() + " добавляет задачу");
        System.out.println("Вас приветствует мастер добавления задач Task Manager!");
        Scanner in = new Scanner(System.in);
        System.out.println("Введите название задачи");
        String title = in.nextLine();
        System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
        int temp = in.nextInt();
        while (!checkIntValue(temp, 0, 1))
        {
            System.out.println("Введено неверное значение! Повторите ещё раз! ");
            System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
            temp = in.nextInt();
        }
        boolean active = temp == 1;

        System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
        temp = in.nextInt();
        while (!checkIntValue(temp, 0, 1))
        {
            System.out.println("Введено неверное значение! Повторите ещё раз! ");
            System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
            temp = in.nextInt();
        }
        boolean repeated = temp == 1;

        if(repeated)
        {
            System.out.println("Дата начала выполнения задачи:");
            LocalDateTime startTime = LocalDateTime.parse(getTimeEvent());
            System.out.println("Дата конца выполнения задачи:");
            LocalDateTime finishTime = LocalDateTime.parse(getTimeEvent());
            System.out.println("Интервал выполнения задачи в данный период (в секундах)");
            int interval = in.nextInt();
            abstractTaskList.add(new Task(title, startTime, finishTime, interval, active, true));
        }
        else
        {
            System.out.println("Дата выполнения задачи:");
            LocalDateTime time = LocalDateTime.parse(getTimeEvent());
            abstractTaskList.add(new Task(title, time, active, false));
        }
        System.out.println("Задача под названием " + title + " успешно добавлена!");
        log.info("Пользователь " + getName() + " добавил " + (repeated ? "повторяющееся " : "не повторяющееся ") + "задание " + title);
    }

    public boolean edit(AbstractTaskList abstractTaskList, Task task) {
        for (Task tempTask : abstractTaskList) {
            if (task == tempTask) {
                System.out.println("\nРедактируемая задача: \n" + task);
                customEditMenuEvent(task);
                Scanner in = new Scanner(System.in);
                String chooseEdit;
                System.out.println("Выбирите желаемое действие:");
                chooseEdit = in.nextLine();
                if (tempTask.isRepeated()) {
                    switch (chooseEdit) {
                        case "1":
                            System.out.println("Введите новое название задачи");
                            String title = in.nextLine();
                            tempTask.setTitle(title);
                            break;
                        case "2":
                            System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                            int temp = in.nextInt();
                            while (!checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean active = temp == 1;
                            tempTask.setActive(active);
                            break;
                        case "3":
                            System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                            temp = in.nextInt();
                            while (!checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean repeated = temp == 1;

                            if(!repeated) {
                                tempTask.setTime(tempTask.getStartTime());
                            }
                            tempTask.setRepeated(repeated);
                            break;
                        case "4":
                            System.out.println("Дата начала выполнения задачи:");
                            LocalDateTime startTime = LocalDateTime.parse(getTimeEvent());
                            tempTask.setTime(startTime, tempTask.getEndTime(), tempTask.getRepeatInterval());
                            break;
                        case "5":
                            System.out.println("Дата конца выполнения задачи:");
                            LocalDateTime finishTime = LocalDateTime.parse(getTimeEvent());
                            tempTask.setTime(tempTask.getStartTime(), finishTime, tempTask.getRepeatInterval());
                            break;
                        case "6":
                            System.out.println("Интервал выполнения задачи в данный период (в секундах)");
                            int interval = in.nextInt();
                            tempTask.setTime(tempTask.getStartTime(), tempTask.getEndTime(), interval);
                            break;
                        case "0":
                            return false;
                        default:
                            System.out.println("Действе под номером " + chooseEdit + " не обнаружено! Попробуйте ввести что-то другое!");
                    }
                } else {
                    switch (chooseEdit) {
                        case "1":
                            System.out.println("Введите новое название задачи");
                            String title = in.nextLine();
                            tempTask.setTitle(title);
                            break;
                        case "2":
                            System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                            int temp = in.nextInt();
                            while (!checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean active = temp == 1;
                            tempTask.setActive(active);
                            break;
                        case "3":
                            System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                            temp = in.nextInt();
                            while (!checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean repeated = temp == 1;
                            if(repeated) {
                                tempTask.setTime(tempTask.getTime(), tempTask.getTime().plusSeconds(1), 1);
                            }
                            tempTask.setRepeated(repeated);
                            break;
                        case "4":
                            System.out.println("Дата выполнения задачи:");
                            LocalDateTime time = LocalDateTime.parse(getTimeEvent());
                            tempTask.setTime(time);
                            break;
                        case "0":
                            return false;
                        default:
                            System.out.println("Действе под номером " + chooseEdit + " не обнаружено! Попробуйте ввести что-то другое!");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void updateTaskEvent(AbstractTaskList abstractTaskList) throws IOException {
        String localDir = System.getProperty("user.dir");
        localDir += "\\src/main/java/ua/edu/sumdu/j2se/bovkun/tasks/file/TaskManager" + getName() + ".out";
        TaskIO.writeBinary(abstractTaskList, new File(localDir));
        System.out.println("Обновление информации пользователя " + getName() + " прошло успешно!");
        log.info("Пользователь " + getName() + " обновил свой файл конфигурации -  " + localDir);
    }

    @Override
    public void notifyEvent(AbstractTaskList abstractTaskList) {
        AbstractTaskList resultTaskList;
        resultTaskList = (AbstractTaskList) Tasks.incoming(abstractTaskList, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        if (resultTaskList.size() != 0)
        {
            System.out.println("Внимание!! В течении часа должны выполнится следующие задачи:");
            System.out.println(resultTaskList);
        }
    }
}