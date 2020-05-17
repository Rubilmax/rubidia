package me.pmilon.RubidiaGuilds.guilds;

public enum Relation {

	ALLY("5", "d"),
	ENEMY("4", "c"),
	MEMBER("2", "a"),
	NEUTRAL("8", "7"),
	PEACEFUL("6", "e");
	
	private final String dcolorCode;
	private final String ccolorCode;
	private Relation(String dcolorCode, String ccolorCode){
		this.dcolorCode = dcolorCode;
		this.ccolorCode = ccolorCode;
	}
	public String getDColorCode() {
		return "§" + dcolorCode;
	}
	public String getCColorCode() {
		return "§" + ccolorCode;
	}
	
}
