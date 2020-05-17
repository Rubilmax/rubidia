package me.pmilon.RubidiaCore.votes;

import java.util.HashMap;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.chat.RChatFixDisplay;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Votes {

	public static Vote currentVote = null;
	public static HashMap<String, Long> lastVotes = new HashMap<String, Long>();
	public static HashMap<RPlayer, Boolean> voters = new HashMap<RPlayer, Boolean>();
	public static BukkitTask task = null;
	
	public static boolean startVote(Vote vote){
		if(canBeStarted(vote)){
			currentVote = vote;
			voters.clear();
			for(RPlayer rp : RPlayer.getOnlines()){
			    TextComponent accept = new TextComponent(("§a[ ✔ POUR]"));
			    TextComponent refuse = new TextComponent(("§c[ ✘ CONTRE]"));
			    ClickEvent acceptEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote 0 0 0 0 0 true");
			    ClickEvent refuseEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote 0 0 0 0 0 false");
			    accept.setClickEvent(acceptEvent);
			    refuse.setClickEvent(refuseEvent);
			    TextComponent text = new TextComponent("            ");
			    text.addExtra(accept);
			    text.addExtra(new TextComponent("       "));
			    text.addExtra(refuse);
			    rp.getChat().addFixDisplay(new RChatFixDisplay(rp,400,null).addLine("").addLine("   §e> " + vote.getQuestion()).addText(text).addLine(""));
				rp.sendTitle("§6§lNouveau vote !", "§eCliquez dans le chat pour voter", 5, 90, 30);
			}
			task = new BukkitTask(Core.instance){

				@Override
				public void run() {
					Votes.endCurrentVote();
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(400);
			return true;
		}
		return false;
	}
	
	public static boolean canBeStarted(Vote vote){
		if(lastVotes.containsKey(vote.getType())){
			return currentVote == null && System.currentTimeMillis()-lastVotes.get(vote.getType()) > vote.getTime();
		}
		return currentVote == null;
	}
	
	public static void vote(RPlayer rp, boolean flag){
		voters.put(rp, flag);
		if(voters.size() >= RPlayer.getOnlines().size()){
			Votes.endCurrentVote();
		}
	}
	
	public static boolean hasVoted(RPlayer rp){
		return voters.containsKey(rp);
	}
	
	public static double getRatio(boolean flag) {
		int nb = 0;
		for(RPlayer rp : Votes.voters.keySet()) {
			if(Votes.voters.get(rp).equals(flag)) {
				nb++;
			}
		}
		return ((double)nb)/RPlayer.getOnlines().size();
	}
	
	public static boolean isOk(){
		return getRatio(true) >= .5;
	}
	
	public static void endCurrentVote(){
		if(currentVote != null){
			if(isOk()){
				for(RPlayer rp : RPlayer.getOnlines()){
					if(!Votes.hasVoted(rp)){
						rp.getChat().forceCloseFixDisplay();
					}
					rp.sendTitle("§2Vote validé", "§a" + Utils.round(getRatio(true), 1) + "% §ePOUR §8- §c" + Utils.round(getRatio(false), 1) + "% §eCONTRE", 5, 90, 30);
				}
				currentVote.run();
			}else{
				for(RPlayer rp : RPlayer.getOnlines()){
					if(!Votes.hasVoted(rp)){
						rp.getChat().forceCloseFixDisplay();
					}
					rp.sendTitle("§4Vote annulé", "§a" + Utils.round(getRatio(true), 1) + "% §ePOUR §8- §c" + Utils.round(getRatio(false), 1) + "% §eCONTRE", 5, 90, 30);
				}
			}
			lastVotes.put(currentVote.getType(), System.currentTimeMillis());
			voters.clear();
			currentVote = null;
		}
		if(task != null){
			task.cancel();
			task = null;
		}
	}
	
}
