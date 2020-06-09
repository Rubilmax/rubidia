package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.RManager.SPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaCore.utils.Configs;
import net.md_5.bungee.api.ChatColor;

public class SPlayerDeletionConfirmationUI extends ConfirmationUI {

	private int id;
	public SPlayerDeletionConfirmationUI(RPlayer rp, SPlayer sp, int id) {
		super(rp.getPlayer(), ("Suppression de personnage"), new String[]{("§aSupprimer le personnage et perdre toute progression"),
				("§cGarder le personnage et conserver la progression")},
				"§7§l" + (sp.getRClass().getName()),
				Arrays.asList("§8" + ("Niveau : ") + "§7§o" + sp.getRLevel(), "§8" + ("XP : ") + "§7§o" + sp.getRExp(), "§8" + ("Maîtrise : ") + "§7§o" + (sp.getMastery().getName()), "§8" + ("Métier : ") + "§7§o" + ChatColor.stripColor((sp.getRJob().getName())), "§8" + ("Points de compétence : ") + "§7§o" + sp.getSkp(), "§8" + ("Points de distinction : ") + "§7§o" + sp.getSkd(), "§8" + ("Force : ") + "§7§o" + sp.getStrength(), "§8" + ("Endurance : ") + "§7§o" + sp.getEndurance(), "§8" + ("Agilité : ") + "§7§o" + sp.getAgility(), "§8" + ("Intelligence : ") + "§7§o" + sp.getIntelligence(), "§8" + ("Perception : ") + "§7§o" + sp.getPerception(), "§8" + ("Meurtres : ") + "§7§o" + sp.getKills(), "§8" + ("Renom : ") + "§7§o" + sp.getRenom(), "", ("§e§oSouhaitez-vous vraiment supprimer ce personnage ?"), "", "§f§m----------------------------------------"));
		this.id = id;
	}
	
	@Override
	protected void yes() {
		rp.getSaves()[id] = null;
		Configs.getPlayerConfig().set("players." + rp.getUniqueId() + ".saves." + id, null);
		rp.sendMessage("§eVotre personnage a bien été supprimé.");
		this.close(false);
	}
	
	@Override
	protected void no() {
		Core.uiManager.requestUI(new SPlayerSelectionMenu(this.getHolder()));
	}

}
