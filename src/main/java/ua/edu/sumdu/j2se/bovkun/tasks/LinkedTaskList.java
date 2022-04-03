package ua.edu.sumdu.j2se.bovkun.tasks;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;
//import java.util.List;

public class LinkedTaskList extends AbstractTaskList{
    int size = 0;
    Node<Task> first;
    Node<Task> last;

    private static class Node<Task> {
        Task item;
        Node<Task> next;
        Node<Task> prev;

        Node(Node<Task> prev, Task element, Node<Task> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    public LinkedTaskList() {}

    public void add(Task task) {
        if(task != null) {
            Node<Task> last = this.last;
            Node<Task> newNode = new Node<>(last, task, null);
            this.last = newNode;
            if (last == null)
                first = newNode;
            else
                last.next = newNode;
            size++;
        }
    }

    public boolean remove(Task task) {
        for (Node<Task> x = first; x != null; x = x.next) {
            if (task == x.item) {

                Task element = x.item;
                Node<Task> next = x.next;
                Node<Task> prev = x.prev;
                if (prev == null) {
                    first = next;
                } else {
                    prev.next = next;
                    x.prev = null;
                }

                if (next == null) {
                    last = prev;
                } else {
                    next.prev = prev;
                    x.next = null;
                }

                x.item = null;
                size--;
                return true;
            }
        }
        return false;
    }

    public Iterator<Task> iterator()
    {
        return new Iterator()
        {
            private int index = -1;

            public boolean hasNext() {
                return (index + 1 < size());
            }

            public Task next() {
                return getTask(++index);
            }

            public void remove() {
                if (index < 0) throw new IllegalStateException("Итератор на нулевом элементе!");
                LinkedTaskList.this.remove(getTask(index));
                --index;
            }
        };
    }

    public int size()
    {
        return size;
    }

    public Task getTask(int index) {
        if(index >= 0 && index <= size) {
            int i = 0;
            Task task = null;
            for (Node<Task> x = first; x != null; x = x.next) {
                if (i == index) {
                    task = x.item;
                }
                i++;
            }
            return task;
        }
        else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public LinkedTaskList clone() {
        return (LinkedTaskList) super.clone();
    }
    @Override
    public int hashCode() { return super.hashCode(); }

    @Override
    public Stream<Task> getStream()
    {
        Task[] tasks = new Task[this.size()];
        for(int i = 0; i < this.size(); i++)
        {
            tasks[i]=this.getTask(i);
        }
        return Arrays.stream(tasks).filter(Objects::nonNull);
    }
}