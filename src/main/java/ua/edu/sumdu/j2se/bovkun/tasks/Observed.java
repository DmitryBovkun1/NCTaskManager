package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.IOException;

public interface Observed {
    public void addObserver(Observer observer) throws IOException;
    public void removeObserver(Observer observer);
    public void mainMenu(Observer observer) throws IOException;
}
