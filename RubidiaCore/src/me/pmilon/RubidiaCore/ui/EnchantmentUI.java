package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.EconomyHandler;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.weapons.REnchantment;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.RandomUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentUI extends UIHandler {

	RPlayer rp = RPlayer.get(this.getHolder());
	private final ItemStack item;
	private int[] costs = new int[4];
	private final List<Enchantment> available = new ArrayList<Enchantment>();
	private final Location location;
	
	public EnchantmentUI(Location location, Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, ("Enchantement"));
		this.item = this.getHolder().getEquipment().getItemInMainHand().clone();
		this.location = location;
		for(int i = 0;i < 4;i++) {
			costs[i] = REnchantment.cost(this.getHolder(), this.item, i);
		}
	}
	
	@Override
	public String getType(){
		return "ENCHANTMENT_MENU";
	}

	@Override
	protected boolean openWindow() {
		RItem rItem = new RItem(this.item);
		ItemMeta meta = this.item.getItemMeta().clone();
		Map<Enchantment, Integer> enchantms = meta.getEnchants();
		int bLevel = this.getMaxBookshelfLevel();
		boolean canEnchant = false;

		for(Enchantment enchant : REnchantment.values()){
			if((!rItem.isWeapon() || !enchant.equals(Enchantment.DURABILITY)) && !enchant.equals(Enchantment.MENDING)){
				if(enchant.canEnchantItem(this.item) || rItem.isWeapon() && rItem.getWeapon().isAttack() && REnchantment.WEAPONS_ENCHANTMENTS.contains(enchant)){
					canEnchant = true;
					if(!enchantms.containsKey(enchant)){
						available.add(enchant);
					}
				}
			}
		}
		if(!canEnchant || me.pmilon.RubidiaQuests.utils.Utils.isQuestItem(this.item)){
			rp.sendMessage("§cVous ne pouvez pas enchanter cet item !");
			this.getUIManager().playerSessions.remove(this.getUIManager().getSession(this.getHolder()).getIdentifier());
			return false;
		}else if(available.isEmpty()){
			rp.sendMessage("§cCet item est déjà complètement enchanté !");
			return false;
		}
		ItemStack itemb = this.item.clone();
		for(int i = 1;i < 5;i++){
			if(bLevel >= i){
				meta.setDisplayName(("§aEnchantement aléatoire | §aPallier §2" + i));
				meta.setLore(Arrays.asList("§8§m                                               ", "", ("§7Enchantez cet item aléatoirement"), ("§7en cliquant dessus."), ("§7Enchantements niveau " + (i == 4 ? "4-5" : (i == 3 ? "2-5" : (i == 2 ? "1-3" : "1-2"))) + " (max)"), "", ("§cCoût : §4" + this.getCost(i) + "§c émeraudes")));
				itemb.setItemMeta(meta);
				itemb.setAmount(i);
				this.getMenu().setItem(i*2-1, itemb);
			}else this.getMenu().setItem(i*2-1, this.getNo(i));
		}
		this.getHolder().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg0) {
		Enchantment enchant = available.get(RandomUtils.random.nextInt(available.size()));
	    int level = this.getLevel(e.getRawSlot());
	    int elvl = this.getEnchantmentIntervalLevel(level);
		int bLevel = this.getMaxBookshelfLevel();
		if(level > bLevel){
			rp.sendMessage("§cVous devez positionner " + level*9 + " bibliothèques autour de la table d'enchantement pour débloquer ce pallier !");
			e.setCancelled(true);
			return;
		}
		
		if(rp.getBank() >= this.getCost(level)){
			EconomyHandler.withdraw(this.getHolder(), this.getCost(level));
			if(elvl > enchant.getMaxLevel())elvl = enchant.getMaxLevel();
			ItemMeta meta = this.item.getItemMeta();
			meta.addEnchant(enchant, elvl, true);
			RItem rItem = new RItem(this.item);
			if(rItem.isWeapon())rItem.getWeapon().updateState(rp, this.item);
			else{
				for(Enchantment ench : meta.getEnchants().keySet()){
					if(ench.equals(REnchantment.SOUL_BIND)){
						int lvl = meta.getEnchants().get(ench);
						String name = "§7Liaison spirituelle " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : lvl == 5 ? "V" : "???");
						if(meta.hasLore()){
							boolean found = false;
							List<String> lore = Utils.getModifiableCopy(meta.getLore());
							for(int i = 0;i < lore.size();i++){
								if(lore.get(i).contains("Liaison spirituelle")){
									lore.set(i, name);
									found = true;
									break;
								}
							}
							if(!found){
								for(int i = lore.size()-1;i >= 0;i--){
									if(lore.size() > i+1)lore.set(i+1, lore.get(i));
									else lore.add(lore.get(i));
								}
								lore.set(0, name);
							}
							meta.setLore(lore);
						}else meta.setLore(Arrays.asList(name));
						break;
					}
				}
			}
			this.item.setItemMeta(meta);
			this.close(false);
			rp.sendMessage("§aD'étranges puissances ont pris le contrôle de votre item !");
		}else rp.sendMessage("§cVous n'avez pas assez d'émeraudes dans votre banque !");
		this.close(false);
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player arg0) {
		ItemStack item = this.getHolder().getEquipment().getItemInMainHand();
		if(item == null || item.getType().equals(Material.AIR)) {
			this.getHolder().getEquipment().setItemInMainHand(this.item);
		}else {
			this.getHolder().getInventory().addItem(this.item);
		}
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}
	
	
	private int getCost(int level){
		if(level < 4)return this.costs[level-1];
		return 0;
	}
	private int getLevel(int slot){
		return (int) Math.round(slot/2.);
	}
	private int getEnchantmentIntervalLevel(int level){
		if(level == 1)return RandomUtils.random.nextInt(2)+1;
		else if(level == 2)return RandomUtils.random.nextInt(3)+1;
		else if(level == 3)return RandomUtils.random.nextInt(3)+2;
		else if(level >= 4)return RandomUtils.random.nextInt(2)+4;
		return 0;
	}
	
	private int getMaxBookshelfLevel(){
		int bookshelfs = 0;
		for(int x = -5;x <= 5;x++){
			for(int y = -5;y <= 5;y++){
				for(int z = -5;z <= 5;z++){
					if(this.getLocation().clone().add(x,y,z).getBlock().getType().equals(Material.BOOKSHELF)){
						bookshelfs += 1;
					}
				}
			}
		}
		return (int) ((double) bookshelfs/9.0D);
	}
	
	private ItemStack getNo(int level){
		ItemStack stack = new ItemStack(Material.BARRIER, level);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(("§4Pallier bloqué - Enchantements n." + (level == 4 ? "3-5" : (level == 3 ? "2-4" : (level == 2 ? "1-3" : "1-2")))));
		meta.setLore(Arrays.asList(("§cVous avez besoin de positionner " + (level*9) + " bibliothèques"), ("§cautour de cette table pour débloquer ce pallier !")));
		stack.setItemMeta(meta);
		return stack;
	}

	public Location getLocation() {
		return location;
	}
}
