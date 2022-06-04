package ua.edu.sumdu.j2se.bovkun.tasks.basic;

public class ListTypes {
    public enum types{ARRAY, LINKED}
    public static types getTypeList(Iterable<Task> taskObj) {
        if(taskObj instanceof LinkedTaskList)
        {
            return types.LINKED;
        }
        else
        {
            return types.ARRAY;
        }
    }
}