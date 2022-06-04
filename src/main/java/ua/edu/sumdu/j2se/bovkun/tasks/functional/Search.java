package ua.edu.sumdu.j2se.bovkun.tasks.functional;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.AbstractTaskList;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.ListTypes;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.Task;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.TaskListFactory;

import java.util.Scanner;

public class Search {
    private static final Logger log = Logger.getLogger(Observer.class);
    public static Task searchTaskEvent(AbstractTaskList abstractTaskList)
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
        while (!DataFormatter.checkIntValue(index, 1, abstractTaskList.size())) {
            System.out.println("Введено неверное значение! Попробуйте ещё раз!");
            System.out.println("Выберите номер задачи которую необходимо изменить ( допустимые индекси задач от 1 до " + abstractTaskList.size() + " )");
            index = input.nextInt();
        }
        goalTask = abstractTaskList.getTask(index - 1);
        return goalTask;
    }

    public static AbstractTaskList searchEvent(Observer observer, AbstractTaskList abstractTaskList, String action)
    {
        boolean success = false;
        AbstractTaskList searchAbstractTaskList = TaskListFactory.createTaskList(ListTypes.getTypeList(abstractTaskList));
        try {
            if (abstractTaskList == null) throw new IllegalArgumentException();

            System.out.println("Введите название задачи (полностью или частично) которую нужно " + action + ":");
            Scanner input = new Scanner(System.in);
            String title = input.nextLine();
            log.info("Пользователь " + observer.getName() + " хочет " + action + " задачу под названием " + title);

            for (Task task : abstractTaskList) {
                if (task.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    searchAbstractTaskList.add(task);
                    success = true;
                }
            }
            log.info("Пользователь " + observer.getName() + (success ? "" : "не") + " обнаружил задачу под названием " + title);
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
}
