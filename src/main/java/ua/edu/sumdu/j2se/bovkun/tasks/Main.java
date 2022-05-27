package ua.edu.sumdu.j2se.bovkun.tasks;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class Main {

	public static void main(String[] args) throws IOException {
		App app = new App();
		User user = new User();
		app.run(user);
	}
}