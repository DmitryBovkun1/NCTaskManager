package ua.edu.sumdu.j2se.bovkun.tasks;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello");
		ArrayTaskList t = new ArrayTaskList();
		t.add(new Task("ABC", 5));
		t.add(new Task("ABC", 5));
		t.add(new Task("ABC", 5));
		t.add(new Task("ABC", 5));
		t.add(new Task("ABC", 5));
		System.out.println(t);
	}
}