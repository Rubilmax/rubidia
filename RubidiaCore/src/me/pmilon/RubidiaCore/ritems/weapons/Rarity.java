package me.pmilon.RubidiaCore.ritems.weapons;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.utils.RandomUtils;

public enum Rarity {
	
	COMMON(1, "§f", "Commun", "Common"),
	UNCOMMON(.25, "§b", "Peu commun", "Uncommon"),
	SET(.1, "§a", "Item de set", "Set item"),
	RARE(.05, "§c", "Rare", "Rare"),
	EPIC(.01, "§e", "Épique", "Epic"),
	LEGENDARY(.002, "§d", "Légendaire", "Legendary");
	
	private double factor;
	private String prefix;
	private String displayEn;
	private String displayFr;
	private Rarity(double factor, String prefix, String displayFr, String displayEn){
		this.factor = factor;
		this.prefix = prefix;
		this.displayEn = displayEn;
		this.displayFr = displayFr;
	}
	
	public double getFactor() {
		return factor;
	}
	
	public void setFactor(double factor) {
		this.factor = factor;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public static Rarity random(Rarity... rarities){
		List<String> multipleRarities = new ArrayList<String>();
		for (Rarity rarity : rarities) {
			for (int i = 0;i < (int) rarity.getFactor() * 1000; i++) {
				multipleRarities.add(rarity.toString());
			}
		}
		
		if (multipleRarities.size() > 0) {
			String selectedRarity = multipleRarities.get(RandomUtils.random.nextInt(multipleRarities.size()));
			return Rarity.valueOf(selectedRarity);
		}
		return Rarity.COMMON;
	}
	
	/*public static Rarity fromName(String name){
		for(Rarity rarity : values()){
			if(rarity.getDisplayEn().equals(name) || rarity.getDisplayFr().equals(name)){
				return rarity;
			}
		}
		return null;
	}*/

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
