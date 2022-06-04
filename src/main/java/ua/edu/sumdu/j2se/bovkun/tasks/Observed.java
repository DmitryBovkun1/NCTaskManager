package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.IOException;

public interface Observed {
    void addObserver(Observer observer) throws IOException;
    void run(Observer observer);
    void mainMenu(Observer observer) throws IOException;
}
