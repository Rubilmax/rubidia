package me.pmilon.RubidiaCore.ritems.weapons;

public class Piercing {

	public enum PiercingType {
		
		STRENGTH("STR","FOR",1),
		ENDURANCE("END","END",1),
		AGILITY("AGI","AGI",1),
		INTELLIGENCE("INT","INT",1),
		PERCEPTION("PER","PER",1),
		ATTACK("ATK","ATQ",10),
		DEFENSE("DEF","DEF",10);
		
		private String displayEn;
		private String displayFr;
		private int amount;
		private PiercingType(String displayEn, String displayFr, int amount){
			this.displayEn = displayEn;
			this.displayFr = displayFr;
			this.amount = amount;
		}
		
		public String getDisplayEn(){
			return this.displayEn;
		}
		
		public String getDisplayFr(){
			return this.displayFr;
		}
		
		public int getAmount(){
			return this.amount;
		}
		
		public static PiercingType fromDisplay(String display){
			for(PiercingType type : values()){
				if(type.getDisplayEn().equals(display) || type.getDisplayFr().equals(display)){
					return type;
				}
			}
			return null;
		}

	}
	
	private PiercingType type;
	public Piercing(PiercingType type){
		this.type = type;
	}
	
	public PiercingType getType() {
		return type;
	}
	
	public void setType(PiercingType type) {
		this.type = type;
	}
	
}
