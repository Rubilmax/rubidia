package me.pmilon.RubidiaQuests.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.entity.Player;

public class QuestCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 2){
			Quest quest = Quest.get(args[1]);
			if(quest != null){
				if(DialogManager.isInDialog(player)){
					if(!rp.getActiveQuests().contains(quest) && (!rp.getDoneQuests().contains(quest) || quest.isRedonable()) && quest.isAvailable(rp)){
					   	rp.getChat().forceCloseFixDisplay();
					   	rp.getChat().clearPNJMessages();
						DialogManager.setNoDialog(player);
						if(args[0].equals("yes")){
							if(quest.accept(rp, args[2])){
								rp.sendMessage("§aVous avez accepté la quête !");
							}else return;
						}else if(args[0].equals("no")){
							rp.sendMessage("§cVous avez refusé la quête.");
						}
					}
				}
			}
		}
	}

}
