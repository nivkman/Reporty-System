package reporty;

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Utils {
	static ArrayList<String> ids = new ArrayList<String>();	
	static Gson gson = new Gson();
	
	static String idGenerator() {
		String id = "";
		int x;
		while (true) {
			for (int i = 0; i < 16; i++) {
				x = (int) (Math.random() * 2);
				if (x != 0) {
					x = (int) (Math.random() * 53);
					x += 65;
					if(x != 92) {
						id += (char) (x);
					}else {
						id += (char) (x+1);
					}
				} else {
					x = (int) (Math.random() * 10);
					id += x;
				}
			}
			if (!ids.contains(id))
				break;

		}
		ids.add(id);
		return id;
	}

	static String toJSON(Object o) {
		
		if(o != null) 
			return gson.toJson(o);				
		return "";
	}
	
	static JsonObject toJSONObject(Object o) {
		String str = gson.toJson(o);
		return gson.fromJson(str, JsonObject.class);
	}
	
	void printIds() {
		ids.forEach((id) -> {
			System.out.println(id);
		});
	}
	
	static String charRemoveAt(String str, int p) {  
         return str.substring(0, p) + str.substring(p + 1);  
      }
	
	static String removeQuotes(String str) {
		str = Utils.charRemoveAt(str,0);
		str = Utils.charRemoveAt(str,str.length()-1);
		
		return str;
	}
}
