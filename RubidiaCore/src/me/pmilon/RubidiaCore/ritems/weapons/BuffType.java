package me.pmilon.RubidiaCore.ritems.weapons;

import org.bukkit.Material;

public enum BuffType {

	LIFT_COST("coût d'élévation en élytres", Material.ELYTRA),//RPlayer
	WALK_SPEED("vitesse de déplacement", Material.FEATHER),//Core.task2
	ATTACK_SPEED("vitesse d'attaque", Material.NAME_TAG),//RPlayer
	ABILITY_DAMAGE("dégâts des compétences", Material.MAGMA_CREAM),//RPlayer
	ABILITY_DEFENSE("défense magique", Material.SLIME_BALL),//RPlayer
	MELEE_DAMAGE("dégâts de mêlée", Material.DIAMOND_SWORD),//DamageManager.getDamages
	RANGED_DAMAGE("dégâts à distance", Material.BOW),//DamageManager.getDamages
	MAGIC_DAMAGE("dégâts magiques", Material.GOLDEN_HOE),//DamageManager.getDamages
	DEFENSE("défense physique", Material.DIAMOND_CHESTPLATE),//RPlayer
	MAX_HEALTH("vie maximale", Material.EGG),//RPlayer.getMaxHealth+Core.task3
	MAX_ENERGY("vigueur maximale", Material.CAULDRON),//RPlayer.getMaxVigor
	ENERGY_REGEN("vitesse de régénération de la vigueur", Material.CAKE),//Core.task1
	CRITIC_DAMAGE("dégâts des coups critiques", Material.ANVIL),//RPlayer
	CRITIC_CHANCE("chance de coup critique", Material.ANVIL),//RPlayer
	BLOCK_CHANCE("blocage", Material.ENCHANTED_BOOK),//RPlayer
	LOOT_BONUS("chance de butin rare", Material.CHEST),//RlivingEntity.kill
	XP("XP", Material.EXPERIENCE_BOTTLE),//RPlayer
	REGENERATION("régénération", Material.GOLDEN_APPLE);//RPlayer
	
	private String displayfr;
	private Material material;
	private BuffType(String displayfr, Material material){
		this.displayfr = displayfr;
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getDisplayFr() {
		return displayfr;
	}

	public void setDisplayFr(String displayfr) {
		this.displayfr = displayfr;
	}
	
}
