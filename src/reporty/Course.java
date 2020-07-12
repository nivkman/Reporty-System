package reporty;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class Course {

	final String id;
	String name;
	int num, studentsAmount;
	boolean status;
	private ArrayList<String> studentsIDs;
	private ArrayList<String> teachersIDs;

// ======================================================================== //
// Course Cons Functions
// ======================================================================== //

	Course() {
		this.studentsIDs = new ArrayList<String>();
		this.teachersIDs = new ArrayList<String>();
		this.id = Utils.idGenerator();
	}

	Course(String name, int num, int studentsAmount) {
		this();
		this.name = name;
		this.num = num;
		this.studentsAmount = studentsAmount;
		this.status = true;
		System.out.println(">> New course created - " + name);
	}

	Course(Course x) {
		this.name = x.name;
		this.num = x.num;
		this.studentsAmount = x.studentsAmount;
		this.status = x.status;
		this.id = x.id;
		this.studentsIDs = x.studentsIDs;
		this.teachersIDs = x.teachersIDs;
		System.out.println(">> New course created - " + name);
	}

// ======================================================================== //
// Course Static Functions
// ======================================================================== //

	static Course createCourse() {
		Scanner s = new Scanner(System.in);
		System.out.println("\n>> Creating New Course");
		System.out.print(">>> Course Name: ");
		String name = s.next();
		System.out.print(">>> Course Number: ");
		int num = s.nextInt();
		System.out.print(">>> Course Student Amount: ");
		int studentsAmount = s.nextInt();

		return new Course(name, num, studentsAmount);
	}

	static Course findOne(String id) {
		Scanner s = new Scanner(System.in);
		Gson g = new Gson();

		Course c = g.fromJson(Controller.findById(id, "courses", "id"), Course.class);

		if (c.num == 0) {
			System.out.println(">> ERROR: Course number isn't founded");
			return null;
		}

		return c;
	}

	static Course findOne(int num) {
		Scanner s = new Scanner(System.in);
		Gson g = new Gson();

		Course c = g.fromJson(Controller.findById(num, "courses"), Course.class);

		if (c.num == 0) {
			System.out.println(">> ERROR: Course number isn't founded");
			return null;
		}

		return c;
	}

	static Course findOne() {
		Scanner s = new Scanner(System.in);
		Gson g = new Gson();

		System.out.print(">> Enter Course Number: ");
		int num = s.nextInt();

		Course c = g.fromJson(Controller.findById(num, "courses"), Course.class);

		if (c.num == 0) {
			System.out.println(">> ERROR: Course number isn't founded");
			return null;
		}

		return c;
	}

	static boolean printAll() {

		Gson g = new Gson();
		Course current;

		ArrayList<JsonObject> coursesList = Controller.findAll("courses");

		if (coursesList.size() == 0)
			return false;

		for (JsonObject course : coursesList) {
			current = g.fromJson(course, Course.class);
			current.print();
		}
		return true;

	}

// ======================================================================== //
// Course GET/SET Functions
// ======================================================================== //

	// Set functions

	void setNum(int num) {
		this.num = num;
		System.out.println(">> UPDATED: course nummber");
	}

	void setName(String name) {
		this.name = name;
		System.out.println(">> UPDATED: course name");
	}

	void setStatus(boolean status) {
		this.status = status;
		System.out.println(">> UPDATED: course activation status");
	}

	// Get functions

	String getId() {
		return id;
	}

	String getName() {
		return name;
	}

	boolean getStatus() {
		return status;
	}

	int getNum() {
		return num;
	}

	ArrayList<String> getStudentsIDs() {
		return studentsIDs;
	}

	ArrayList<String> getTeachersIDs() {
		return teachersIDs;
	}

// ======================================================================== //
// Course Functions
// ======================================================================== //

	Course updateInfo(Scanner s) {

		System.out.println(
				"\n>> What do you want to change?\n1. Course Name\n2. Course Number\n3. Course Status\n0. Cencel");
		System.out.print("\n>> Select an option: ");

		switch (s.nextInt()) {
		case 0:
			System.out.println(">>> Action canceled");
			break;

		case 1:
			System.out.print("\n>>> Insert course name: ");
			this.setName(s.next());
			break;

		case 2:
			System.out.print("\n>>> Insert course number: ");
			this.setNum(s.nextInt());
			break;

		case 3:
			System.out.print("\n>>> Change Course Activation: ");
			this.setStatus(s.nextBoolean());
			break;

		default:
			System.out.println(">>> Wrong action selection");
			break;
		}

		return this;
	}

	void print() {
		System.out.println("\n==========================\n>> Course Name: " + name + "\n>> Course Number: " + num
				+ "\n>> Status: " + (status ? "Active" : "Disactive") + "\n>> Course Max Students Number:"
				+ studentsAmount + "\n>> ID: " + id + "\n==========================\n");
	}

// ======================================================================== //
// Students
// ======================================================================== //

	void addStudentsList() {
		System.out.println(">> Adding Students List to " + name + " Course (insert 0 to stop)");
		for (int i = 0; i < studentsAmount; i++)
			if (!addStudent())
				break;
	}

	boolean addStudent() {
		String id = Student.getIdByTz();
		Gson g = new Gson();
		if (id == null)
			return false;

		if (!studentsIDs.contains(id)) {
			studentsIDs.add(id);
			JsonObject x = Controller.findById(id, "students", "id");
			Student s = g.fromJson(x, Student.class);
			s.addCourse(this.id);
			Controller.update("update", "students", s, s.id);
			System.out.println(">> UPDATED: new student added to course");
		} else
			System.out.println(">> ERROR: student signed in already");

		return true;
	}

	void removeStudent(String x) {
		if (studentsIDs.contains(x)) {
			studentsIDs.remove(x);
			Controller.update("update", "courses", this, this.id);
			System.out.println(">> UPDATED: " + x + " removed from " + name + " course");
		} else
			System.out.println(">> ERROR: student isn't signed in to " + name);
	}

	// Returns all students of the course
	ArrayList<Student> getStudents() {

		ArrayList<Student> res = new ArrayList<Student>();
		Gson g = new Gson();

		ArrayList<JsonObject> students = Controller.findAll("students");

		if (students != null) {
			for (JsonObject obj : students) {

				Student s = g.fromJson(obj, Student.class);
				if (s != null && s.courses.contains(this.id))
					res.add(s);
			}
		}

		return res;
	}

	void printStudents() {
		ArrayList<Student> students = getStudents();
		int i = 1;
		if (students.size() > 0) {
			System.out.println("\n>> Printing students of course " + name + "\n=======================");
			for (Student student : students) {
				System.out.println(
						i + ". " + student.getFirstName() + " " + student.getLastName() + ", " + student.getTz());
				i++;
			}
			System.out.println("=======================");
		} else
			System.out.println(">> No student signed to this course");
	}

// ======================================================================== //
// Teachers
// ======================================================================== //

	void addTeachersList() {
		System.out.println(">> Adding Teachers List to " + name + " Course (insert 0 to stop)");
		while (addTeacher())
			;
	}

	boolean addTeacher() {
		String id = Teacher.getIdByTz();
		Gson g = new Gson();
		if (id == null)
			return false;

		if (!teachersIDs.contains(id)) {
			teachersIDs.add(id);
			JsonObject x = Controller.findById(id, "teachers", "id");
			Teacher t = g.fromJson(x, Teacher.class);
			t.addCourse(this.id);
			Controller.update("update", "teachers", t, t.id);
			System.out.println(">> UPDATED: new teacher added to course");
		} else
			System.out.println(">> ERROR: teacher signed in already");

		return true;
	}

	void removeTeacher(String x) {
		if (teachersIDs.contains(x)) {
			teachersIDs.remove(x);
			Controller.update("update", "courses", this, this.id);
			System.out.println(">> UPDATED: " + x + " removed from " + name + " course");
		} else
			System.out.println(">> ERROR: teacher isn't signed in to " + name);
	}

	// Returns all teachers of the course
	ArrayList<Teacher> getTeachers() {

		ArrayList<Teacher> res = new ArrayList<Teacher>();
		Gson g = new Gson();

		ArrayList<JsonObject> teachers = Controller.findAll("teachers");

		if (teachers != null) {
			for (JsonObject obj : teachers) {

				Teacher t = g.fromJson(obj, Teacher.class);
				// The teacher teach this course
				if (t != null && t.courses.contains(this.id))
					res.add(t);
			}
		}

		return res;
	}

	void printTeachers() {
		ArrayList<Teacher> teachers = getTeachers();
		int i = 1;

		if (teachers.size() > 0) {
			System.out.println("\n>> Printing teachers of course " + name + "\n=======================");
			for (Teacher teacher : teachers) {
				System.out.println(
						i + ". " + teacher.getFirstName() + " " + teacher.getLastName() + ", " + teacher.getTz());
				i++;
			}
			System.out.println("=======================");
		} else
			System.out.println(">> No teacher signed to this course");
	}

// ======================================================================== //
// Grades
// ======================================================================== //

	ArrayList<Grade> getGrades() {

		ArrayList<Grade> res = new ArrayList<Grade>();
		Gson g = new Gson();

		ArrayList<JsonObject> grades = Controller.findAll("grades");

		// Search in grades, all the grades of the course
		for (JsonObject obj : grades) {

			if (obj.get("courseID").getAsString().equals(this.id)) {

				Grade grade = g.fromJson(obj, Grade.class);
				res.add(grade);
			}
		}
		return res;
	}

	void printGrades() {
		ArrayList<Grade> grades = getGrades();

		printStart();
		for (Grade grade : grades) {
			grade.printByRow();
		}
		printEnd();
	}

	void printStart() {
		String open = "======================================================================================";
		System.out.println(open);
		// 26 spaces
		final String width = "                       ";
		StringBuffer sname = new StringBuffer(25);
		sname.append(width + " |");
		sname.replace(0, 3, "name");

		StringBuffer title = new StringBuffer(25);
		title.append(width + "   |");
		title.replace(0, 6, "title");
		StringBuffer prntg = new StringBuffer(25);
		prntg.append(width + "   |");
		prntg.replace(0, 9, "percentage");

		StringBuffer grade = new StringBuffer(25);
		grade.append(width + "   ");
		grade.replace(0, 4, "grade");
		System.out.print("|");
		System.out.print(sname);
		System.out.print(title);
		System.out.print(prntg);
		System.out.print(grade);
		System.out.print(sname.length());
		System.out.print("\n");
	}

	void printEnd() {
		String close = "======================================================================================";
		System.out.println(close);
	}
}
