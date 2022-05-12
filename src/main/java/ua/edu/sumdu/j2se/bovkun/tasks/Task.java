package ua.edu.sumdu.j2se.bovkun.tasks;

import java.util.Objects;
import java.time.LocalDateTime;

public class Task {
    private String title;
    private LocalDateTime time;
    private LocalDateTime start;
    private LocalDateTime end;
    private int interval;
    private boolean active=false;
    private boolean repeated;

    public Task(){}

    public Task(String title)
    {
        repeated = false;
        setTitle(title);
        setTime(LocalDateTime.now());
    }

    public Task(String title, LocalDateTime time)
    {
        if(time != null) {
            repeated = false;
            setTitle(title);
            setTime(time);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
    public Task(String title, LocalDateTime start, LocalDateTime end, int interval)
    {
        if(start != null && end != null && interval > 0) {
            repeated = true;
            setTitle(title);
            setTime(start, end, interval);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
    public Task(String title, LocalDateTime time, LocalDateTime start, LocalDateTime end, int interval, boolean active, boolean repeat) {
        this.title = title;
        this.active = active;
        this.repeated = repeat;
        this.time = time;
        this.start = start;
        this.end = end;
        this.interval = interval;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title=title;
    }
    public boolean isActive()
    {
        return active;
    }
    public void setActive(boolean active)
    {
        this.active=active;
    }
    public LocalDateTime getTime()
    {
        if(!isRepeated()) {
            return time;
        }
        else
        {
            return getStartTime();
        }
    }
    public void setTime(LocalDateTime time)
    {
        if(time != null) {
            this.time = time;
            if (isRepeated()) {
                repeated = false;
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    public LocalDateTime getStartTime()
    {
        if(!isRepeated()) {
            return getTime();
        }
        else {
            return start;
        }
    }
    public LocalDateTime getEndTime()
    {
        if(!isRepeated()) {
            return getTime();
        }
        else {
            return end;
        }
    }
    public int getRepeatInterval()
    {
        if(isRepeated()) {
            return interval;
        }
        else
        {
            return 0;
        }
    }
    public void setTime(LocalDateTime start, LocalDateTime end, int interval)
    {
        if(start != null && end != null && interval > 0) {
            this.start = start;
            this.end = end;
            this.interval = interval;
            if (!isRepeated()) {
                repeated = true;
            }
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
    public boolean isRepeated()
    {
        return repeated;
    }
    public LocalDateTime nextTimeAfter(LocalDateTime current)
    {
        if (active)
        {
            if (time != null && 0 > current.compareTo(time))
            {
                return time;
            }

            if(start != null && 0 > current.compareTo(start))
            {
                return start;
            }
            else if(start != null)
            {
                LocalDateTime tempTime = start;

                do {
                    if(tempTime.plusSeconds(interval).compareTo(end) <= 0)
                    {
                        tempTime = tempTime.plusSeconds(interval);
                    }
                    else {
                        return null;
                    }
                } while(current.compareTo(tempTime) >= 0);
                return tempTime;
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        String temp = "Название - " + this.title + ", Активна - ";
        if(this.active)
        {
            temp += "Да";
        }
        else
        {
            temp += "Нет";
        }
        temp += ", Есть повтор - ";
        if(this.repeated)
        {
            temp += "Да";
            temp += ", Начало - " + this.start + ", Конец - " + this.end + ", Интервал - " + this.interval + "\n";
        }
        else
        {
            temp += "Нет";
            temp += ", Время - " + this.time + "\n";
        }
        return temp;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.title, this.active, this.repeated, this.time, this.start, this.end, this.interval);
    }

    @Override
    public Task clone()
    {
        //return new Task(this.title, this.time, this.start, this.end, this.interval, this.active, this.repeated);
        Task temp = new Task();
        temp.title = this.title;
        temp.active = this.active;
        temp.repeated = this.repeated;
        temp.time = this.time;
        temp.start = this.start;
        temp.end = this.end;
        temp.interval = this.interval;
        return temp;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
        {
            return true;
        }
        if(obj instanceof Task)
        {
            return true;
        }
        Task convert = (Task) obj;
        if(this.repeated != convert.repeated || this.active != convert.active || this.time != convert.time
                || !Objects.equals(this.title, convert.title) || this.interval != convert.interval
                || this.start != convert.start || this.end != convert.end)
        {
            return false;
        }
        return true;
    }
}