package reporty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.*;

public class Database {

	private static Database object;
	private FileReader reader;
	private FileWriter writer;
	private final Gson gson;
	private final String dbName;
	JsonObject db;

	private Database() {
		gson = new Gson();

		this.dbName = "reporty.db.json";

		try {
			File dbFile = openDBFile();
			this.reader = new FileReader(dbFile);
			this.writer = new FileWriter(this.dbName, true);

			if (dbFile.length() > 0) {

				if (reader != null) {
					this.db = gson.fromJson(reader, JsonObject.class);
				}
			} else
				init();

		} catch (FileNotFoundException ex) {
			System.out.println(">> Database not found");
		} catch (IOException ex) {
			System.out.println(">> Database writer not created");
		}
	}

	public JsonObject getDB() {
		if(this.object != null)
			return this.db;
		return new JsonObject();
	}
	
	public static synchronized Database getInstance() {

		if (object == null)
			object = new Database();
		return object;
	}

	File openDBFile() {
		File db = new File(this.dbName);

		if (!db.exists()) {
			try {
				db.createNewFile();
				System.out.println(">> New database has been created");
			} catch (IOException ex) {
				System.out.println(">> Database could not be opened");
			}
		}
		return db;
	}

	void init() {
		JsonObject dbSekelton = new JsonObject();

		dbSekelton.add("students", new JsonArray());
		dbSekelton.add("grades", new JsonArray());
		dbSekelton.add("courses", new JsonArray());
		dbSekelton.add("teachers", new JsonArray());

		this.db = dbSekelton;
		String json = gson.toJson(this.db);
		try {
			writer.write(json);
		} catch (IOException ex) {
			System.out.println(">> Database init failed");
		}

	}

	void closeDB() {
		try {
			this.reader.close();
			this.writer.close();
			this.writer = new FileWriter(this.dbName);
			this.writer.write(this.db.toString());
			this.writer.close();
		} catch (IOException ex) {
			System.out.println(">> Reporty failed to exit");
		}

	}

	void emptyDB() {	
		JsonObject dbSekelton = new JsonObject();

		dbSekelton.add("students", new JsonArray());
		dbSekelton.add("grades", new JsonArray());
		dbSekelton.add("courses", new JsonArray());
		dbSekelton.add("teachers", new JsonArray());
		this.db = dbSekelton;
	}
	
	void insert(Object obj, String field) {
		JsonObject json = gson.fromJson(gson.toJson(obj), JsonObject.class);
		JsonElement f = db.get(field);
		JsonArray arr = (JsonArray) f;
		if (obj != null && arr != null)
			arr.add(json);
	}

	boolean remove(String id, String field) {
		JsonArray arr = this.db.get(field).getAsJsonArray();
		boolean success = false;

		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getAsJsonObject().get("id").getAsString().equals(id)) {
				arr.remove(i);
				success = true;
				break;
			}
		}

		if (!success) {
			System.out.println(">>> DB ERROR: id isn't founded");
		}

		return success;
	}

	boolean update(String field, Object newObj, String id) {

		boolean success = false;
		JsonArray arr = this.db.get(field).getAsJsonArray();

		JsonObject oldObj = null;
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getAsJsonObject().get("id").getAsString().equals(id)) {
				oldObj = arr.get(i).getAsJsonObject();
				arr.remove(i);
				break;
			}
		}

		if (oldObj != null) {
			insert(newObj, field);
			success = true;
		}

		return success;
	}

}
