package me.pmilon.RubidiaCore.ritems.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.crafts.Crafts;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.BukkitConverter;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaCore.utils.RandomUtils;

public class Weapons {
	
	public static final List<Material> TOOLS = Arrays.asList(Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_HOE,
			Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_HOE,
			Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_HOE,
			Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_HOE,
			Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_HOE,
			Material.BOW, Material.ELYTRA, Material.SHEARS);
	
	public static final String[] SKIN_WEAPONS = new String[]{"BOW","_HOE","_SWORD","_AXE","_BOOTS","_LEGGINGS","_CHESTPLATE","_HELMET"};
	public static final String[] NAME_WEAPONS = new String[]{"Arc","","","","Bottes","Gants","Plastron","Casque"};
	
	public static double getSkinFactor(Material type){
		return type.equals(Material.ELYTRA) ? .008 :
			(type.equals(Material.SHEARS) ? .07 :
				(type.equals(Material.BOW) ? .024 :
					(type.toString().contains("DIAMOND_") ? .005 : 
						(type.toString().contains("GOLDEN_") ? .1 : .05))));
	}
	public static int getSkinAmount(Material type){
		if(type.equals(Material.WOODEN_AXE))return 10;
		else if(type.equals(Material.WOODEN_HOE))return 18;
		else if(type.equals(Material.WOODEN_SWORD))return 10;
		else if(type.equals(Material.BOW))return 39;
		else if(type.equals(Material.STONE_AXE))return 17;
		else if(type.equals(Material.STONE_HOE))return 18;
		else if(type.equals(Material.STONE_SWORD))return 18;
		else if(type.equals(Material.IRON_AXE))return 17;
		else if(type.equals(Material.IRON_HOE))return 18;
		else if(type.equals(Material.IRON_SWORD))return 18;
		else if(type.equals(Material.DIAMOND_AXE))return 24;
		else if(type.equals(Material.DIAMOND_HOE))return 28;
		else if(type.equals(Material.DIAMOND_SWORD))return 24;
		else if(type.equals(Material.GOLDEN_AXE))return 8;
		else if(type.equals(Material.GOLDEN_HOE))return 8;
		else if(type.equals(Material.GOLDEN_SWORD))return 8;
		else if(type.toString().contains("LEATHER_")){
			if(type.equals(Material.LEATHER_HELMET))return 18;
			else return 19;
		}else if(type.toString().contains("CHAINMAIL_"))return 18;
		else if(type.toString().contains("IRON_"))return 19;
		else if(type.toString().contains("DIAMOND_"))return 19;
		else if(type.toString().contains("GOLDEN_"))return 19;
		return 0;
	}
	
	public static List<Weapon> weapons = new ArrayList<Weapon>();
	public static final HashSet<Material> WEAPON_TYPES = new HashSet<Material>();
	public static final HashSet<Material> COMMON_WEAPON_TYPES = new HashSet<Material>();
	
	public static void onEnable(boolean debug){
		weapons.clear();
		if(Configs.getWeaponsConfig().contains("weapons")){
			for(String id : Configs.getWeaponsConfig().getConfigurationSection("weapons").getKeys(false)){
				String path = "weapons." + id;
				Weapon weapon = new Weapon(id,
						Configs.getWeaponsConfig().getString(path + ".name"),
						Rarity.valueOf(Configs.getWeaponsConfig().getString(path + ".rarity")),
						Material.valueOf(BukkitConverter.convert(Configs.getWeaponsConfig().getString(path + ".type"))),
						RClass.valueOf(Configs.getWeaponsConfig().getString(path + ".rClass")),
						Configs.getWeaponsConfig().getDouble(path + ".dropChance"),
						Configs.getWeaponsConfig().getInt(path + ".minDamages"),
						Configs.getWeaponsConfig().getInt(path + ".maxDamages"),
						Configs.getWeaponsConfig().getInt(path + ".level"),
						(Configs.getWeaponsConfig().contains(path + ".setUUID") ? Configs.getWeaponsConfig().getString(path + ".setUUID") : "0000"),
						WeaponUse.valueOf(Configs.getWeaponsConfig().getString(path + ".weaponUse")),
						Configs.getWeaponsConfig().getDouble(path + ".attackSpeed"),
						Configs.getWeaponsConfig().getInt(path + ".skinId"));
				weapons.add(weapon);
				weapon.updateName();
				if((weapon.getType().toString().contains("BOW") || weapon.getType().toString().contains("_SWORD") || weapon.getType().toString().contains("_AXE") || weapon.getType().toString().contains("HOE")|| !weapon.isAttack()) && weapon.getRarity().toString().contains("COMMON")){
					weapon.setMinDamages(Weapons.getAverageMinStat(weapon));
					weapon.setMaxDamages(Weapons.getAverageMaxStat(weapon));
					weapon.setDropChance(Weapons.getAverageDropChance(weapon));
					weapon.setAttackSpeed(Weapons.getAverageAttackSpeed(weapon));
				}
				if(debug){
					if(Weapons.weapons.size() % 50 == 0){
						Core.console.sendMessage("§6LOADED §e" + Weapons.weapons.size() + " §6WEAPONS");
					}
				}
			}
		}
		Core.console.sendMessage("§6LOADED §e" + Weapons.weapons.size() + " §6WEAPONS");
		Sets.onEnable(debug);
		Weapons.updateTypes();
	}
	
	public static void onDisable(){
		for(Weapon weapon : weapons){
			if(weapon.isModified()){
				weapon.setModified(false);
				String path = "weapons." + weapon.getUUID();
				Configs.getWeaponsConfig().set(path + ".name", weapon.getName());
				Configs.getWeaponsConfig().set(path + ".rarity", weapon.getRarity().toString());
				Configs.getWeaponsConfig().set(path + ".type", weapon.getType().toString());
				Configs.getWeaponsConfig().set(path + ".rClass", weapon.getRClass().toString());
				Configs.getWeaponsConfig().set(path + ".dropChance", weapon.getDropChance());
				Configs.getWeaponsConfig().set(path + ".minDamages", weapon.getMinDamages());
				Configs.getWeaponsConfig().set(path + ".maxDamages", weapon.getMaxDamages());
				Configs.getWeaponsConfig().set(path + ".level", weapon.getLevel());
				Configs.getWeaponsConfig().set(path + ".attack", weapon.isAttack());
				Configs.getWeaponsConfig().set(path + ".setUUID", weapon.getSetUUID());
				Configs.getWeaponsConfig().set(path + ".weaponUse", weapon.getWeaponUse().toString());
				Configs.getWeaponsConfig().set(path + ".attackSpeed", weapon.getAttackSpeed());
				Configs.getWeaponsConfig().set(path + ".skinId", weapon.getSkinId());
				Configs.getWeaponsConfig().set(path + ".itemDamage", null);
			}
		}
		Sets.onDisable();
		Configs.saveWeaponsConfig();
	}
	
	public static void updateTypes(){
		Weapons.WEAPON_TYPES.clear();
		Weapons.COMMON_WEAPON_TYPES.clear();
		for(Weapon weapon : weapons){
			Weapons.WEAPON_TYPES.add(weapon.getType());
			if(weapon.getRarity().equals(Rarity.COMMON)) {
				Weapons.COMMON_WEAPON_TYPES.add(weapon.getType());
			}
		}
	}
	
	public static HashSet<Weapon> getByType(Material type){
		HashSet<Weapon> weapons = new HashSet<Weapon>();
		for(Weapon weapon : Weapons.weapons){
			if(weapon.getType().equals(type)){
				weapons.add(weapon);
			}
		}
		return weapons;
	}
	
	public static HashSet<Weapon> getByRarity(Rarity... rarities){
		HashSet<Rarity> rarities2 = new HashSet<Rarity>(Arrays.asList(rarities));
		HashSet<Weapon> weapons = new HashSet<Weapon>();
		for(Weapon weapon : Weapons.weapons){
			if(rarities2.contains(weapon.getRarity())){
				weapons.add(weapon);
			}
		}
		return weapons;
	}
	
	public static HashSet<Weapon> getByRClass(RClass rClass){
		HashSet<Weapon> weapons = new HashSet<Weapon>();
		for(Weapon weapon : Weapons.weapons){
			if(weapon.getRClass().equals(rClass)){
				weapons.add(weapon);
			}
		}
		return weapons;
	}
	
	public static HashSet<Weapon> getByLevel(int level, int tolerance){
		HashSet<Weapon> weapons = new HashSet<Weapon>();
		for(Weapon weapon : Weapons.weapons){
			if(weapon.getLevel() >= level-tolerance && weapon.getLevel() <= level+tolerance){
				weapons.add(weapon);
			}
		}
		return weapons;
	}
	
	public static Weapon getByUUID(String uuid){
		for(Weapon weapon : Weapons.weapons){
			if(weapon.getUUID().equals(uuid)){
				return weapon;
			}
		}
		return null;
	}
	
	public static Weapon getByName(String name){
		for(Weapon weapon : Weapons.weapons){
			if(weapon.getName().equals(name)){
				return weapon;
			}
		}
		return null;
	}
	
	public static Weapon nearest(int level, boolean attack, RClass rClass, String type, Rarity... rarities){
		List<Weapon> available = new ArrayList<Weapon>(weapons);
		List<Rarity> rarities2 = new ArrayList<Rarity>();
		if(rarities != null)rarities2 = Arrays.asList(rarities);
		for(Weapon weapon : weapons){
			if(!rarities2.contains(weapon.getRarity())){
				available.remove(weapon);
			}else if(type != null){
				if(!weapon.getType().toString().contains(type)){
					available.remove(weapon);
				}
			}else if(rClass != null){
				if(!weapon.getRClass().equals(rClass)){
					available.remove(weapon);
				}
			}else if((weapon.isAttack() && !attack) || (!weapon.isAttack() && attack)){
				available.remove(weapon);
			}
		}
		
		if(available.size() > 0){
			Weapon weapon = available.get(0);
			for(Weapon weap : available){
				if(Math.abs(level-weap.getLevel()) < Math.abs(level-weapon.getLevel()))weapon = weap;
			}
			return weapon;
		}
		return null;
	}
	
	public static Weapon random(int levelMin, int levelMax, Material... types){
		List<Weapon> available = new ArrayList<Weapon>(weapons);
		List<Material> materials = new ArrayList<Material>();
		if(types != null){
			materials = Arrays.asList(types);
			for(Weapon weapon : weapons){
				if(!materials.contains(weapon.getType())
						|| (weapon.getLevel() < levelMin
								&& weapon.getLevel() > levelMax)){
					available.remove(weapon);
				}
			}
		}
		
		if(available.size() > 0){
			return available.get(RandomUtils.random.nextInt(available.size()));
		}
		return null;
	}
	
	public static Weapon random(RPlayer rp, int levelMin, int levelMax, Material type){
		List<Weapon> available = new ArrayList<Weapon>(weapons);
		for(Weapon weapon : weapons){
			if(!weapon.getType().equals(type)
					|| (weapon.getLevel() < levelMin
							&& weapon.getLevel() > levelMax)
					|| !weapon.canUse(rp).isEmpty()){
				available.remove(weapon);
			}
		}
		
		if(available.size() > 0){
			return available.get(RandomUtils.random.nextInt(available.size()));
		}
		return null;
	}

	public static Weapon random(Material type, Rarity... rarities){
		HashSet<Weapon> available = Weapons.getByType(type);
		if(available.size() > 0){
			rarities = Weapons.filterAvailableRarities(available, rarities);
			if(rarities.length > 0){
				Rarity rarity = Rarity.random(rarities);
				List<Weapon> weapons = Arrays.asList(available.toArray(new Weapon[available.size()]));
				Weapon weapon = weapons.get(RandomUtils.random.nextInt(available.size()));
				while(!weapon.getRarity().equals(rarity)){
					weapon = weapons.get(RandomUtils.random.nextInt(available.size()));
				}
				return weapon;
			}
		}
		return null;
	}

	public static Weapon random(Material type, int level, Rarity... rarities){
		HashSet<Weapon> available = Weapons.getByType(type);
		available.addAll(Weapons.getByLevel(level, 9));
		if(available.size() > 0){
			rarities = Weapons.filterAvailableRarities(available, rarities);
			if(rarities.length > 0){
				Rarity rarity = Rarity.random(rarities);
				List<Weapon> weapons = Arrays.asList(available.toArray(new Weapon[available.size()]));
				Weapon weapon = weapons.get(RandomUtils.random.nextInt(available.size()));
				while(!weapon.getRarity().equals(rarity)){
					weapon = weapons.get(RandomUtils.random.nextInt(available.size()));
				}
				return weapon;
			}
		}
		return null;
	}
	
	public static Rarity[] filterAvailableRarities(HashSet<Weapon> weapons, Rarity... rarities){
		List<Rarity> newRarities = new ArrayList<Rarity>();
		for(int i = 0;i < rarities.length;i++){
			Rarity rarity = rarities[i];
			for(Weapon weapon : weapons){
				if(weapon.getRarity().equals(rarity)){
					newRarities.add(rarity);
					break;
				}
			}
		}
		return newRarities.toArray(new Rarity[newRarities.size()]);
	}

	public static int getAverageMinStat(Weapon weapon){
		double min = 0;
		if(weapon.isAttack()){
			double r = Math.pow(((double) weapon.getLevel())/Settings.LEVEL_MAX,1.24);
			if(weapon.getType().toString().contains("BOW")){
				min = r * (Settings.WEAPONS_DMG_MAX - Settings.BOWS_DMG_RANGE_MAX/2);
			}else if(weapon.getType().toString().contains("_SWORD")){
				min = r * (Settings.WEAPONS_DMG_MAX - Settings.SWORDS_DMG_RANGE_MAX/2);
			}else if(weapon.getType().toString().contains("_AXE")){
				min = r * (Settings.WEAPONS_DMG_MAX - Settings.AXES_DMG_RANGE_MAX/2);
			}else if(weapon.getType().toString().contains("_HOE")){
				min = r * (Settings.WEAPONS_DMG_MAX - Settings.WANDS_DMG_RANGE_MAX/2);
			}else min = 1;
			
			switch (weapon.getRClass()){
			case VAGRANT:
				return (int) Math.round(min);
			case PALADIN:
				return (int) Math.round(min*Settings.PALADIN_DMG_FACTOR);
			case MAGE:
				return (int) Math.round(min*Settings.MAGE_DMG_FACTOR);
			case RANGER:
				return (int) Math.round(min*Settings.RANGER_DMG_FACTOR);
			case ASSASSIN:
				return (int) Math.round(min*Settings.ASSASSIN_DMG_FACTOR);
			default:
				return 1;
			}
		}else{
			double r = ((double) weapon.getLevel())/Settings.LEVEL_MAX;
			if(weapon.getType().toString().contains("SHIELD")){
				min = r * (Settings.SHIELDS_DEF_MAX - Settings.SHIELDS_DEF_RANGE_MAX/2);
			}else if(weapon.getType().toString().contains("_HELMET")){
				min = r * (Settings.HELMETS_DEF_MAX - Settings.HELMETS_DEF_RANGE_MAX/2);
			}else if(weapon.getType().toString().contains("_CHESTPLATE")){
				min = r * (Settings.ARMORS_DEF_MAX - Settings.ARMORS_DEF_RANGE_MAX/2);
			}else if(weapon.getType().toString().contains("_LEGGINGS")){
				min = r * (Settings.GAUNTLETS_DEF_MAX - Settings.GAUNTLETS_DEF_RANGE_MAX/2);
			}else if(weapon.getType().toString().contains("_BOOTS")){
				min = r * (Settings.BOOTS_DEF_MAX - Settings.BOOTS_DEF_RANGE_MAX/2);
			}else min = 1;
			
			switch (weapon.getRClass()){
			case VAGRANT:
				return (int) Math.round(min);
			case PALADIN:
				return (int) Math.round(min*Settings.PALADIN_DEF_FACTOR);
			case MAGE:
				return (int) Math.round(min*Settings.MAGE_DEF_FACTOR);
			case RANGER:
				return (int) Math.round(min*Settings.RANGER_DEF_FACTOR);
			case ASSASSIN:
				return (int) Math.round(min*Settings.ASSASSIN_DEF_FACTOR);
			default:
				return 1;
			}
		}
	}
	public static int getAverageMaxStat(Weapon weapon){
		double min = 0;
		if(weapon.isAttack()){
			double r = Math.pow(((double) weapon.getLevel())/Settings.LEVEL_MAX,1.24);
			if(weapon.getType().toString().contains("BOW")){
				min = r * (Settings.WEAPONS_DMG_MAX + Settings.BOWS_DMG_RANGE_MAX/2) + 1;
			}else if(weapon.getType().toString().contains("_SWORD")){
				min = r * (Settings.WEAPONS_DMG_MAX + Settings.SWORDS_DMG_RANGE_MAX/2) + 1;
			}else if(weapon.getType().toString().contains("_AXE")){
				min = r * (Settings.WEAPONS_DMG_MAX + Settings.AXES_DMG_RANGE_MAX/2) + 1;
			}else if(weapon.getType().toString().contains("_HOE")){
				min = r * (Settings.WEAPONS_DMG_MAX + Settings.WANDS_DMG_RANGE_MAX/2) + 1;
			}else min = 1;
			
			switch (weapon.getRClass()){
			case VAGRANT:
				return (int) Math.round(min);
			case PALADIN:
				return (int) Math.round(min*Settings.PALADIN_DMG_FACTOR);
			case MAGE:
				return (int) Math.round(min*Settings.MAGE_DMG_FACTOR);
			case RANGER:
				return (int) Math.round(min*Settings.RANGER_DMG_FACTOR);
			case ASSASSIN:
				return (int) Math.round(min*Settings.ASSASSIN_DMG_FACTOR);
			default:
				return 1;
			}
		}else{
			double r = ((double) weapon.getLevel())/Settings.LEVEL_MAX;
			if(weapon.getType().toString().contains("SHIELD")){
				min = r * (Settings.SHIELDS_DEF_MAX + Settings.SHIELDS_DEF_RANGE_MAX/2) + 1;
			}else if(weapon.getType().toString().contains("_HELMET")){
				min = r * (Settings.HELMETS_DEF_MAX + Settings.HELMETS_DEF_RANGE_MAX/2) + 1;
			}else if(weapon.getType().toString().contains("_CHESTPLATE")){
				min = r * (Settings.ARMORS_DEF_MAX + Settings.ARMORS_DEF_RANGE_MAX/2) + 1;
			}else if(weapon.getType().toString().contains("_LEGGINGS")){
				min = r * (Settings.GAUNTLETS_DEF_MAX + Settings.GAUNTLETS_DEF_RANGE_MAX/2) + 1;
			}else if(weapon.getType().toString().contains("_BOOTS")){
				min = r * (Settings.BOOTS_DEF_MAX + Settings.BOOTS_DEF_RANGE_MAX/2) + 1;
			}else min = 1;
			
			switch (weapon.getRClass()){
			case VAGRANT:
				return (int) Math.round(min);
			case PALADIN:
				return (int) Math.round(min*Settings.PALADIN_DEF_FACTOR);
			case MAGE:
				return (int) Math.round(min*Settings.MAGE_DEF_FACTOR);
			case RANGER:
				return (int) Math.round(min*Settings.RANGER_DEF_FACTOR);
			case ASSASSIN:
				return (int) Math.round(min*Settings.ASSASSIN_DEF_FACTOR);
			default:
				return 1;
			}
		}
	}
	public static double getAverageDropChance(Weapon weapon){
		return Utils.round((1-((double)weapon.getLevel())/Settings.LEVEL_MAX) * Settings.WEAPON_RARITY_MAX + Settings.WEAPON_RARITY_MIN,6);
	}
	public static double getAverageAttackSpeed(Weapon weapon){
		if(weapon.isAttack()){
			switch (weapon.getRClass()){
			case VAGRANT:
				return Utils.round(Math.pow(((double)weapon.getLevel())/Settings.LEVEL_MAX,.8) * Settings.WEAPONS_SPD_MAX + Settings.WEAPONS_SPD_MIN,6);
			case PALADIN:
				return Utils.round((Math.pow(((double)weapon.getLevel())/Settings.LEVEL_MAX,.8) * Settings.WEAPONS_SPD_MAX + Settings.WEAPONS_SPD_MIN)*Settings.PALADIN_SPD_FACTOR,6);
			case MAGE:
				return Utils.round((Math.pow(((double)weapon.getLevel())/Settings.LEVEL_MAX,.8) * Settings.WEAPONS_SPD_MAX + Settings.WEAPONS_SPD_MIN)*Settings.MAGE_SPD_FACTOR,6);
			case RANGER:
				return Utils.round((Math.pow(((double)weapon.getLevel())/Settings.LEVEL_MAX,.8) * Settings.WEAPONS_SPD_MAX + Settings.WEAPONS_SPD_MIN)*Settings.RANGER_SPD_FACTOR,6);
			case ASSASSIN:
				return Utils.round((Math.pow(((double)weapon.getLevel())/Settings.LEVEL_MAX,.8) * Settings.WEAPONS_SPD_MAX + Settings.WEAPONS_SPD_MIN)*Settings.ASSASSIN_SPD_FACTOR,6);
			default:
				return 1;
			}
		}
		return 1;
	}

	public static boolean checkEquipment(RPlayer rp) {
		boolean elytra = false;
		boolean update = false;
		Player player = rp.getPlayer();
		ItemStack helmet = player.getEquipment().getHelmet();
		ItemStack chestplate = player.getEquipment().getChestplate();
		ItemStack gauntlets = player.getEquipment().getLeggings();
		ItemStack boots = player.getEquipment().getBoots();
		ItemStack mainHand = player.getEquipment().getItemInMainHand();
		ItemStack offHand = player.getEquipment().getItemInOffHand();
		if(helmet != null) {
			RItem rItem = new RItem(helmet);
			if(rItem.isWeapon()) {
				Weapon weapon = rItem.getWeapon();
				String usage = weapon.canUse(rp);
				if(!usage.isEmpty()){
					player.getInventory().addItem(helmet);
					player.getInventory().setHelmet(null);
					rp.sendMessage("§cVous ne pouvez porter ce casque car " + usage);
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					update = true;
				}
			} else if(helmet.getType().toString().contains("_HELMET")) {
				player.getInventory().addItem(helmet);
				player.getInventory().setHelmet(null);
				rp.sendMessage("§cCe casque doit être éveillé avant d'être porté");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				update = true;
			}
		}
		if(chestplate != null) {
			RItem rItem = new RItem(chestplate);
			if(rItem.isWeapon()) {
				Weapon weapon = rItem.getWeapon();
				String usage = weapon.canUse(rp);
				if(!usage.isEmpty()){
					player.getInventory().addItem(chestplate);
					player.getInventory().setChestplate(null);
					rp.sendMessage("§cVous ne pouvez porter ce plastron car " + usage);
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					update = true;
				}
			} else if(chestplate.getType().toString().contains("_CHESTPLATE")) {
				player.getInventory().addItem(chestplate);
				player.getInventory().setChestplate(null);
				rp.sendMessage("§cCe plastron doit être éveillé avant d'être porté");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				update = true;
			} else if(chestplate.getType().equals(Material.ELYTRA)) {
				elytra = true;
			}
		}
		if(gauntlets != null) {
			RItem rItem = new RItem(gauntlets);
			if(rItem.isWeapon()) {
				Weapon weapon = rItem.getWeapon();
				String usage = weapon.canUse(rp);
				if(!usage.isEmpty()){
					player.getInventory().addItem(gauntlets);
					player.getInventory().setLeggings(null);
					rp.sendMessage("§cVous ne pouvez porter cette paire de gants car " + usage);
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					update = true;
				}
			} else if(gauntlets.getType().toString().contains("_LEGGINGS")) {
				player.getInventory().addItem(gauntlets);
				player.getInventory().setLeggings(null);
				rp.sendMessage("§cCette paire de gants doit être éveillée avant d'être portée");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				update = true;
			}
		}
		if(boots != null) {
			RItem rItem = new RItem(boots);
			if(rItem.isWeapon()) {
				Weapon weapon = rItem.getWeapon();
				String usage = weapon.canUse(rp);
				if(!usage.isEmpty()){
					player.getInventory().addItem(boots);
					player.getInventory().setBoots(null);
					rp.sendMessage("§cVous ne pouvez porter cette paire de bottes car " + usage);
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					update = true;
				}
			} else if(boots.getType().toString().contains("_BOOTS")) {
				player.getInventory().addItem(boots);
				player.getInventory().setBoots(null);
				rp.sendMessage("§cCette paire de bottes doit être éveillée avant d'être portée");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				update = true;
			}
		}
		if(mainHand != null) {
			RItem rItem = new RItem(mainHand);
			if(rItem.isWeapon()) {
				Weapon weapon = rItem.getWeapon();
				if(weapon.isAttack()) {
					String usage = weapon.canUse(rp);
					if(!usage.isEmpty()){
						player.getInventory().addItem(mainHand);
						player.getInventory().setItemInMainHand(null);
						rp.sendMessage("§cVous ne pouvez utiliser cette arme car " + usage);
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
						update = true;
					}
				}
			}
		}
		if(offHand != null) {
			RItem rItem = new RItem(offHand);
			if(rItem.isWeapon()) {
				Weapon weapon = rItem.getWeapon();
				String usage = weapon.canUse(rp);
				if(!usage.isEmpty()){
					player.getInventory().addItem(offHand);
					player.getInventory().setItemInOffHand(null);
					if(weapon.isAttack()) {
						rp.sendMessage("§cVous ne pouvez utiliser cette arme car " + usage);
					} else if(offHand.getType().equals(Material.SHIELD)) {
						rp.sendMessage("§cVous ne pouvez utiliser ce bouclier car " + usage);
					}
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
					update = true;
				}
			} else if(offHand.getType().equals(Material.SHIELD)) {
				player.getInventory().addItem(offHand);
				player.getInventory().setItemInOffHand(null);
				rp.sendMessage("§cCe bouclier doit être éveillé avant d'être porté");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				update = true;
			}
		}
		
		if(update) {
			Utils.updateInventory(player);
		}
		
		return elytra;
	}
	
	public static void update(final RPlayer rp) {
		new BukkitTask(Core.instance) {//to update player's inventory after event (method is called before the event happens)

			@Override
			public void run() {
				if(rp.isOnline()) {
					Inventory inventory = rp.getPlayer().getInventory();
					for(int slot = 0;slot < inventory.getSize();slot++) {
						ItemStack stack = inventory.getItem(slot);
						if(stack != null) {
							RItem rItem = new RItem(stack);
							if(rItem.isWeapon()) {
								Weapon weapon = rItem.getWeapon();
								inventory.setItem(slot, weapon.updateState(rp, stack));
							} else if(Weapons.COMMON_WEAPON_TYPES.contains(stack.getType())) {
						    	ItemMeta meta = stack.getItemMeta();
						    	meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						    	meta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
						    	stack.setItemMeta(meta);
								inventory.setItem(slot, stack);
							}
						}
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(0);
	}

}
