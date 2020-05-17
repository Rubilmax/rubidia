package me.pmilon.RubidiaQuests.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.pmilon.RubidiaCore.commands.abstracts.HybridAdminCommandExecutor;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.quests.Quest;

public class QuestProblemsCommandExecutor extends HybridAdminCommandExecutor {

	@Override
	public void onAdminCommand(CommandSender sender, String[] args) {
		List<String> problems = new ArrayList<String>();
		for(Quest quest : QuestsPlugin.questColl.data()){
			String problem = quest.getProblem();
			if(!problem.isEmpty()){
				problems.add(quest.getColoredTitle() + ": " + quest.getColoredSubtitle() + " §f> " + problem);
			}
		}
		for(String problem : problems){
			sender.sendMessage(problem);
		}
	}

}
