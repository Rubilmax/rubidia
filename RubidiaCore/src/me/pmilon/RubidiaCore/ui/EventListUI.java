package me.pmilon.RubidiaCore.ui;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.REvents.Event;
import me.pmilon.RubidiaCore.REvents.Events;
import me.pmilon.RubidiaCore.REvents.Event.EventType;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;

public class EventListUI extends ListMenuUIHandler<Event> {

	public EventListUI(Player p) {
		super(p, "Current event list", "Evènements en cours", 1);
		for(Event event : Events.currentEvents){
			this.list.add(event);
		}
	}

	@Override
	protected void onOpen() {
	}

	@Override
	protected ItemStack getItem(Event e) {
		ItemStack item = new ItemStack(e.getType().getMaterial(),1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§5§l" + e.getType().toString());
		meta.setLore(Arrays.asList("§d" + e.getSubtitle(), "§7" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(e.getStartDate())) + "  §8>>  §7" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(e.getStartDate() + e.getDuration())), String.valueOf("§8Factor: §7" + e.getFactor()), e.isActive() ? "§2En cours..." : (e.getStartDate() > System.currentTimeMillis() ? "§6Débutera automatiquement" : "§4Terminé")));
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack is) {
		int slot = e.getRawSlot();
		Core.uiManager.requestUI(new EventManager(this.getHolder(), Events.currentEvents.get(slot)));
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(("§8§lListe des évènements en cours"));
		meta.setLore(Arrays.asList(("§7Cliquez gauche sur un évènement pour le gérer"), ("§7ou cliquez ici pour en créer un nouveau")));
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Event event = new Event(EventType.XP, "Evènement en préparation", System.currentTimeMillis() + 24*60*60*1000L, 1*60*60*1000L, 1.0, false);
		Events.currentEvents.add(event);
		Core.uiManager.requestUI(new EventManager(this.getHolder(), event));
	}

	@Override
	protected void onPageTurn() {
	}

	@Override
	public String getType() {
		return "EVENT_LIST_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

}
