package me.pmilon.RubidiaCore.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.couples.CBuff;
import me.pmilon.RubidiaCore.couples.Couple;
import me.pmilon.RubidiaCore.couples.CoupleColl;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoupleMenu extends UIHandler {
	
	public static Material[] DYES = new Material[]{Material.ORANGE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE,
			Material.BLUE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE,
			Material.BROWN_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE};
	public static String[] COLOR_CODES = new String[]{"4", "6", "e", "a", "2", "b", "9", "5"};

	private Couple couple;
	private int SLOT_DIVORCE = 8;
	public CoupleMenu(Player p, Couple couple) {
		super(p);
		this.couple = couple;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(("Gestion du mariage"), 32));
	}

	@Override
	public String getType() {
		return "COUPLE_MENU";
	}

	@Override
	protected boolean openWindow() {
		for(int i = 0;i < CoupleColl.buffs.size();i++){
			CBuff buff = CoupleColl.buffs.get(i);
			boolean locked = this.getCouple().getXPTime() < buff.getXpTime();
			ItemStack item = new ItemStack( locked ? Material.LIGHT_GRAY_STAINED_GLASS_PANE : DYES[i], 1);
			ItemMeta meta = item.getItemMeta();
			String[] name = buff.getName().split("-");
			meta.setDisplayName("§" + (locked ? "7" : COLOR_CODES[i]) + "§l❤ " + (name[1]) + " ❤");
			
			long time2 = locked ? buff.getXpTime()-this.getCouple().getXPTime() : 0L;
			long days2 = TimeUnit.MILLISECONDS.toDays(time2);
			time2 -= TimeUnit.DAYS.toMillis(days2);
			long hours2 = TimeUnit.MILLISECONDS.toHours(time2);
			time2 -= TimeUnit.HOURS.toMillis(hours2);
			long minutes2 = TimeUnit.MILLISECONDS.toMinutes(time2);
			time2 -= TimeUnit.MINUTES.toMillis(minutes2);
			
			List<String> lore = new ArrayList<String>();
			lore.add("§8" + (buff.getLevel() >= 0 ? "+" : "") + buff.getLevel() + "% " + (buff.getType().getDisplayFr()));
			if(locked)lore.add(("§8Temps d'activation : ") + String.format("§7%02d §8" + ("j.") + " §7%02d §8h. §7%02d §8min.", days2, hours2, minutes2));
			meta.setLore(lore);
			item.setItemMeta(meta);
			this.getMenu().setItem(i, item);
		}
		this.getMenu().setItem(this.SLOT_DIVORCE, this.getDivorce());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_DIVORCE) {
			Core.uiManager.requestUI(new DivorceConfirmationUI(rp));
		}
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	public ItemStack getDivorce(){
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7§lInformations");
		long time1 = this.getCouple().getXPTime();
		long days1 = TimeUnit.MILLISECONDS.toDays(time1);
		time1 -= TimeUnit.DAYS.toMillis(days1);
		long hours1 = TimeUnit.MILLISECONDS.toHours(time1);
		time1 -= TimeUnit.HOURS.toMillis(hours1);
		long minutes1 = TimeUnit.MILLISECONDS.toMinutes(time1);
		time1 -= TimeUnit.MINUTES.toMillis(minutes1);
		
		CBuff buff = this.getCouple().getNextLockedBuff();
		long time2 = buff != null ? buff.getXpTime()-this.getCouple().getXPTime() : 0L;
		long days2 = TimeUnit.MILLISECONDS.toDays(time2);
		time2 -= TimeUnit.DAYS.toMillis(days2);
		long hours2 = TimeUnit.MILLISECONDS.toHours(time2);
		time2 -= TimeUnit.HOURS.toMillis(hours2);
		long minutes2 = TimeUnit.MILLISECONDS.toMinutes(time2);
		time2 -= TimeUnit.MINUTES.toMillis(minutes2);
		Date date = new Date(this.getCouple().getWeddingDate());
		meta.setLore(Arrays.asList(("§8Votre date de mariage : §7") + new SimpleDateFormat("dd/MM/yyyy").format(date) + (" §8à §7") + new SimpleDateFormat("HH:mm").format(date), ("§8Votre temps total passé ensemble connectés : ") + String.format("§7%02d §8" + ("j.") + " §7%02d §8h. §7%02d §8min.", days1, hours1, minutes1), ("§8Temps nécessaire à l'activation du prochain buff : ") + String.format("§7%02d §8" + ("j.") + " §7%02d §8h. §7%02d §8min.", days2, hours2, minutes2), "", ("§c§oCliquez ici pour divorcer avec §4§o" + this.getCouple().getCompanion(rp).getName())));
		item.setItemMeta(meta);
		return item;
	}

	public Couple getCouple() {
		return couple;
	}

	public void setCouple(Couple couple) {
		this.couple = couple;
	}

}
