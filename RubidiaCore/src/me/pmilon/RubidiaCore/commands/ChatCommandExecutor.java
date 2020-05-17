package me.pmilon.RubidiaCore.commands;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.chat.ChatType;
import me.pmilon.RubidiaCore.commands.abstracts.PlayerCommandExecutor;

import org.bukkit.entity.Player;

public class ChatCommandExecutor extends PlayerCommandExecutor {

	@Override
	public void onCommand(Player player, RPlayer rp, String[] args) {
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("settings")){
				if(args.length > 2){
					if(args[1].equalsIgnoreCase("chatHeight")){
						if(args[2].equalsIgnoreCase("up"))rp.setChatHeight(rp.getChatHeight()+1);
						else if(args[2].equalsIgnoreCase("down"))rp.setChatHeight(rp.getChatHeight()-1);
					}else if(args[1].equalsIgnoreCase("height")){
						if(args[2].equalsIgnoreCase("up"))rp.setChatboxHeight(rp.getChatboxHeight()+1);
						else if(args[2].equalsIgnoreCase("down"))rp.setChatboxHeight(rp.getChatboxHeight()-1);
					}else if(args[1].equalsIgnoreCase("width")){
						if(args[2].equalsIgnoreCase("up"))rp.setChatboxWidth(rp.getChatboxWidth()+1);
						else if(args[2].equalsIgnoreCase("down"))rp.setChatboxWidth(rp.getChatboxWidth()-1);
					}
				}else{
					rp.getChat().setEditMode(false);
					rp.getChat().clear();
					rp.getChat().addInfo(("§aVos paramètres de chatbox ont été modifiés et sauvegardés !"));
				}
				rp.getChat().update();
			}else if(args[0].equalsIgnoreCase("up")){
				if(args.length > 1){
					if(args[1].equalsIgnoreCase("chat"))rp.getChat().setChatStartShift(rp.getChat().getChatStartShift()+1);
					else if(args[1].equalsIgnoreCase("info"))rp.getChat().setInfoStartShift(rp.getChat().getInfoStartShift()+1);
					rp.getChat().update();
				}
			}else if(args[0].equalsIgnoreCase("down")){
				if(args.length > 1){
					if(args[1].equalsIgnoreCase("chat") && rp.getChat().getChatStartShift() > 0)rp.getChat().setChatStartShift(rp.getChat().getChatStartShift()-1);
					else if(args[1].equalsIgnoreCase("info") && rp.getChat().getInfoStartShift() > 0)rp.getChat().setInfoStartShift(rp.getChat().getInfoStartShift()-1);
					rp.getChat().update();
				}
			}else if(args[0].equalsIgnoreCase("fixDisplay")){
				rp.getChat().closeFixDisplay();
				rp.getChat().update();
			}else{
				try {
					ChatType type = ChatType.valueOf(args[0].toUpperCase());
					if(rp.getChat().getShownTypes().contains(type)){
						if(rp.getChat().getDefaultType().equals(type)){
							if(rp.getChat().getShownTypes().size() > 1){
								rp.getChat().getShownTypes().remove(type);
								rp.getChat().setDefaultType(rp.getChat().getShownTypes().get(0));
							}
						}else rp.getChat().setDefaultType(type);
					}else rp.getChat().getShownTypes().add(type);
					rp.getChat().update();
				} catch(Exception ex) {
				}
			}
		}
	}

}
