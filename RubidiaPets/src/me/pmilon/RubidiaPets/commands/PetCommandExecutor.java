package me.pmilon.RubidiaPets.commands;

import java.util.ArrayList;
import java.util.UUID;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.utils.RandomUtils;
import me.pmilon.RubidiaPets.pets.Pearl;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaPets.pets.Pets;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.inventory.ItemStack;

public class PetCommandExecutor implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command argcmd1, String label,
			String[] args) {
		if(sender.isOp()){
			if(args.length > 0){
				RPlayer rp = RPlayer.getFromName(args[0]);
				if(rp != null){
					if(args.length > 1){
						EntityType type = null;
						try{
							type = EntityType.valueOf(args[1].toUpperCase());
						}catch(Exception e){
							sender.sendMessage("§4" + args[1].toUpperCase() + "§c is not a valid entity type! See existing:");
							String types = "§c";
							for(EntityType tpe : EntityType.values()){
								if(tpe.getEntityClass() != null && Creature.class.isAssignableFrom(tpe.getEntityClass()))types += tpe.toString() + ", ";
							}
							sender.sendMessage(types);
							return false;
						}
						if(type != null && type.getEntityClass() != null && Creature.class.isAssignableFrom(type.getEntityClass())){
							String name;
							if(args.length > 2)name = args[2];
							else name = "PET" + RandomUtils.random.nextInt(10000);
							Pet rppet = null;
							for(Pet pet : rp.getPets()){
								if(pet.getName().equals(name)){
									rppet = pet;
									break;
								}
							}
							if(rppet != null){
								rppet.destroy();
								rp.getPets().remove(rppet);
								sender.sendMessage("§4" + name + " §cdoes no longer exist... Sad choice :'(");
							}else{
								Pet pet = new Pet(UUID.randomUUID().toString(), name, 0, 0.0, 20.0, 0, 0, 0, 0, type, 0, false, new ArrayList<Pearl>(), false, DyeColor.BLACK, Color.BLACK, Style.BLACK_DOTS, 0, new ItemStack(Material.AIR,1), Rabbit.Type.BLACK, Ocelot.Type.BLACK_CAT, Parrot.Variant.BLUE);
								Pets.pets.add(pet);
								rp.getPets().add(pet);
								sender.sendMessage("§4§l" + rp.getName() + " §ahas been given §4" + name + " §a!");
								rp.sendMessage("§aVous avez reçu un nouveau compagnon ! Vous pouvez le sortir en utilisant /pets.");
							}
						}else sender.sendMessage("§cThis type of pet is invalid.");
					}else sender.sendMessage("§cPlease use /pet " + args[0] + "[type] (name)");
				}else sender.sendMessage("§4" + args[0] + " §chas never been on this server!");
			}else sender.sendMessage("§cPlease use /pet [player] [type] (name)");
		}else sender.sendMessage("§cYou really thought you could do that without being operator?");
		return true;
	}

}
