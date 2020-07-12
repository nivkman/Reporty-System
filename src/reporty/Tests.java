package reporty;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.plugins.MockMaker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Tests {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void SuccessLogin() {

		Gson g = new Gson();

		Database dbmocked = mock(Database.class);

		JsonObject obj = new JsonObject();

		Student s = new Student();
		s.email = "user@gmail.com";
		s.password = "AaBb";

		obj.addProperty("students", g.toJson(s));

		doReturn(obj).when(dbmocked).getDB();

		dbmocked.getDB().addProperty("students", g.toJson(s));

		Student fromDb = g.fromJson(dbmocked.getDB().get("students").getAsString(), Student.class);

		Assertions.assertEquals(fromDb.email, s.email);
		Assertions.assertEquals(fromDb.password, s.password);
	}

	@Nested
	class DatabaseTests {

		@Test
		public void saveCourseSuccessed() {
			Gson gson = new Gson();

			JsonObject obj = new JsonObject();
			Course c = new Course();
			ArrayList<Course> courses = new ArrayList<Course>();
			courses.add(c);
			obj.addProperty("courses", gson.toJson(c));
			// doReturn(obj).when(dbmocked).getDB();
			Controller.update("insert", "courses", c, null);

			JsonObject db = Database.getInstance().db;
			JsonArray arr = db.get("courses").getAsJsonArray();
			String id = arr.get(0).getAsJsonObject().get("id").getAsString();
			Assertions.assertTrue(id.equals(c.id));
		}

		@Test
		public void saveTeacherSuccessed() {
			Gson gson = new Gson();

			JsonObject obj = new JsonObject();
			Teacher t = new Teacher();
			obj.addProperty("teachers", gson.toJson(t));

			Controller.update("insert", "teachers", t, null);

			JsonObject db = Database.getInstance().db;
			JsonArray arr = db.get("teachers").getAsJsonArray();
			String id = arr.get(0).getAsJsonObject().get("id").getAsString();
			Assertions.assertTrue(id.equals(t.id));
		}

		@Test
		public void saveGradeSuccessed() {
			Gson gson = new Gson();

			JsonObject obj = new JsonObject();
			Grade g = new Grade();
			g.title = "Quiz Fall Semester";
			g.zion = 86;
			g.percentage = 2;
			obj.addProperty("grades", gson.toJson(g));

			Controller.update("insert", "grades", g, null);

			JsonObject db = Database.getInstance().db;
			JsonArray arr = db.get("grades").getAsJsonArray();
			String id = arr.get(0).getAsJsonObject().get("id").getAsString();
			Assertions.assertTrue(id.equals(g.id));
		}

		@Test
		public void saveStudentSuccessed() {
			Gson gson = new Gson();

			JsonObject obj = new JsonObject();
			Student s = new Student();
			obj.addProperty("students", gson.toJson(s));

			Controller.update("insert", "students", s, null);

			JsonObject db = Database.getInstance().db;
			JsonArray arr = db.get("students").getAsJsonArray();
			String id = arr.get(0).getAsJsonObject().get("id").getAsString();
			Assertions.assertTrue(id.equals(s.id));
		}

		@AfterEach
		public void clearDB() {
			Database.getInstance().emptyDB();
			Database.getInstance().closeDB();
		}
	}

	@Nested
	class UserTests {

		@Test
		public void UserCreateSuccessed() {
			User user = new Teacher();

			Assertions.assertTrue(user.firstName != null && user.lastName != null && user.email != null
					&& user.password != null && user.tz != null);
		}

		@Test
		void IdUniqeSuccessed() {
			boolean collides = false;
			int count = 0;
			String one = Utils.idGenerator();

			while (count < 100) {

				String two = Utils.idGenerator();
				if (one.equals(two))
					collides = true;
				count++;
			}
			Assertions.assertTrue(collides == false);
		}

		@Test
		public void TeacherCreateSuccessed() {
			Teacher t = new Teacher();

			Assertions.assertTrue(t.courses != null && t.courses.size() == 0);
		}

		@Test
		public void StudentCreateSuccessed() {
			Student s = new Student();

			Assertions.assertTrue(s.rectorExcellence == false && s.courses != null && s.degree != null);
		}

	}

	@Nested
	class CourseTests {

		@Test
		public void AddStudentSuccessed() throws UnsupportedEncodingException {

			Course c = new Course();
			c.name = "Math";
			Controller.update("insert", "courses", c, null);

			Student s = new Student();
			s.tz = "1020";

			String tz = "1020";
			InputStream testInput = new ByteArrayInputStream(tz.getBytes("UTF-8"));
			System.setIn(testInput);

			Controller.update("insert", "students", s, null);
			boolean added = c.addStudent();
			Assertions.assertTrue(added);
		}

		@Test
		public void AddTeacherSuccessed() throws UnsupportedEncodingException {
			Course c = new Course();
			c.name = "Math";
			Controller.update("insert", "courses", c, null);

			Teacher t = new Teacher();
			t.tz = "1010";

			String tz = "1010";
			InputStream testInput = new ByteArrayInputStream(tz.getBytes("UTF-8"));
			System.setIn(testInput);

			Controller.update("insert", "teachers", t, null);
			boolean added = c.addTeacher();
			Assertions.assertTrue(added);
		}

		@AfterEach
		public void clearDB() {
			Database.getInstance().emptyDB();
			Database.getInstance().closeDB();
		}

	}

	@Nested
	class GradeTests {

		@Test
		void removeGradesSuccess() throws UnsupportedEncodingException {

			Grade grd = new Grade();
			grd.studentID = "1020";
			grd.title = "Quiz Fall Sem";
			Controller.update("insert", "grades", grd, null);
			JsonArray grades = Database.getInstance().db.get("grades").getAsJsonArray();
			JsonObject grade = grades.get(0).getAsJsonObject();
			if (grades.size() > 0) {
				boolean isRemoved = Controller.update("remove", "grades", null, grade.get("id").getAsString());
				Assertions.assertTrue(isRemoved);
			}
			Assertions.assertFalse(false);

		}

		@AfterEach
		public void clearDB() {
			Database.getInstance().emptyDB();
			Database.getInstance().closeDB();
		}

	}

}
