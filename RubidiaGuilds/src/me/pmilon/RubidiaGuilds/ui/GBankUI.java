package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import me.pmilon.RubidiaGuilds.guilds.Guild;

public class GBankUI extends UIHandler {
	
	private Guild guild;
	private int SLOT_MELT = 8;
	public GBankUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, this.getGuild().getName() + " : " + ("Banque de guilde"));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "GUILD_BANK_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player arg1) {
		if(e.isShiftClick()) {
			ItemStack item = e.getCurrentItem();
			if(item != null){
				if(!item.getType().equals(Material.EMERALD) && !item.getType().equals(Material.EMERALD_BLOCK)){
					e.setCancelled(true);
				}else this.menu.setItem(this.SLOT_MELT, this.getMelt());
			}
		}
	}

	@Override
	public void onInventoryClick(final InventoryClickEvent e, Player p) {
		int slot = e.getRawSlot();
		if(slot == this.SLOT_MELT){
			e.setCancelled(true);
			int count = this.count();
			if(this.getGuild().getBank() + count > this.getGuild().getMaxBankAmount()) {
				rp.sendMessage("§cIl n'y a pas assez de place pour toutes ces émeraudes dans votre banque de guilde!");
			}else {
				for(int i = 0;i < 8;i++) {
					if(!this.getMenu().getItem(i).getType().equals(Material.EMERALD) && !this.getMenu().getItem(i).equals(Material.EMERALD_BLOCK)){
						this.getHolder().getInventory().addItem(this.getMenu().getItem(i));
					}
					this.getMenu().setItem(i, new ItemStack(Material.AIR));
				}
				EconomyHandler.deposit(this.getHolder(), count);
			}
		}
		this.menu.setItem(this.SLOT_MELT, this.getMelt());
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		for(int i =0;i < 8;i++) {
			ItemStack item = this.getMenu().getItem(i);
			if(item != null) {
				this.getHolder().getInventory().addItem(item);
			}
		}
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_MELT, this.getMelt());
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	private ItemStack getMelt(){
		int count = this.count();
		ItemStack item = new ItemStack(Material.LAVA_BUCKET,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6§l" + ("Fonderie de guilde"));
		meta.setLore(Arrays.asList("§7" + ("Envoyer ces matériaux à la fonderie de la guilde"),
				"§7" + ("et ajouter §a" + count + " §7émeraude" + (count > 1 ? "s" : "") + " à votre banque de guilde."),
				"", "§f§l" + ("Solde actuel : §7" + this.getGuild().getBank() + (this.getGuild().getMaxBankAmount() == -1 ? "⟡" : "§8/" + this.getGuild().getMaxBankAmount()))));
		item.setItemMeta(meta);
		return item;
	}
	
	private int count() {
		int amount = 0;
		for(int i = 0;i < 8;i++) {
			ItemStack item = this.getMenu().getItem(i);
			if(item != null) {
				amount += item.getType().equals(Material.EMERALD_BLOCK) ? 9 : (item.getType().equals(Material.EMERALD) ? 1 : 0);
			}
		}
		return amount;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}
}
