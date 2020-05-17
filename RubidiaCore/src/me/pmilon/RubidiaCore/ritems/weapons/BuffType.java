package me.pmilon.RubidiaCore.ritems.weapons;

import org.bukkit.Material;

public enum BuffType {

	LIFT_COST("co�t d'�l�vation en �lytres", Material.ELYTRA),//RPlayer
	WALK_SPEED("vitesse de d�placement", Material.FEATHER),//Core.task2
	ATTACK_SPEED("vitesse d'attaque", Material.NAME_TAG),//RPlayer
	ABILITY_DAMAGE("d�g�ts des comp�tences", Material.MAGMA_CREAM),//RPlayer
	ABILITY_DEFENSE("d�fense magique", Material.SLIME_BALL),//RPlayer
	MELEE_DAMAGE("d�g�ts de m�l�e", Material.DIAMOND_SWORD),//DamageManager.getDamages
	RANGED_DAMAGE("d�g�ts � distance", Material.BOW),//DamageManager.getDamages
	MAGIC_DAMAGE("d�g�ts magiques", Material.GOLDEN_HOE),//DamageManager.getDamages
	DEFENSE("d�fense physique", Material.DIAMOND_CHESTPLATE),//RPlayer
	MAX_HEALTH("vie maximale", Material.EGG),//RPlayer.getMaxHealth+Core.task3
	MAX_ENERGY("vigueur maximale", Material.CAULDRON),//RPlayer.getMaxVigor
	ENERGY_REGEN("vitesse de r�g�n�ration de la vigueur", Material.CAKE),//Core.task1
	CRITIC_DAMAGE("d�g�ts des coups critiques", Material.ANVIL),//RPlayer
	CRITIC_CHANCE("chance de coup critique", Material.ANVIL),//RPlayer
	BLOCK_CHANCE("blocage", Material.ENCHANTED_BOOK),//RPlayer
	LOOT_BONUS("chance de butin rare", Material.CHEST),//RlivingEntity.kill
	XP("XP", Material.EXPERIENCE_BOTTLE),//RPlayer
	REGENERATION("r�g�n�ration", Material.GOLDEN_APPLE);//RPlayer
	
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
