package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;

public class DuelCompetitiveUI extends ConfirmationUI {

	private RPlayer rpfrom;
	public DuelCompetitiveUI(RPlayer rp, RPlayer rpfrom) {
		super(rp.getPlayer(), ("Duel compétitif disponible"), new String[]{}, "§7§l" + ("Duel compétitif"), Arrays.asList("§8" + ("Vous avez " + (Math.abs(rp.getRLevel()-rpfrom.getRLevel())) + " niveaux d'écart avec"), "§8" + rpfrom.getName() + ". " + ("Il est loyal de mettre votre renom en jeu."), "§8" + ("Souhaitez-vous proposer un duel compétitif ?")));
		this.rpfrom = rpfrom;
		this.setNoOnLeave(false);
	}

	@Override
	protected void yes() {
		rp.requestDuel(rpfrom,true);
		this.close(false);
	}

	@Override
	protected void no() {
		rp.requestDuel(rpfrom,false);
		this.close(false);
	}

}
