package reporty;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class Student extends User implements IUserFactory {
	String degree;
	int year;
	boolean rectorExcellence;
	ArrayList<String> courses;

// ======================================================================== //
// Student Cons Functions
// ======================================================================== //

	Student() {
		super("Student");
		degree = "";
		rectorExcellence = false;
		courses = new ArrayList<String>();
	}

	Student(String firstName, String lastName, String tz, String email, String password, String degree) {
		super(firstName, lastName, tz, email, password, 100, "Student");
		this.degree = degree;
		rectorExcellence = false;
		year = 1;
		courses = new ArrayList<String>();
		System.out.println(">> UPDATED: new student created - " + firstName + " " + lastName);
	}

	Student(Student x) {
		super(x.firstName, x.lastName, x.tz, x.email, x.password, 100, "Student");
		degree = x.degree;
		rectorExcellence = x.rectorExcellence;
		activation = x.activation;
		year = x.year;
		courses = x.courses;
		System.out.println(">> UPDATED: new student created - " + x.firstName + " " + x.lastName);
	}

// ======================================================================== //
// Student Static Functions
// ======================================================================== //

	static Student createStudent(Student student) {
		Scanner s = new Scanner(System.in);
		System.out.println("\n>> Creating New Student");
		System.out.print(">>> First Name: ");
		student.firstName = s.next();
		System.out.print(">>> Last Name: ");
		student.lastName = s.next();
		System.out.print(">>> T.Z: ");
		student.tz = s.next();
		System.out.print(">>> Email: ");
		student.email = s.next();
		System.out.print(">>> Password: ");
		student.password = s.next();
		System.out.print(">>> Degree: ");
		student.degree = s.next();
		return student;
	}

	static Student createStudent() {
		Scanner s = new Scanner(System.in);
		System.out.println("\n>> Creating New Student");
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
		System.out.print(">>> Degree: ");
		String degree = s.next();

		return new Student(firstName, lastName, tz, email, password, degree);
	}

	static Student findOneById(String id) {
		Scanner s = new Scanner(System.in);
		Gson g = new Gson();

		Student s1 = g.fromJson(Controller.findById(id, "students", "id"), Student.class);

		return s1;
	}

	static Student findOne(String tz) {
		Scanner s = new Scanner(System.in);
		Gson g = new Gson();

		Student s1 = g.fromJson(Controller.findById(tz, "students", "tz"), Student.class);

		if (s1.tz.equals("")) {
			System.out.println(">> ERROR: T.Z isn't founded");
			return null;
		}

		return s1;
	}

	static Student findOne() {
		Scanner s = new Scanner(System.in);
		Gson g = new Gson();

		System.out.print(">> Enter Student T.Z: ");
		String tz = s.next();

		if (tz.equals("0")) {
			System.out.println(">> STOPPED: function stopped successfully");
			return null;
		}

		Student s1 = g.fromJson(Controller.findById(tz, "students", "tz"), Student.class);

		if (s1.tz.equals("")) {
			System.out.println(">> ERROR: T.Z isn't founded");
			return null;
		}

		return s1;
	}

	static void printAllStudents() {

		Gson g = new Gson();
		Student current;
		ArrayList<JsonObject> studentList = Controller.findAll("students");
		if (studentList.size() > 0)
			for (JsonObject student : studentList) {
				current = g.fromJson(student, Student.class);
				current.print();
			}
		else
			System.out.println(">> No students in the system yet");

	}

	static String getIdByTz() {
		Student current = Student.findOne();
		if (current != null)
			return current.getId();
		return null;
	}

// ======================================================================== //
// Student GET/SET Functions
// ======================================================================== //

	// Set functions

	void setYear(int year) {
		this.year = year;
	}

	void setDegree(String degree) {
		this.degree = degree;
	}

	void setRectorExcellence() {
		rectorExcellence = !rectorExcellence;
	}

	Student updateInfo(Scanner s) {
		System.out.println(
				"\n>> What do you want to change?\n1. First Name\n2. Last Name\n3. T.Z\n4. Email\n5. Password\n6. Degree");
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
		case 6:
			System.out.print("\n>>> Insert degree: ");
			this.setDegree(s.next());
			break;

		default:
			break;
		}

		return this;
	}

	// Get functions

	int getYear() {
		return year;
	}

	String getDegree() {
		return degree;
	}

	boolean IsRectorExcellence() {
		return rectorExcellence;
	}

// ======================================================================== //
// Student Functions
// ======================================================================== //

	public User Create() {
		return createStudent(new Student());
	}

	void print() {
		super.print();
		System.out.println(">> Degree: " + degree + "\n>> Year: " + year + "\n>> Rector Excellence: " + rectorExcellence
				+ "\n=======================");
	}

// ======================================================================== //
// Courses
// ======================================================================== //

	void addCourse(String courseID) {
		if (!courses.contains(courseID)) {
			courses.add(courseID);
			System.out.println(">> UPDATED: new course added to " + firstName + " " + lastName);
		} else
			System.out.println(">> ERROR: student is already signed to " + courseID);
	}

	void removeCourse(String courseID) {
		if (courses.contains(courseID)) {
			courses.remove(courseID);
			System.out.println(">> UPDATED: " + courseID + " removed from courses");
		} else
			System.out.println(">> ERROR: the course isn't in your courses list");
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
			removeCourse.removeStudent(this.id);

			// Update student
			Controller.update("update", "students", this, this.id);
			res = true;
		}

		return res;
	}

	// return list of courses student has
	ArrayList<Course> getCourses() {

		ArrayList<JsonObject> coursesJsonObject = Controller.getCoursesByStudentId(this.id);

		ArrayList<Course> courses = new ArrayList<Course>();

		for (JsonObject courseJson : coursesJsonObject) {
			Gson g = new Gson();

			Course course = g.fromJson(courseJson, Course.class);
			courses.add(course);
		}

		return courses;
	}

	void printAllCourses() {
		ArrayList<Course> arr = getCourses();
		if (arr.size() != 0)
			arr.forEach((course) -> {
				System.out.println("==========================\n>> Course Name: " + course.getName()
						+ "\n>> Course Number: " + course.getNum() + "\n==========================");
			});
		else
			System.out.println(">> Student courses is empty");
	}

	int getNumberOfCourses() {
		return courses.size();
	}

// ======================================================================== //
// Grades
// ======================================================================== //

	ArrayList<Grade> getGradesByCourseId(String courseId) {
		ArrayList<Grade> grades = new ArrayList<Grade>();

		ArrayList<JsonObject> gradesJsonObjectList = Controller.getGradesByCourseAndStudentId(courseId, this.id);

		for (JsonObject gradeJson : gradesJsonObjectList) {
			Gson g = new Gson();

			Grade grade = g.fromJson(gradeJson, Grade.class);
			grades.add(grade);
		}

		return grades;
	}

	void printAllGrades() {
		String id = Course.findOne().id;
		double sum = 0;

		ArrayList<Grade> gradesInCourse = getGradesByCourseId(id);

		if (gradesInCourse.size() > 0) {
			System.out.println("\n=======================");
			for (Grade g : gradesInCourse) {
				// Print grades
				sum += g.getZion() * (g.getPercentage()/100);
				System.out.println(g.getTitle() + ": " + g.getZion() + ", " + g.getPercentage() +"%");
			}
			System.out.println("-----------------------");
			System.out.println("Final Grade - " + sum);
			System.out.println("=======================");
		}else
		System.out.println(">> There are no grades");
	}

	void printCourseGrades(String courseID) {
		if (!courses.contains(courseID)) {
			System.out.println(">> ERROR: student isn't signed to this course");
		} else {

		}
	}
}
