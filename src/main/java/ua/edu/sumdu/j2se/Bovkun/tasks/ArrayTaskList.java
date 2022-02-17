package ua.edu.sumdu.j2se.Bovkun.tasks;

import java.util.ArrayList;

public class ArrayTaskList {
    //ArrayList<Task> TaskList = new ArrayList<>();
    Task[] TaskList = new Task[4];
    int size = 0;

    public void add(Task task)
    {
        /*TaskList.add(task);*/
        size++;
        if(TaskList.length < size)
        {
            int tempSize=2*TaskList.length+1;
            Task[] TempTaskList = new Task[tempSize];
            System.arraycopy(TaskList, 0, TempTaskList, 0, TaskList.length);
            TaskList = new Task[tempSize];
            System.arraycopy(TempTaskList, 0, TaskList, 0, TempTaskList.length);
        }
        TaskList[size-1]=task;
    }
    public boolean remove(Task task)
    {
        /*
        int index = TaskList.indexOf(task);
        //возвращает индекс первого вхождения объекта obj в список.
        // Если объект не найден, то возвращается -1

        if(index == -1) {
            return false;
        }
        TaskList.remove(index);
        return true;
        */
        int index = -1;
        for(int i = 0; i < size; i++)
        {
            if(TaskList[i] == task)
            {
                index = i;
                break;
            }
        }

        if(index == -1) {
            return false;
        }

        for(int i = index; i < size - 1; i++)
        {
            TaskList[i] = TaskList[i+1];
        }
        size--;
        return true;
    }
    public int size()
    {
        return size;
        //return TaskList.size();
    }
    public Task getTask(int index)
    {
        return TaskList[index];
        //return TaskList.get(index);
    }
    public ArrayTaskList incoming(int from, int to)
    {
        ArrayTaskList resultList = new ArrayTaskList();
        for(int i = 0; i < size(); i++)
        {
            /*if((TaskList.get(i).getStartTime() > from && TaskList.get(i).getEndTime() < to) && TaskList.get(i).isActive())
            {
                resultList.add(TaskList.get(i));
            }*/
            if((TaskList[i].getStartTime() > from && TaskList[i].getEndTime() < to) && TaskList[i].isActive())
            {
                resultList.add(TaskList[i]);
            }
        }
        return resultList;
    }
}