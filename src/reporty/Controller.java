package reporty;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Controller {

	static boolean update(String action, String field, Object x, String id) {

		Database db = Database.getInstance();
		boolean isUpdate = false;
		try {
			switch (action) {
			case "insert":
				db.insert(x, field);
				isUpdate = true;
				break;

			case "remove":
				isUpdate = db.remove(id, field);
				break;

			case "update":
				isUpdate = db.update(field, x, id);
				break;

			default:
				break;
			}
		} catch (Exception ex) {
			System.out.println(">>> Controller update failed " + ex);
			isUpdate = false;
		} finally {
			db.closeDB();
		}
		return isUpdate;
	}

	// Returns JsonObject after searching by name
	static JsonObject findByName(String name, String field) {

		JsonObject res = new JsonObject();
		Database driver = Database.getInstance();
		JsonArray arr = (JsonArray) driver.db.get(field);
		String cleanName;

		for (JsonElement elm : arr) {
			cleanName = elm.getAsJsonObject().get("name").getAsString();

			if (cleanName.equals(name)) {
				res = elm.getAsJsonObject();
			}
		}
		return res;
	}

	// Returns JsonObject after searching by id
	static JsonObject findById(String id, String field, String lable) {

		JsonObject res = new JsonObject();
		Database driver = Database.getInstance();
		JsonArray arr = (JsonArray) driver.db.get(field);
		String cleanTz;

		for (JsonElement elm : arr) {
			cleanTz = elm.getAsJsonObject().get(lable).getAsString();

			if (cleanTz.equals(id)) {
				res = elm.getAsJsonObject();
			}
		}
		return res;
	}

	static JsonObject findById(int num, String field) {

		JsonObject res = new JsonObject();
		Database driver = Database.getInstance();
		JsonArray arr = (JsonArray) driver.db.get(field);
		int cleanNum;

		for (JsonElement elm : arr) {
			cleanNum = elm.getAsJsonObject().get("num").getAsInt();

			if (cleanNum == num) {
				res = elm.getAsJsonObject();
			}
		}
		return res;
	}

	static ArrayList<JsonObject> findAll(String field) {

		Database driver = Database.getInstance();
		ArrayList<JsonObject> newList = new ArrayList<JsonObject>();
		JsonArray list = (JsonArray) driver.db.get(field);

		if (list != null) {

			// for any course, check if studentId exist
			for (JsonElement elm : list) {
				newList.add(elm.getAsJsonObject());
			}
		}

//		driver.closeDB();	
		return newList;
	}

	// Returns JsonObject list of all courses student has
	static ArrayList<JsonObject> getCoursesByStudentId(String id) {

		ArrayList<JsonObject> coursesList = new ArrayList<JsonObject>();
		Database driver = Database.getInstance();
		JsonArray courses = (JsonArray) driver.db.get("courses");

		if (courses != null) {

			// For any course, check if studentId exist
			for (JsonElement course : courses) {
				JsonArray studentsInCourse = (JsonArray) course.getAsJsonObject().get("studentsIDs");

				if (studentsInCourse.contains(new JsonPrimitive(id)))
					coursesList.add(course.getAsJsonObject());
			}
		}

//		driver.closeDB();
		return coursesList;
	}

	// Returns JsonObject list of all grades student has by a specific course id
	static ArrayList<JsonObject> getGradesByCourseAndStudentId(String courseId, String studentId) {

		ArrayList<JsonObject> gradesList = new ArrayList<JsonObject>();

		Database driver = Database.getInstance();

		JsonArray grades = (JsonArray) driver.db.get("grades");

		// for any grade, check if it has the same studentId and courseId
		for (JsonElement grade : grades) {
			JsonObject jObject = grade.getAsJsonObject();

			String _studentId = jObject.get("studentID").getAsString();
			String _courseId = jObject.get("courseID").getAsString();

			// found a grade of a student at the course
			if (_studentId.equals(studentId) && _courseId.equals(courseId)) {
				// add to the grades list
				gradesList.add(grade.getAsJsonObject());
			}

		}

		return gradesList;
	}
}
