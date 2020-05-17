package me.pmilon.RubidiaQuests.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.handlers.TeleportHandler;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaMonsters.regions.Monster;
import me.pmilon.RubidiaMonsters.regions.Monsters;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.utils.Configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class QEvent {

	private String questUUID;
	private  int index;
	private QEventType type;
	private int amount;
	private double range;
	private Location location;
	private String monsterUUID;
	private int monsterLevel;
	private PotionEffect potionEffect;
	private List<Block> blocks;
	private ItemStack itemStack;
	
	private boolean modified;
	public HashMap<RPlayer, List<LivingEntity>> monsters = new HashMap<RPlayer, List<LivingEntity>>();

	public QEvent(String questUUID, int index, QEventType type, int amount, double range, Location location, String monsterUUID, int monsterLevel, PotionEffect potionEffect, List<Block> blocks, ItemStack itemStack, boolean modified){
		this.questUUID = questUUID;
		this.index = index;
		this.type = type;
		this.amount = amount;
		this.range = range;
		this.location = location;
		this.monsterUUID = monsterUUID;
		this.monsterLevel = monsterLevel;
		this.potionEffect = potionEffect;
		this.blocks = blocks;
		this.itemStack = itemStack;
		this.modified = modified;
	}
	
	public void save(boolean debug){
		if(this.isModified()){
			this.setModified(false);
			String path = this.getQuestPath() + "." + this.getIndex();
			Configs.getQuestsConfig().set(path + ".type", this.getType().toString());
			Configs.getQuestsConfig().set(path + ".amount", this.getAmount());
			Configs.getQuestsConfig().set(path + ".range", this.getRange());
			Configs.getQuestsConfig().set(path + ".location", this.getLocation());
			Configs.getQuestsConfig().set(path + ".monsterUUID", this.getMonsterUUID());
			Configs.getQuestsConfig().set(path + ".monsterLevel", this.getMonsterLevel());
			Configs.getQuestsConfig().set(path + ".potionEffect", this.getPotionEffect());
			Configs.getQuestsConfig().set(path + ".itemStack", this.getItemStack());
			List<String> blocks = new ArrayList<String>();
			for(Block block : this.getBlocks())blocks.add(block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ());
			Configs.getQuestsConfig().set(path + ".blocks", blocks);
			if(debug)QuestsPlugin.console.sendMessage("    §6QEvent §e" + this.getIndex() + " :: " + this.getType().toString());
		}
	}
	
	public void delete(){
		String path = this.getQuestPath() + "." + this.getIndex();
		Configs.getQuestsConfig().set(path, null);
	}
	
	public void doEvent(RPlayer rp){
		if(this.getType().equals(QEventType.TELEPORTATION)){
			if(rp.isOnline()){
				Player player = rp.getPlayer();
				TeleportHandler.teleport(player, this.getLocation());
			}
		}else if(this.getType().equals(QEventType.EFFECT)){
			if(rp.isOnline()){
				final Player player = rp.getPlayer();
				Bukkit.getScheduler().runTaskLater(QuestsPlugin.instance, new Runnable(){//delay to avoid blindness remove with dialogEnd
					public void run(){
						player.addPotionEffect(getPotionEffect());
					}
				}, 1);
			}
		}else if(this.getType().equals(QEventType.SPAWN)){
			Location location = this.getLocation();
			Random random = new Random();
			List<LivingEntity> mobs = new ArrayList<LivingEntity>();
			for(int i = 0;i < this.getAmount();i++){
				Vector v = new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize().multiply(this.getRange());
				Location spawnLoc = location.clone().add(v);
				LivingEntity entity = this.getMonster().spawn(spawnLoc, this.getMonsterLevel(), true).getEntity();
				entity.setMetadata("questUUID", new FixedMetadataValue(QuestsPlugin.instance, this.getQuestUUID()));
				entity.setMetadata("qEventIndex", new FixedMetadataValue(QuestsPlugin.instance, this.getIndex()));
				mobs.add(entity);
			}
			monsters.put(rp, mobs);
		}else if(this.getType().equals(QEventType.BLOCKS)){
			List<Block> blocks = this.getBlocks();
			for(final Block block : blocks){
				new BukkitTask(QuestsPlugin.instance){
					public void run(){
						final Material material = block.getType();
						final BlockData data = block.getBlockData();
						block.getWorld().getBlockAt(block.getLocation()).setType(Material.AIR);
						new BukkitTask(QuestsPlugin.instance){
							public void run(){
								block.getWorld().getBlockAt(block.getLocation()).setType(material);
								block.getWorld().getBlockAt(block.getLocation()).setBlockData(data);
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(getAmount()*20);
					}

					@Override
					public void onCancel() {
					}
					
				}.runTaskLater(blocks.indexOf(block));
			}
		}else if(this.getType().equals(QEventType.ITEM)){
			if(rp.isOnline()){
				rp.getPlayer().getInventory().addItem(this.getItemStack());
			}
			
		}
	}

	public String getQuestUUID() {
		return questUUID;
	}

	public void setQuestUUID(String questUUID) {
		this.questUUID = questUUID;
		this.setModified(true);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		this.setModified(true);
	}

	public QEventType getType() {
		return type;
	}

	public void setType(QEventType type) {
		this.type = type;
		this.setModified(true);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		this.setModified(true);
	}
	
	public Quest getQuest(){
		return Quest.get(this.getQuestUUID());
	}
	
	public String getQuestPath(){
		return "quests." + this.getQuestUUID() + ".qEvents";
	}
	
	public String getInformation() {
		if(this.getType().equals(QEventType.TELEPORTATION))return this.getLocation().getBlockX() + " " + this.getLocation().getBlockY() + " " + this.getLocation().getBlockZ();
		else if(this.getType().equals(QEventType.EFFECT))return this.getPotionEffect().getType().toString() + " " + this.getPotionEffect().getAmplifier() + " " + this.getPotionEffect().getDuration() + "sec " + (this.getPotionEffect().hasParticles() ? "particles" : "no particles");
		else if(this.getType().equals(QEventType.SPAWN))return this.getAmount() + " " + this.getMonster().getName() + " lv." + this.getMonsterLevel() + ", " + this.getRange() + " blocks around " + this.getLocation().getBlockX() + " " + this.getLocation().getBlockY() + " " + this.getLocation().getBlockZ();
		return "";
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
		this.setModified(true);
	}

	public PotionEffect getPotionEffect() {
		return potionEffect;
	}

	public void setPotionEffect(PotionEffect potionEffect) {
		this.potionEffect = potionEffect;
		this.setModified(true);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public Monster getMonster(){
		Monster monster = Monsters.get(this.getMonsterUUID());
		return monster == null ? Monsters.monsters.get(0) : monster;
	}

	public int getMonsterLevel() {
		return monsterLevel;
	}

	public void setMonsterLevel(int monsterLevel) {
		this.monsterLevel = monsterLevel;
		this.setModified(true);
	}

	public String getMonsterUUID() {
		return monsterUUID;
	}

	public void setMonsterUUID(String monsterUUID) {
		this.monsterUUID = monsterUUID;
		this.setModified(true);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.setModified(true);
	}
}
