package me.pmilon.RubidiaCore.ritems.weapons;

public class Buff {

	private int id;
	private BuffType type;
	private int level;
	
	public Buff(int id, BuffType type, int level){
		this.id = id;
		this.type = type;
		this.level = level;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BuffType getType() {
		return type;
	}

	public void setType(BuffType type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public double getFactor(){
		return .01*this.getLevel();
	}
}
