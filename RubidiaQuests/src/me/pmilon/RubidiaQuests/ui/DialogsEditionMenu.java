package me.pmilon.RubidiaQuests.ui;

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
import me.pmilon.RubidiaQuests.dialogs.MessageManager;

public abstract class DialogsEditionMenu extends UIHandler {

	private List<String> dialogs;
	
	private int SLOT_BACK = 8;
	public DialogsEditionMenu(Player p, String title, List<String> dialogs) {
		super(p);
		this.dialogs = dialogs;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(title, 32));
	}
	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	@Override
	public String getType() {
		return "DIALOGS_EDITION_MENU";
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
		//
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_BACK)this.back(p);
		else if(e.isRightClick()){
			if(this.dialogs.size() > slot){
				this.dialogs.remove(slot);
				Core.uiManager.requestUI(this);
			}
		} else this.close(true, slot);
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		//
	}
	
	@Override
	protected boolean openWindow() {
		List<String> dialogs = this.getDialogs();
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				String dialog = MessageManager.filterDialog(this.getMessage());
				
				if(dialog.startsWith("--")){
					if(dialogs.size() > this.getListeningId()-1){
						dialogs.set(this.getListeningId()-1, dialogs.get(this.getListeningId()-1) + dialog.replaceFirst("--", ""));
					}else dialogs.add(dialog.replaceFirst("--", ""));
				}else{
					if(dialogs.size() > this.getListeningId()){
						dialogs.set(this.getListeningId(), dialog);
					}else dialogs.add(dialog);
				}
			}
		}
		for(int i = 0;i < 8;i++){
			ItemStack dialogis = new ItemStack(Material.BOOK, 1);
			ItemMeta meta = dialogis.getItemMeta();
			if(dialogs.size() > i)meta.setDisplayName(dialogs.get(i));
			else meta.setDisplayName("");
			dialogis.setItemMeta(meta);
			this.menu.setItem(i, dialogis);
		}
		this.setDialogs(dialogs);
		this.saveDialogs();
		
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	public List<String> getDialogs(){
		return this.dialogs;
	}
	
	public void setDialogs(List<String> dialogs){
		this.dialogs = dialogs;
	}
	
	protected abstract void saveDialogs();
	
	protected abstract void back(Player player);
	
	protected ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}

}
