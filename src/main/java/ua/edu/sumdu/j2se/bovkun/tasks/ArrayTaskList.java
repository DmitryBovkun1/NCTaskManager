package ua.edu.sumdu.j2se.bovkun.tasks;

import java.util.LinkedList;

public class ArrayTaskList extends AbstractTaskList{
    //ArrayList<Task> TaskList = new ArrayList<>();
    Task[] taskList = new Task[4];
    int size = 0;

    public void add(Task task)
    {
        if(task != null) {
            /*TaskList.add(task);*/
            size++;
            if (taskList.length < size) {
                int tempSize = 2 * taskList.length + 1;
                Task[] tempTaskList = new Task[tempSize];
                System.arraycopy(taskList, 0, tempTaskList, 0, taskList.length);
                taskList = new Task[tempSize];
                System.arraycopy(tempTaskList, 0, taskList, 0, tempTaskList.length);
            }
            taskList[size - 1] = task;
        }
        else
        {
            throw new IllegalArgumentException();
        }
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
            if(taskList[i] == task)
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
            taskList[i] = taskList[i+1];
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
        if(index >= 0 && index <= size) {
            return taskList[index];
            //return TaskList.get(index);
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
    }
}