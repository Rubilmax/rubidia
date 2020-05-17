package me.pmilon.RubidiaQuests.dialogs;

import org.bukkit.Sound;

public enum DialogType {

	AMBIENT(Sound.ENTITY_VILLAGER_AMBIENT),
	YES(Sound.ENTITY_VILLAGER_YES),
	NO(Sound.ENTITY_VILLAGER_NO),
	EXCLAMATION(Sound.ENTITY_VILLAGER_TRADE),
	SHOCKED(Sound.ENTITY_VILLAGER_HURT),
	SICK(Sound.ENTITY_VILLAGER_DEATH);
	
	private final Sound sound;
	private DialogType(Sound sound) {
		this.sound = sound;
	}
	
	public Sound getSound() {
		return sound;
	}
	
}
