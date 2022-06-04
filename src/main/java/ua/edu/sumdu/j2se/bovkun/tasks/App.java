package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.io.IOException;
import org.apache.log4j.Logger;

public class App implements Observed {

    private final LinkedTaskList linkedTaskList = new LinkedTaskList();
    private static final Logger log = Logger.getLogger(App.class);
    private boolean repeated = true;
    public LinkedTaskList getList()
    {
        return linkedTaskList;
    }
    @Override
    public void addObserver(Observer observer) {
        String localDir = System.getProperty("user.dir");
        localDir += "\\src/main/java/ua/edu/sumdu/j2se/bovkun/tasks/file/main_user.out";
        try(FileOutputStream fos=new FileOutputStream(localDir, true)) {
            if(checkObserver(observer))
            {
                System.out.println("Пользователь уже существует!");
                log.info("Пользователь " + observer.getName() + " уже существует!");
            }
            else
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
                    else if(passwd == null || passwd.equals(""))
                    {
                        System.out.println("Ошибка! Пароль не может быть пустым!!");
                        log.info("Пользователь " + observer.getName() + " попытался войти под пустым паролем!");
                        throw new IllegalArgumentException("Введено некоректное значение переменной пароля!");
                    }
                } while (!Objects.equals(passwd, passwdRepeat));
                if(!passwd.equals("")) {
                    observer.setPassword(Security.hash(passwd));
                    DataOutputStream stream1 = new DataOutputStream(fos);
                    stream1.writeUTF(observer.getName());
                    stream1.writeUTF(observer.getPassword());
                    System.out.println("Пользователя добавлено!");
                    log.info("Пользователь " + observer.getName() + " добавлен!");
                }
            }
        }
        catch (IOException ex)
        {
            System.out.println("Произошла ошибка!" + ex.getMessage() + "\nПопробуйте ещё раз!");
            log.error("Произошла ошибка при добавлении пользователя " + observer.getName() + " - " + ex.getMessage());
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

    public boolean checkLogin(Observer observer, String action) throws IOException {
        boolean success = false;

        System.out.println("Пожалуйста введите ваше имя:");
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        if(temp == null || temp.equals(""))
        {
            System.out.println("Ошибка! Имя не может быть пустым!!");
            throw new IllegalArgumentException("Введено некорректное значение имени пользователя! Попробуйте ещё раз!");
        }
        else {
            observer.setName(temp);

            if (checkObserver(observer) && Objects.equals(action, "1")) {
                int trys = 3;
                while (trys != 0 && !success) {
                    System.out.println("Введите пароль пользователя " + observer.getName() + " ( осталось " + trys + " попытки )");
                    String tempPassword = in.nextLine();
                    if (Objects.equals(Security.hash(tempPassword), observer.getPassword())) {
                        success = true;
                    }
                    trys--;
                }
                if (success) {
                    System.out.println("-------С возвращением  " + observer.getName() + " !-------");
                    log.info("Вход пользователя " + observer.getName() + " произошел успешно!");
                } else {
                    System.out.println("Вход выполнен неудачно для пользователя " + observer.getName());
                    log.info("Вход пользователя " + observer.getName() + " произошел неудачно! Пароль не совпадает");
                }
                return success;
            } else if (Objects.equals(action, "1")) {
                System.out.println("Пользователя с именем " + observer.getName() + " не существует!");
                System.out.println("Зарегестрируйте его!");
                return false;
            }
            if (!checkObserver(observer) && Objects.equals(action, "2")) {
                log.info("Пользователя с именем " + observer.getName() + " не обнаружено!");
                addObserver(observer);
                success = true;
            } else if (Objects.equals(action, "2")) {
                System.out.println("Пользователь с именем " + observer.getName() + " уже существует!");
                System.out.println("Войдите под его учетными данными!");
                log.info("Регестрация пользователя " + observer.getName() + " произошла неудачно! Пользователь существует!");
            }
        }
        return success;
    }

    @Override
    public void run(Observer observer) {
        try {
            welcomeMenu(observer);
            mainMenu(observer);
        }
        catch (Exception e)
        {
            System.out.println("Что-то пошло не так! Критическая ошибка.");
            log.error("В приложении произошла критическая ошибка - " + e.getMessage());
        }
    }

    public void welcome() {
        log.info("Произошел вход в систему!");
        System.out.println("Вас приветствует программа Task Manager.\nЗдесь Вы сможете управлять своим расписанием");
        System.out.println("Для начала работы вам необходимо выбрать действие для входа!");

    }

    public void welcomeMenu(Observer observer) {
        boolean status = false;
        welcome();
        while(repeated && !status) {
            try {
                observer.startMenuEvent();
                Scanner in = new Scanner(System.in);
                String action = in.nextLine();
                switch (action) {
                    case "1":
                    case "2":
                        status = checkLogin(observer, action);
                        if (status) {
                            observer.customFileEvent(linkedTaskList);
                        }
                        break;
                    case "0":
                        repeated = false;
                        break;
                    default:
                        System.out.println("Действе под номером " + action + " не обнаружено! Попробуйте ввести что-то другое!");
                        break;
                }
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                log.error("В приложении произошла ошибка - " + e.getMessage());
            }
            catch (Exception e)
            {
                System.out.println("Произошла " + ((e.getMessage() != null) ? " ошибка " + e.getMessage() : "непредвиденная ошибка! Обратитесь в службу поддержки! ") + "\n");
                log.error("В приложении произошла " + ((e.getMessage() != null) ? "ошибка " + e.getMessage() : "непредвиденная ошибка "));
            }
        }
    }

    @Override
    public void mainMenu(Observer observer) {
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
