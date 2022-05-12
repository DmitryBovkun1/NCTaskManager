package ua.edu.sumdu.j2se.bovkun.tasks;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.time.*;

public class TaskIO {
    public static void write(AbstractTaskList tasks, OutputStream out) throws IOException
    {
        DataOutputStream stream = new DataOutputStream(out);
        stream.writeInt(tasks.size());
        for(int i = 0; i < tasks.size(); i++)
        {
            stream.writeInt(tasks.getTask(i).getTitle().length());
            stream.writeUTF(tasks.getTask(i).getTitle());
            stream.writeBoolean(tasks.getTask(i).isActive());
            stream.writeBoolean(tasks.getTask(i).isRepeated());

            ZonedDateTime tempTime;

            if(tasks.getTask(i).isRepeated())
            {
                stream.writeInt(tasks.getTask(i).getRepeatInterval());
                tempTime = ZonedDateTime.of(tasks.getTask(i).getStartTime(), ZoneId.systemDefault());
                stream.writeLong(tempTime.toInstant().toEpochMilli());
                tempTime = ZonedDateTime.of(tasks.getTask(i).getEndTime(), ZoneId.systemDefault());
                stream.writeLong(tempTime.toInstant().toEpochMilli());
            }
            else
            {
                tempTime = ZonedDateTime.of(tasks.getTask(i).getTime(), ZoneId.systemDefault());
                stream.writeLong(tempTime.toInstant().toEpochMilli());
            }
        }
    }
    public static void read(AbstractTaskList tasks, InputStream in) throws IOException
    {
        DataInputStream stream = new DataInputStream(in);
        int size = stream.readInt();
        for(int i = 0; i < size; i++)
        {
            stream.readInt();
            String title = stream.readUTF();
            boolean active = stream.readBoolean();
            boolean repeated = stream.readBoolean();
            Task temp;
            if(repeated)
            {
                int interval = stream.readInt();
                long start = stream.readLong();
                long end = stream.readLong();
                temp = new Task(title, LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault()), LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault()), interval, active, true);
            }
            else
            {
                long time = stream.readLong();
                temp = new Task(title, LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()), active, false);
            }
            tasks.add(temp);
        }
    }
    public static void writeBinary(AbstractTaskList tasks, File file) throws IOException
    {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        TaskIO.write(tasks, fileOutputStream);
    }
    public static void readBinary(AbstractTaskList tasks, File file) throws IOException
    {
        FileInputStream fileInputStream = new FileInputStream(file);
        TaskIO.read(tasks, fileInputStream);
    }
    public static void write(AbstractTaskList tasks, Writer out) throws IOException
    {
        GsonBuilder gson = new GsonBuilder();
        out.write(gson.setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli()
                );
            }
        }).create().toJson(tasks));
        out.flush();
    }
    public static void read(AbstractTaskList tasks, Reader in) throws IOException
    {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonElement.getAsLong()), ZoneId.systemDefault());
            }
        });
        for(Task task : gson.create().fromJson(in, tasks.getClass()))
        {
            tasks.add(task);
        }
    }
    public static void writeText(AbstractTaskList tasks, File file) throws IOException
    {
        FileWriter fileWriter = new FileWriter(file);
        TaskIO.write(tasks, fileWriter);
    }
    public static void readText(AbstractTaskList tasks, File file) throws IOException
    {
        FileReader fileReader = new FileReader(file);
        TaskIO.read(tasks, fileReader);
    }
}
