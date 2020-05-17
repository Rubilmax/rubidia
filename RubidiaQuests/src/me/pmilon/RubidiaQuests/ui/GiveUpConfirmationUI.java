package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import org.bukkit.Bukkit;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.ConfirmationUI;
import me.pmilon.RubidiaQuests.events.RPlayerGiveUpQuestEvent;
import me.pmilon.RubidiaQuests.quests.Quest;

public class GiveUpConfirmationUI extends ConfirmationUI {

	public final Quest quest;
	public GiveUpConfirmationUI(RPlayer rp, Quest quest) {
		super(rp.getPlayer(), ("Abandonner une quête"),
				new String[]{("§aAbandonner la quête et perdre toute progression"),("§cGarder la quête et conserver la progression")},
				quest.getColoredTitle(),
				Arrays.asList(quest.getSubtitle(), "", ("§7Êtes-vous certain de vouloir abandonner cette quête ?")));
		this.quest = quest;
	}

	@Override
	protected void no() {
		Core.uiManager.requestUI(new PlayerQuestList(this.getHolder()));
	}

	@Override
	protected void yes() {
		RPlayerGiveUpQuestEvent event = new RPlayerGiveUpQuestEvent(this.rp, quest);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			RPlayer rp = event.getRPlayer();
			for(Quest quest : event.getQuest().giveUp(rp)){
				rp.sendMessage("§cVous avez abandonné la quête §4" + quest.getColoredTitle());
			}
			if(rp.isOnline())Core.uiManager.requestUI(new PlayerQuestList(rp.getPlayer()));
		}
	}

}
