package me.pmilon.RubidiaCore.RManager;

public enum Gender {
	
	MALE("Homme"),
	FEMALE("Femme"),
	UNKNOWN("Inconnu");
	
	private final String name;
	private Gender(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
