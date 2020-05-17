package me.pmilon.RubidiaQuests.pnjs;

import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.utils.Configs;
import me.pmilon.RubidiaQuests.utils.Utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class PasserPNJ extends DialogerPNJ {

	private String targetName;
	private Location targetLocation;
	
	public PasserPNJ(String uuid, String name, Location loc, int age, List<String> dialogs, boolean fix, String targetName, Location targetLocation) {
		super(uuid, "PASSEUR", "§9§l", name, "§b", PNJType.PASSER, loc, age, fix, dialogs, true);
		this.targetName = targetName;
		this.targetLocation = targetLocation;
	}

	@Override
	public void onTalk(Player p) {
		RPlayer rp = RPlayer.get(p);
		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
		Utils.sendQuestion(rp, "Souhaitez-vous allez à " + this.getTargetName() + " ?", "/pass yes " + this.getUniqueId(), "/pass no");
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

	@Override
	protected void onDelete() {
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}

	@Override
	protected void onSubSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".targetName", this.getTargetName());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".targetLocation", this.getTargetLocation());
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

}
