package me.pmilon.RubidiaGuilds.guilds;

import java.util.HashMap;

public enum Permission {

	BUILD("canBuild"),
	CLAIM("canClaim"),
	INVITE("canInvite"),
	USE_DOORS("canUseDoors"),
	USE_CHESTS("canUseChests"),
	RENAME("canRename"),
	DESCRIPTION("canChangeDescription"),
	CAPE("canModifyDisplay"),
	CLAIM_PREFS("canModifyClaimsPrefs"),
	RANK_PREFS("canModifyRanksUnderPrefs"),
	MEMBER_PREFS("canModifyPerMemberPrefs"),
	RELATIONS("canManageRelations"),
	DEFAULT_RANK("canSetDefaultRank"),
	BANK_DEPOSIT("canGiveBank"),
	OFFER("canOffer"),
	DAMAGE_MOBS("canDamageMobs");
	
	private final String permission;
	private Permission(String permission){
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public static HashMap<String, Boolean> getDefault(boolean defaultFlag){
		HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
		for(Permission permission : Permission.values()){
			permissions.put(permission.getPermission(), defaultFlag);
		}
		return permissions;
	}
}
