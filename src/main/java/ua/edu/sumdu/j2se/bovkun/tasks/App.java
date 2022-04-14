package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.Scanner;
import java.io.IOException;
import org.apache.log4j.Logger;

public class App implements Observed {

    private final LinkedTaskList linkedTaskList = new LinkedTaskList();
    private static final Logger log = Logger.getLogger(App.class);

    public LinkedTaskList getList()
    {
        return linkedTaskList;
    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
        finally{
            sourceChannel.close();
            destChannel.close();
        }
    }

    @Override
    public void addObserver(Observer observer) throws IOException {
        String localDir = System.getProperty("user.dir");
        localDir += "\\src/main/java/ua/edu/sumdu/j2se/bovkun/tasks/file/main_user.out";
        copyFileUsingChannel(new File(localDir), new File(localDir + "1"));
        try(FileOutputStream fos=new FileOutputStream(localDir);
        FileInputStream fis=new FileInputStream(localDir + "1")) {
            if(checkObserver(observer))
            {
                System.out.println("Пользователь уже существует!");
                log.info("Пользователь " + observer.getName() + " уже существует!");
            }
            {
                DataOutputStream stream1 = new DataOutputStream(fos);
                DataInputStream stream2 = new DataInputStream(fis);
                stream1.writeUTF(observer.getName());
                while (fis.available() > 0) {
                    stream1.writeUTF(stream2.readUTF());
                }
                System.out.println("Пользователя добавлено!");
                log.info("Пользователь " + observer.getName() + " добавлен!");
            }
            new File(localDir + "1").delete();
        }
        catch (IOException ex)
        {
            System.out.println("Произошла ошибка!" + ex.getMessage() + "\nПопробуйте ещё раз!");
            log.error("Произошла ошибка при добавлении пользователя " + observer.getName() + " - " + ex.getMessage());
        }
    }

    @Override
    public void removeObserver(Observer observer)
    {
        try(FileInputStream fis=new FileInputStream("files/main_user");
            FileOutputStream fos=new FileOutputStream("files/main_user1"))
        {
            if(checkObserver(observer)) {
                DataInputStream stream1 = new DataInputStream(fis);
                DataOutputStream stream2 = new DataOutputStream(fos);
                while ((stream1.read()) != -1) {
                    String tempString = stream1.readUTF();
                    if (!Objects.equals(observer.getName(), tempString)) {
                        stream2.writeUTF(tempString);
                    }
                }

                copyFileUsingChannel(new File("files/main_user1"), new File("files/main_user"));
                new File("files/main_user1").delete();
            }
            else
            {
                System.out.println("Пользователя с таким именем не существует!");
            }
        }
        catch (IOException ex)
        {
            System.out.println("Произошла ошибка! " + ex.getMessage() + "\nПопробуйте ещё раз!");
            log.error("Произошла ошибка при удалении пользователя " + observer.getName() + " - " + ex.getMessage());
        }
    }

    private boolean checkObserver(Observer observer) throws IOException {
        String localDir = System.getProperty("user.dir");
        localDir += "\\src/main/java/ua/edu/sumdu/j2se/bovkun/tasks/file/main_user.out";
        File file = new File(localDir);
        if(!file.exists() || file.length() == 0) {
            if(file.createNewFile())
            {
                log.info("Файл для хранения учетных записей пользователей создан.");
            }
        }
        else {
            try (FileInputStream fis = new FileInputStream(localDir)) {

                DataInputStream stream2 = new DataInputStream(fis);

                Observer temp = new User();

                while (stream2.available() > 0) {
                    temp.setName(stream2.readUTF());
                    if (Objects.equals(observer.getName(), temp.getName())) {
                        return true;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Произошла ошибка! " + ex.getMessage() + "\nПопробуйте ещё раз!");
                log.error("Произошла ошибка при проверке учетной записи пользователя " + observer.getName() + " - " + ex.getMessage());
            }
        }
        return false;
    }

    public void checkLogin(Observer observer) throws IOException {
        System.out.println("Пожалуйста введите ваше имя:");
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        observer.setName(temp);

        if(checkObserver(observer)) {
            System.out.println("-------С возвращением  " + observer.getName() + " !-------");
            log.info("Вход пользователя " + observer.getName() + " произошел успешно!");
        }
        else {
            log.info("Пользователя с именем " + observer.getName() + " не обнаружено!");
            addObserver(observer);
        }
        observer.customFileEvent(linkedTaskList);
    }

    public void welcome(Observer observer) throws IOException {
        log.info("Произошел вход в систему!");
        System.out.println("Вас приветствует программа Task Manager.\nЗдесь Вы сможете управлять своим расписанием");
        System.out.println("Для начала работы вам необходимо внести Ваше имя! Имя должно быть уникальным в системе!");
        System.out.println("Если Вы уже заходили в эту программу, то введите имя которое Вы вводили до этого!");
        checkLogin(observer);
    }

    @Override
    public void mainMenu(Observer observer) throws IOException {
        boolean repeated = true;
        while(repeated) {
            try {
                observer.customMenuEvent();
                Scanner in = new Scanner(System.in);
                String action = in.nextLine();
                switch (action) {
                    case "1":
                        observer.printEvent(linkedTaskList, 0);
                        break;
                    case "2":
                        observer.printEvent(linkedTaskList, 7);
                        break;
                    case "3":
                        observer.addTaskEvent(linkedTaskList);
                        break;
                    case "4":
                        observer.printTaskEvent(linkedTaskList);
                        break;
                    case "5":
                        observer.editTaskEvent(linkedTaskList);
                        break;
                    case "6":
                        observer.deleteTaskEvent(linkedTaskList);
                        break;
                    case "7":
                        observer.updateTaskEvent(linkedTaskList);
                        break;
                    case "8":
                        repeated = false;
                        break;
                    default:
                        System.out.println("Действе под номером " + action + " не обнаружено! Попробуйте ввести что-то другое!");
                }
                log.info("Выбрано действие под номером - " + action + " Пользователем - " + observer.getName());
            }
            catch (Exception e)
            {
                System.out.println("Произошла ошибка " + e.getMessage());
                log.error("В приложении произошла ошибка - " + e.getMessage());
            }
        }
    }
}
