package me.pmilon.RubidiaGuilds.guilds;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class Rank extends PermissionsHolder {
	
	private ItemStack item;
	private String name;
	private int id;
	public Rank(ItemStack item, String name, int id, HashMap<String, Boolean> permissions, boolean[] canHome, boolean[] canSetHome){
		super(permissions, canHome, canSetHome);
		this.item = item;
		this.name = name;
		this.id = id;
	}
	
	public ItemStack getItemStack() {
		return item.clone();
	}
	
	public void setItemStack(ItemStack item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void resetPermissions(GMember member){
		for(Permission permission : Permission.values()){
			member.setPermission(permission, this.getPermission(permission));
		}
		for(int i = 0;i < 8;i++){
			member.setCanHome(i, this.canHome(i));
			member.setCanSetHome(i, this.canSetHome(i));
		}
	}
	
	public void resetPermissions(Guild guild){
		for(GMember member : guild.getMembers()){
			if(member.getRank().getId() == this.getId()){
				this.resetPermissions(member);
			}
		}
	}
}
