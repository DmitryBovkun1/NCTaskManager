package ua.edu.sumdu.j2se.bovkun.tasks.functional;

import ua.edu.sumdu.j2se.bovkun.tasks.basic.AbstractTaskList;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.ListTypes;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.Task;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.TaskListFactory;

import java.time.LocalDateTime;
import java.util.*;

public class Tasks {
    public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end)
    {
        AbstractTaskList resultList = TaskListFactory.createTaskList(ListTypes.getTypeList(tasks));
        AbstractTaskList tempList = TaskListFactory.createTaskList(ListTypes.getTypeList(tasks));
        for(Task item : tasks)
        {
            tempList.add(item);
        }
        for(int i = 0; i < tempList.size(); i++)
        {
            if(tempList.getTask(i).nextTimeAfter(start) != null && tempList.getTask(i).nextTimeAfter(start).compareTo(end) <= 0)
            {
                resultList.add(tempList.getTask(i));
            }
        }
        return resultList;
    }

    public static SortedMap<LocalDateTime, Set<Task>> calendar(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end)
    {
        SortedMap<LocalDateTime, Set<Task>> tempMap = new TreeMap<>();
        AbstractTaskList tempList = TaskListFactory.createTaskList(ListTypes.getTypeList(tasks));
        for(Task item : tasks)
        {
            tempList.add(item);
        }

        for (int i = 0; i < tempList.size(); i++) {
            if (!tempList.getTask(i).isRepeated()) {
                if (tempList.getTask(i).getTime().compareTo(start) >= 0 && tempList.getTask(i).getTime().compareTo(end) <= 0) {
                    tempMap.putIfAbsent(tempList.getTask(i).getTime(), new HashSet<Task>());
                    tempMap.get(tempList.getTask(i).getTime()).add(tempList.getTask(i));
                }
            }
            else {
                LocalDateTime currentTime = tempList.getTask(i).nextTimeAfter(start);
                while (currentTime != null && currentTime.compareTo(end) <= 0) {
                    tempMap.putIfAbsent(currentTime, new HashSet<Task>());
                    tempMap.get(currentTime).add(tempList.getTask(i));
                    currentTime = tempList.getTask(i).nextTimeAfter(currentTime);
                }
            }
        }
        return tempMap;
    }
}
