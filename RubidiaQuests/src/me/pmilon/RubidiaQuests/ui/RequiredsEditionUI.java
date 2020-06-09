package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler;
import me.pmilon.RubidiaQuests.pnjs.PNJHandler.PNJType;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Required;
import me.pmilon.RubidiaQuests.quests.RequiredHolder;
import me.pmilon.RubidiaQuests.quests.RequiredType;

public class RequiredsEditionUI extends UIHandler {

	private RequiredHolder requiredHolder; //QUEST or GATE
	private PNJHandler pnj;
	private int SLOT_BACK = 17;
	public RequiredsEditionUI(Player p, RequiredHolder requiredHolder, PNJHandler pnj) {
		super(p);
		
		this.requiredHolder = requiredHolder;
		this.pnj = pnj;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate((this.getRequiredHolder() instanceof Quest ? ((Quest) this.getRequiredHolder()).getTitle() : " | Requireds"),32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "REQUIREDS_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK){
			if(this.getPnj().getType().equals(PNJType.QUEST))Core.uiManager.requestUI(new QuestEditionMenu(this.getHolder(), (Quest)this.getRequiredHolder(), (QuestPNJ) this.getPnj()));
		}else{
			Required required = null;
			for(Required rq : this.getRequiredHolder().getRequireds()){
				if(slot == rq.getIndex()){
					required = rq;
					break;
				}
			}
			if(required != null){
				if(e.isRightClick()){
					this.getRequiredHolder().delete(required);
					Core.uiManager.requestUI(new RequiredsEditionUI(this.getHolder(), this.getRequiredHolder(), this.getPnj()));
				}else Core.uiManager.requestUI(new RequiredEditionMenu(this.getHolder(), this.getRequiredHolder(), this.getPnj(), required));
			}else{
				required = new Required(this.getRequiredHolder().getUUID(), slot, RequiredType.LEVEL, new ItemStack(Material.STONE, 1), RClass.VAGRANT, RJob.JOBLESS, ((Quest)this.getRequiredHolder()).getUUID(), 0, Mastery.VAGRANT, this.getHolder().getWorld().getTime(), this.getHolder().getWorld().getTime(), false, "Required not filled", true);
				this.getRequiredHolder().getRequireds().add(required);
				Core.uiManager.requestUI(new RequiredEditionMenu(this.getHolder(), this.getRequiredHolder(), this.getPnj(), required));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}

	@Override
	protected boolean openWindow() {
		for(Required required : this.getRequiredHolder().getRequireds()){
			this.menu.setItem(required.getIndex(), this.getReq(required));
		}
		for(int i = 0;i < this.SLOT_BACK;i++){
			if(this.getMenu().getItem(i) == null || this.getMenu().getItem(i).getType().equals(Material.AIR))this.menu.setItem(i, this.getReq(null));
		}
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	protected ItemStack getReq(Required required){
		ItemStack item;
		if(required != null){
			item = new ItemStack(required.getType().getDisplay(), 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(required.getType().toString());
			meta.setLore(Arrays.asList(required.getInformation(), required.isDialog() ? required.getNoDialog() : "Not dialoging"));
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
			item.setItemMeta(meta);
		}else{
			item = new ItemStack(Material.SLIME_BALL, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("NOTHING");
			item.setItemMeta(meta);
		}
		return item;
	}

	public PNJHandler getPnj() {
		return pnj;
	}

	public void setPnj(PNJHandler pnj) {
		this.pnj = pnj;
	}

	public RequiredHolder getRequiredHolder() {
		return requiredHolder;
	}

	public void setRequiredHolder(RequiredHolder requiredHolder) {
		this.requiredHolder = requiredHolder;
	}


}
