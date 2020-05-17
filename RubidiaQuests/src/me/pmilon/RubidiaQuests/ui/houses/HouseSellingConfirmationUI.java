package me.pmilon.RubidiaQuests.ui.houses;

import java.util.Arrays;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaQuests.houses.House;

public class HouseSellingConfirmationUI extends ConfirmationUI {

	private House house;
	public HouseSellingConfirmationUI(Player p, House house) {
		super(p, "Vente de maison",
				new String[] {"�aVendre la maison est gagner " + (int) Math.round(house.getPrice()*.7) + " �meraudes",
						"�cConserver la maison et son meublement"},
				"�7�l" + (int) Math.round(house.getPrice()*.7) + " �meraudes", Arrays.asList("�8�tes-vous s�r de vouloir vendre votre maison ?"));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new HouseManagerUI(this.getHolder(), this.getHouse()));
	}

	@Override
	protected void yes() {
		this.getHouse().sell();
		this.close(false);
		rp.sendMessage("�aVous avez vendu votre maison ! Votre h�te s'est occup� du nettoyage.");
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

}
