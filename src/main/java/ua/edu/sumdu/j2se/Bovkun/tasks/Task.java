package ua.edu.sumdu.j2se.Bovkun.tasks;

public class Task {
    private String title;
    private int time;
    private int start;
    private int end;
    private int interval;
    private boolean active=false;
    private boolean repeated;

    public Task(String title, int time)
    {
        repeated=false;
        setTitle(title);
        setTime(time);
    }
    public Task(String title, int start, int end, int interval)
    {
        repeated=true;
        setTitle(title);
        setTime(start, end, interval);
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
    public int getTime()
    {
        if(!isRepeated()) {
            return time;
        }
        else
        {
            return getStartTime();
        }
    }
    public void setTime(int time)
    {
        this.time = time;
        if(isRepeated()) {
            repeated=false;
        }
    }
    public int getStartTime()
    {
        if(!isRepeated()) {
            return getTime();
        }
        else {
            return start;
        }
    }
    public int getEndTime()
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
    public void setTime(int start, int end, int interval)
    {
        this.start=start;
        this.end=end;
        this.interval=interval;
        if(!isRepeated())
        {
            repeated=true;
        }
    }
    public boolean isRepeated()
    {
        return repeated;
    }
    public int nextTimeAfter(int current)
    {
        if (active)
        {
            if (time > current && time != 0)
            {
                return time;
            }

            if(start > current && start != 0)
            {
                return start;
            }
            else if(start != 0)
            {
                int tempTime = start;

                do {
                    if(tempTime + interval <= end)
                    {
                        tempTime += interval;
                    }
                    else {
                        return -1;
                    }
                } while(current >= tempTime);
                return tempTime;
            }
        }
        return -1;
    }
}