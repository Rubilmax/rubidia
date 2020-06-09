package me.pmilon.RubidiaCore.couples;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

public class Couple {

	private String uuid;
	private RPlayer companion1;
	private RPlayer companion2;
	private long XPtime;
	private long weddingDate;
	
	private boolean modified = false;
	public Couple(String uuid, RPlayer rp1, RPlayer rp2, long XPTime, long weddingDate){
		this.uuid = uuid;
		this.companion1 = rp1;
		this.companion2 = rp2;
		this.XPtime = XPTime;
		this.weddingDate = weddingDate;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
		this.setModified(true);
	}
	
	public RPlayer getCompanion1() {
		return companion1;
	}
	
	public void setCompanion1(RPlayer companion1) {
		this.companion1 = companion1;
		this.setModified(true);
	}

	public RPlayer getCompanion2() {
		return companion2;
	}

	public void setCompanion2(RPlayer companion2) {
		this.companion2 = companion2;
		this.setModified(true);
	}

	public long getXPTime() {
		return XPtime;
	}

	public void setXPTime(long xPtime) {
		XPtime = xPtime;
		this.setModified(true);
	}

	public long getWeddingDate() {
		return weddingDate;
	}

	public void setWeddingDate(long weddingDate) {
		this.weddingDate = weddingDate;
		this.setModified(true);
	}
	
	public List<CBuff> getAvailableBuffs(){
		List<CBuff> buffs = new ArrayList<CBuff>();
		for(CBuff buff : CoupleColl.buffs){
			if(this.getXPTime() >= buff.getXpTime()){
				buffs.add(buff);
			}
		}
		return buffs;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public RPlayer getCompanion(RPlayer rp){
		if(this.getCompanion1().equals(rp))return this.getCompanion2();
		return this.getCompanion1();
	}
	
	public void divorce(RPlayer divorcer){
		this.getCompanion1().setCoupleUUID(null);
		this.getCompanion2().setCoupleUUID(null);
		
		divorcer.setLastDivorce(System.currentTimeMillis());
		
		RPlayer rp = this.getCompanion1().equals(divorcer) ? this.getCompanion2() : this.getCompanion1();
		if(rp.isOnline()){
			if(Core.uiManager.hasActiveSession(rp.getPlayer())){
				UIHandler handler = Core.uiManager.getSession(rp.getPlayer()).getUIHandler();
				if(handler.getType().equals("COUPLE_MENU"))handler.close(false);
			}
			rp.getPlayer().playSound(rp.getPlayer().getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, .5F);
			rp.getPlayer().sendMessage(" ");
			rp.sendMessage("§4" + divorcer.getName() + " §ca divorcé...");
			rp.getPlayer().sendMessage(" ");
		}
		if(divorcer.isOnline()){
			divorcer.getPlayer().playSound(divorcer.getPlayer().getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, .5F);
			divorcer.getPlayer().sendMessage(" ");
			divorcer.sendMessage("§cVous avez divorcé !");
			divorcer.getPlayer().sendMessage(" ");
		}
	}
	
	public CBuff getNextLockedBuff(){
		List<CBuff> buffs = this.getAvailableBuffs();
		for(CBuff buff : CoupleColl.buffs){
			if(!buffs.contains(buff))return buff;
		}
		return null;
	}
	
	public boolean isOnline(){
		return RPlayer.getOnlines().contains(this.getCompanion1()) && RPlayer.getOnlines().contains(this.getCompanion2());
	}
}
