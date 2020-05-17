package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;

public class DivorceConfirmationUI extends ConfirmationUI {

	public DivorceConfirmationUI(RPlayer rp) {
		super(rp.getPlayer(), "Divorce", new String[]{("§aDivorcer avec " + rp.getCouple().getCompanion(rp).getName() + " et perdre tous les buffs"),("§cRester marié à " + rp.getCouple().getCompanion(rp).getName())},
				"§7§l" + rp.getCouple().getCompanion(rp).getName(), Arrays.asList(("§8Êtes-vous sûr de vouloir divorcer ?")));
	}

	@Override
	protected void yes() {
		if(rp.getCouple() != null){
			rp.getCouple().divorce(rp);
		}
		this.close(false);
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new CoupleMenu(rp.getPlayer(), rp.getCouple()));
	}

}
