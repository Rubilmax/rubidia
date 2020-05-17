package me.pmilon.RubidiaCore.crafts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.pmilon.RubidiaCore.ritems.weapons.Weapons;

public class Crafts {

	public static final List<Material> HIDED_ATTRIBUTES_CRAFTS = Arrays.asList(/*Material.TURTLE_HELMET,*///TODO Turtle helmet spawn after recipe done on every recipe...
			Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL,
			Material.STONE_PICKAXE, Material.STONE_SHOVEL,
			Material.IRON_PICKAXE, Material.IRON_SHOVEL,
			Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL,
			Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL);
	public static final String ROUSABLE_LORE = "§8Item éveillable";
	
	public static void initialize(Plugin plugin) {
		List<Recipe> recipes = new ArrayList<Recipe>();
		
		//first we remove crafts of every weapons
	    Iterator<Recipe> iterator = plugin.getServer().recipeIterator();
	    while(iterator.hasNext()) {
	    	Recipe recipe = iterator.next();
	    	if(recipe != null) {
	    		Material type = recipe.getResult().getType();
	    		if(type.toString().contains("_LEGGINGS") || Weapons.COMMON_WEAPON_TYPES.contains(type) || Crafts.HIDED_ATTRIBUTES_CRAFTS.contains(type)) {
	    			if(!type.toString().contains("_LEGGINGS")) {
	    				recipes.add(recipe);
	    			}
	    			iterator.remove();
	    		}
	    	}
	    }
	    
	    //we add them back, with modified lores
	    for(Recipe recipe : recipes) {
	    	ItemStack item = new ItemStack(recipe.getResult().getType(), 1);
	    	ItemMeta meta = item.getItemMeta();
	    	meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	if(Weapons.COMMON_WEAPON_TYPES.contains(recipe.getResult().getType())) {
		    	meta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
	    	}
	    	item.setItemMeta(meta);
	    	if(recipe instanceof ShapedRecipe) {
	    		ShapedRecipe recipe2 = (ShapedRecipe)recipe;
		    	ShapedRecipe shapedRecipe = new ShapedRecipe(recipe2.getKey(), item).shape(recipe2.getShape());
		    	for(Character chr : recipe2.getChoiceMap().keySet()) {
		    		shapedRecipe.setIngredient(chr, recipe2.getChoiceMap().get(chr));
		    	}
		    	for(Character chr : recipe2.getIngredientMap().keySet()) {
		    		ItemStack stack = recipe2.getIngredientMap().get(chr);
		    		if(stack != null) {
			    		shapedRecipe.setIngredient(chr, stack.getType());
			    		shapedRecipe.setIngredient(chr, stack.getData());
		    		}
		    	}
		    	plugin.getServer().addRecipe(shapedRecipe);
	    	}else if(recipe instanceof ShapelessRecipe) {
	    		ShapelessRecipe recipe2 = (ShapelessRecipe)recipe;
	    		ShapelessRecipe shapelessRecipe = new ShapelessRecipe(recipe2.getKey(), item);
	    		for(RecipeChoice ingredient : recipe2.getChoiceList()) {
	    			shapelessRecipe.addIngredient(ingredient);
	    		}
	    		for(ItemStack ingredient : recipe2.getIngredientList()) {
	    			shapelessRecipe.addIngredient(ingredient.getType());
	    			shapelessRecipe.addIngredient(ingredient.getData());
	    		}
		    	plugin.getServer().addRecipe(shapelessRecipe);
	    	}
	    }
	    
	    //we add crafts for chainmail elements
	    ItemStack chainmailHelmet = new ItemStack(Material.CHAINMAIL_HELMET);
	    ItemMeta helmetMeta = chainmailHelmet.getItemMeta();
	    helmetMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    	if(Weapons.COMMON_WEAPON_TYPES.contains(Material.CHAINMAIL_HELMET)) {
    		helmetMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
    	}
    	chainmailHelmet.setItemMeta(helmetMeta);
	    ItemStack chainmailChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
	    ItemMeta chestplateMeta = chainmailChestplate.getItemMeta();
	    chestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    	if(Weapons.COMMON_WEAPON_TYPES.contains(Material.CHAINMAIL_CHESTPLATE)) {
    		chestplateMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
    	}
    	chainmailChestplate.setItemMeta(chestplateMeta);
	    ItemStack chainmailBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
	    ItemMeta bootsMeta = chainmailBoots.getItemMeta();
	    bootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    	if(Weapons.COMMON_WEAPON_TYPES.contains(Material.CHAINMAIL_BOOTS)) {
    		bootsMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
    	}
    	chainmailBoots.setItemMeta(bootsMeta);
    	
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailHelmet"), chainmailHelmet).shape("#$#", "$ $").setIngredient('#', Material.IRON_INGOT).setIngredient('$', Material.FLINT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailChestplate"), chainmailChestplate).shape("$ $", "#$#", "$#$").setIngredient('#', Material.IRON_INGOT).setIngredient('$', Material.FLINT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailBoots"), chainmailBoots).shape("# #", "$ $").setIngredient('#', Material.IRON_INGOT).setIngredient('$', Material.FLINT));
		
		//we add crafts for gauntlets
		ItemStack leatherGauntlet = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemMeta leatherMeta = leatherGauntlet.getItemMeta();
		leatherMeta.setDisplayName("§fGants en cuir");
		leatherMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
		leatherMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		leatherGauntlet.setItemMeta(leatherMeta);
		ItemStack chainmailGauntlet = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
		ItemMeta chainmailMeta = chainmailGauntlet.getItemMeta();
		chainmailMeta.setDisplayName("§fGants de mailles");
		chainmailMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
		chainmailMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		chainmailGauntlet.setItemMeta(chainmailMeta);
		ItemStack ironGauntlet = new ItemStack(Material.IRON_LEGGINGS, 1);
		ItemMeta ironMeta = ironGauntlet.getItemMeta();
		ironMeta.setDisplayName("§fGants en fer");
		ironMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
		ironMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ironGauntlet.setItemMeta(ironMeta);
		ItemStack diamondGauntlet = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		ItemMeta diamondMeta = diamondGauntlet.getItemMeta();
		diamondMeta.setDisplayName("§fGants en diamant");
		diamondMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
		diamondMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		diamondGauntlet.setItemMeta(diamondMeta);
		ItemStack goldenGauntlet = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
		ItemMeta goldenMeta = goldenGauntlet.getItemMeta();
		goldenMeta.setDisplayName("§fGants en or");
		goldenMeta.setLore(Arrays.asList(Crafts.ROUSABLE_LORE));
		goldenMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		goldenGauntlet.setItemMeta(goldenMeta);
		
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "leatherGauntlet"), leatherGauntlet).shape(" # ", "###", "## ").setIngredient('#', Material.LEATHER));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "chainmailGauntlet"), chainmailGauntlet).shape(" # ", "#$#", "$# ").setIngredient('#', Material.FLINT).setIngredient('$', Material.IRON_INGOT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "ironGauntlet"), ironGauntlet).shape(" # ", "###", "## ").setIngredient('#', Material.IRON_INGOT));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "diamondGauntlet"), diamondGauntlet).shape(" # ", "###", "## ").setIngredient('#', Material.DIAMOND));
		plugin.getServer().addRecipe(new ShapedRecipe(new NamespacedKey(plugin, "goldenGauntlet"), goldenGauntlet).shape(" # ", "###", "## ").setIngredient('#', Material.GOLD_INGOT));
	}
	
}
