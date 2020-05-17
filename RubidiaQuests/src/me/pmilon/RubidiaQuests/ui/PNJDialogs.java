package me.pmilon.RubidiaQuests.ui;

import org.bukkit.entity.Player;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaQuests.pnjs.DialogerPNJ;

public class PNJDialogs extends DialogsEditionMenu {

	private DialogerPNJ pnj;
	public PNJDialogs(Player p, DialogerPNJ dialogerPNJ) {
		super(p, dialogerPNJ.getName() + " | Edition", dialogerPNJ.getDialogs());
		this.pnj = dialogerPNJ;
	}
	
	@Override
	protected void saveDialogs() {
		this.getPnj().setDialogs(this.getDialogs());
	}
	@Override
	protected void back(Player player) {
		Core.uiManager.requestUI(new PNJSettings(this.getHolder(), this.getPnj()));
	}

	public DialogerPNJ getPnj() {
		return pnj;
	}

	public void setPnj(DialogerPNJ pnj) {
		this.pnj = pnj;
	}

}
