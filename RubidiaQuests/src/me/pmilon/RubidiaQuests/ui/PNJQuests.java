package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.quests.Reward;

public class PNJQuests extends UIHandler {

	private QuestPNJ pnj;
	public PNJQuests(Player p, QuestPNJ pnj) {
		super(p);
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(pnj.getName() + " | Quests",32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "PNJ_QUESTS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == 8)Core.uiManager.requestUI(new PNJSettings(this.getHolder(), this.getPnj()));
		else{
			List<Quest> quests = this.getPnj().getQuests();
			if(quests.size() > slot){
				if(e.isRightClick()){
					quests.remove(quests.get(slot));
					this.getPnj().setQuests(quests);
					Core.uiManager.requestUI(new PNJQuests(this.getHolder(), this.getPnj()));
				}else Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), this.getPnj().getQuests().get(slot), this.getPnj()));
			}else Core.uiManager.requestUI(new PreQuestEditionUI(this.getHolder(), this.getPnj()));
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < 8;i++){
			ItemStack item;
			if(this.getPnj().getQuests().size() > i){
				Quest quest = this.getPnj().getQuests().get(i);
				item = new ItemStack(Material.BOOK, 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(quest.getColoredTitle());
				List<String> lore = new ArrayList<String>();
				lore.add(quest.getColoredSubtitle());
				lore.add("§7§lObjectifs");
				for(Objective objective : quest.getObjectives()){
					lore.add("§8" + objective.getType().getToDo() + objective.getInformation());
				}
				lore.add("§4§lPré-requis");
				for(Required required : quest.getRequireds()) {
					lore.add("§c" + required.getInformation());
				}
				lore.add("§2§lRécompenses");
				for(Reward reward : quest.getRewards()) {
					lore.add("§a" + reward.getInfos());
				}
				item.setItemMeta(meta);
			}else{
				item = new ItemStack(Material.WRITABLE_BOOK, 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("No Quest");
				item.setItemMeta(meta);
			}
			this.menu.setItem(i, item);
		}
		this.menu.setItem(8, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}

	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
}
