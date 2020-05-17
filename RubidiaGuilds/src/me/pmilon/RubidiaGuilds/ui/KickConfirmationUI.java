package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public class KickConfirmationUI extends ConfirmationUI {

	public final Guild guild;
	public final GMember member;
	public KickConfirmationUI(RPlayer rp, Guild guild, GMember kicked) {
		super(rp.getPlayer(), ("Ejection de ") + kicked.getName(),
				new String[]{("§aEjecter " + kicked.getName() + " de votre guilde"),("§cGarder " + kicked.getName() + " parmis les rangs de votre guilde")},
				"§7§l" + kicked.getName(), Arrays.asList("", ("§8Êtes-vous certain de vouloir éjecter ce joueur ?")));
		this.guild = guild;
		this.member = kicked;
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), guild, member, 3));
	}

	@Override
	protected void yes() {
		if(member.isOnline()){
			RPlayer.get(member).sendMessage("§cVous avez été éjecté de la guilde !");
		}
		guild.removeMember(member);
		guild.broadcastMessage(Relation.MEMBER, "§4" + member.getName() + " §ca été éjecté de la guilde !");
		this.getUIManager().requestUI(new GMembersUI(this.getHolder(), guild));
	}

}
