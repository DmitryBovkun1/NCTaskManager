package ua.edu.sumdu.j2se.bovkun.tasks.functional;

import org.apache.log4j.Logger;

import java.io.File;

public class FileFormatter {
    private static final Logger log = Logger.getLogger(Observer.class);

    public static void makeWorkDirectoryEvent()
    {
        String localDir = System.getProperty("user.dir");
        localDir += "\\src/main/java/ua/edu/sumdu/j2se/bovkun/tasks/file/";
        File file = new File(localDir);
        if (file.mkdirs())
        {
            log.info("Рабочая директория создана!!");
        }
    }
}
