package me.pmilon.RubidiaCore.ritems.general;

import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class ItemStacks {

	public static ItemStack setMetadata(ItemStack item, String metadata, NBTBase value) {
	    net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound());
	    compound.set(metadata, value);
	    nmsItemStack.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
	}

	public static ItemStack setInt(ItemStack item, String metadata, int value) {
	    net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound());
	    compound.setInt(metadata, value);
	    nmsItemStack.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
	}
	
	public static boolean hasMetadata(ItemStack item, String metadata) {
	    net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound());
        return compound.hasKey(metadata);
	}
	
	public static int getInt(ItemStack item, String metadata) {
	    net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound());
        return compound.getInt(metadata);
	}
	
}
