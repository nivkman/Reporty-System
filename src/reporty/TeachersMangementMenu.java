// checked: #0, #1, #2, #3, #4, #5, #6


package reporty;

import java.util.Scanner;

public class TeachersMangementMenu implements IReportyMenu {

	@Override
	public void run() {
		Scanner s = new Scanner(System.in);
		Teacher t;
		boolean teacherContinue = true;

		try {

			while (teacherContinue == true) {
				System.out.println(
						"\n>> Teachers Management Menu:\n-----------------------------\n1. Add a New Teacher\n2. Edit Teacher Information\n3. Print Teacher Information\n4. Print Teacher Courses\n5. Print All Teachers\n6. Delete a Teacher\n0. GO Back");
				System.out.print("\n>> Select an option: ");

				switch (s.nextInt()) {

				case 0: {
					teacherContinue = false;
					break;
				}

				case 1: {
					Teacher teacher = new Teacher();
					teacher = (Teacher) teacher.Create();
					Controller.update("insert", "teachers", teacher, null);
					System.out
							.println(">> UPDATED: new teacher created - " + teacher.firstName + " " + teacher.lastName);
					break;
				}

				case 2: {
					t = Teacher.findOne();
					if (t != null) {
						t.updateInfo(s);
						Controller.update("update", "teachers", t, t.id);
						t.print();
					}
					break;
				}

				case 3: {
					t = Teacher.findOne();
					if (t != null) {
						t.print();
					}
					break;
				}

				case 4: {
					t = Teacher.findOne();
					t.printCourses();		
					break;
				}

				case 5: {
					Teacher.printAllTeachers();
					break;
				}
				case 6: {
					t = Teacher.findOne();
					if(t.id != "")
						if(t.getCourses().size() == 0) {
							if (Controller.update("remove", "teachers", null, t.id))
								System.out.println(">> The teacher has been deleted successfully ");
						}else
							System.out.println(">> ERROR: you must delete all the teacher courses before");
					break;
				}

				default:
					break;
				}
			}
		} catch (NullPointerException ex) {
		}
	}
}
