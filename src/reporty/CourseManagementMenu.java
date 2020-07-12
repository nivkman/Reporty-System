// checked: #0, #1, #2, #3, #4, #5, #6 ,#7, #8, #9, #10, #11, #15

package reporty;

import java.util.Scanner;

public final class CourseManagementMenu implements IReportyMenu {

	@Override
	public void run() {
		Scanner s = new Scanner(System.in);
		Course c = null;
		int num = 0;
		boolean courseContinue = true;

		while (courseContinue == true) {
			try {
				System.out.println(
						"\n>> Course Management Menu:\n----------------------------\n1. Add a New Course\n2. Edit Course Information\n3. Delete Course\n4. Print Course Information\n5. Print All Courses\n----------------------------\n6. Insert Students to Course\n7. Remove Student From Course\n8. Print Course Students\n----------------------------\n9. Insert Teachers to Course\n10. Remove Teacher From Course\n11. Print Course Teachers\n----------------------------\n0. GO Back");
				System.out.print("\n>> Select an option: ");

				num = s.nextInt();

				switch (num) {

				// general cases
				case 0: {
					courseContinue = false;
					break;
				}

				// courses cases
				case 1: {
					c = Course.createCourse();
					Controller.update("insert", "courses", c, null);
					break;
				}

				case 2: {
					c = Course.findOne();
					if (c.num != 0) {
						c.updateInfo(s);
						Controller.update("update", "courses", c, c.id);
						c.print();
					}
					break;
				}

				case 3: {
					c = Course.findOne();
					if (c.num != 0) {
						if (c.getStudentsIDs().size() == 0)
							if (c.getTeachersIDs().size() == 0)
								if (Controller.update("remove", "courses", null, c.id))
									System.out.println(">> The course has been deleted successfully ");
								else
									System.out.println(">> ERROR: something goes wrong ...");
							else
								System.out.println(">> ERROR: you must delete all the teachers course before");
						else
							System.out.println(">> ERROR: you must delete all the students and teachers course before");
					}
					break;
				}

				case 4: {
					c = Course.findOne();
					if (c.num != 0)
						c.print();
					break;
				}

				case 5: {
					if(!Course.printAll())
							System.out.println(">> Courses list is empty");
					break;
				}

				// students cases

				case 6: {
					c = Course.findOne();
					if (c.num != 0) {
						c.addStudentsList();
						Controller.update("update", "courses", c, c.id);
					}
					break;
				}

				case 7: {
					Student s1 = Student.findOne();
					if (s1.removeFromCourse())
						System.out.println(">> The student has been removed from course successfully ");
					else
						System.out.println(">> ERROR: could not remove the student from this course ");

					break;
				}

				case 8: {
					c = Course.findOne();
					c.printStudents();
					break;
				}

				// teachers cases

				case 9: {
					c = Course.findOne();
					if (c.num != 0) {
						c.addTeachersList();
						Controller.update("update", "courses", c, c.id);
					}
					break;
				}

				case 10: {
					Teacher t = Teacher.findOne();
					if (t.removeFromCourse())
						System.out.println(">> The teacher has been removed from course successfully ");
					else
						System.out.println(">> ERROR: could not remove the teacher from this course ");

					break;
				}

				case 11: {
					c = Course.findOne();
					c.printTeachers();
					break;
				}

				// default case
				default:
					break;
				}
			} catch (NullPointerException ex) {
//				System.out.println(">> System ERROR: " + ex);
			}
		}
	}

}
