package me.pmilon.RubidiaCore.ui.abstracts;

import java.util.Arrays;
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
import me.pmilon.RubidiaCore.tasks.BukkitTask;


public abstract class ConfirmationUI extends UIHandler {

	private final String[] details;//shown over yes & no (size 2)
	private final String information;//title of book
	private final List<String> confirmation;//lore of book
	private boolean noOnLeave = true;
	public ConfirmationUI(Player p, String title, String[] details, String information, List<String> confirmation) {
		super(p);
		this.details = details;
		this.information = information;
		this.confirmation = confirmation;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(title,32));
	}

	@Override
	public String getType() {
		return "CONFIRMATION_MENU";
	}

	@Override
	protected boolean openWindow() {
		ItemStack ok = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
		ItemStack notok = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta meta = ok.getItemMeta();
		meta.setDisplayName(("§2§lOUI"));
		if(details.length > 1)meta.setLore(Arrays.asList(details[0]));
		ok.setItemMeta(meta);
		meta.setDisplayName(("§4§lNON"));
		if(details.length > 1)meta.setLore(Arrays.asList(details[1]));
		notok.setItemMeta(meta);
		for(int i = 0;i < 4;i++){
			this.menu.setItem(i, ok);
			this.menu.setItem(8-i, notok);
		}
		
		ItemStack quest = new ItemStack(Material.BOOK, 1);
		ItemMeta qmeta = quest.getItemMeta();
		qmeta.setDisplayName(information);
		qmeta.setLore(confirmation);
		quest.setItemMeta(qmeta);
		this.menu.setItem(4, quest);
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot < 4)yes();
		else if(slot != 4)no();
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(this.noOnLeave) {
			new BukkitTask(Core.instance) {//must be synchronous because elsehow: player leave inventory so UIHandler stop listening to player while we reopen an inventory (for some no() methods)

				@Override
				public void run() {
					no();
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(0);
		}
	}
	
	protected abstract void yes();
	protected abstract void no();

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	public boolean isNoOnLeave() {
		return noOnLeave;
	}

	public void setNoOnLeave(boolean noOnLeave) {
		this.noOnLeave = noOnLeave;
	}

}
