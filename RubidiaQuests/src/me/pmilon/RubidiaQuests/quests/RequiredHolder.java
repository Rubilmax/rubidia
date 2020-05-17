package me.pmilon.RubidiaQuests.quests;

import java.util.List;

public abstract class RequiredHolder {
	
	private String uuid;
	List<Required> requireds;
	private boolean modified = false;
	private boolean requiredsModified = false;
	protected RequiredHolder(String uuid, List<Required> requireds){
		this.requireds = requireds;
		this.uuid = uuid;
	}
	
	public List<Required> getRequireds(){
		return this.requireds;
	}
	
	public void setRequireds(List<Required> requireds){
		this.requireds = requireds;
		this.setRequiredsModified(true);
	}

	public void delete(Required required) {
		required.delete();
		this.getRequireds().remove(required);
		this.setRequiredsModified(true);
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
		this.setModified(true);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isRequiredsModified() {
		return requiredsModified;
	}

	public void setRequiredsModified(boolean requiredsModified) {
		this.requiredsModified = requiredsModified;
	}
	
}
