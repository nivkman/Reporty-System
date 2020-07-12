package reporty;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class Grade {
	String courseID;
	String studentID;
	final String id;
	double zion, percentage;
	String title;

// ======================================================================== //
// Grade Cons Functions
// ======================================================================== //

	Grade() {
		this.courseID = "";
		this.studentID = "";
		this.zion = 0;
		this.percentage = 0;
		this.title = "";
		this.id = Utils.idGenerator();
	}

	Grade(String courseID, String studentID, double zion, double percentage, String title) {
		this.courseID = courseID;
		this.studentID = studentID;
		this.zion = zion;
		this.percentage = percentage;
		this.title = title;
		this.id = Utils.idGenerator();
	}

// ======================================================================== //
// Grade Static Functions
// ======================================================================== //

	static boolean createList() {
		boolean res = true;
		Scanner s = new Scanner(System.in);
		ArrayList<Grade> grades = new ArrayList<Grade>();
		Student stud;
		double zion = 0;
		Course c = Course.findOne();

		if (c == null)
			return false;

		ArrayList<Student> students = c.getStudents();
		if (students.size() == 0) {
			System.out.println(">> ERROR: students course list is empty");
			res = false;
			return res;
		}

		System.out.print(">> Insert Grade Title: ");
		String title = s.next();

		System.out.print(">> Insert " + title + " Percentage: ");
		double percentage = s.nextDouble();

		for (int i = 0; i < students.size(); i++) {
			stud = students.get(i);
			System.out.print(">> Insert grade for - " + stud.getFirstName() + " " + stud.getLastName() + ", "
					+ stud.getTz() + ": ");
			zion = s.nextDouble();
			if (zion == -1.0)
				break;

			Grade newGrade = new Grade(c.getId(), stud.getId(), zion, percentage, title);
			Controller.update("insert", "grades", newGrade, null);
			grades.add(newGrade);
		}

		System.out.println(">> Insertion done");
		return res;
	}

	static ArrayList<Grade> filterByCourse(String courseID) {
		Gson g = new Gson();

		ArrayList<Grade> grades = new ArrayList<Grade>();

		ArrayList<JsonObject> gradesJson = Controller.findAll("grades");
		gradesJson.forEach((item) -> {
			Grade temp = g.fromJson(item, Grade.class);
			if (temp.getCourseId().equals(courseID)) {
				grades.add(temp);
			}
		});

		return grades;
	}

	static Grade findOne() {
		Gson g = new Gson();
		Scanner s = new Scanner(System.in);

		ArrayList<Grade> grades = new ArrayList<Grade>();

		Course c = Course.findOne();
		if (c == null)
			return null;
		final String courseID = c.id;

		final String studentID = Student.getIdByTz();
		if (studentID == null)
			return null;

		System.out.print(">> Insert a Grade Title: ");
		String title = s.next();

		ArrayList<JsonObject> gradesJson = Controller.findAll("grades");
		gradesJson.forEach((item) -> {
			Grade temp = g.fromJson(item, Grade.class);
			if (temp.getCourseId().equals(courseID) && temp.studentID.equals(studentID)
					&& temp.getTitle().equals(title)) {
				grades.add(temp);
			}
		});

		if (grades.size() == 0) {
			System.out.println(">> ERROR: grade title wasn't founded");
			return null;
		}

		if (grades.get(0) != null)
			return grades.get(0);

		System.out.println(">> ERROR: could not found the grade");
		return null;
	}

// ======================================================================== //
// Grade GET/SET Functions
// ======================================================================== //

	// gets functions

	String getStudentId() {
		return studentID;
	}

	String getCourseId() {
		return courseID;
	}

	double getZion() {
		return zion;
	}

	double getPercentage() {
		return percentage;
	}

	String getTitle() {
		return title;
	}

	// sets functions
	void setStudentId(String studentID) {
		this.studentID = studentID;
	}

	void setCourseId(String courseID) {
		this.courseID = courseID;
	}

	void setZion(double zion) {
		this.zion = zion;
	}

	void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	void setTitle(String title) {
		this.title = title;
	}

// ======================================================================== //
// Grade Functions
// ======================================================================== //

	Grade update() {
		Scanner s = new Scanner(System.in);
		System.out.println(
				"\n>> What do you want to change?\n1. Title\n2. Grade\n3. Percentage\n4. Student\n5. Course\n 0. Cencel");
		System.out.print("\n Select an option: ");

		switch (s.nextInt()) {
		case 0:
			System.out.println(">>> Action canceled");
			break;
		case 1:
			System.out.print("\n>>> Insert title: ");
			this.setTitle(s.next());
			break;
		case 2:
			System.out.print("\n>>> Insert grade: ");
			this.setZion(s.nextDouble());
			break;
		case 3:
			System.out.print("\n>>> Insert percentage: ");
			this.setPercentage(s.nextDouble());
			break;
		case 4:
			System.out.print("\n>>> Insert student T.Z: ");
			Student stud = Student.findOne(s.next());
			if (stud == null)
				return null;
			this.setStudentId(stud.getId());
			break;
		case 5:
			System.out.print("\n>>> Insert course number: ");
			Course c = Course.findOne(s.nextInt());
			if (c == null)
				return null;
			this.setCourseId(c.getId());
			break;

		default:
			break;
		}

		System.out.println(">>> Saved changes ");
		return this;
	}

	void print() {
		System.out.println("\n=======================\n>> Title: " + title + "\n>> Grade: " + zion + "\n>> Percentage: "
				+ percentage + "\n>> Course ID: " + courseID + "\n>> Student ID: " + studentID
				+ "\n=======================");
	}

	void printByRow() {
		final String tab = "\t\t";
		// 26 spaces
		final String width = "|                        ";
		final String line = "|";
		final String space = " ";

		Student info = Student.findOneById(this.studentID);
		StringBuffer name = new StringBuffer(25);
		name.append(width);
		String fullName = info.getFirstName() + space + info.getLastName();
		name.replace(1, fullName.length(), fullName);

		StringBuffer title = new StringBuffer(25);

		title.append(width);
		title.replace(1, this.title.length(), this.title);

		StringBuffer percentage = new StringBuffer(25);
		percentage.append(width);
		percentage.replace(1, 3, this.percentage + "%");

		StringBuffer zion = new StringBuffer(25);
		zion.append(width);
		zion.replace(1, 3, this.zion + "");

		System.out.print(name);
		System.out.print(title);
		System.out.print(percentage);
		System.out.print(zion);

		System.out.print("\n");
	}
}