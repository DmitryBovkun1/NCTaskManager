package ua.edu.sumdu.j2se.bovkun.tasks.functional;

import ua.edu.sumdu.j2se.bovkun.tasks.basic.AbstractTaskList;
import ua.edu.sumdu.j2se.bovkun.tasks.basic.Task;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Edit {
    public static boolean edit(AbstractTaskList abstractTaskList, Task task) {
        for (Task tempTask : abstractTaskList) {
            if (task == tempTask) {
                System.out.println("\nРедактируемая задача: \n" + task);
                View.customEditMenuEvent(task);
                Scanner in = new Scanner(System.in);
                String chooseEdit;
                System.out.println("Выбирите желаемое действие:");
                chooseEdit = in.nextLine();
                if (tempTask.isRepeated()) {
                    switch (chooseEdit) {
                        case "1":
                            System.out.println("Введите новое название задачи");
                            String title = in.nextLine();
                            tempTask.setTitle(title);
                            break;
                        case "2":
                            System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                            int temp = in.nextInt();
                            while (!DataFormatter.checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean active = temp == 1;
                            tempTask.setActive(active);
                            break;
                        case "3":
                            System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                            temp = in.nextInt();
                            while (!DataFormatter.checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean repeated = temp == 1;

                            if(!repeated) {
                                tempTask.setTime(tempTask.getStartTime());
                            }
                            tempTask.setRepeated(repeated);
                            break;
                        case "4":
                            System.out.println("Дата начала выполнения задачи:");
                            LocalDateTime startTime = LocalDateTime.parse(DataFormatter.getTimeEvent());
                            tempTask.setTime(startTime, tempTask.getEndTime(), tempTask.getRepeatInterval());
                            break;
                        case "5":
                            System.out.println("Дата конца выполнения задачи:");
                            LocalDateTime finishTime = LocalDateTime.parse(DataFormatter.getTimeEvent());
                            tempTask.setTime(tempTask.getStartTime(), finishTime, tempTask.getRepeatInterval());
                            break;
                        case "6":
                            System.out.println("Интервал выполнения задачи в данный период (в секундах)");
                            int interval = in.nextInt();
                            tempTask.setTime(tempTask.getStartTime(), tempTask.getEndTime(), interval);
                            break;
                        case "0":
                            return false;
                        default:
                            System.out.println("Действе под номером " + chooseEdit + " не обнаружено! Попробуйте ввести что-то другое!");
                    }
                } else {
                    switch (chooseEdit) {
                        case "1":
                            System.out.println("Введите новое название задачи");
                            String title = in.nextLine();
                            tempTask.setTitle(title);
                            break;
                        case "2":
                            System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                            int temp = in.nextInt();
                            while (!DataFormatter.checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Активировать задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean active = temp == 1;
                            tempTask.setActive(active);
                            break;
                        case "3":
                            System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                            temp = in.nextInt();
                            while (!DataFormatter.checkIntValue(temp, 0, 1))
                            {
                                System.out.println("Введено неверное значение! Повторите ещё раз! ");
                                System.out.println("Повторить задачу (1 - ДА : 0 - НЕТ)");
                                temp = in.nextInt();
                            }
                            boolean repeated = temp == 1;
                            if(repeated) {
                                tempTask.setTime(tempTask.getTime(), tempTask.getTime().plusSeconds(1), 1);
                            }
                            tempTask.setRepeated(repeated);
                            break;
                        case "4":
                            System.out.println("Дата выполнения задачи:");
                            LocalDateTime time = LocalDateTime.parse(DataFormatter.getTimeEvent());
                            tempTask.setTime(time);
                            break;
                        case "0":
                            return false;
                        default:
                            System.out.println("Действе под номером " + chooseEdit + " не обнаружено! Попробуйте ввести что-то другое!");
                    }
                }
            }
        }
        return true;
    }
}
