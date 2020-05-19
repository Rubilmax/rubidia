package me.pmilon.RubidiaCore.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Relation;

public class RChatMessage {

	private final RPlayer rp;
	private final RPlayer privateTarget;
	private final GMember gmember;
	private final Guild guild;
	private final ChatType type;
	private String message;
	private ItemStack item;
	
	private HashMap<RPlayer,String> builtMessages = new HashMap<RPlayer,String>();
	
	public RChatMessage(RPlayer rp, RPlayer privateTarget, GMember gmember, Guild guild, ChatType type, String message, ItemStack item){
		this.rp = rp;
		this.gmember = gmember;
		this.guild = guild;
		this.type = type;
		this.message = message;
		this.item = item;
		this.privateTarget = privateTarget;
	}

	public RPlayer getRP() {
		return rp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GMember getGMember() {
		return gmember;
	}

	public Guild getGuild() {
		return guild;
	}

	public ChatType getType() {
		return type;
	}
	
	//builds message as seen by rp
	public String build(RPlayer rp, GMember gm, Guild guild){
		if(!this.getBuiltMessages().containsKey(rp)){
			String message = "";
			switch (this.getType()){
			case GLOBAL:
				String cname = "";
				String classPrefix = (this.getRP().getMastery().getId() >= Mastery.MASTER.getId() ? this.getRP().getRClass().getDarkColor() + (this.getRP().getMastery().getId() >= Mastery.HERO.getId() ? "§l" : "") : this.getRP().getRClass().getColor()) + "[" + this.getRP().getRClass().toString().split("")[0] + "] ";
				String mcolor = "§7";
				String vip = "";
				if(rp.knows(this.getRP()) || this.getRP().equals(rp)){
					cname = "§6";
					mcolor = "§f";
				}
				if(this.getRP().isOp()){
					cname = "§4";
					mcolor = "§f";
				}
				if(this.getRP().isVip()){
					if(this.getRP().isOp()){
						cname = "§4";
						mcolor = "§f";
					}else{
						cname = "§6§l";
					}
					vip = "§8§l[§6§lV§8§l]§4 ";
				}
				message += vip + classPrefix;
				if(this.getGMember().hasGuild()){
					Relation relation = this.getGuild().getRelationTo(gm.getGuild());
					message += relation.getCColorCode() + "[" + relation.getDColorCode() + this.getGuild().getName() + relation.getCColorCode() + "] ";
				}
				message += this.getRP().getRClass().getColor() + cname + this.getRP().getName() + "  " + mcolor;
				break;
			case SHOUT:
				String classPrefix1 = this.getRP().getRClass().toString().split("")[0];
				message += "§4§l[" + classPrefix1 + "]§c §l" + this.getRP().getName() + "§c  ";
				break;
			case GUILD:
				message += Relation.MEMBER.getDColorCode() + "§l> " + Relation.MEMBER.getDColorCode() + "(" + this.getGMember().getRank().getName() + ") " + Relation.MEMBER.getCColorCode() + "§o" + this.getRP().getName() + Relation.MEMBER.getCColorCode() + "  ";
				break;
			case ALLIANCE:
				Relation relation = this.getGuild().getRelationTo(guild);
				String guildName = relation.getDColorCode() + "§l[" + this.getGuild().getName() + "]";
				String rankName = relation.getDColorCode() + "§o(" + this.getGMember().getRank().getName() + ")";
				message += guildName + " " + rankName + " " + relation.getCColorCode() + "§o" + this.getRP().getName() + relation.getCColorCode() + "  ";
				break;
			case PRIVATE:
				message += "§6[" + this.getRP().getName() + "] §l>§6 [" + this.getPrivateTarget().getName() + "]§e  ";
				break;
			case STAFF:
				message += "§6§l> " + this.getRP().getName() + "§e  ";
				break;
			default:
				break;
			}
			
			this.getBuiltMessages().put(rp, message + this.getMessage().replaceAll("%item", this.getItemMessage()));
		}
		return this.getBuiltMessages().get(rp);
	}

	public HashMap<RPlayer,String> getBuiltMessages() {
		return builtMessages;
	}

	public void setBuiltMessages(HashMap<RPlayer,String> builtMessages) {
		this.builtMessages = builtMessages;
	}
	
	public void send(){
		List<RPlayer> rps = null;
		switch(this.getType()){
		case GLOBAL: case SHOUT:
			rps = RPlayer.getOnlines();
			break;
		case GUILD:
			rps = new ArrayList<RPlayer>();
			for(GMember member : this.getGuild().getMembers()){
				RPlayer rp = RPlayer.get(member);
				if(rp.isOnline())rps.add(rp);
			}
			break;
		case ALLIANCE:
			rps = new ArrayList<RPlayer>();
			for(GMember member : this.getGuild().getMembers()){
				RPlayer rp = RPlayer.get(member);
				if(rp.isOnline())rps.add(rp);
			}
			for(Guild guild : this.getGuild().getAllies()){
				for(GMember member : guild.getMembers()){
					RPlayer rp = RPlayer.get(member);
					if(rp.isOnline())rps.add(rp);
				}
			}
			break;
		case PRIVATE:
			rps = Arrays.asList(this.getRP(), this.getPrivateTarget());
			break;
		case STAFF:
			rps = new ArrayList<RPlayer>();
			for(RPlayer rp : RPlayer.getOnlines()){
				if(rp.isOp()){
					rps.add(rp);
				}
			}
			break;
		default:
			break;
		}
		if(rps != null){
			for(RPlayer rp : rps){
				if(rp.getChat().getShownTypes().contains(this.getType()) || this.getType().equals(ChatType.PRIVATE)){
					GMember member = GMember.get(rp);
					rp.getChat().addChatMessage(this.getRP(), this.getMessage().contains("%item") ? this.getItem() : null, this.build(rp, member, member.getGuild()));
					rp.getChat().update();
				}
			}
		}
	}
	
	public String getItemMessage(){
		if(this.getItem() != null){
			return "[" + ChatColor.stripColor(CraftItemStack.asNMSCopy(this.getItem()).getName().getString()) + "]";
		}
		return "";
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public RPlayer getPrivateTarget() {
		return privateTarget;
	}
}
