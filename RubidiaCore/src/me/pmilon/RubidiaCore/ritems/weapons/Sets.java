package me.pmilon.RubidiaCore.ritems.weapons;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Utils;

public class Sets {

	public static List<Set> sets = new ArrayList<Set>();
	
	public static void onEnable(boolean debug){
		sets.clear();
		if(Configs.getWeaponsConfig().contains("sets")){
			for(String set : Configs.getWeaponsConfig().getConfigurationSection("sets").getKeys(false)){
				String path = "sets." + set;

				List<Weapon> weapons = new ArrayList<Weapon>();
				if(Configs.getWeaponsConfig().contains(path + ".weaponsUUIDs")){
					for(String uuid : Configs.getWeaponsConfig().getStringList(path + ".weaponsUUIDs")){
						weapons.add(Weapons.getByUUID(uuid));
					}
				}
				
				List<Buff> buffs = new ArrayList<Buff>();
				if(Configs.getWeaponsConfig().contains(path + ".buffs")){
					for(String buff : Configs.getWeaponsConfig().getConfigurationSection(path + ".buffs").getKeys(false)){
						int buffId = Integer.valueOf(buff);
						buffs.add(new Buff(buffId,
								BuffType.valueOf(Configs.getWeaponsConfig().getString(path + ".buffs." + buff + ".type")),
								Configs.getWeaponsConfig().getInt(path + ".buffs." + buff + ".level")));
					}
				}
				
				Set fSet = new Set(set,
						Configs.getWeaponsConfig().getString(path + ".name"),
						buffs,
						weapons);
				Sets.sets.add(fSet);
				if(Utils.isInteger(set)){
					Configs.getWeaponsConfig().set(path, null);
					fSet.setUUID(UUID.randomUUID().toString());
				}
				if(debug)Core.console.sendMessage("§6Set Loaded : §e" + fSet.getName());
			}
		}
	}
	
	public static void onDisable(){
		for(Set set : sets){
			if(set.isModified()){
				set.setModified(false);
				String path = "sets." + set.getUUID();
				Configs.getWeaponsConfig().set(path + ".name", set.getName());
				set.saveWeapons();
				set.saveBuffs();
			}
		}
	}
	
	public static Set get(String uuid){
		for(Set set : sets){
			if(set.getUUID().equals(uuid)){
				return set;
			}
		}
		return null;
	}
	
}
