package me.pmilon.RubidiaMonsters.ui.monsters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.attacks.AbstractAttack;
import me.pmilon.RubidiaMonsters.attacks.AbstractAttack.AttackType;
import me.pmilon.RubidiaMonsters.regions.Drop;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaMonsters.utils.Configs;

public class MonsterEditionMenu extends UIHandler {

	private Region region;
	private Monster monster;
	
	private int SLOT_NAME = 0, SLOT_TYPE = 1, SLOT_XPF = 2, SLOT_HPF = 3, SLOT_DF = 4, SLOT_AVR = 5, SLOT_DROPS = 6, SLOT_BACK = 7, SLOT_DELETE = 8;
	private int LIST_ID_NAME = 1, LIST_ID_TYPE = 2, LIST_ID_XPF = 3, LIST_ID_HPF = 4, LIST_ID_DF = 5;
	public MonsterEditionMenu(Player p, Region region, Monster monster) {
		super(p);
		this.region = region;
		this.monster = monster;
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "MONSTER_EDITION_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent arg0, Player arg1) {
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player arg1) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_NAME)this.close(true, this.LIST_ID_NAME);
		else if(slot == this.SLOT_TYPE)this.close(true, this.LIST_ID_TYPE);
		else if(slot == this.SLOT_XPF){
			if(!this.getMonster().isAverage()){
				this.close(true, this.LIST_ID_XPF);
			}else rp.sendMessage("§cCe monstre est average ! Désactivez cette option pour modifier ses statistiques.");
		}else if(slot == this.SLOT_HPF){
			if(!this.getMonster().isAverage()){
				this.close(true, this.LIST_ID_HPF);
			}else rp.sendMessage("§cCe monstre est average ! Désactivez cette option pour modifier ses statistiques.");
		}else if(slot == this.SLOT_DF){
			if(!this.getMonster().isAverage()){
				this.close(true, this.LIST_ID_DF);
			}else rp.sendMessage("§cCe monstre est average ! Désactivez cette option pour modifier ses statistiques.");
		}else if(slot == this.SLOT_AVR){
			this.getMonster().setAverage(!this.getMonster().isAverage());
			this.getMenu().setItem(this.SLOT_AVR, this.getAverage());
			if(this.getMonster().isAverage()){
				double level = (this.getRegion().getMinLevel()+this.getRegion().getMaxLevel())/2.0;
				this.getMonster().setHealthFactor(me.pmilon.RubidiaMonsters.utils.Utils.getHPFactor(level));
				this.getMonster().setXPFactor(me.pmilon.RubidiaMonsters.utils.Utils.getXPFactor(level));
				this.getMonster().setDamagesFactor(me.pmilon.RubidiaMonsters.utils.Utils.getDFactor(level));
				this.menu.setItem(this.SLOT_XPF, this.getXpf());
				this.menu.setItem(this.SLOT_HPF, this.getHpf());
				this.menu.setItem(this.SLOT_DF, this.getDf());
			}
		}else if(slot == this.SLOT_DROPS)Core.uiManager.requestUI(new MonsterDropsEditionUI(this.getHolder(), this.getRegion(), this.getMonster()));
		else if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new MonstersUI(this.getHolder(), this.getRegion()));
		else if(slot == this.SLOT_DELETE){
			Monsters.monsters.remove(this.getMonster());
			for(Region region : Regions.regions){
				region.getMonsters().remove(this.getMonster());
			}
			Configs.getMonstersConfig().set("monsters." + this.getMonster().getUUID(), null);
			Configs.saveMonstersConfig();
			Core.uiManager.requestUI(new MonstersUI(this.getHolder(), this.getRegion()));
		}else if(slot > 8){
			if(this.getMonster().getAttacks().size() <= slot-9){
				AbstractAttack attack = new AbstractAttack(AttackType.LEAP_ATTACK);
				this.getMonster().getAttacks().add(attack);
				Core.uiManager.requestUI(new MonsterAttackEditionMenu(this.getHolder(), this.getRegion(), this.getMonster(), attack));
			}else{
				Core.uiManager.requestUI(new MonsterAttackEditionMenu(this.getHolder(), this.getRegion(), this.getMonster(), this.getMonster().getAttacks().get(slot-9)));
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_NAME){
					boolean ok =true;
					for(Monster monster : Monsters.monsters){
						if(monster.getName().toLowerCase().equals(this.getMessage().toLowerCase())){
							ok = false;
							rp.sendMessage("§cUn monstre avec le nom §4" + this.getMessage().toLowerCase() + " §cexiste déjà !");
							break;
						}
					}
					if(ok)this.getMonster().setName(this.getMessage());
				}else if(this.getListeningId() == this.LIST_ID_TYPE){
					if(this.getHolder().getEquipment().getItemInMainHand() != null){
						EntityType typef = null;
						for(EntityType type : EntityType.values()){
							if(type.toString().equalsIgnoreCase(this.getMessage())){
								typef = type;
								break;
							}
						}
						if(typef != null)this.getMonster().setType(typef);
					}
				}else if(this.getListeningId() == this.LIST_ID_XPF){
					if(Utils.isDouble(this.getMessage())){
						double xPf = Double.valueOf(this.getMessage());
						this.getMonster().setXPFactor(xPf);
					}
				}else if(this.getListeningId() == this.LIST_ID_HPF){
					if(Utils.isDouble(this.getMessage())){
						double hPf = Double.valueOf(this.getMessage());
						this.getMonster().setHealthFactor(hPf);
					}
				}else if(this.getListeningId() == this.LIST_ID_DF){
					if(Utils.isDouble(this.getMessage())){
						double df = Double.valueOf(this.getMessage());
						this.getMonster().setDamagesFactor(df);
					}
				}
			}
		}
		
		this.menu = Bukkit.createInventory(this.getHolder(), 18, StringUtils.abbreviate("Monster Edition :: " + this.getMonster().getName(),32));
		
		this.menu.setItem(this.SLOT_NAME, this.getName());
		this.menu.setItem(this.SLOT_TYPE, this.getMType());
		this.menu.setItem(this.SLOT_XPF, this.getXpf());
		this.menu.setItem(this.SLOT_HPF, this.getHpf());
		this.menu.setItem(this.SLOT_DF, this.getDf());
		this.menu.setItem(this.SLOT_AVR, this.getAverage());
		this.menu.setItem(this.SLOT_DROPS, this.getDrops());
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_DELETE, this.getDelete());
		for(int i = 9;i < this.getMenu().getSize();i++){
			if(this.getMonster().getAttacks().size() > i-9){
				ItemStack stack = new ItemStack(Material.MAGMA_CREAM);
				ItemMeta meta = stack.getItemMeta();
				meta.setDisplayName(this.getMonster().getAttacks().get(i-9).getType().toString());
				List<String> factors = new ArrayList<String>();
				for(double factor : this.getMonster().getAttacks().get(i-9).getWeights()){
					factors.add("§8" + String.valueOf(factor));
				}
				meta.setLore(factors);
				stack.setItemMeta(meta);
				this.menu.setItem(i, stack);
			}else{
				ItemStack stack = new ItemStack(Material.SLIME_BALL);
				ItemMeta meta = stack.getItemMeta();
				meta.setDisplayName("Attack #" + (i-8));
				stack.setItemMeta(meta);
				this.menu.setItem(i, stack);
			}
		}
		return this.getHolder().openInventory(this.menu) != null;
	}

	private ItemStack getName(){
		ItemStack item = new ItemStack(Material.MAP, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Name: " + this.getMonster().getName());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getBack(){
		ItemStack back = new ItemStack(Material.MELON, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Get Back");
		back.setItemMeta(meta);
		return back;
	}
	private ItemStack getMType(){
		ItemStack item = new ItemStack(Material.valueOf(this.getMonster().getType().toString() + "_SPAWN_EGG"), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Type: " + this.getMonster().getType().toString());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getXpf(){
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("XPFactor: " + this.getMonster().getXPFactor());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getHpf(){
		ItemStack item = new ItemStack(Material.SHIELD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("HPFactor: " + this.getMonster().getHealthFactor());
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getDf(){
		ItemStack item = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("DFactor: " + this.getMonster().getDamagesFactor());
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	protected ItemStack getAverage(){
		ItemStack item = new ItemStack(this.getMonster().isAverage() ? Material.LIME_DYE : Material.GRAY_DYE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Average stats : " + String.valueOf(this.getMonster().isAverage()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getDrops(){
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Drops");
		List<String> lore = new ArrayList<String>();
		for(Drop drop : this.getMonster().getDrops()){
			lore.add(drop.getItem().getType().toString() + ": " + drop.getProbability());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getDelete(){
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Delete");
		item.setItemMeta(meta);
		return item;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

}
