package me.pmilon.RubidiaPets.pets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaPets.PetsPlugin;
import me.pmilon.RubidiaPets.utils.Configs;

public class Pets {
	
	public static final List<Pet> pets = new ArrayList<Pet>();
	
	public static void onEnable(boolean debug){
		if (debug) PetsPlugin.console.sendMessage("§a   Loading Pets...");
		if(Configs.getPetsConfig().contains("pets")){
			for(String uuid : Configs.getPetsConfig().getConfigurationSection("pets").getKeys(false)){
				List<Pearl> pearls = new ArrayList<Pearl>();
				for(String pearl : Configs.getPetsConfig().getStringList("pets." + uuid + ".activePearls")){
					Pearl perl = Pearl.fromString(pearl);
					if(perl != null)pearls.add(perl);
				}
				Pet pet = new Pet(uuid,
						Configs.getPetsConfig().getString("pets." + uuid + ".name"),
						Configs.getPetsConfig().getInt("pets." + uuid + ".level"),
						Configs.getPetsConfig().getDouble("pets." + uuid + ".exp"),
						Configs.getPetsConfig().getDouble("pets." + uuid + ".health"),
						Configs.getPetsConfig().getInt("pets." + uuid + ".distinctionPoints"),
						Configs.getPetsConfig().getInt("pets." + uuid + ".ardor"),
						Configs.getPetsConfig().getInt("pets." + uuid + ".patience"),
						Configs.getPetsConfig().getInt("pets." + uuid + ".acuity"),
						EntityType.valueOf(Configs.getPetsConfig().getString("pets." + uuid + ".type")),
						Configs.getPetsConfig().getInt("pets." + uuid + ".age"),
						Configs.getPetsConfig().getBoolean("pets." + uuid + ".saddle"), pearls,
						Configs.getPetsConfig().getBoolean("pets." + uuid + ".active"),
						Configs.getPetsConfig().getString("pets." + uuid + ".wolf.collarColor").equals("SILVER") ? DyeColor.LIGHT_GRAY : DyeColor.valueOf(Configs.getPetsConfig().getString("pets." + uuid + ".wolf.collarColor")),
						Color.valueOf(Configs.getPetsConfig().getString("pets." + uuid + ".horse.color")),
						Style.valueOf(Configs.getPetsConfig().getString("pets." + uuid + ".horse.style")),
						Configs.getPetsConfig().getInt("pets." + uuid + ".horse.domestication"),
						(ItemStack) Configs.getPetsConfig().get("pets." + uuid + ".horse.armor"),
						Rabbit.Type.valueOf(Configs.getPetsConfig().getString("pets." + uuid + ".rabbit.type")),
						Cat.Type.valueOf(Configs.getPetsConfig().getString("pets." + uuid + ".cat.type")),
						Configs.getPetsConfig().contains("pets." + uuid + ".parrot.type") ? Parrot.Variant.valueOf(Configs.getPetsConfig().getString("pets." + uuid + ".parrot.type")) : Parrot.Variant.BLUE);
				pets.add(pet);
				if(pet.getType() == null || pet.getType().getEntityClass() == null || !Creature.class.isAssignableFrom(pet.getType().getEntityClass())){
					pet.destroy();
				}
				if(Pets.pets.size() % 100 == 0){
					if(debug)PetsPlugin.console.sendMessage("§6LOADED §e" + Pets.pets.size() + " §6PETS");
				}
			}
		}
		if(debug)PetsPlugin.console.sendMessage("§6LOADED §e" + Pets.pets.size() + " §6PETS");
	}
	
	public static Pet get(String UUID){
		for(Pet pet : pets){
			if(pet.getUUID().equals(UUID)){
				return pet;
			}
		}
		return null;
	}
	
	public static Pet get(Entity entity){
		if(entity.hasMetadata("pet")){
			if(!entity.getMetadata("pet").isEmpty()){
				for(Pet pet : pets){
					if(entity.getMetadata("pet").get(0).asString().equals(pet.getUUID())){
						return pet;
					}
				}
			}
		}
		return null;
	}
	
	public static void save(boolean debug){
		if (debug) PetsPlugin.console.sendMessage("§a   Saving Pets...");
		for(Pet pet : pets){
			String path = "pets." + pet.getUUID();
			Configs.getPetsConfig().set(path + ".name", pet.getName());
			Configs.getPetsConfig().set(path + ".level", pet.getLevel());
			Configs.getPetsConfig().set(path + ".exp", pet.getExp());
			Configs.getPetsConfig().set(path + ".health", pet.getHealth());
			Configs.getPetsConfig().set(path + ".distinctionPoints", pet.getDistinctionPoints());
			Configs.getPetsConfig().set(path + ".ardor", pet.getArdor());
			Configs.getPetsConfig().set(path + ".patience", pet.getPatience());
			Configs.getPetsConfig().set(path + ".acuity", pet.getAcuity());
			Configs.getPetsConfig().set(path + ".type", pet.getType().toString());
			Configs.getPetsConfig().set(path + ".age", pet.getAge());
			Configs.getPetsConfig().set(path + ".saddle", pet.isSaddle());
			Configs.getPetsConfig().set(path + ".active", pet.isActive());
			Configs.getPetsConfig().set(path + ".wolf.collarColor", pet.getCollarColor().toString());
			Configs.getPetsConfig().set(path + ".horse.color", pet.getColor().toString());
			Configs.getPetsConfig().set(path + ".horse.style", pet.getStyle().toString());
			Configs.getPetsConfig().set(path + ".horse.domestication", pet.getDomestication());
			Configs.getPetsConfig().set(path + ".horse.armor", pet.getArmor());
			Configs.getPetsConfig().set(path + ".rabbit.type", pet.getRabbitType().toString());
			Configs.getPetsConfig().set(path + ".cat.type", pet.getCatType().toString());
			Configs.getPetsConfig().set(path + ".parrot.type", pet.getParrotType().toString());
			List<String> pearls = new ArrayList<String>();
			for(Pearl pearl : pet.getActivePearls()){
				if(pearl != null)pearls.add(pearl.toString());
			}
			Configs.getPetsConfig().set(path + ".activePearls", pearls);
			if(debug)PetsPlugin.console.sendMessage("§eSaved §6" + pet.getName());
		}
		Configs.savePetsConfig();
	}
	
}
