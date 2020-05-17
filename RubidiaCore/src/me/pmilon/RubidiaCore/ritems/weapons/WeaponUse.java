package me.pmilon.RubidiaCore.ritems.weapons;

public enum WeaponUse {

	MELEE("Melee","de mêlée"),
	RANGE("Ranged","à distance"),
	MAGIC("Magic","magique"),
	MELEE_RANGE("Melee & ranged","polyvalente");
	
	private String displayEn;
	private String displayFr;
	private WeaponUse(String displayEn, String displayFr){
		this.displayEn = displayEn;
		this.displayFr = displayFr;
	}
	
	public String getDisplayEn() {
		return displayEn;
	}
	
	public void setDisplayEn(String displayEn) {
		this.displayEn = displayEn;
	}

	public String getDisplayFr() {
		return displayFr;
	}

	public void setDisplayFr(String displayFr) {
		this.displayFr = displayFr;
	}
}
