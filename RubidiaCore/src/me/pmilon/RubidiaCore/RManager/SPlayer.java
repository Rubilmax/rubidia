package me.pmilon.RubidiaCore.RManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.pmilon.RubidiaCore.abilities.RAbility;
import me.pmilon.RubidiaCore.events.RXPSource;
import me.pmilon.RubidiaCore.jobs.RJob;
import me.pmilon.RubidiaCore.levels.RLevels;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaPets.pets.Pet;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class SPlayer {

	private int id;
	private int rlevel;
	private double rexp;
	private RClass rClass;
	private RJob rJob;
	private Mastery mastery;
	private int skp;
	private int skd;
	private HashMap<RAbility, Integer> abilityLevels;
	private int strength;
	private int endurance;
	private int agility;
	private int intelligence;
	private int perception;
	private double currentnrj;
	private int kills;
	private int renom;
	private List<Pet> pets;
	private HashMap<Integer, ItemStack> creative;
	private HashMap<Integer, ItemStack> survival;
	private int bank;
	private List<Quest> activeQuests;
	private List<Quest> doneQuests;
	private Quest followedQuest;
	private Location lastLocation;
	private HashMap<Integer, ItemStack> lastInventory = new HashMap<Integer, ItemStack>();
	private HashMap<Integer, ItemStack> enderchest = new HashMap<Integer, ItemStack>();
	private double lastHealth;
	private int lastFoodLevel;
	
	private boolean loaded = false;
	private boolean modified = false;
	public SPlayer(int id, int rlevel, double rexp, RClass rClass, RJob rJob,
			Mastery mastery, int skp, int skd, HashMap<RAbility, Integer> abilityLevels, int strength, int endurance, int agility,
			int intelligence, int perception, double currentnrj, int kills, int renom, List<Pet> pets,
			HashMap<Integer, ItemStack> creative, HashMap<Integer, ItemStack> survival,
			int bank, List<Quest> activeQuests, List<Quest> doneQuests, Quest followedQuest, Location lastLocation,
			HashMap<Integer, ItemStack> lastInventory, HashMap<Integer, ItemStack> enderchest,
			double lastHealth, int lastFoodLevel){
		this.id = id;
		this.rlevel = rlevel;
		this.rexp = rexp;
		this.rClass = rClass;
		this.rJob = rJob;
		this.mastery = mastery;
		this.skp = skp;
		this.skd = skd;
		this.abilityLevels = abilityLevels;
		this.strength = strength;
		this.endurance = endurance;
		this.agility = agility;
		this.intelligence = intelligence;
		this.perception = perception;
		this.currentnrj = currentnrj;
		this.kills = kills;
		this.renom = renom;
		this.pets = pets;
		this.creative = creative;
		this.survival = survival;
		this.bank = bank;
		this.activeQuests = activeQuests;
		this.doneQuests = doneQuests;
		this.followedQuest = followedQuest;
		this.lastLocation = lastLocation;
		this.lastInventory = lastInventory;
		this.enderchest = enderchest;
		this.lastHealth = lastHealth;
		this.lastFoodLevel = lastFoodLevel;
	}
	public int getRLevel() {
		return rlevel;
	}
	public void setRLevel(int rlevel, RXPSource source) {
		this.rlevel = rlevel;
		this.setModified(true);
	}
	public double getRExp() {
		return rexp;
	}
	public void setRExp(double rexp, RXPSource source, RPlayer rp) {
		this.rexp = rexp;
		if(this.rexp >= RLevels.getRLevelTotalExp(this.rlevel)){
			int newLevel = this.rlevel;
			double exp = this.rexp;
			while(exp >= RLevels.getRLevelTotalExp(newLevel) && newLevel < Settings.LEVEL_MAX){
				exp -= RLevels.getRLevelTotalExp(newLevel);
				newLevel += 1;
			}
			if(newLevel < Settings.LEVEL_MAX) {
				this.rexp = exp;
			} else this.rexp = RLevels.getRLevelTotalExp(newLevel);
			rp.setRLevel(newLevel, source);
		}
		this.setModified(true);
	}
	public RClass getRClass() {
		return rClass;
	}
	public void setRClass(RClass rClass) {
		this.rClass = rClass;
		this.setModified(true);
	}
	public RJob getRJob() {
		return rJob;
	}
	public void setRJob(RJob rJob) {
		this.rJob = rJob;
		this.setModified(true);
	}
	public Mastery getMastery() {
		return mastery;
	}
	public void setMastery(Mastery mastery) {
		this.mastery = mastery;
		this.setModified(true);
	}
	public int getSkp() {
		return skp;
	}
	public void setSkp(int skp) {
		this.skp = skp;
		this.setModified(true);
	}
	public int getSkd() {
		return skd;
	}
	public void setSkd(int skd) {
		this.skd = skd;
		this.setModified(true);
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
		this.setModified(true);
	}
	public int getEndurance() {
		return endurance;
	}
	public void setEndurance(int endurance) {
		this.endurance = endurance;
		this.setModified(true);
	}
	public int getAgility() {
		return agility;
	}
	public void setAgility(int agility) {
		this.agility = agility;
		this.setModified(true);
	}
	public int getIntelligence() {
		return intelligence;
	}
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
		this.setModified(true);
	}
	public int getPerception() {
		return perception;
	}
	public void setPerception(int perception) {
		this.perception = perception;
		this.setModified(true);
	}
	public double getCurrentnrj() {
		return currentnrj;
	}
	public void setCurrentnrj(double currentnrj) {
		this.currentnrj = currentnrj;
		this.setModified(true);
	}
	public int getKills() {
		return kills;
	}
	public void setKills(int kills) {
		this.kills = kills;
		this.setModified(true);
	}
	public int getRenom() {
		return renom;
	}
	public void setRenom(int renom) {
		this.renom = renom;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getCreative() {
		return creative;
	}
	public void setCreative(HashMap<Integer, ItemStack> creative) {
		this.creative = creative;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getSurvival() {
		return survival;
	}
	public void setSurvival(HashMap<Integer, ItemStack> survival) {
		this.survival = survival;
		this.setModified(true);
	}
	public int getBank() {
		return bank;
	}
	public void setBank(int bank) {
		this.bank = bank;
		this.setModified(true);
	}
	public List<Quest> getActiveQuests() {
		return activeQuests;
	}
	public void setActiveQuests(List<Quest> activeQuests) {
		this.activeQuests = activeQuests;
		this.setModified(true);
	}
	public List<Quest> getDoneQuests() {
		return doneQuests;
	}
	public void setDoneQuests(List<Quest> doneQuests) {
		this.doneQuests = doneQuests;
		this.setModified(true);
	}
	public Quest getFollowedQuest() {
		return followedQuest;
	}
	public void setFollowedQuest(Quest followedQuest) {
		this.followedQuest = followedQuest;
		this.setModified(true);
	}
	
	public void save(RPlayer rp){
		String path = "players." + rp.getUniqueId() + ".saves." + this.getId();
		Configs.getPlayerConfig().set(path + ".rLevel", this.getRLevel());
		Configs.getPlayerConfig().set(path + ".rExp", this.getRExp());
		Configs.getPlayerConfig().set(path + ".rClass", this.getRClass().toString());
		Configs.getPlayerConfig().set(path + ".rJob", this.getRJob().toString());
		Configs.getPlayerConfig().set(path + ".mastery", this.getMastery().toString());
		Configs.getPlayerConfig().set(path + ".skillpoints", this.getSkp());
		Configs.getPlayerConfig().set(path + ".distinctionpoints", this.getSkd());
		for(RAbility ability : this.getAbilityLevels().keySet()) {
			Configs.getPlayerConfig().set(path + ".ability." + ability.toString(), this.getAbilityLevels().get(ability));
		}
		Configs.getPlayerConfig().set(path + ".strength", this.getStrength());
		Configs.getPlayerConfig().set(path + ".endurance", this.getEndurance());
		Configs.getPlayerConfig().set(path + ".agility", this.getAgility());
		Configs.getPlayerConfig().set(path + ".intelligence", this.getIntelligence());
		Configs.getPlayerConfig().set(path + ".perception", this.getPerception());
		Configs.getPlayerConfig().set(path + ".currentnrj", this.getCurrentnrj());
		Configs.getPlayerConfig().set(path + ".kills", this.getKills());
		Configs.getPlayerConfig().set(path + ".renom", this.getRenom());
		Configs.getPlayerConfig().set(path + ".lastLocation", this.getLastLocation());
		Configs.getPlayerConfig().set(path + ".lastHealth", this.getLastHealth());
		Configs.getPlayerConfig().set(path + ".lastFoodLevel", this.getLastFoodLevel());
		Configs.getPlayerConfig().set(path + ".bank", this.getBank());
		for(int i : this.getCreative().keySet()){
			Configs.getPlayerConfig().set(path + ".gamemode.creative." + i, this.getCreative().get(i));
		}
		for(int i : this.getSurvival().keySet()){
			Configs.getPlayerConfig().set(path + ".gamemode.survival." + i, this.getSurvival().get(i));
		}
		for(int i : this.getLastInventory().keySet()){
			Configs.getPlayerConfig().set(path + ".lastInventory." + i, this.getLastInventory().get(i));
		}
		for(int i : this.getEnderchest().keySet())Configs.getPlayerConfig().set(path + ".enderChest." + i, this.getEnderchest().get(i));
		List<String> pets = new ArrayList<String>();
		for(Pet pet : this.getPets())pets.add(pet.getUUID());
		Configs.getPlayerConfig().set(path + ".pets", pets);
		List<String> activeQuests = new ArrayList<String>();
		for(Quest quest : this.getActiveQuests())activeQuests.add(quest.getUUID());
		Configs.getPlayerConfig().set(path + ".activeQuests", activeQuests);
		List<String> doneQuests = new ArrayList<String>();
		for(Quest quest : this.getDoneQuests())doneQuests.add(quest.getUUID());
		Configs.getPlayerConfig().set(path + ".doneQuests", doneQuests);
		if(this.getFollowedQuest() != null)Configs.getPlayerConfig().set(path + ".followedQuest", this.getFollowedQuest().getUUID());
		else Configs.getPlayerConfig().set(path + ".followedQuest", null);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		this.setModified(true);
	}
	public Location getLastLocation() {
		return lastLocation;
	}
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getLastInventory() {
		return lastInventory;
	}
	public void setLastInventory(HashMap<Integer, ItemStack> lastInventory) {
		this.lastInventory = lastInventory;
		this.setModified(true);
	}
	public HashMap<Integer, ItemStack> getEnderchest() {
		return enderchest;
	}
	public void setEnderchest(HashMap<Integer, ItemStack> enderchest) {
		this.enderchest = enderchest;
		this.setModified(true);
	}
	public double getLastHealth() {
		return lastHealth;
	}
	public void setLastHealth(double lastHealth) {
		this.lastHealth = lastHealth;
		this.setModified(true);
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	public int getLastFoodLevel() {
		return lastFoodLevel;
	}
	public void setLastFoodLevel(int lastFoodLevel) {
		this.lastFoodLevel = lastFoodLevel;
		this.setModified(true);
	}
	public List<Pet> getPets() {
		return pets;
	}
	public void setPets(List<Pet> pets) {
		this.pets = pets;
		this.setModified(true);
	}
	public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public HashMap<RAbility, Integer> getAbilityLevels() {
		return abilityLevels;
	}
	public void setAbilityLevels(HashMap<RAbility, Integer> abilityLevels) {
		this.abilityLevels = abilityLevels;
		this.setModified(true);
	}
	public int getAbilityLevel(RAbility ability) {
		if(this.abilityLevels.containsKey(ability)) {
			return this.abilityLevels.get(ability);
		}
		return 0;
	}
	public void setAbilityLevel(RAbility ability, int level) {
		this.abilityLevels.put(ability, level);
		this.setModified(true);
	}
}
