package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.ritems.general.RItemStack;
import me.pmilon.RubidiaCore.ritems.general.RItemStacks;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;

public class ItemListUI extends ListMenuUIHandler<RItemStack> {

	public ItemListUI(Player p) {
		super(p, "Custom item list", "Liste des items personnalisés",5);
		this.list.addAll(RItemStacks.ITEMS);
	}

	@Override
	protected void onOpen() {
	}

	@Override
	protected ItemStack getItem(RItemStack e) {
		return e.getItemStack();
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack is) {
		if(is != null){
			int slot = e.getRawSlot();
			RItemStack rItem = this.get(slot);
			this.getHolder().getInventory().addItem(rItem.getItemStack());
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Cliquez sur un item pour en obtenir une copie.")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
	}

	@Override
	protected void onPageTurn() {
	}

	@Override
	public String getType() {
		return "ITEM_LIST_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

}
