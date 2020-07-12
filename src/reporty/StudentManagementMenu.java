// checked: #0, #1, #2, #3, #4, #5, #6, #7, #8

package reporty;

import java.util.Scanner;

public class StudentManagementMenu implements IReportyMenu {

	@Override
	public void run() {
		Scanner s = new Scanner(System.in);
		Student s1 = null;
		int num = 0;
		boolean studentContinue = true;

		while (studentContinue == true) {
			try {
				System.out.println(
						"\n>> Students Management Menu:\n-----------------------------\n1. Add a New Student\n2. Edit Student Information\n3. Print Student Information\n4. Print All Students\n5. Print Student Courses\n6. Print Student Grades in Course\n7. Delete a Student\n0. GO Back");
				System.out.print("\n>> Select an option: ");

				num = s.nextInt();

				switch (num) {

				case 0: {
					studentContinue = false;
					break;
				}

				case 1: {
					Student student = new Student();
					student = (Student) student.Create();
					Controller.update("insert", "students", student, null);
					System.out
							.println(">> UPDATED: new student created - " + student.firstName + " " + student.lastName);
					break;
				}

				case 2: {
					s1 = Student.findOne();
					if (s1 != null) {
						s1.updateInfo(s);
						Controller.update("update", "students", s1, s1.id);
						s1.print();
					}
					break;
				}

				case 3: {
					s1 = Student.findOne();
					if (s1 != null) {
						s1.print();
					}
					break;
				}

				case 4: {
					Student.printAllStudents();
					break;
				}

				case 5: {
					s1 = Student.findOne();
					s1.printAllCourses();
					break;
				}

				case 6: {
					s1 = Student.findOne();
					s1.printAllGrades();
					break;
				}


				case 7: {
					s1 = Student.findOne();
					if(s1.id != "")
						if(s1.getCourses().size() == 0) {
							if (Controller.update("remove", "students", null, s1.id))
								System.out.println(">> The student has been deleted successfully ");
						}else
							System.out.println(">> ERROR: you must delete all the student courses before");
					break;
				}
				
				default:
					break;
				}
			} catch (NullPointerException ex) {

			}
		}
	}
}
