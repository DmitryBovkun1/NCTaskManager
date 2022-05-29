package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.InputMismatchException;
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
                String passwd;
                String passwdRepeat;
                do {
                    Scanner in = new Scanner(System.in);
                    System.out.println("Введите пароль");
                    passwd = in.nextLine();
                    System.out.println("Повторите пароль");
                    passwdRepeat = in.nextLine();
                    if(!Objects.equals(passwd, passwdRepeat))
                    {
                        System.out.println("Пароли не совпадают!! Попробуйте ещё раз");
                    }
                } while (!Objects.equals(passwd, passwdRepeat));
                observer.setPassword(Security.hash(passwd));
                DataOutputStream stream1 = new DataOutputStream(fos);
                DataInputStream stream2 = new DataInputStream(fis);
                stream1.writeUTF(observer.getName());
                stream1.writeUTF(observer.getPassword());
                while (fis.available() > 0) {
                    stream1.writeUTF(stream2.readUTF());
                }
                System.out.println("Пользователя добавлено!");
                log.info("Пользователь " + observer.getName() + " добавлен!");
            }
        }
        catch (IOException ex)
        {
            System.out.println("Произошла ошибка!" + ex.getMessage() + "\nПопробуйте ещё раз!");
            log.error("Произошла ошибка при добавлении пользователя " + observer.getName() + " - " + ex.getMessage());
        }
        new File(localDir + "1").delete();
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
                    temp.setPassword(stream2.readUTF());
                    if (Objects.equals(observer.getName(), temp.getName())) {
                        observer.setPassword(temp.getPassword());
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

    public boolean checkLogin(Observer observer) throws IOException {
        boolean success = false;

        System.out.println("Пожалуйста введите ваше имя:");
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();

        observer.setName(temp);

        if(checkObserver(observer)) {
            int trys = 3;
            while(trys != 0 && !success)
            {
                System.out.println("Введите пароль пользователя " + observer.getName() + " ( осталось " + trys + " попытки )");
                String tempPassword = in.nextLine();
                if (Objects.equals(Security.hash(tempPassword), observer.getPassword()))
                {
                    success = true;
                }
                trys--;
            }
            if(success)
            {
                System.out.println("-------С возвращением  " + observer.getName() + " !-------");
                log.info("Вход пользователя " + observer.getName() + " произошел успешно!");
            }
            else {
                System.out.println("Вход выполнен неудачно для пользователя " + observer.getName());
                log.info("Вход пользователя " + observer.getName() + " произошел неудачно! Пароль не совпадает");
            }
        }
        else {
            log.info("Пользователя с именем " + observer.getName() + " не обнаружено!");
            addObserver(observer);
            success = true;
        }
        return success;
    }

    @Override
    public void run(Observer observer) {
        try {
            while (!welcome(observer));
            observer.customFileEvent(linkedTaskList);
            mainMenu(observer);
        }
        catch (Exception e)
        {
            System.out.println("Что-то пошло не так! Критическая ошибка.");
            log.error("В приложении произошла критическая ошибка - " + e.getMessage());
        }
    }

    @Override
    public boolean welcome(Observer observer) throws IOException {
        log.info("Произошел вход в систему!");
        System.out.println("Вас приветствует программа Task Manager.\nЗдесь Вы сможете управлять своим расписанием");
        System.out.println("Для начала работы вам необходимо внести Ваше имя! Имя должно быть уникальным в системе!");
        System.out.println("Если Вы уже заходили в эту программу, то введите имя которое Вы вводили до этого!");
        return checkLogin(observer);
    }

    @Override
    public void mainMenu(Observer observer) {
        boolean repeated = true;
        while(repeated) {
            try {
                observer.notifyEvent(linkedTaskList);
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
            catch (InputMismatchException e)
            {
                System.out.println("Произошла критическая ошибка несоответствия типа!!" + "\n");
                log.error("В приложении произошла ошибка - несоответствия типа");
            }
            catch (Exception e)
            {
                System.out.println("Произошла " + ((e.getMessage() != null) ? " ошибка " + e.getMessage() : "непредвиденная ошибка! Обратитесь в службу поддержки! ") + "\n");
                log.error("В приложении произошла " + ((e.getMessage() != null) ? "ошибка " + e.getMessage() : "непредвиденная ошибка "));
            }
        }
    }
}
