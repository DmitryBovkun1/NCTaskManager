package ua.edu.sumdu.j2se.bovkun.tasks;

public class TaskListFactory {
    static public AbstractTaskList createTaskList(ListTypes.types type) {
        if(type.equals(ListTypes.types.ARRAY))
        {
            return new ArrayTaskList();
        }
        else
        {
            return new LinkedTaskList();
        }
    }
}