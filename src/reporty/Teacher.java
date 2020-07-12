package reporty;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class Teacher extends User implements IUserFactory {
	ArrayList<String> courses;

// ======================================================================== //
// Teacher Cons Functions
// ======================================================================== //

	Teacher() {
		super("Teacher");
		courses = new ArrayList<String>();
	}

	Teacher(String firstName, String lastName, String tz, String email, String password) {
		super(firstName, lastName, tz, email, password, 200, "Teacher");
		courses = new ArrayList<String>();
		System.out.println(">> UPDATED: new teacher created - " + firstName + " " + lastName);
	}

	Teacher(Teacher x) {
		super(x.firstName, x.lastName, x.tz, x.email, x.password, 200, "Teacher");
		courses = x.courses;
		System.out.println(">> UPDATED: new teacher created - " + x.firstName + " " + x.lastName);
	}

// ======================================================================== //
// Teacher Static Functions
// ======================================================================== //

	static Teacher createTeacher(Teacher teacher) {
		Scanner s = new Scanner(System.in);
		System.out.println("\n>> Creating New Teacher");
		System.out.print(">>> First Name: ");
		String firstName = s.next();
		System.out.print(">>> Last Name: ");
		String lastName = s.next();
		System.out.print(">>> T.Z: ");
		String tz = s.next();
		System.out.print(">>> Email: ");
		String email = s.next();
		System.out.print(">>> Password: ");
		String password = s.next();

		teacher.firstName = firstName;
		teacher.lastName = lastName;
		teacher.tz = tz;
		teacher.email = email;
		teacher.password = password;
		return teacher;
	}

	static Teacher createTeacher() {
		Scanner s = new Scanner(System.in);
		System.out.println("\n>> Creating New Teacher");
		System.out.print(">>> First Name: ");
		String firstName = s.next();
		System.out.print(">>> Last Name: ");
		String lastName = s.next();
		System.out.print(">>> T.Z: ");
		String tz = s.next();
		System.out.print(">>> Email: ");
		String email = s.next();
		System.out.print(">>> Password: ");
		String password = s.next();

		return new Teacher(firstName, lastName, tz, email, password);
	}

	static Teacher findOne() {
		Scanner s = new Scanner(System.in);
		Gson g = new Gson();

		System.out.print(">> Enter Teacher T.Z: ");
		String tz = s.next();

		if (tz.equals("0")) {
			System.out.println(">> STOPPED: function stopped successfully");
			return null;
		}

		Teacher t = g.fromJson(Controller.findById(tz, "teachers", "tz"), Teacher.class);

		if (t.tz.equals("")) {
			System.out.println(">> ERROR: T.Z isn't founded");
			return null;
		}

		return t;
	}

	static void printAllTeachers() {

		Gson g = new Gson();
		Teacher current;
		ArrayList<JsonObject> teacherList = Controller.findAll("teachers");
		if (teacherList.size() > 0)
			for (JsonObject teacher : teacherList) {
				current = g.fromJson(teacher, Teacher.class);
				current.print();
			}
		else
			System.out.println(">> No teachers in the system yet");

	}

	static String getIdByTz() {
		Teacher current = Teacher.findOne();
		if (current != null)
			return current.getId();
		return null;
	}

// ======================================================================== //
// Teacher GET/SET Functions
// ======================================================================== //

	int getNumberOfCourses() {
		return courses.size();
	}

// ======================================================================== //
// Teacher Functions
// ======================================================================== //

	public User Create() {
		return createTeacher(new Teacher());
	}

	Teacher updateInfo(Scanner s) {

		System.out.println(
				"\n>> What do you want to change?\n1. First Name\n2. Last Name\n3. T.Z\n4. Email\n5. Password");
		System.out.print("\n Select an option: ");

		switch (s.nextInt()) {
		case 0:
			System.out.println(">>> Action canceled");
			break;
		case 1:
			System.out.print("\n>>> Insert first name: ");
			this.setFirstName(s.next());
			break;
		case 2:
			System.out.print("\n>>> Insert last name: ");
			this.setLastName(s.next());
			break;
		case 3:
			System.out.print("\n>>> Insert T.Z: ");
			this.setTz(s.next());
			break;
		case 4:
			System.out.print("\n>>> Insert email: ");
			this.setEmail(s.next());
			break;
		case 5:
			System.out.print("\n>>> Insert password: ");
			this.setPassword(s.next());
			break;

		default:
			break;

		}

		return this;
	}

	void print() {
		super.print();
		System.out.println("=======================");
	}

// ======================================================================== //
// Courses
// ======================================================================== //

	void addCourse(String id) {
		courses.add(id);
	}

	void removeCourse(String id) {
		courses.remove(id);
	}

	boolean removeFromCourse() {

		boolean res = false;
		ArrayList<Course> myCourses = getCourses();

		String courseId = Course.findOne().id;

		Course removeCourse = null;

		for (Course c : myCourses) {
			// Found the course in courses list
			if (c.id.equals(courseId)) {
				// Save for remove
				removeCourse = c;
				break;
			}
		}

		if (removeCourse != null) {
			this.removeCourse(removeCourse.id);
			removeCourse.removeTeacher(this.id);

			// Update student
			Controller.update("update", "teachers", this, this.id);
			res = true;
		}

		return res;
	}

	ArrayList<Course> getCourses() {

		ArrayList<Course> res = new ArrayList<Course>();
		Gson g = new Gson();

		// Get courses
		ArrayList<JsonObject> courses = Controller.findAll("courses");

		// Search all teacher courses
		for (JsonObject obj : courses) {

			// Get the techersIDs of the course object
			JsonArray teacherIds = (JsonArray) obj.get("teachersIDs");

			if (teacherIds != null) {
				for (JsonElement id : teacherIds) {

					String idTxt = "".concat(id.getAsString());

					if (idTxt.equals(this.id)) {
						res.add(g.fromJson(obj, Course.class));
					}
				}
			}
		}

		return res;
	}

	void printCourses() {
		JsonObject json = null;
		Course current = null;
		Gson g = new Gson();

		if (courses.size() > 0) {
			System.out.println("=======================");
			for (int i = 0; i < courses.size(); i++) {
				json = Controller.findById(courses.get(i), "courses", "id");
				current = g.fromJson(json, Course.class);
				System.out.println((i + 1) + ". " + current.getName() + ", " + current.getNum());

			}
			System.out.println("=======================");
		} else {
			System.out.println(">> Courses list is empty");
		}
	}

// ======================================================================== //
// Students
// ======================================================================== //

	ArrayList<Student> getStudents() {
		ArrayList<Student> res = new ArrayList<Student>();
		ArrayList<Course> courses = getCourses();

		for (Course course : courses) {

			// The teacher is one of the course teachers
			for (Teacher t : course.getTeachers()) {

				if (t.id != null && t.id.equals(this.id)) {
					res.addAll(course.getStudents());
				}
			}
		}
		return res;
	}
}