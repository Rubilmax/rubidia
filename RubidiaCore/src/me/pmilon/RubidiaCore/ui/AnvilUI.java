package me.pmilon.RubidiaCore.ui;

import java.util.Arrays;
import java.util.Random;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ritems.weapons.REnchantment;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilUI extends UIHandler {

	RPlayer rp = RPlayer.get(this.getHolder());
	private final ItemStack item;
	private final ItemStack item0;
	private final ItemStack item1;
	private final Block block;
	private final int cost;
	private int ITEM_0_SLOT = 2, ITEM_1_SLOT = 3, FINAL_ITEM_SLOT = 6;
	private boolean DONE = false;
	
	public AnvilUI(Player p, Block anvil, ItemStack is_0, ItemStack is_1, ItemStack is_2) {
		super(p);
		this.setMenu(Bukkit.createInventory(this.getHolder(), 9, ("Forge")));
		this.item0 = is_0;
		this.item1 = is_1;
		this.item = is_2;
		this.block = anvil;
		ItemMeta im = is_2.getItemMeta();
		int cost = 0;
		if(im.hasDisplayName()) {
			if(rp.isVip())cost += 6;
			else cost += 10;
		}
		if(im.hasEnchants()){
			cost += REnchantment.cost(p, is_2, 2);
		}
		this.cost = cost;
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "ANVIL_MENU";
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(this.ITEM_0_SLOT, item0);
		this.getMenu().setItem(this.ITEM_1_SLOT, item1);
		this.getMenu().setItem(FINAL_ITEM_SLOT, this.item);
		this.getMenu().setItem(0, this.getInfos());
		this.getMenu().setItem(8, this.getInfos());
		this.getMenu().setItem(1, this.getItemInfos());
		this.getMenu().setItem(4, this.getItemInfos());
		this.getMenu().setItem(5, this.getFinalInfos());
		this.getMenu().setItem(7, this.getFinalInfos());
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	private ItemStack getFinalInfos() {
		ItemStack is = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(("§aItem final"));
		meta.setLore(Arrays.asList("§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-", ("§7Confirmez la modification"), ("§7en cliquant dessus."), ("§cCoût : §4" + this.cost + "§c émeraudes"), "§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-§e-§6-"));
		is.setItemMeta(meta);
		return is;
	}

	private ItemStack getItemInfos() {
		ItemStack is = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(("§4Items de base"));
		meta.setLore(Arrays.asList(("§cItems utilisés pour la modification")));
		is.setItemMeta(meta);
		return is;
	}

	private ItemStack getInfos() {
		ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(("§8Forge"));
		meta.setLore(Arrays.asList(("§7Menu de validation")));
		is.setItemMeta(meta);
		return is;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == FINAL_ITEM_SLOT || slot == FINAL_ITEM_SLOT-1 || slot == FINAL_ITEM_SLOT+1){
			RPlayer rp = RPlayer.get(p);
			if(this.block.getType().toString().contains("ANVIL")) {
				if(rp.getBank() >= this.cost){
					EconomyHandler.withdraw(this.getHolder(), this.cost);
					p.getInventory().addItem(this.item);
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 3, 3);
					Random r = new Random();
					if(r.nextInt(4) == 0){
						if(this.block.getType().equals(Material.ANVIL)){
							this.block.setType(Material.CHIPPED_ANVIL);
						}else if(this.block.getType().equals(Material.CHIPPED_ANVIL)){
							this.block.setType(Material.DAMAGED_ANVIL);
						}else{
							this.block.setType(Material.AIR);
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 3, 3);
						}
					}
					DONE = true;
				}else rp.sendMessage("§cVous n'avez pas assez d'émeraudes dans votre banque !");
			}else rp.sendMessage("§cLa forge a disparue !");
			this.close(false);
		}else if(slot != 0 && slot != 8)this.closeUI();
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, final Player p) {
		if(!DONE){
			if(this.item0 != null)p.getInventory().addItem(this.item0);
			if(this.item1 != null)p.getInventory().addItem(this.item1);
		}else{
			if(this.item1 != null){
				if(this.item1.getAmount() > 1){
					this.item1.setAmount(this.item1.getAmount()-1);//because item in slot 1 is the dominating
					p.getInventory().addItem(this.item1);
				}
			}
		}
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
}
