package me.pmilon.RubidiaCore.couples;

import java.util.ArrayList;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.weapons.BuffType;
import me.pmilon.RubidiaCore.utils.Configs;
import me.pmilon.RubidiaCore.utils.Database;
import me.pmilon.RubidiaCore.utils.Utils;

public class CoupleColl extends Database<String, Couple> {

	public static final List<CBuff> buffs = new ArrayList<CBuff>();
	
	public CoupleColl(){
		if(Configs.getCouplesConfig().contains("couples")){
			for(String coupleUUID : Configs.getCouplesConfig().getConfigurationSection("couples").getKeys(false)){
				String path = "couples." + coupleUUID;
				Couple couple = new Couple(coupleUUID,
						Core.rcoll.get(Configs.getCouplesConfig().getString(path + ".companion1")),
						Core.rcoll.get(Configs.getCouplesConfig().getString(path + ".companion2")),
						Configs.getCouplesConfig().getLong(path + ".xpTime"),
						Configs.getCouplesConfig().getLong(path + ".weddingDate"));
				this.load(coupleUUID, couple);
				if(this.size() % 100 == 0){
					Core.console.sendMessage("§6LOADED §e" + this.size() + " §6COUPLES");
				}
			}
		}
		for(String buffId : Configs.getCouplesConfig().getConfigurationSection("buffs").getKeys(false)){
			if(Utils.isInteger(buffId)){
				int id = Integer.valueOf(buffId);
				String path = "buffs." + buffId;
				CBuff buff = new CBuff(id,
						Configs.getCouplesConfig().getString(path + ".name"),
						BuffType.valueOf(Configs.getCouplesConfig().getString(path + ".type")),
						Configs.getCouplesConfig().getInt(path + ".level"),
						Configs.getCouplesConfig().getLong(path + ".xpTime")*60*60*1000L);
				buffs.add(buff);
			}
		}
		Core.console.sendMessage("§6LOADED §e" + this.size() + " §6COUPLES & §e" + CoupleColl.buffs.size() + " §6BUFFS");
	}

	@Override
	protected void save(boolean debug, Couple couple) {
		if(couple.isModified()){
			couple.setModified(false);
			String path = "couples." + couple.getUUID();
			Configs.getCouplesConfig().set(path + ".companion1", couple.getCompanion1().getUniqueId());
			Configs.getCouplesConfig().set(path + ".companion2", couple.getCompanion2().getUniqueId());
			Configs.getCouplesConfig().set(path + ".xpTime", couple.getXPTime());
			Configs.getCouplesConfig().set(path + ".weddingDate", couple.getWeddingDate());
			if(debug)Core.console.sendMessage("§6Saved §e" + couple.getUUID());
		}
	}

	@Override
	protected void onSaveStart(boolean debug) {
		if (debug) Core.console.sendMessage("§eSaving couples...");
	}

	@Override
	protected void onSaveEnd(boolean debug) {
		Configs.savePlayerConfig();
	}

	@Override
	protected Couple getDefault(String uuid) {
		Couple couple = new Couple(uuid, null, null, 0L, System.currentTimeMillis());
		this.load(uuid, couple);
		return couple;
	}
	
}
