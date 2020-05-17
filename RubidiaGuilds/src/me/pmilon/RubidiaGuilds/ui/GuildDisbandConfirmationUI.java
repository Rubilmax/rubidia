package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.bukkit.Bukkit;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaGuilds.events.GMemberDisbandGuildEvent;
import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GuildDisbandConfirmationUI extends ConfirmationUI {

	private final Guild guild;
	public GuildDisbandConfirmationUI(RPlayer rp, Guild guild) {
		super(rp.getPlayer(), ("Dissolution de guilde"), new String[]{("§aDissoudre la guilde et perdre toute sa progression"),("§cGarder la guilde et conserver sa progression")},
				"§7§l" + ("Confirmation de dissolution"), Arrays.asList(("§8Êtes-vous certain de vouloir dissoudre cette guilde ?")));
		this.guild = guild;
	}

	@Override
	protected void yes() {
		GMemberDisbandGuildEvent event = new GMemberDisbandGuildEvent(this.getGuild(), gm);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			event.getGuild().disband();
			this.close(false);
			rp.sendMessage("§cVous avez dissout §4§l" + event.getGuild().getName() + "§c.");
		}
		this.close(false);
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new GMenuUI(this.getHolder(), this.getGuild()));
	}

	public Guild getGuild() {
		return guild;
	}

}
