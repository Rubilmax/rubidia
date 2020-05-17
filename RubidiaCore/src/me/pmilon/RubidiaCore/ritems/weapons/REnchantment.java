package me.pmilon.RubidiaCore.ritems.weapons;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;

public class REnchantment {

	public static List<Enchantment> WEAPONS_ENCHANTMENTS = Arrays.asList(Enchantment.DAMAGE_ALL,
															Enchantment.DAMAGE_ARTHROPODS,
															Enchantment.DAMAGE_UNDEAD,
															Enchantment.FIRE_ASPECT,
															Enchantment.KNOCKBACK,
															Enchantment.LOOT_BONUS_MOBS,
															Enchantment.LUCK);
	
	public static Enchantment SOUL_BIND = new Enchantment(new NamespacedKey(Core.instance, "soul_bind")){

		@Override
		public boolean canEnchantItem(ItemStack item) {
			return true;
		}

		@Override
		public boolean conflictsWith(Enchantment arg0) {
			return false;
		}

		@Override
		public EnchantmentTarget getItemTarget() {
			return EnchantmentTarget.ALL;
		}

		@Override
		public int getMaxLevel() {
			return 5;
		}

		@Override
		public String getName() {
			return "SOUL_BIND";
		}

		@Override
		public int getStartLevel() {
			return 1;
		}

		@Override
		public boolean isCursed() {
			return false;
		}

		@Override
		public boolean isTreasure() {
			return false;
		}
		
	};
	
	public static void registerEnchantments(){
		if (Enchantment.getByKey(REnchantment.SOUL_BIND.getKey()) == null) {
			try {
			    Field f = Enchantment.class.getDeclaredField("acceptingNew");
			    f.setAccessible(true);
			    f.set(null, true);
			} catch (Exception e) {
			    e.printStackTrace();
			}
			Enchantment.registerEnchantment(REnchantment.SOUL_BIND);
			Enchantment.stopAcceptingRegistrations();
			System.out.println("Registered enchantment: " + REnchantment.SOUL_BIND.getKey().getKey());
		}
	}
	
	public static Enchantment[] values(){
		return new Enchantment[]{Enchantment.ARROW_DAMAGE,
				Enchantment.ARROW_FIRE,
				Enchantment.ARROW_INFINITE,
				Enchantment.ARROW_KNOCKBACK,
				Enchantment.DAMAGE_ALL,
				Enchantment.DAMAGE_ARTHROPODS,
				Enchantment.DAMAGE_UNDEAD,
				Enchantment.DEPTH_STRIDER,
				Enchantment.DIG_SPEED,
				Enchantment.DURABILITY,
				Enchantment.FIRE_ASPECT,
				Enchantment.FROST_WALKER,
				Enchantment.KNOCKBACK,
				Enchantment.LOOT_BONUS_BLOCKS,
				Enchantment.LOOT_BONUS_MOBS,
				Enchantment.LUCK,
				Enchantment.LURE,
				Enchantment.MENDING,
				Enchantment.OXYGEN,
				Enchantment.PROTECTION_ENVIRONMENTAL,
				Enchantment.PROTECTION_EXPLOSIONS,
				Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,
				Enchantment.PROTECTION_PROJECTILE,
				Enchantment.SILK_TOUCH,
				Enchantment.THORNS,
				Enchantment.WATER_WORKER,
				REnchantment.SOUL_BIND};
	}

	public static int cost(Player player, ItemStack item, int level) {
		RPlayer rp = RPlayer.get(player);
		int DCOST = (rp.isVip() ? 47 : 59)+level*8;
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if(meta.hasEnchants()) {
				String m = item.getType().toString();
				Map<Enchantment, Integer> enchantms = meta.getEnchants();
				if(m.contains("WOODEN_")){
					for(Enchantment enchant : REnchantment.values()){
						if(enchantms.containsKey(enchant)){
							if(rp.isVip())DCOST += level*(enchantms.get(enchant));
							else DCOST += (level+1)*(enchantms.get(enchant));
						}
					}
				}else if(m.contains("STONE_")){
					for(Enchantment enchant : REnchantment.values()){
						if(enchantms.containsKey(enchant)){
							if(rp.isVip())DCOST += level*2*(enchantms.get(enchant));
							else DCOST += (level*2+2)*(enchantms.get(enchant));
						}
					}
				}else if(m.contains("IRON_")){
					for(Enchantment enchant : REnchantment.values()){
						if(enchantms.containsKey(enchant)){
							if(rp.isVip())DCOST += (level*4+1)*(enchantms.get(enchant));
							else DCOST += (level*5+1)*(enchantms.get(enchant));
						}
					}
				}else if(m.contains("DIAMOND_")){
					for(Enchantment enchant : REnchantment.values()){
						if(enchantms.containsKey(enchant)){
							if(rp.isVip())DCOST += (level*6-1)*(enchantms.get(enchant));
							else DCOST += (level*7-1)*(enchantms.get(enchant));
						}
					}
				}else if(m.contains("GOLD_")){
					for(Enchantment enchant : REnchantment.values()){
						if(enchantms.containsKey(enchant)){
							if(rp.isVip())DCOST += (level*8+2)*(enchantms.get(enchant));
							else DCOST += (level*9+3)*(enchantms.get(enchant));
						}
					}
				}else{
					for(Enchantment enchant : REnchantment.values()){
						if(enchantms.containsKey(enchant)){
							if(rp.isVip())DCOST += level*3*(enchantms.get(enchant));
							else DCOST += (level*4-1)*(enchantms.get(enchant));
						}
					}
				}
			}
		}
		return (DCOST < 0 ? 0 : DCOST)*item.getAmount();
	}
	
}
