package me.pmilon.RubidiaMonsters.regions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.attacks.AbstractAttack;
import me.pmilon.RubidiaMonsters.attacks.AbstractAttack.AttackType;
import me.pmilon.RubidiaMonsters.utils.Configs;

public class Monsters {

	public static List<Monster> monsters = new ArrayList<Monster>();
	public static HashMap<UUID, Monster> entities = new HashMap<UUID, Monster>();
	
	@SuppressWarnings("unchecked")
	public static void onEnable(boolean debug){
		if (debug) RubidiaMonstersPlugin.console.sendMessage("§a    Loading Monsters...");
		if(Configs.getMonstersConfig().contains("monsters")){
			for(String mId : Configs.getMonstersConfig().getConfigurationSection("monsters").getKeys(false)){
				String path = "monsters." + mId;
				
				List<Drop> drops = new ArrayList<Drop>();
				if(Configs.getMonstersConfig().contains(path + ".drops")){
					for(String dId : Configs.getMonstersConfig().getConfigurationSection(path + ".drops").getKeys(false)){
						int dropId = Integer.valueOf(dId);
						String dropPath = path + ".drops." + dropId;
						Drop drop = new Drop(dropId,
								Configs.getMonstersConfig().getItemStack(dropPath + ".item"),
								Configs.getMonstersConfig().getDouble(dropPath + ".probability"));
						drops.add(drop);
					}
				}
				
				List<AbstractAttack> attacks = new ArrayList<AbstractAttack>();
				if(Configs.getMonstersConfig().contains(path + ".attacks")){
					for(String stype : Configs.getMonstersConfig().getConfigurationSection(path + ".attacks").getKeys(false)){
						AttackType type = AttackType.valueOf(stype);
						String attackPath = path + ".attacks." + stype;
						List<Double> factrs = (List<Double>) Configs.getMonstersConfig().get(attackPath + ".factors");
						double[] factors = new double[factrs.size()];
						for(int i = 0;i < factors.length;i++){
							factors[i] = factrs.get(i);
						}
						List<Double> wghts = (List<Double>) Configs.getMonstersConfig().get(attackPath + ".weights");
						double[] weights = new double[wghts.size()];
						for(int i = 0;i < weights.length;i++){
							weights[i] = wghts.get(i);
						}
						AbstractAttack attack = new AbstractAttack(type,
								Configs.getMonstersConfig().getInt(attackPath + ".cooldown"),
								Configs.getMonstersConfig().getDouble(attackPath + ".learningFactor"),
								weights, factors);
						attacks.add(attack);
					}
				}
				Monster monster = new Monster(mId,
						Configs.getMonstersConfig().getString(path + ".name"),
						EntityType.valueOf(Configs.getMonstersConfig().getString(path + ".type")),
						Configs.getMonstersConfig().getDouble(path + ".xpFactor"),
						Configs.getMonstersConfig().getDouble(path + ".healthFactor"),
						Configs.getMonstersConfig().getDouble(path + ".damagesFactor"),
						drops, Configs.getMonstersConfig().getBoolean(path + ".average"), attacks);
				monsters.add(monster);
				if(Utils.isInteger(mId)){
					Configs.getMonstersConfig().set(path, null);
					monster.setUUID(UUID.randomUUID().toString());
				}
				if(Monsters.monsters.size() % 25 == 0){
					if(debug)RubidiaMonstersPlugin.console.sendMessage("§6LOADED §e" + Monsters.monsters.size() + " §6MONSTERS");
				}
			}
		}
		if(debug)RubidiaMonstersPlugin.console.sendMessage("§6LOADED §e" + Monsters.monsters.size() + " §6MONSTERS");
	}
	
	public static void save(boolean debug){
		if (debug) RubidiaMonstersPlugin.console.sendMessage("§a    Saving Monsters...");
		for(Monster monster : monsters){
			String path = "monsters." + monster.getUUID();
			Configs.getMonstersConfig().set(path + ".name", monster.getName());
			Configs.getMonstersConfig().set(path + ".type", monster.getType().toString());
			Configs.getMonstersConfig().set(path + ".xpFactor", monster.getXPFactor());
			Configs.getMonstersConfig().set(path + ".healthFactor", monster.getHealthFactor());
			Configs.getMonstersConfig().set(path + ".damagesFactor", monster.getDamagesFactor());
			Configs.getMonstersConfig().set(path + ".average", monster.isAverage());
			for(Drop drop : monster.getDrops()){
				String path2 = path + ".drops." + drop.getIndex();
				Configs.getMonstersConfig().set(path2 + ".item", drop.getItem());
				Configs.getMonstersConfig().set(path2 + ".probability", drop.getProbability());
			}
			for(AbstractAttack attack : monster.getAttacks()){
				String path2 = path + ".attacks." + attack.getType().toString();
				Configs.getMonstersConfig().set(path2 + ".cooldown", attack.getCooldown());
				Configs.getMonstersConfig().set(path2 + ".learningFactor", attack.getLearningFactor());
				Configs.getMonstersConfig().set(path2 + ".factors", attack.getFactors());
				Configs.getMonstersConfig().set(path2 + ".weights", attack.getWeights());
			}
			if(debug)RubidiaMonstersPlugin.console.sendMessage("§6Saved §e" + monster.getName() + " §6| §e" + monster.getType().toString());
		}
		Configs.saveMonstersConfig();
	}
	
	public static Monster get(String uuid){
		for(Monster monster : Monsters.monsters){
			if(monster.getUUID().equals(uuid)){
				return monster;
			}
		}
		return null;
	}
	
	public static Monster get(LivingEntity entity){
		if(Monsters.entities.containsKey(entity.getUniqueId())){
			return Monsters.entities.get(entity.getUniqueId());
		}
		
		return null;
	}
	
}
