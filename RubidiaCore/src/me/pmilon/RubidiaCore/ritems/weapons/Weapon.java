package me.pmilon.RubidiaCore.ritems.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ritems.general.ItemStacks;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.weapons.Piercing.PiercingType;
import me.pmilon.RubidiaCore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagString;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Weapon {

	private String UUID;
	private String name;
	private Rarity rarity;
	private Material type;
	private RClass rClass;
	private double dropChance;
	private int minDamages;
	private int maxDamages;
	private int level;
	private String setUUID;
	private WeaponUse weaponUse;
	private double attackSpeed;
	private int skinId;
	
	private RItem rItem;
	private int suppLevel = 0;
	private List<Piercing> piercings = new ArrayList<Piercing>();
	private Map<Enchantment, Integer> enchants;
	private int holes = 0;
	private boolean modified = false;
	public Weapon(String UUID, String name, Rarity rarity, Material type, RClass rClass, double dropChance, int minDamages, int maxDamages, int level, String setUUID, WeaponUse weaponUse, double attackSpeed, int skinId){
		this.UUID = UUID;
		this.name = name;
		this.rarity = rarity;
		this.type = type;
		this.rClass = rClass;
		this.dropChance = dropChance;
		this.minDamages = minDamages;
		this.maxDamages = maxDamages;
		this.level = level;
		this.suppLevel = 0;
		this.setUUID = setUUID;
		this.weaponUse = weaponUse;
		this.attackSpeed = attackSpeed;
		this.skinId = skinId;
	}
	
	public Weapon(RItem rItem){
		this.rItem = rItem;
		this.type = rItem.getItem().getType();
		ItemMeta meta = rItem.getItem().getItemMeta();
		if(meta.hasEnchants())this.setEnchants(meta.getEnchants());
		String title = meta.getDisplayName();
		this.name = ChatColor.stripColor(title).split(" \\(")[0];
		String[] sParts = title.split("\\+");
		if(sParts.length > 1)this.suppLevel = Integer.valueOf(sParts[1].split("\\)")[0]);
		String[] hParts = title.split("/");
		if(hParts.length > 1){
			this.holes = Integer.valueOf(hParts[1].split("\\)")[0]);
			for(String s : meta.getLore()){
				if(s.startsWith("§d")){
					String[] parts = ChatColor.stripColor(s).split(" \\+");
					PiercingType type = PiercingType.fromDisplay(parts[0]);
					for(int i = 0;i < Integer.valueOf(parts[1])/((double)type.getAmount());i++){
						piercings.add(new Piercing(type));
					}
				}
			}
		}
		for(Weapon weapon : Weapons.weapons){
			if(weapon.getName().equals(this.name) && weapon.getType().equals(this.type)){
				this.rarity = weapon.getRarity();
				this.rClass = weapon.getRClass();
				this.level = weapon.getLevel();
				this.minDamages = weapon.getMinDamages();
				this.maxDamages = weapon.getMaxDamages();
				this.dropChance = weapon.getDropChance();
				this.UUID = weapon.getUUID();
				this.setUUID = weapon.getSetUUID();
				this.weaponUse = weapon.getWeaponUse();
				this.attackSpeed = weapon.getAttackSpeed();
				this.skinId = weapon.getSkinId();
				break;
			}
		}
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setModified(true);
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
		this.setModified(true);
	}

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
		this.setModified(true);
	}

	public RClass getRClass() {
		return rClass;
	}

	public void setRClass(RClass rClass) {
		this.rClass = rClass;
		this.setModified(true);
	}

	public double getDropChance() {
		return dropChance;
	}

	public void setDropChance(double dropChance) {
		this.dropChance = dropChance;
		this.setModified(true);
	}

	public int getMinDamages() {
		return (int) (minDamages + suppLevel*(1+this.getLevel()/5.8));
	}

	public void setMinDamages(int minDamages) {
		this.minDamages = minDamages;
		this.setModified(true);
	}

	public int getMaxDamages() {
		return (int) Math.round(maxDamages + suppLevel*(1+this.getLevel()/4.9));
	}

	public void setMaxDamages(int maxDamages) {
		this.maxDamages = maxDamages;
		this.setModified(true);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		this.setModified(true);
	}

	public String getUUID() {
		return UUID;
	}

	public WeaponUse getWeaponUse() {
		return weaponUse;
	}

	public void setWeaponUse(WeaponUse weaponUse) {
		this.weaponUse = weaponUse;
		this.setModified(true);
	}
	
	public void setUUID(String uUID) {
		UUID = uUID;
		this.setModified(true);
	}
	
	public boolean isAttack() {
		return !this.getType().toString().contains("_CHESTPLATE")
				&& !this.getType().toString().contains("_HELMET")
				&& !this.getType().toString().contains("_LEGGINGS")
				&& !this.getType().toString().contains("_BOOTS")
				&& !this.getType().equals(Material.SHIELD);
	}
	
	public String getDisplayName() {
		return this.getRarity().getPrefix() + "§l" + this.getName() + (this.getSuppLevel() > 0 ? " §7(+" + this.getSuppLevel() + ")" : "") + (this.getHoles() > 0 ? (" §5(" + this.getPiercings().size() + "/" + this.getHoles() + ")") : "");
	}
	
	public ItemStack getNewItemStack(RPlayer rp){
		ItemStack stack = new ItemStack(this.getType(), 1);
		ItemMeta meta = stack.getItemMeta();
		((Damageable) meta).setDamage((int) Math.ceil(this.getType().getMaxDurability()*this.getSkinId()*Weapons.getSkinFactor(this.getType())));
		meta.setDisplayName(this.getDisplayName());
		List<String> lore = new ArrayList<String>();
		lore.addAll(Arrays.asList("§8" + (this.isAttack() ? "Arme " + this.getWeaponUse().getDisplayFr() : "Pièce d'armure"), "§7Rareté : " + this.getRarity().getPrefix() + this.getRarity().getDisplayFr(), "", (this.isAttack() ? "§7Dégâts : §4" : "§7Défense : §4") + this.getMinDamages() + " §8- §4" + this.getMaxDamages()));
		if(this.isAttack())lore.add("§7Vitesse d'attaque : §8" + (this.getAttackSpeed() >= 1.75 ? "Très rapide" : this.getAttackSpeed() >= 1.25 ? "Rapide" : this.getAttackSpeed() >= .9 ? "Moyenne" : this.getAttackSpeed() >= .5 ? "Lente" : "Très lente"));
		lore.addAll(Arrays.asList("", "§7Classe : §8" + this.getRClass().getName(), "§7Niveau : §8" + this.getLevel()));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		stack.setItemMeta(meta);
		if(rp != null)stack = this.updateState(rp, stack);
		return stack;
	}
	
	public Weapon getNewInstance(){
		return new Weapon(new RItem(this.getNewItemStack(null)));
	}

	public RItem getrItem() {
		return rItem;
	}

	public void setrItem(RItem rItem) {
		this.rItem = rItem;
	}

	public int getSuppLevel() {
		return suppLevel;
	}

	public void setSuppLevel(int suppLevel) {
		this.suppLevel = suppLevel;
	}

	public boolean isSimilar(Weapon weapon){
		return this.getName().equals(weapon.getName()) && this.getLevel() == weapon.getLevel() && this.getRarity().equals(weapon.getRarity()) && this.getRClass().equals(weapon.getRClass()) && this.getType().equals(weapon.getType());
	}
	
	public Set getSet(){
		return Sets.get(this.getSetUUID());
	}
	
	public boolean isSetItem(){
		return this.getRarity().equals(Rarity.SET) && !this.getSetUUID().equals("0000");
	}

	public ItemStack updateState(RPlayer rp, ItemStack item){
		int itDamage = (int) Math.ceil(this.getType().getMaxDurability()*this.getSkinId()*Weapons.getSkinFactor(this.getType()));
		ItemMeta meta = item.getItemMeta();
		if(((Damageable) meta).getDamage() != itDamage) {
			((Damageable) meta).setDamage(itDamage);
		}
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS);
		List<String> lore = meta.hasLore() ? Utils.getModifiableCopy(meta.getLore()) : new ArrayList<String>();
		for(int i = lore.size()-1;i >= 0;i--){
			lore.remove(i);
		}
		if(rp != null){
			lore.addAll(Arrays.asList("§8" + (this.isAttack() ? ("Arme " + this.getWeaponUse().getDisplayFr()) : ("Pièce d'armure")), ("§7Rareté : ") + this.getRarity().getPrefix() + (this.getRarity().getDisplayFr()), "", (this.isAttack() ? ("§7Dégâts : §4") : ("§7Défense : §4")) + this.getMinDamages() + " §8- §4" + this.getMaxDamages()));
			if(this.isAttack())lore.add("§7" + ("Vitesse d'attaque :") + " §8" + (this.getAttackSpeed() >= 1.75 ? ("Très rapide") : this.getAttackSpeed() >= 1.25 ? ("Rapide") : this.getAttackSpeed() >= .9 ? ("Moyenne") : this.getAttackSpeed() >= .5 ? ("Lente") : ("Très lente")));
			lore.addAll(Arrays.asList("", ("§7Classe : ") + (this.getRClass().equals(rp.getRClass()) || this.getRClass().equals(RClass.VAGRANT) ? "§e" : "§8") + (this.getRClass().getName()), ("§7Niveau : ") + (rp.getRLevel() >= this.getLevel() ? "§e" : "§8") + this.getLevel()));
			if(this.isSetItem()){
				lore.addAll(Arrays.asList("", "  §2§oSet " + this.getSet().getName()));
				lore.addAll(this.getSet().getWeaponState(rp.getPlayer()));
				lore.addAll(Arrays.asList("", "  §6§o" + ("Attributs de set")));
				lore.addAll(this.getSet().getBuffState(rp.getPlayer()));
			}
			if(this.hasEnchants()){
				lore.addAll(Arrays.asList("", "  §9§o" + ("Enchantements")));
				for(Enchantment enchant : this.getEnchants().keySet()){
					int level = this.getEnchants().get(enchant);
					meta.addEnchant(enchant, level, true);
					String name = "§7    §l";
					name += level == 1 ? "I  " : level == 2 ? "II " : level == 3 ? "III" : level == 4 ? "IV §7" : level == 5 ? "V  §7" : "???";//colorcodesto align enchants
					name += "  §7";
					if(enchant.equals(Enchantment.ARROW_DAMAGE))name += ("Puissance");
					else if(enchant.equals(Enchantment.ARROW_FIRE))name += ("Flamme");
					else if(enchant.equals(Enchantment.ARROW_INFINITE))name += ("Infinité");
					else if(enchant.equals(Enchantment.ARROW_KNOCKBACK))name += ("Frappe");
					else if(enchant.equals(Enchantment.DAMAGE_ALL))name += ("Puissance");
					else if(enchant.equals(Enchantment.DAMAGE_ARTHROPODS))name += ("Fléau des arthropodes");
					else if(enchant.equals(Enchantment.DAMAGE_UNDEAD))name += ("Châtiment");
					else if(enchant.equals(Enchantment.DEPTH_STRIDER))name += ("Agilité aquatique");
					else if(enchant.equals(Enchantment.DIG_SPEED))name += ("Efficacité");
					else if(enchant.equals(Enchantment.DURABILITY))name += ("Durabilité");
					else if(enchant.equals(Enchantment.FIRE_ASPECT))name += ("Aura de feu");
					else if(enchant.equals(Enchantment.FROST_WALKER))name += ("Semelles givrantes");
					else if(enchant.equals(Enchantment.KNOCKBACK))name += ("Recul");
					else if(enchant.equals(Enchantment.LOOT_BONUS_BLOCKS))name += ("Fortune");
					else if(enchant.equals(Enchantment.LOOT_BONUS_MOBS))name += ("Butin");
					else if(enchant.equals(Enchantment.LUCK))name += ("Chance");
					else if(enchant.equals(Enchantment.LURE))name += ("Appât");
					else if(enchant.equals(Enchantment.MENDING))name += ("Raccomodage");
					else if(enchant.equals(Enchantment.OXYGEN))name += ("Apnée");
					else if(enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL))name += "Protection";
					else if(enchant.equals(Enchantment.PROTECTION_EXPLOSIONS))name += ("Protection contre les explosions");
					else if(enchant.equals(Enchantment.PROTECTION_FALL))name += ("Chute amortie");
					else if(enchant.equals(Enchantment.PROTECTION_FIRE))name += ("Protection contre le feu");
					else if(enchant.equals(Enchantment.PROTECTION_PROJECTILE))name += ("Protection contre les projectiles");
					else if(enchant.equals(Enchantment.SILK_TOUCH))name += ("Touché de soie");
					else if(enchant.equals(Enchantment.THORNS))name += ("Épines");
					else if(enchant.equals(Enchantment.WATER_WORKER))name += ("Affinité aquatique");
					else if(enchant.equals(REnchantment.SOUL_BIND))name += ("Liaison spirituelle");
					lore.add(name);
				}
			}
			if(!this.getPiercings().isEmpty()){
				lore.add("");
				for(PiercingType type : PiercingType.values()){
					int amount = 0;
					for(Piercing piercing : this.getPiercings()){
						if(piercing.getType().equals(type)){
							amount++;
						}
					}
					if(amount > 0)lore.addAll(Arrays.asList("§d" + (type.getDisplayFr()) + " +" + amount*type.getAmount()));
				}
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
			if(this.isAttack()){
		        //we change attack speed
		        NBTTagList modifiers = new NBTTagList();
		        NBTTagCompound damage = new NBTTagCompound();
		        damage.setString("AttributeName", "generic.attackSpeed");
		        damage.setString("Name", "generic.attackSpeed");
		        damage.setDouble("Amount", this.getAttackSpeed()*rp.getAttackSpeedFactor()-4);
		        damage.setInt("Operation", 0);
		        damage.setInt("UUIDLeast", 894654);
		        damage.setInt("UUIDMost", 2872);
		        modifiers.add(damage);
				item = ItemStacks.setMetadata(item, "AttributeModifiers", modifiers);
		        
		        //we restrict usage of weapons
		        NBTTagList canDestroy = new NBTTagList();
		        canDestroy.add(NBTTagString.a("minecraft:air"));
				item = ItemStacks.setMetadata(item, "CanDestroy", canDestroy);
			}
		}
		return item;
	}

	public String getSetUUID() {
		return setUUID;
	}

	public void setSetUUID(String setUUID) {
		this.setUUID = setUUID;
	}

	public String canUse(RPlayer rp){
		if(!rp.isOp()) {
			if(!this.getRClass().equals(RClass.VAGRANT) && !rp.getRClass().equals(this.getRClass())) {
				return "vous n'êtes pas " + this.getRClass().getName();
			} else if(this.getLevel() > rp.getRLevel()) {
				return "votre niveau est insuffisant";
			}
		}
		
		return "";
	}

	public double getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(double attackSpeed) {
		this.attackSpeed = attackSpeed;
		this.setModified(true);
	}

	public List<Piercing> getPiercings() {
		return piercings;
	}

	public void setPiercings(List<Piercing> piercings) {
		this.piercings = piercings;
	}

	public int getHoles() {
		return holes;
	}

	public void setHoles(int holes) {
		this.holes = holes;
	}

	public Map<Enchantment, Integer> getEnchants() {
		return enchants;
	}

	public void setEnchants(Map<Enchantment, Integer> enchants) {
		this.enchants = enchants;
	}
	
	public boolean hasEnchants(){
		return this.getEnchants() != null;
	}

	public int getSkinId() {
		return skinId;
	}

	public void setSkinId(int skinId) {
		this.skinId = skinId;
		this.setModified(true);
	}
	
	public void updateName(){
		for(int i = 0;i < Weapons.SKIN_WEAPONS.length;i++){
			if(this.getType().toString().contains(Weapons.SKIN_WEAPONS[i])){
				String name = Weapons.NAME_WEAPONS[i];
				if(!ChatColor.stripColor(this.getName()).startsWith(name)){
					this.setName(name + (name.isEmpty() ? "" : " ") + this.getName().replaceAll("Epée ", "").replaceAll("Jambières ", "").replaceAll("Armure ", ""));
				}
			}
		}
	}
}
