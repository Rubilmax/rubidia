package me.pmilon.RubidiaMonsters.ui.regions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.levels.RLevels;
import me.pmilon.RubidiaCore.ritems.weapons.Rarity;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.ritems.weapons.Weapons;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaCore.utils.LocationUtils;

import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Region;
import me.pmilon.RubidiaMonsters.regions.Regions;
import me.pmilon.RubidiaMonsters.regions.Region.RegionType;

@SuppressWarnings("deprecation")
public class RegionsUI extends ListMenuUIHandler<Region>{

	public RegionsUI(Player p) {
		super(p, "Regions", "Regions", 5);
		for(RegionType type : RegionType.values()){
			List<Region> regions = new ArrayList<Region>();
			for(Region region : Regions.regions){
				if(!region.isDungeonRoom() && region.getType().equals(type)){
					regions.add(region);
				}
			}
			Collections.sort(regions, new Comparator<Region>() {
		        public int compare(Region r1, Region r2) {
		            return (int) (r1.getMediumLevel())-(int) (r2.getMediumLevel());
		        }
		    });
			this.list.addAll(regions);
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack infos = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = infos.getItemMeta();
		meta.setDisplayName("§8Informations");
		meta.setLore(Arrays.asList(("§7Choisissez une région à éditer"), ("§7ou cliquez ici pour en créer une nouvelle")));
		infos.setItemMeta(meta);
		return infos;
	}

	@Override
	protected ItemStack getItem(Region region) {
		Wool wool = new Wool(region.getType().getDyeColor());
		ItemStack item = wool.toItemStack(1);
		ItemMeta meta = item.getItemMeta();
		String name = "";
		for(Monster monster : region.getMonsters()){
			name += "§f§l" + monster.getName() + (region.getMonsters().indexOf(monster) != region.getMonsters().size()-1 ? " §7/ " : "");
		}
		meta.setDisplayName(name);
		List<String> lore = new ArrayList<String>();
		double averageHP = 0;
		double averageDef = 0;
		double averageAtq = 0;
		double averageLevel = 0;
		double averageXP = 0;
		double averageHit = 0;
		double averageRage = 0;
		double damagesAtAverageLevel = 0;
		if(region.entities.size() > 0){
			for(Monster monster : region.entities){
				averageLevel += monster.getLevel();
				averageXP += monster.getLevel()*monster.getXPFactor();
				averageHP += monster.getLevel()*monster.getHealthFactor();
				averageAtq += monster.getDamages();
				averageDef += (DamageManager.getDamageResistance(RDamageCause.MELEE, monster.getEntity(), true)+DamageManager.getDamageResistance(RDamageCause.MAGIC, monster.getEntity(), true)+DamageManager.getDamageResistance(RDamageCause.RANGE, monster.getEntity(), true))/3.0;
				if(monster.isEnraged())averageRage++;
			}
			averageLevel /= region.entities.size();
			averageXP /= region.entities.size();
			averageHP /= region.entities.size();
			averageDef /= region.entities.size();
			averageAtq /= region.entities.size();
			averageRage /= region.entities.size();
			Weapon weapon = Weapons.nearest((int)averageLevel, true, null, null, Rarity.COMMON);
			damagesAtAverageLevel = weapon != null ? ((weapon.getMinDamages()+weapon.getMaxDamages())/2.0)*(10.0/averageDef) : 0.0;
			averageHit = damagesAtAverageLevel > 0 ? me.pmilon.RubidiaCore.utils.Utils.round(averageHP/damagesAtAverageLevel,2) : 0.0;
		}
		double xpAtAverageLevel = RLevels.getRLevelTotalExp((int) averageLevel);
		double levelDiff = averageLevel-(int)((region.getMinLevel()+region.getMaxLevel())/2.0);
		double suggestXPPercent = 1.0/(10+470.0*(averageLevel/150.0));
		double suggestHit = 1.5+6.5*(averageLevel/150.0);
		double suggestMobHit = 12.0-9.0*(averageLevel/150.0);
		double defenseAtAverageLevel = 0;
		for(String type : new String[]{"_HELMET","_CHESTPLATE","_LEGGINGS","_BOOTS"}){
			Weapon weapon = Weapons.nearest((int)averageLevel, false, RClass.VAGRANT, type, Rarity.COMMON);
			if(weapon != null)defenseAtAverageLevel+=DamageManager.getDefensePoint(weapon.getNewItemStack(null), RDamageCause.MELEE, true);
		}
		double suggestMobDamages = defenseAtAverageLevel > 0 ? (20.0/suggestMobHit)*(defenseAtAverageLevel/10.0) : 0;
		boolean xp = averageXP/xpAtAverageLevel >= suggestXPPercent*.8 && averageXP/xpAtAverageLevel <= suggestXPPercent*1.2;
		boolean hp = averageHit >= suggestHit*.8 && averageHit <= suggestHit*1.2;
		boolean d = averageAtq >= suggestMobDamages*.8 && averageAtq <= suggestMobDamages*1.2;
		lore.addAll(Arrays.asList("§8Dimensions:  §f" + region.getXRange() + " " + region.getYRange() + " " + region.getZRange() + "  §8(§f" + (region.isSquare() ? "square" : "ellipse") + "§8)",
								"§8Level: §f" + region.getMinLevel() + " §8- §f" + region.getMaxLevel() + "  §8(average: §f" + me.pmilon.RubidiaCore.utils.Utils.round(region.getMediumLevel(),1) + "§8)",
								"§8Fading level: §f" + region.isFadingLevel(),
								"§8Rage probability: §f" + region.getRageProbability()*100 + "%",
								"§8YShift: §f" + region.getYShift(),
								"§7§m---------------------------------------",
								"§8Monsters amount: §f" + region.entities.size() + "§8/§f" + region.getMaxMonstersAmount(),
								"§8Average rage: §f" + me.pmilon.RubidiaCore.utils.Utils.round(averageRage*100,2) +"%",
								"§8Average level: §f" + me.pmilon.RubidiaCore.utils.Utils.round(averageLevel,2) + "  §8(=§f" + me.pmilon.RubidiaCore.utils.Utils.round(xpAtAverageLevel,2) + "XP§8) (" + (levelDiff > 0 ? "§a+" + me.pmilon.RubidiaCore.utils.Utils.round(levelDiff,2) : "§c" + me.pmilon.RubidiaCore.utils.Utils.round(levelDiff,2)) + "§8)",
								"§8Average XP: §f" + me.pmilon.RubidiaCore.utils.Utils.round(averageXP,2) + "  §8(=" + (xp ? "§a" : "§c") + me.pmilon.RubidiaCore.utils.Utils.round(averageXP*100.0/xpAtAverageLevel,2) + "%§8) (suggested: §f" + me.pmilon.RubidiaCore.utils.Utils.round(suggestXPPercent*100,2) + "%§8)",
								"§8Average HP: §f" + me.pmilon.RubidiaCore.utils.Utils.round(averageHP,2),
								"§8Average ATQ: " + (d ? "§a" : "§c") + me.pmilon.RubidiaCore.utils.Utils.round(averageAtq,2) + " §8(suggested: §f" + me.pmilon.RubidiaCore.utils.Utils.round(suggestMobDamages,2) + "§8)",
								"§8Average DEF: §f" + me.pmilon.RubidiaCore.utils.Utils.round(averageDef,2),
								"§8Average hits: " + (hp ? "§a" : "§c") + me.pmilon.RubidiaCore.utils.Utils.round(averageHit,2) + " §8(suggested: §f" + me.pmilon.RubidiaCore.utils.Utils.round(suggestHit,2) + "§8)"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player arg1, ItemStack is) {
		if(is != null){
			int slot = e.getRawSlot();
			if(e.isRightClick()){
				/*Region region = this.get(slot);
				if(region.watchersTasks.containsKey(this.getHolder())){
					this.get(slot).stopShowingPlayer(this.getHolder());
				}else{
					this.get(slot).showPlayer(this.getHolder());
				}*/
				TeleportHandler.teleport(this.getHolder(), this.get(slot).getCenter());
			}else{
				Core.uiManager.requestUI(new RegionManager(this.getHolder(), this.get(slot)));
			}
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Region region = new Region(UUID.randomUUID().toString(), LocationUtils.getCenter(this.getHolder().getLocation()), 5, 514, 5, new ArrayList<Monster>(), true, 1, 10, .2, true, 0, 0, null, RegionType.DEFAULT);
		region.setMaxMonstersAmount((int) Math.round(region.getSize()/66));
		Regions.regions.add(region);
		Core.uiManager.requestUI(new RegionManager(this.getHolder(), region));
	}

	@Override
	protected void onOpen() {
	}

	@Override
	public String getType() {
		return "REGIONS_LIST_MENU";
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}

}
