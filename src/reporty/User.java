package reporty;

public abstract class User {
	String firstName, lastName, email, password, id, type, tz;
	boolean activation;
	int level;

	User(String type) {
		this.id = Utils.idGenerator();
		this.tz = "";
		this.type = type;
		this.firstName = "";
		this.lastName = "";
		this.email = "";
		this.password = "";
	}

	User(String firstName, String lastName, String tz, String email, String password, int level, String type) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.level = level;
		this.type = type;
		this.activation = true;
		this.tz = tz;
		this.id = Utils.idGenerator();
	}

	User(User x) {
		this.firstName = x.firstName;
		this.lastName = x.lastName;
		this.email = x.email;
		this.password = x.password;
		this.level = x.level;
		this.type = x.type;
		this.activation = true;
		this.tz = x.tz;
		this.id = Utils.idGenerator();
	}

	void setFirstName(String firstName) {
		this.firstName = firstName;
		System.out.println(">> UPDATED: firstname");
	}

	void setLastName(String lastName) {
		this.lastName = lastName;
		System.out.println(">> UPDATED: lastname");
	}

	void setEmail(String email) {
		this.email = email;
		System.out.println(">> UPDATED: email");
	}

	void setPassword(String password) {
		this.password = password;
		System.out.println(">> UPDATED: password");
	}

	void setTz(String tz) {
		this.tz = tz;
		System.out.println(">> UPDATED: T.Z");
	}

	void resetPassword() {
		System.out.println(">> password has been rest");
	}

	void setActivation(boolean activation) {
		this.activation = activation;
		System.out.println(">> UPDATED: activation");
	}

	void setLevel(int level) {
		this.level = level;
		System.out.println(">> UPDATED: level");
	}

	String getFirstName() {
		return firstName;
	}

	String getLastName() {
		return lastName;
	}

	String getEmail() {
		return email;
	}

	String getPassword() {
		return password;
	}

	String getTz() {
		return tz;
	}

	String getId() {
		return id;
	}

	boolean getActivation() {
		return activation;
	}

	int getLevel() {
		return level;
	}

	void print() {
		System.out.println("=======================\n>> User Type: " + type + "\n>> Name: " + firstName + " " + lastName
				+ "\n>> T.Z: " + tz + "\n>> Email: " + email + "\n>> Password: " + password + "\n>> ID: " + id
				+ "\n>> Activation: " + activation + "\n>> Level: " + level);
	}
}
