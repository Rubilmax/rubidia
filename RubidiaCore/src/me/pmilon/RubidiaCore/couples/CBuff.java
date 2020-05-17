package me.pmilon.RubidiaCore.couples;

import me.pmilon.RubidiaCore.ritems.weapons.Buff;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;

public class CBuff extends Buff {

	private String name;
	private long xpTime;
	public CBuff(int id, String name, BuffType type, int level, long xpTime) {
		super(id, type, level);
		this.name = name;
		this.xpTime = xpTime;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public long getXpTime() {
		return xpTime;
	}

	public void setXpTime(long xpTime) {
		this.xpTime = xpTime;
	}

}
