package me.pmilon.RubidiaCore.ritems.general;

import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.Scrolls;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RItem {
	
	private ItemStack item;
	private Weapon weaponReference;
	private Scroll scrollReference;
	public RItem(ItemStack item){
		this.item = item;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public boolean isWeapon(){
		if(this.weaponReference != null){
			if(this.weaponReference.getRarity() != null){
				return true;
			}
		}
		
		if(this.getItem().hasItemMeta()){
			ItemMeta meta = this.getItem().getItemMeta();
			if(meta.getDisplayName() != null && meta.getLore() != null){
				this.weaponReference = new Weapon(this);
				return this.weaponReference.getRarity() != null;
			}
		}
		return false;
	}

	public boolean isScroll(){
		if(this.scrollReference != null){
			return true;
		}

		if(this.getItem().getType().equals(Material.MAP)){
			if(this.getItem().hasItemMeta()){
				ItemMeta meta = this.getItem().getItemMeta();
				if(meta.hasDisplayName()){
					if(meta.getDisplayName().contains("scroll") || meta.getDisplayName().contains("Parchemin")){
						this.scrollReference = Scrolls.get(this.getItem());
						return this.scrollReference != null;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isCustom(){
		for(RItemStack item : RItemStacks.ITEMS){
			if(item.getItemStack().isSimilar(this.getItem())){
				return true;
			}
		}
		return false;
	}
	
	public Weapon getWeapon(){
		return this.weaponReference;
	}
	
	public Scroll getScroll(){
		return this.scrollReference;
	}
}
