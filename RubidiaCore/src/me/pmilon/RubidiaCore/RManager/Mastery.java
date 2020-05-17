package me.pmilon.RubidiaCore.RManager;

public enum Mastery {

	VAGRANT(0,"Vagabond"),
	ADVENTURER(15,"Aventurier"),
	ASPIRANT(25,"Aspirant"),
	SPECIALIST(40,"Spécialiste"),
	EXPERT(60,"Expert"),
	MASTER(85,"Maître"),
	HERO(120,"Héros");
	
	private final int level;
	private final String name;
	private Mastery(int level, String name){
		this.level = level;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getId() {
		for(int i = 0;i < Mastery.values().length;i++) {
			if(this.equals(Mastery.values()[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
}
