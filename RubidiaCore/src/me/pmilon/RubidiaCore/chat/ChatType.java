package me.pmilon.RubidiaCore.chat;

public enum ChatType {

	GLOBAL("GLOBAL", "§7", "§8"),
	SHOUT("CRIS", "§c", "§4"),
	PARTY("GROUPE", "§a", "§2"),
	GUILD("GUILDE", "§a", "§2"),
	ALLIANCE("ALLIANCE", "§d", "§5"),
	PRIVATE("", "§e", "§6"),
	STAFF("STAFF", "§c", "§4");

	private final String displayfr;
	private final String selectedColor;
	private final String defaultColor;
	private ChatType(String displayfr, String selectedColor, String defaultColor){
		this.displayfr = displayfr;
		this.selectedColor = selectedColor;
		this.defaultColor = defaultColor;
	}

	public String getDisplayFR() {
		return displayfr;
	}

	public String getSelectedColor() {
		return selectedColor;
	}

	public String getDefaultColor() {
		return defaultColor;
	}
}
