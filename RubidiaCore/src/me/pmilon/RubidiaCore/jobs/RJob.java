package me.pmilon.RubidiaCore.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public enum RJob {
	
	JOBLESS(Material.IRON_SHOVEL, "§fChômeur", new ArrayList<Material>(), new ArrayList<String>(), new ArrayList<Material>()),
	SCULPTOR(Material.IRON_PICKAXE, "§aSculpteur", Arrays.asList(Material.REDSTONE_BLOCK, Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.LAPIS_BLOCK, Material.IRON_BLOCK,
			Material.CUT_RED_SANDSTONE, Material.CUT_SANDSTONE, Material.PURPUR_PILLAR, Material.QUARTZ_PILLAR, Material.BONE_BLOCK,
			Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.BRICKS), Arrays.asList("SMOOTH_", "CHISELED_", "POLISHED_"), new ArrayList<Material>()),
	CARPENTER(Material.IRON_AXE, "§bMenuisier", new ArrayList<Material>(), Arrays.asList("_FENCE", "_STAIRS", "_SLAB", "_TRAPDOOR",
			"_PRESSURE_PLATE", "_FENCE_GATE", "_BUTTON", "_DOOR", "_BOAT"), Arrays.asList(Material.OAK_FENCE, Material.OAK_STAIRS, Material.OAK_SLAB,
					Material.OAK_TRAPDOOR, Material.OAK_PRESSURE_PLATE, Material.OAK_FENCE_GATE, Material.OAK_BUTTON, Material.OAK_DOOR, Material.OAK_BOAT)),
	DECORATOR(Material.SHEARS, "§eDécorateur", Arrays.asList(Material.SHEARS, Material.FLOWER_POT, Material.PAINTING, Material.ITEM_FRAME),
			Arrays.asList("_WOOL", "_TERRACOTTA", "_CONCRETE_POWDER", "_BED", "_CARPET", "_STAINED_GLASS"), Arrays.asList(Material.WHITE_BED, Material.WHITE_CARPET));
	
	private final Material display;
	private final String name;
	private final List<Material> crafts;
	private final List<String> craftFilters;
	private final List<Material> craftBypass;
	private RJob(Material display, String name, List<Material> crafts, List<String> craftFilters, List<Material> craftBypass){
		this.display = display;
		this.name = name;
		this.crafts = crafts;
		this.craftFilters = craftFilters;
		this.craftBypass = craftBypass;
	}

	public Material getDisplay() {
		return display;
	}

	public List<Material> getCrafts() {
		return crafts;
	}
	
	public boolean check(Material type) {
		if((this.crafts.contains(type) || this.isFiltered(type)) && !this.craftBypass.contains(type)) {
			return true;
		}
		return false;
	}
	
	private boolean isFiltered(Material type) {
		for(String material : this.craftFilters) {
			if(type.toString().contains(material)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}
}
