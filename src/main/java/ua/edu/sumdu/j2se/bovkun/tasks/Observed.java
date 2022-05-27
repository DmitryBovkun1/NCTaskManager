package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.IOException;

public interface Observed {
    void addObserver(Observer observer) throws IOException;
    void removeObserver(Observer observer);
    void run(Observer observer);
    boolean welcome(Observer observer) throws IOException;
    void mainMenu(Observer observer) throws IOException;
}
