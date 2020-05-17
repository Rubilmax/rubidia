package me.pmilon.RubidiaQuests.commands;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerAdminCommandExecutor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QuestItemCommandExecutor extends PlayerAdminCommandExecutor{

	public static final String QUEST_STRING = "§eItem de quête";

	@Override
	public void onAdminCommand(Player player, RPlayer rp, String[] args) {
		ItemStack inHand = player.getEquipment().getItemInMainHand();
		if(inHand != null){
			if(!inHand.getType().equals(Material.AIR)){
				ItemMeta meta = inHand.getItemMeta();
				List<String> lore = meta.getLore();
				if(lore != null){
					if(!lore.isEmpty()){
						if(!lore.get(0).equals(QuestItemCommandExecutor.QUEST_STRING)){
							for(int i = (lore.size()-1);i >= 0;i--){
								String s = lore.get(i);
								if(i == (lore.size()-1))lore.add(s);
								else lore.set(i+1, s);
							}
							lore.set(0, QuestItemCommandExecutor.QUEST_STRING);
						}else player.sendMessage("§cAlready quest item");
					}else lore.add(QuestItemCommandExecutor.QUEST_STRING);
				}else{
					lore = new ArrayList<String>();
					lore.add(QuestItemCommandExecutor.QUEST_STRING);
				}
				meta.setLore(lore);
				inHand.setItemMeta(meta);
				player.getEquipment().setItemInMainHand(inHand);
			}else player.sendMessage("§cQuest item with air ?");
		}else player.sendMessage("§cQuest item with air ?");
	}
}
