package reporty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.*;

public class Reporty {

	private HashMap<String, IReportyMenu> menus;
	private JFrame frame;
	private Scanner s;

	Reporty() {

		menus = new HashMap<String, IReportyMenu>();
		menus.put("4", new GradeManagementMenu());
		menus.put("3", new CourseManagementMenu());
		menus.put("2", new TeachersMangementMenu());
		menus.put("1", new StudentManagementMenu());

		s = new Scanner(System.in);
		System.out.println("Enter 1 to log in or 2 to sign up");

		int num = s.nextInt();

		switch (num) {
		case 1: {
			login();
			break;
		}
		case 2: {
			signUp();
			break;
		}
		case 3: {
			mainMenu();
			break;
		}
		}
	}

	public void dispose() {
		this.s.close();
	}

	void frameInitParams() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(1000, 150);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}

	void signUp() {
		this.frame = new JFrame("Sign Up");

		frameInitParams();
		JTextArea firstName, lastName, email, password;
		JLabel firstNameLabel, lastNameLabel, emailLabel, passwordLabel;

		JButton btn = new JButton("Sign Up");

		firstName = new JTextArea(2, 20);
		lastName = new JTextArea(2, 20);
		email = new JTextArea(2, 20);
		password = new JTextArea(2, 20);

		firstNameLabel = new JLabel("First Name");
		lastNameLabel = new JLabel("Last Name");
		emailLabel = new JLabel("Email");
		passwordLabel = new JLabel("Password");

		btn.setBounds(10, 10, 10, 10);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String firstNameTxt = firstName.getText().toString();
				String lastNameTxt = lastName.getText().toString();
				String emailTxt = email.getText().toString();
				String passwordTxt = password.getText().toString();

				if (firstNameTxt.length() > 0 && lastNameTxt.length() > 0 && emailTxt.length() > 0
						&& passwordTxt.length() > 0) {

					Student s = new Student(firstNameTxt, lastNameTxt, "", emailTxt, passwordTxt, "");
					Controller.update("insert", "students", s, null);
					JOptionPane.showMessageDialog(null, "Welcome " + s.firstName, "Success sign up",
							JOptionPane.PLAIN_MESSAGE);
					frame.dispose();
					mainMenu();
				}
			}
		});

		JPanel firstNamePanel, lastNamePanel, emailPanel, passwordPanel, buttonPanel;

		firstNamePanel = new JPanel();
		firstNamePanel.setSize(200, 50);

		lastNamePanel = new JPanel();
		lastNamePanel.setSize(200, 50);

		emailPanel = new JPanel();
		emailPanel.setSize(200, 50);

		passwordPanel = new JPanel();
		passwordPanel.setSize(200, 50);

		buttonPanel = new JPanel();
		buttonPanel.setSize(200, 50);

		firstNamePanel.add(firstNameLabel);
		firstNamePanel.add(firstName);

		lastNamePanel.add(lastNameLabel);
		lastNamePanel.add(lastName);

		emailPanel.add(emailLabel);
		emailPanel.add(email);

		passwordPanel.add(passwordLabel);
		passwordPanel.add(password);

		buttonPanel.add(btn);

		JPanel container = new JPanel();

		container.add(firstNamePanel);
		container.add(lastNamePanel);
		container.add(emailPanel);
		container.add(passwordPanel);
		container.add(buttonPanel);

		this.frame.getContentPane().add(container);

	}

	void login() {
		this.frame = new JFrame("Log in");

		frameInitParams();
		JTextArea email, password;
		JLabel emailLabel, passwordLabel;

		JButton btn = new JButton("Log in");

		email = new JTextArea(2, 20);
		password = new JTextArea(2, 20);

		emailLabel = new JLabel("Email");
		passwordLabel = new JLabel("Password");

		btn.setBounds(10, 10, 10, 10);

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String pass = password.getText().toString();
				String emailTxt = email.getText().toString();
				Database drive = Database.getInstance();

				JsonArray students = (JsonArray) drive.db.get("students");
				JsonArray teachers = (JsonArray) drive.db.get("teachers");

				boolean found = false;
				if (students != null && teachers != null) {

					Gson g = new Gson();

					for (JsonElement student : students) {

						JsonObject studentJson = student.getAsJsonObject();
						User u = g.fromJson(studentJson, User.class);
						String password = u.password;
						String email = u.email;

						if (password.equals(pass) && emailTxt.equals(email)) {

							found = true;
							break;
						}
					}

					if (found != true) {
						for (JsonElement teacher : teachers) {

							JsonObject teacherJson = teacher.getAsJsonObject();
							User u = g.fromJson(teacherJson, User.class);
							String password = u.password;
							String email = u.email;

							if (password.equals(pass) && emailTxt.equals(email)) {

								found = true;
								break;
							}
						}
					}
				}

				if (found == false) {
					JOptionPane.showMessageDialog(null, "Please check Email or Password and try again",
							"Logging in failed", JOptionPane.PLAIN_MESSAGE);
					System.exit(0);
				} else {
					// user logged in success, and can continue to the CLI
					frame.dispose();
					JOptionPane.showMessageDialog(null, "Welcome to Reporty", "Success", JOptionPane.PLAIN_MESSAGE);
					mainMenu();
				}

			}
		});

		JPanel emailPanel, passwordPanel, buttonPanel;

		emailPanel = new JPanel();
		emailPanel.setSize(200, 50);

		passwordPanel = new JPanel();
		passwordPanel.setSize(200, 50);

		buttonPanel = new JPanel();
		buttonPanel.setSize(200, 50);

		emailPanel.add(emailLabel);
		emailPanel.add(email);

		passwordPanel.add(passwordLabel);
		passwordPanel.add(password);

		buttonPanel.add(btn);

		JPanel container = new JPanel();

		container.add(emailPanel);
		container.add(passwordPanel);
		container.add(buttonPanel);

		this.frame.getContentPane().add(container);
	}

	void mainMenu() {

		boolean menuContinue = true;

		while (menuContinue == true) {

			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (InterruptedException ex) {
			} catch (IOException ex) {
			}
			System.out.println(
					"\n>> Reporty System - Main Menu:\n-------------------------------\nEnter 1 for Students Management\nEnter 2 for Teachers Management\nEnter 3 for Courses Management\nEnter 4 for Grades Management\nEnter 0 for Exit");
			System.out.print("\n>> Select an option: ");

			switch (s.next()) {

			case "0":
				System.out.println(">> Exit Reporty System");
				menuContinue = false;
				System.exit(0);
				break;

			case "1":
				menus.get("1").run();
				break;

			case "2":
				menus.get("2").run();
				break;

			case "3":
				menus.get("3").run();
				break;

			case "4":
				menus.get("4").run();
				break;

			default:
				break;
			}
		}

		// close all
		dispose();
	}
}
