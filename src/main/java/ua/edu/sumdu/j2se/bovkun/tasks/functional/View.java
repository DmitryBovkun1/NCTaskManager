package ua.edu.sumdu.j2se.bovkun.tasks.functional;

import ua.edu.sumdu.j2se.bovkun.tasks.basic.Task;

public class View {
    public static void welcome() {
        System.out.println("Вас приветствует программа Task Manager.\nЗдесь Вы сможете управлять своим расписанием");
        System.out.println("Для начала работы вам необходимо выбрать действие для входа!");
    }
    public static void startMenuEvent()
    {
        System.out.println("----------Добро пожаловать!-----------");
        System.out.println("----------Выберите действие------------");
        System.out.println("-----------1.Авторизация--------------");
        System.out.println("-----------2.Регистрация-------------");
        System.out.println("--------------0.Выход---------------");
    }

    public static void customMenuEvent()
    {
        System.out.println("-----------Добро пожаловать!-----------");
        System.out.println("----------------Меню-------------------");
        System.out.println("--------1.Список всех задач------------");
        System.out.println("--------2.Задачи на неделю------------");
        System.out.println("----------3.Новая задача--------------");
        System.out.println("------4.Информация про задачу--------");
        System.out.println("------5.Редактировать задачу--------");
        System.out.println("---------6.Удалить задачу-----------");
        System.out.println("-------7.Сохранить изменения-------");
        System.out.println("--------------8.Выход---------------");
    }

    public static void customEditMenuEvent(Task task)
    {
        System.out.println("------------Меню редактирования----------------");
        System.out.println("--------1.Изменить название задачи------------");
        System.out.println("-----2.Активировать/деактивировать задачу-----");
        System.out.println("-------3.Повторить/не повторять задачу-------");
        if (task.isRepeated()) {
            System.out.println("-4.Редактировать дату начала выполнения задачи-");
            System.out.println("-5.Редактировать дату конца выполнения задачи-");
            System.out.println("-----6.Редактировать интервал выполнения-----");
        }
        else
        {
            System.out.println("----4.Редактировать дату выполнения задачи----");
        }
        System.out.println("------------------0.Выход-------------------");
    }
}
