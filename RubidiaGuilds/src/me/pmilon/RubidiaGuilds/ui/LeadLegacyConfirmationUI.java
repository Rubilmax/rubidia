package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Rank;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public class LeadLegacyConfirmationUI extends ConfirmationUI {

	public final Guild guild;
	public final GMember member;
	public LeadLegacyConfirmationUI(RPlayer rp, Guild guild, GMember leader) {
		super(rp.getPlayer(), ("Gestion de la direction"),
				new String[]{("§aLéguer la direction de votre guilde à " + leader.getName()),("§cConserver la direction de votre guilde")},
				leader.getName(), Arrays.asList("", ("§7Êtes-vous certain de vouloir léguer la direction ?")));
		this.guild = guild;
		this.member = leader;
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), guild, member, 3));
	}

	@Override
	protected void yes() {
		Rank rank = guild.getRanks()[6];
		for(int i = 1;i < 7;i++){
			Rank rk = guild.getRanks()[i];
			if(rk != null){
				rank = rk;
			}
		}
		guild.getLeader().setRank(rank);
		member.setRank(guild.getRanks()[0]);
		guild.broadcastAllMessage("§&d" + member.getName() + " §&ca léguer la direction à §&d" + member.getName() + " §&c!");
		guild.broadcastMessage(Relation.MEMBER, "§2" + member.getName() + " §aa léguer la direction à §2" + member.getName() + " §a!");
		guild.broadcastAllyMessage("§5" + member.getName() + " §da léguer la direction à §5" + member.getName() + " §d!");
		Core.uiManager.requestUI(new GMemberPrefsUI(this.getHolder(), guild, member, 3));
	}
}
