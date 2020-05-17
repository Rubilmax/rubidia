package me.pmilon.RubidiaGuilds.guilds;

import java.util.HashMap;

public abstract class PermissionsHolder {

	private HashMap<String, Boolean> permissions;
	protected boolean[] canHome;
	protected boolean[] canSetHome;
	private boolean modified = false;
	public PermissionsHolder(HashMap<String,Boolean> permissions, boolean[] canHome, boolean[] canSetHome){
		this.permissions = permissions;
		this.canHome = canHome;
		this.canSetHome = canSetHome;
	}

	public HashMap<String, Boolean> getPermissions() {
		return permissions;
	}

	public void setPermissions(HashMap<String, Boolean> permissions) {
		this.permissions = permissions;
	}
	
	public boolean getPermission(Permission permission){
		if(this.getPermissions().containsKey(permission.getPermission())){
			return this.getPermissions().get(permission.getPermission());
		}
		return false;
	}
	
	public void setPermission(Permission permission, boolean flag){
		this.getPermissions().put(permission.getPermission(), flag);
		this.setModified(true);
	}

	public boolean canSetHome(int index) {
		return canSetHome[index];
	}

	public void setCanSetHome(int index, boolean canSetHome) {
		this.canSetHome[index] = canSetHome;
		this.setModified(true);
	}

	public boolean canHome(int index) {
		return canHome[index];
	}

	public void setCanHome(int index, boolean canHome) {
		this.canHome[index] = canHome;
		this.setModified(true);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
}
