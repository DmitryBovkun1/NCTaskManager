package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.IOException;
import java.util.List;

public interface Observer {
    String getName();
    String getPassword();
    void setName(String name);
    void setPassword(String passwd);
    void startMenuEvent();
    void customMenuEvent();
    void printEvent(AbstractTaskList abstractTaskList, int time) throws IOException;
    void addTaskEvent(AbstractTaskList abstractTaskList);
    void printTaskEvent(AbstractTaskList abstractTaskList);
    void deleteTaskEvent(AbstractTaskList abstractTaskList);
    void editTaskEvent(AbstractTaskList abstractTaskList);
    void updateTaskEvent(AbstractTaskList abstractTaskList) throws IOException;
    void customFileEvent(AbstractTaskList abstractTaskList) throws IOException;
    void notifyEvent(AbstractTaskList abstractTaskList);
}
