// checked: #0, #1, #2, #3, #4

package reporty;

import java.util.Scanner;

public final class GradeManagementMenu implements IReportyMenu {

	@Override
	public void run() {
		try {

			Scanner s = new Scanner(System.in);
			Grade g = null;
			int num = 0;
			boolean gradesContinue = true;

			while (gradesContinue == true) {

				System.out.println(
						"\n>> Grades Management Menu:\n----------------------------\n1. Insert New Grades to Course\n2. Edit Grade in Course\n3. Remove Grade From Course\n4. Print Course Grades\n----------------------------\n0. GO Back");
				System.out.print("\n>> Select an option: ");

				num = s.nextInt();

				switch (num) {
				case 0: {
					gradesContinue = false;
					break;
				}

				case 1: {
					Grade.createList();
					break;
				}

				case 2: {
					Grade d = Grade.findOne();
					if (d != null) {
						d.update();
						Controller.update("update", "grades", d, d.id);
						d.print();
					}

					break;
				}

				case 3: {
					Grade d = Grade.findOne();
					if (d != null) {
						Controller.update("remove", "grades", null, d.id);
						System.out.println(">> The grade has been removed successfully");
					}
					break;
				}

				case 4: {
					Course c = Course.findOne();
					c.printGrades();
					break;
				}

				}

			}

		} catch (NullPointerException ex) {
		} catch (Exception ex) {
		}
	}

}
