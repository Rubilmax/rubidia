package me.pmilon.RubidiaCore.ui.weapons;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Set;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.WeaponUse;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Utils;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponEditionMenu extends UIHandler{

	private final Weapon weapon;
	
	private int SLOT_NAME = 0, SLOT_RARITY = 1, SLOT_RCLASS = 2, SLOT_MIN_DMG = 3, SLOT_MAX_DMG = 4, SLOT_DROP_CHANCE = 5, SLOT_LEVEL = 6, SLOT_SET = 7, SLOT_BACK = 8, SLOT_TAKE = 10, SLOT_USE = 11, SLOT_ATQSPD = 12, SLOT_SKIN = 13, SLOT_UPDATE = 14, SLOT_DELETE = 16;
	private int LIST_ID_NAME = 1, LIST_ID_MINDMG = 2, LIST_ID_MAXDMG = 3, LIST_ID_DROP = 4, LIST_ID_LVL = 5, LIST_ID_ATQSPD = 6;
	
	private boolean isSkinnable = false;
	
	public WeaponEditionMenu(Player p, Weapon weapon) {
		super(p);
		this.weapon = weapon;
		this.menu = Bukkit.createInventory(this.getHolder(), 18, weapon.getName().contains("Aucun nom") ? "New weapon": "Weapon Edition");
	}

	@Override
	public String getType() {
		return "WEAPONS_EDITION_MENU";
	}

	@Override
	protected boolean openWindow() {
		if(this.getMessage() != null){
			if(!this.getMessage().isEmpty()){
				if(this.getListeningId() == this.LIST_ID_NAME){
					this.getWeapon().setName(this.getMessage());
					if(!this.getHolder().getEquipment().getItemInMainHand().getType().equals(Material.AIR)){
						this.getWeapon().setType(this.getHolder().getEquipment().getItemInMainHand().getType());
					}
					weapon.updateName();
				}
				else if(this.getListeningId() == this.LIST_ID_MINDMG){
					if(Utils.isInteger(this.getMessage())){
						int min = Integer.valueOf(this.getMessage());
						if(this.getWeapon().getMaxDamages()-min > 0){
							this.getWeapon().setMinDamages(min);
						}else rp.sendMessage("§cLes dégâts minimum doivent être au moins de 1 de moins que les maximums !");
					}
				}else if(this.getListeningId() == this.LIST_ID_MAXDMG){
					if(Utils.isInteger(this.getMessage())){
						int max = Integer.valueOf(this.getMessage());
						if(max-this.getWeapon().getMinDamages() > 0){
							this.getWeapon().setMaxDamages(max);
						}else rp.sendMessage("§cLes dégâts maximum doivent être au moins de 1 de plus que les minimums !");
					}
				}else if(this.getListeningId() == this.LIST_ID_LVL){
					if(Utils.isInteger(this.getMessage())){
						this.getWeapon().setLevel(Integer.valueOf(this.getMessage()));
					}
				}else if(this.getListeningId() == this.LIST_ID_DROP){
					if(Utils.isDouble(this.getMessage())){
						this.getWeapon().setDropChance(Double.valueOf(this.getMessage()));
					}
				}else if(this.getListeningId() == this.LIST_ID_ATQSPD){
					if(Utils.isDouble(this.getMessage())){
						this.getWeapon().setAttackSpeed(Double.valueOf(this.getMessage()));
					}
				}
			}
		}
		
		this.menu.setItem(this.SLOT_NAME, this.getName());
		this.menu.setItem(this.SLOT_RARITY, this.getRarity());
		this.menu.setItem(this.SLOT_RCLASS, this.getRClass());
		this.menu.setItem(this.SLOT_MIN_DMG, this.getMinDmg());
		this.menu.setItem(this.SLOT_MAX_DMG, this.getMaxDmg());
		this.menu.setItem(this.SLOT_DROP_CHANCE, this.getDropChance());
		this.menu.setItem(this.SLOT_LEVEL, this.getLevel());
		this.menu.setItem(this.SLOT_SET, this.getSet());
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_TAKE, this.getTake());
		this.menu.setItem(this.SLOT_UPDATE, this.getUpdate());
		if(this.getWeapon().isAttack()){
			this.menu.setItem(this.SLOT_USE, this.getUse());
			this.menu.setItem(this.SLOT_ATQSPD, this.getAtqSpd());
		}
		for(String s : Weapons.SKIN_WEAPONS){
			if(this.getWeapon().getType().toString().contains(s)){
				this.menu.setItem(this.SLOT_SKIN, this.getSkin());
				this.isSkinnable = true;
				break;
			}
		}
		this.menu.setItem(this.SLOT_DELETE, this.getDelete());
		return this.getHolder().openInventory(this.menu) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		int slot = e.getRawSlot();
		if(slot == this.SLOT_NAME)this.close(true, this.LIST_ID_NAME);
		else if(slot == this.SLOT_RARITY){
			if(this.getWeapon().getRarity().equals(Rarity.COMMON))this.getWeapon().setRarity(Rarity.UNCOMMON);
			else if(this.getWeapon().getRarity().equals(Rarity.UNCOMMON))this.getWeapon().setRarity(Rarity.SET);
			else if(this.getWeapon().getRarity().equals(Rarity.SET))this.getWeapon().setRarity(Rarity.RARE);
			else if(this.getWeapon().getRarity().equals(Rarity.RARE))this.getWeapon().setRarity(Rarity.EPIC);
			else if(this.getWeapon().getRarity().equals(Rarity.EPIC))this.getWeapon().setRarity(Rarity.LEGENDARY);
			else if(this.getWeapon().getRarity().equals(Rarity.LEGENDARY))this.getWeapon().setRarity(Rarity.COMMON);
			this.menu.setItem(this.SLOT_RARITY, this.getRarity());
		}else if(slot == this.SLOT_RCLASS){
			if(this.getWeapon().getRClass().equals(RClass.VAGRANT))this.getWeapon().setRClass(RClass.PALADIN);
			else if(this.getWeapon().getRClass().equals(RClass.PALADIN))this.getWeapon().setRClass(RClass.RANGER);
			else if(this.getWeapon().getRClass().equals(RClass.RANGER))this.getWeapon().setRClass(RClass.MAGE);
			else if(this.getWeapon().getRClass().equals(RClass.MAGE))this.getWeapon().setRClass(RClass.ASSASSIN);
			else if(this.getWeapon().getRClass().equals(RClass.ASSASSIN))this.getWeapon().setRClass(RClass.VAGRANT);
			this.menu.setItem(this.SLOT_RCLASS, this.getRClass());
		}else if(slot == this.SLOT_USE && this.getWeapon().isAttack()){
			if(this.getWeapon().getWeaponUse().equals(WeaponUse.MELEE))this.getWeapon().setWeaponUse(WeaponUse.RANGE);
			else if(this.getWeapon().getWeaponUse().equals(WeaponUse.RANGE))this.getWeapon().setWeaponUse(WeaponUse.MELEE_RANGE);
			else if(this.getWeapon().getWeaponUse().equals(WeaponUse.MELEE_RANGE))this.getWeapon().setWeaponUse(WeaponUse.MAGIC);
			else if(this.getWeapon().getWeaponUse().equals(WeaponUse.MAGIC))this.getWeapon().setWeaponUse(WeaponUse.MELEE);
			this.menu.setItem(this.SLOT_USE, this.getUse());
		}else if(slot == this.SLOT_MIN_DMG)this.close(true, this.LIST_ID_MINDMG);
		else if(slot == this.SLOT_MAX_DMG)this.close(true, this.LIST_ID_MAXDMG);
		else if(slot == this.SLOT_DROP_CHANCE)this.close(true, this.LIST_ID_DROP);
		else if(slot == this.SLOT_LEVEL)this.close(true, this.LIST_ID_LVL);
		else if(slot == this.SLOT_ATQSPD && this.getWeapon().isAttack())this.close(true, this.LIST_ID_ATQSPD);
		else if(slot == this.SLOT_UPDATE){
			this.getWeapon().setMinDamages(Weapons.getAverageMinStat(this.getWeapon()));
			this.getWeapon().setMaxDamages(Weapons.getAverageMaxStat(this.getWeapon()));
			this.getWeapon().setDropChance(Weapons.getAverageDropChance(this.getWeapon()));
			this.getWeapon().setAttackSpeed(Weapons.getAverageAttackSpeed(this.getWeapon()));
			this.getMenu().setItem(this.SLOT_MIN_DMG, this.getMinDmg());
			this.getMenu().setItem(this.SLOT_MAX_DMG, this.getMaxDmg());
			this.getMenu().setItem(this.SLOT_DROP_CHANCE, this.getDropChance());
			this.getMenu().setItem(this.SLOT_ATQSPD, this.getAtqSpd());
		}else if(slot == this.SLOT_SKIN && this.isSkinnable)Core.uiManager.requestUI(new WeaponSkinSelectionMenu(this.getHolder(), this.getWeapon()));
		else if(slot == this.SLOT_SET){
			if(e.isRightClick()){
				if(this.getWeapon().isSetItem()){
					this.getWeapon().getSet().getWeapons().remove(this.getWeapon());
				}
				this.getWeapon().setSetUUID("0000");
				this.menu.setItem(this.SLOT_SET, this.getSet());
			}else{
				Core.uiManager.requestUI(new WeaponSetSelectionMenu(this.getHolder(), this.getWeapon()));
			}
		}
		else if(slot == this.SLOT_BACK)Core.uiManager.requestUI(new WeaponsUI(this.getHolder()));
		else if(slot == this.SLOT_TAKE)this.getHolder().getInventory().addItem(this.getWeapon().getNewItemStack(rp));
		else if(slot == this.SLOT_DELETE){
			Weapons.weapons.remove(this.getWeapon());
			if(this.getWeapon().getSet() != null)this.getWeapon().getSet().getWeapons().remove(this.getWeapon());
			Configs.getWeaponsConfig().set("weapons." + this.getWeapon().getUUID(), null);
			Configs.saveWeaponsConfig();
			Core.uiManager.requestUI(new WeaponsUI(this.getHolder()));
			rp.sendMessage("§cArme supprimée !");
		}
		this.menu.setItem(this.SLOT_TAKE, this.getTake());
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

	public Weapon getWeapon() {
		return weapon;
	}
	
	private ItemStack getName(){
		ItemStack item = new ItemStack(Material.MAP, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fName");
		meta.setLore(Arrays.asList(this.getWeapon().getRarity().getPrefix() + "§l" + this.getWeapon().getName()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getRarity(){
		ItemStack item = new ItemStack(Material.DIAMOND, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fRarity");
		meta.setLore(Arrays.asList(this.getWeapon().getRarity().getPrefix() + this.getWeapon().getRarity().toString()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getRClass(){
		ItemStack item = new ItemStack(this.getWeapon().getRClass().getDisplay(), 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fRClass");
		meta.setLore(Arrays.asList(this.getWeapon().getRClass().toString()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getMinDmg(){
		ItemStack item = new ItemStack(Material.WOODEN_PICKAXE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fMin damages");
		meta.setLore(Arrays.asList(String.valueOf(this.getWeapon().getMinDamages())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getMaxDmg(){
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fMax damages");
		meta.setLore(Arrays.asList(String.valueOf(this.getWeapon().getMaxDamages())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getDropChance(){
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fDrop chance");
		meta.setLore(Arrays.asList(String.valueOf(this.getWeapon().getDropChance())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getLevel(){
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fLevel");
		meta.setLore(Arrays.asList(String.valueOf(this.getWeapon().getLevel())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getUse(){
		ItemStack item = new ItemStack(Material.ARROW, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fWeapon use");
		meta.setLore(Arrays.asList(this.getWeapon().getWeaponUse().toString()));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getAtqSpd(){
		ItemStack item = new ItemStack(Material.FEATHER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fAttack speed");
		meta.setLore(Arrays.asList(String.valueOf(this.getWeapon().getAttackSpeed())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getSkin(){
		ItemStack item = new ItemStack(Material.GUNPOWDER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fSkin ID");
		meta.setLore(Arrays.asList(String.valueOf(this.getWeapon().getSkinId())));
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getSet(){
		ItemStack item = new ItemStack(Material.ARMOR_STAND, 1);
		ItemMeta meta = item.getItemMeta();
		if(this.getWeapon().isSetItem()){
			Set set = this.getWeapon().getSet();
			if(set != null)meta.setDisplayName("Set " + set.getName());
			else{
				this.getWeapon().setSetUUID("0000");
				meta.setDisplayName("No set");
			}
		}else{
			meta.setDisplayName("No set");
		}
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getTake(){
		ItemStack item = this.getWeapon().getNewItemStack(rp);
		item.setAmount(this.getWeapon().getLevel() > 0 ? (this.getWeapon().getLevel() > 64 ? 64 : this.getWeapon().getLevel()) : 1);
		return item;
	}
	private ItemStack getDelete(){
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4§lDELETE");
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
	private ItemStack getUpdate(){
		ItemStack back = new ItemStack(Material.REDSTONE_BLOCK, 1);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Update stats to level");
		back.setItemMeta(meta);
		return back;
	}

}
