package me.pmilon.RubidiaQuests.pnjs;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class InhabitantPNJ extends DialogerPNJ {

	public InhabitantPNJ(String uuid, String title, String name, Location loc, int age, List<String> dialogs, boolean fix) {
		super(uuid, title, "§6§l", name, "§e", PNJType.INHABITANT, loc, age, fix, dialogs, false);
	}

	@Override
	protected void onDelete() {
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

	@Override
	protected void onSubSave() {
	}

	@Override
	protected void onTalk(Player player) {
	}

}
