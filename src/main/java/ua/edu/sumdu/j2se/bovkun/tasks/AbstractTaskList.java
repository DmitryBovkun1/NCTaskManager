package ua.edu.sumdu.j2se.bovkun.tasks;

public abstract class AbstractTaskList {
    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract int size();
    public abstract Task getTask(int index);
    public final AbstractTaskList incoming(int from, int to)
    {
        AbstractTaskList resultList = TaskListFactory.createTaskList(ListTypes.getTypeList(this));
        for(int i = 0; i < size(); i++)
        {
            if(getTask(i).nextTimeAfter(from) != -1 && getTask(i).nextTimeAfter(from) <= to)
            {
                resultList.add(getTask(i));
            }
        }
        return resultList;
    }
}