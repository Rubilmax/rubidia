package me.pmilon.RubidiaCore.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class Utils {

	public static final long MILLIS_IN_YEAR = 31556952000L;
	
	public static void updateInventory(final Player p){
		new BukkitTask(Core.instance){
			public void run(){
				p.updateInventory();
				RPlayer.get(p).updateVigor();
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(0);
	}
	
	public static Entity getEntityFromUUID(World w, String uuid){
		for(Entity entity : w.getEntities()){
			if(entity.getUniqueId().toString().equals(uuid))return entity;
		}
		return null;
	}
	
	@Deprecated
	public static List<LivingEntity> getInSightEntities(Player player, RDamageCause cause){
		List<LivingEntity> entities = DamageManager.toDamageableEntityList(player.getWorld().getEntities());
		List<LivingEntity> inSight = new ArrayList<LivingEntity>();
		for(LivingEntity entity : entities){
			if(DamageManager.canDamage(player, entity, cause)) {
				if(player.hasLineOfSight(entity)) {
					inSight.add(entity);
				}
			}
		}
		return inSight;
	}
	
	public static LivingEntity getInSightDamageableEntity(Player player, RDamageCause cause, double range){
		Vector direction = player.getEyeLocation().getDirection().normalize().multiply(.5);
		Location check = player.getEyeLocation().add(direction);
		List<Chunk> chunks = new ArrayList<Chunk>();
		for(int i = 0;i < range/.5;i++){
			Chunk chunk = check.getChunk();
			if(!chunks.contains(chunk)) {
				for(LivingEntity entity : DamageManager.toDamageableEntityList(Arrays.asList(chunk.getEntities()))) {
					if(DamageManager.canDamage(player, entity, cause)) {
						if(!entity.isDead() && (check.distanceSquared(entity.getLocation()) < .5 || check.distanceSquared(entity.getEyeLocation()) < .5)) {
							return entity;
						}
					}
				}
				chunks.add(chunk);
			}
			check.add(direction);
		}
		return null;
	}

	public static ItemStack setGlowingWithoutAttributes(ItemStack is){
		ItemMeta meta = is.getItemMeta();
		meta.addEnchant(Enchantment.LUCK, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(meta);
		return is;
	}

	public static boolean addSafeBuff(Player p, PotionEffect buff){
		if(p.hasPotionEffect(buff.getType())){
			for(PotionEffect active : p.getActivePotionEffects()){
				if(active.getType().equals(buff.getType())){
					if(active.getAmplifier() <= buff.getAmplifier()){
						p.addPotionEffect(buff);
						return true;
					}
				}
			}
			return false;
		}else{
			p.addPotionEffect(buff);
			return true;
		}
	}
	
	public static Object hashMapKeyOf(HashMap<?,?> map, Object value){
		for(Object k : map.keySet()){
			if(map.get(k).equals(value))return k;
		}
		return null;
	}
	public static boolean isInteger(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(Exception ex){return false;}
	}
	public static boolean isDouble(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch(Exception ex){return false;}
	}

	public static String valuesOf(Class<?> enum1){
		String result = "";
		Object[] list = enum1.getEnumConstants();
		for(int i = 0;i < list.length;i++){
			result += list[i];
			if(i != list.length-1)result += ", ";
		}
		return result;
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	public static <E> List<E> getModifiableCopy(List<E> list){
		List<E> newList = new ArrayList<E>();
		newList.addAll(list);
		return newList;
	}
	
	public static int getAmountCanHold(Player player, ItemStack itemStack){
		int amount = 0;
		for(int slot = 0;slot < 36;slot++){
			if(slot != 8){
				ItemStack item = player.getInventory().getItem(slot);
				if(item == null || item.getType().equals(Material.AIR))amount += 64;
				else if(item.isSimilar(itemStack))amount += 64-item.getAmount();
			}
		}
		return amount;
	}

}
