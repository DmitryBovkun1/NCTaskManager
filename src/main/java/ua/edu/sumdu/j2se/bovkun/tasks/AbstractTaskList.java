package ua.edu.sumdu.j2se.bovkun.tasks;

import java.lang.reflect.Array;

public class AbstractTaskList {
    public void add(Task task) {}
    public boolean remove(Task task) {return false;}
    public int size() {return 0;}
    public Task getTask(int index) {return new Task(null, 0);}
    public final AbstractTaskList incoming(int from, int to)
    {
        AbstractTaskList resultList = new AbstractTaskList();
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