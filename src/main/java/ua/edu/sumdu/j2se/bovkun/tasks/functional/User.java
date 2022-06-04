package ua.edu.sumdu.j2se.bovkun.tasks.functional;

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
import ua.edu.sumdu.j2se.bovkun.tasks.basic.AbstractTaskList;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.ListTypes;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.Task;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.TaskListFactory;

public class User implements Observer {
    String name;
    String password;

    private static final Logger log = Logger.getLogger(Observer.class);

    public User() {
        FileFormatter.makeWorkDirectoryEvent();
    }

    @Override
    public void printTaskEvent(AbstractTaskList abstractTaskList)
    {
        AbstractTaskList searchAbstractTaskList = Search.searchEvent(this, abstractTaskList, "посмотреть");
        if(searchAbstractTaskList.size() != 0) {
            int i = 1;
            System.out.println("Обнаружено - " + searchAbstractTaskList.size() + " задач с таким именем.");
            for (Task task : searchAbstractTaskList) {
                System.out.println( "Задача № " + i + " :\n" + task );
                i++;
            }
        }
    }

    @Override
    public void deleteTaskEvent(AbstractTaskList abstractTaskList)
    {
        AbstractTaskList searchAbstractTaskList = Search.searchEvent(this, abstractTaskList, "удалить");

        if(searchAbstractTaskList.size() != 0) {
            if(abstractTaskList.remove(Search.searchTaskEvent(searchAbstractTaskList)))
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
        AbstractTaskList searchAbstractTaskList = Search.searchEvent(this, abstractTaskList, "редактировать");

        if(searchAbstractTaskList.size() != 0)
        {
            Task searchTask = Search.searchTaskEvent(searchAbstractTaskList);
            log.info("Пользователь " + getName() + " изменяет задачу:\n" + searchTask);
            while (Edit.edit (abstractTaskList, searchTask));
        }
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
        while (!DataFormatter.checkIntValue(temp, 0, 1))
        {
            System.out.println("Введено неверное значение! Повторите ещё раз! ");
            System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
            temp = in.nextInt();
        }
        boolean active = temp == 1;

        System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
        temp = in.nextInt();
        while (!DataFormatter.checkIntValue(temp, 0, 1))
        {
            System.out.println("Введено неверное значение! Повторите ещё раз! ");
            System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
            temp = in.nextInt();
        }
        boolean repeated = temp == 1;

        if(repeated)
        {
            System.out.println("Дата начала выполнения задачи:");
            LocalDateTime startTime = LocalDateTime.parse(DataFormatter.getTimeEvent());
            System.out.println("Дата конца выполнения задачи:");
            LocalDateTime finishTime = LocalDateTime.parse(DataFormatter.getTimeEvent());
            System.out.println("Интервал выполнения задачи в данный период (в секундах)");
            int interval = in.nextInt();
            abstractTaskList.add(new Task(title, startTime, finishTime, interval, active, true));
        }
        else
        {
            System.out.println("Дата выполнения задачи:");
            LocalDateTime time = LocalDateTime.parse(DataFormatter.getTimeEvent());
            abstractTaskList.add(new Task(title, time, active, false));
        }
        System.out.println("Задача под названием " + title + " успешно добавлена!");
        log.info("Пользователь " + getName() + " добавил " + (repeated ? "повторяющееся " : "не повторяющееся ") + "задание " + title);
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