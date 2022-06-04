package ua.edu.sumdu.j2se.bovkun.tasks.functional;

import ua.edu.sumdu.j2se.bovkun.tasks.basic.AbstractTaskList;

import java.io.IOException;

public interface Observer {
    String getName();
    String getPassword();
    void setName(String name);
    void setPassword(String passwd);
    void printEvent(AbstractTaskList abstractTaskList, int time) throws IOException;
    void addTaskEvent(AbstractTaskList abstractTaskList);
    void printTaskEvent(AbstractTaskList abstractTaskList);
    void deleteTaskEvent(AbstractTaskList abstractTaskList);
    void editTaskEvent(AbstractTaskList abstractTaskList);
    void updateTaskEvent(AbstractTaskList abstractTaskList) throws IOException;
    void customFileEvent(AbstractTaskList abstractTaskList) throws IOException;
    void notifyEvent(AbstractTaskList abstractTaskList);
}
