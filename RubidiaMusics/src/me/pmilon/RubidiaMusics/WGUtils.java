package me.pmilon.RubidiaMusics;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGUtils {

	public static ProtectedRegion getHighestPriorityWithFlag(ApplicableRegionSet set, Flag<?> flag){
		ProtectedRegion record = null;
		int priority = -10000000;
		for(ProtectedRegion region : set.getRegions()){
			if(region.getFlag(flag) != null && region.getPriority() > priority){
				record = region;
				priority = region.getPriority();
			}
		}
		return record;
	}
	
}
