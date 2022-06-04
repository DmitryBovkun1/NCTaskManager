package ua.edu.sumdu.j2se.bovkun.tasks;

import ua.edu.sumdu.j2se.bovkun.tasks.functional.App;
import ua.edu.sumdu.j2se.bovkun.tasks.functional.User;

public class Main {

	public static void main(String[] args) {
		App app = new App();
		User user = new User();
		app.run(user);
	}
}