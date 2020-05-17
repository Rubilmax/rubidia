package me.pmilon.RubidiaQuests.pnjs;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.houses.House;
import me.pmilon.RubidiaQuests.ui.houses.HouseManagerUI;
import me.pmilon.RubidiaQuests.utils.Configs;
import me.pmilon.RubidiaQuests.utils.Utils;

public class HostPNJ extends PNJHandler {

	private String houseUniqueId;
	public HostPNJ(String uuid, String name, Location loc, int age, String houseUniqueId) {
		super(uuid, "HÔTE", "§2§l", name, "§a", PNJType.HOST, loc, age, false);
		this.houseUniqueId = houseUniqueId;
	}

	@Override
	protected void onDelete() {
		Configs.getHousesConfig().set("houses." + this.getHouseUniqueId(), null);
		QuestsPlugin.houseColl.remove(this.getHouseUniqueId());
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

	public String getHouseUniqueId() {
		return houseUniqueId;
	}

	public void setHouseUniqueId(String houseUniqueId) {
		this.houseUniqueId = houseUniqueId;
	}

	public House getHouse() {
		return House.get(this.getHouseUniqueId());
	}

	@Override
	protected void onRightClick(PlayerInteractEntityEvent e, final Player player, Villager villager) {
		final RPlayer rp = RPlayer.get(player);
		final PNJHandler instance = this;
		final House house = this.getHouse();
		if(house.isInhabited()) {
			RPlayer owner = house.getOwner();
			if(owner.equals(rp)) {
				PNJDialog dialog = new PNJDialog(player, instance, getEntity(),
						Arrays.asList("%rapidBienvenue chez vous, %player !", "%rapidVous souhaitez modifier quelque chose dans votre maison ?"),
						DialogType.EXCLAMATION, new Runnable() {

							@Override
							public void run() {
								Core.uiManager.requestUI(new HouseManagerUI(player, house));
							}
					
				}, false, true);
				dialog.start();
			} else {
				PNJDialog dialog = new PNJDialog(player, instance, getEntity(),
						Arrays.asList("%rapidBonjour %player, bienvenue chez §e" + owner.getName() + "§a !"),
						DialogType.EXCLAMATION, null, false, true);
				dialog.start();
			}
		} else {
			House rpHouse = House.get(rp);
			if(rpHouse != null) {
				PNJDialog dialog = new PNJDialog(player, this, villager,
						Arrays.asList("Bonjour %player, bienvenue !",
								"Cette maison est disponible pour seulement " + this.getHouse().getPrice() + ".",
								"Mais vous devez d'abord vendre la vôtre pour habiter ici !"), DialogType.EXCLAMATION, null, false, true);
				dialog.start();
			} else {
				PNJDialog dialog = new PNJDialog(player, this, villager, Arrays.asList("Bonjour %player, bienvenue chez vous !",
						"Pour seulement " + house.getPrice() + " émeraudes, vous pouvez profiter de cette maison à vie !",
						"Alors, intéressé ?"), DialogType.AMBIENT, new Runnable(){
					public void run(){
						Utils.sendQuestion(rp, "Souhaitez-vous acheter cette maison ?",
								"/house buy " + getUniqueId(),
								"/house no " + getUniqueId());
					}
				}, true, true);
				dialog.start();
			}
		}
	}

	@Override
	protected void onSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".houseUniqueId", this.getHouseUniqueId());
	}
	
}
