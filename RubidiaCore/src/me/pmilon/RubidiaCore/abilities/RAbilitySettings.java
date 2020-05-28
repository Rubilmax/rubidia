package me.pmilon.RubidiaCore.abilities;

import me.pmilon.RubidiaCore.RManager.RClass;

public enum RAbilitySettings {

	PALADIN_1(25,0,0,0,20),
	PALADIN_2(25,0,0,0,20),
	PALADIN_3(25,0,0,0,25),
	PALADIN_4(25,0,0,-20,0),
	PALADIN_5(20,18,8,140,115),
	PALADIN_6(20,21,28,6.2,11.3),
	PALADIN_7(20,26,39,7.9,15.1),
	PALADIN_8(15,19,16,1.35,3),
	PALADIN_9(15,20,29,2.9,4.8),
	PALADIN_10(30,31,44,4.9,12.2),
	PALADIN_11(30,28,45,8.7,16.7),
	PALADIN_12(30,39,52,4.5,15.8),
	
	RANGER_1(25,0,0,0,18),
	RANGER_2(25,0,0,0,20),
	RANGER_3(25,0,0,0,20),
	RANGER_4(25,0,0,0,4),
	RANGER_5(20,21,28,4,16),
	RANGER_6(20,25,36,6.3,10.1),
	RANGER_7(15,28,19,85,100),
	RANGER_8(20,29,41,7.7,14.2),
	RANGER_9(15,24,37,10.5,16.6),
	RANGER_10(30,27,43,1,5),
	RANGER_11(30,43,58,15.2,30.3),
	RANGER_12(30,39,54,10.3,18.1),
	
	MAGE_1(25,0,0,0,20),
	MAGE_2(25,0,0,0,20),
	MAGE_3(25,0,0,0,20),
	MAGE_4(25,0,0,0,20),
	MAGE_5(15,10,71,1,10),
	MAGE_6(20,26,35,3,13),
	MAGE_7(15,28,12,2.5,5.7),
	MAGE_8(20,41,1.19,3.4,4.9),
	MAGE_9(20,30,69,5.9,13.7),
	MAGE_10(30,36,60,5.1,9.2),
	MAGE_11(30,41,1.19,3.4,.57),
	MAGE_12(30,38,55,4.9,16.7),
	
	ASSASSIN_1(40,17,.64,1.5,.22),
	ASSASSIN_2(30,24,.83,3,.17),
	ASSASSIN_3(30,0,0,100,3.5),
	ASSASSIN_4(30,29,1.01,.8,.46),
	ASSASSIN_5(30,33,.77,1,.14),
	ASSASSIN_6(20,6,.3,2.3,.17),
	ASSASSIN_7(20,38,1.18,3.1,.72),
	ASSASSIN_8(30,47,1.08,3.9,.62);
	
	private final int levelMax;
	private final double vigorStart;
	private final double vigorEnd;
	private final double damagesStart;
	private final double damagesEnd;
	private RAbilitySettings(int levelMax, double vigorStart, double vigorEnd, double damagesStart, double damagesEnd){
		this.levelMax = levelMax;
		this.vigorStart = vigorStart;
		this.vigorEnd = vigorEnd;
		this.damagesStart = damagesStart;
		this.damagesEnd = damagesEnd;
	}
	
	public int getLevelMax() {
		return levelMax;
	}

	public double getVigorStart() {
		return vigorStart;
	}

	public double getVigorEnd() {
		return vigorEnd;
	}

	public double getDamagesStart() {
		return damagesStart;
	}

	public double getDamagesEnd() {
		return damagesEnd;
	}
	
	public double getVigorMin() {
		return vigorStart;
	}
	
	public double getVigorPerLevel() {
		return (vigorEnd-vigorStart)/levelMax;
	}
	
	public double getDamagesMin() {
		return damagesStart;
	}
	
	public double getDamagesPerLevel() {
		return (damagesEnd-damagesStart)/levelMax;
	}
	
	public static RAbilitySettings getSettings(RClass rClass, int index) {
		return RAbilitySettings.valueOf(rClass.toString() + "_" + index);
	}
	
}
