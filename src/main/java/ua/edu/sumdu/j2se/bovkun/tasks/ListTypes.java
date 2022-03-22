package ua.edu.sumdu.j2se.bovkun.tasks;

public class ListTypes {
    public enum types{ARRAY, LINKED}
    public static types getTypeList(AbstractTaskList taskObj) {
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