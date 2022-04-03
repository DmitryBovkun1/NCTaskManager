package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.IOException;
import java.util.List;

public interface Observer {
    public String getName();
    public void setName(String name);
    public void customMenuEvent();
    public void printEvent (AbstractTaskList abstractTaskList, int time) throws IOException;
    public void addTaskEvent (AbstractTaskList abstractTaskList);
    public void printTaskEvent(AbstractTaskList abstractTaskList);
    public void deleteTaskEvent(AbstractTaskList abstractTaskList);
    public void editTaskEvent(AbstractTaskList abstractTaskList);
    public void updateTaskEvent(AbstractTaskList abstractTaskList) throws IOException;
    public void customFileEvent(AbstractTaskList abstractTaskList) throws IOException;
}
