package me.pmilon.RubidiaCore.chat;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import me.pmilon.RubidiaCore.RManager.RPlayer;

import org.bukkit.ChatColor;

public class RChatUtils {

	public static int MAX_CHAT_WIDTH = 78;
	public static int MIN_CHAT_WIDTH = 39;
	public static int MAX_CHAT_HEIGHT = 20;
	public static int MIN_CHAT_HEIGHT = 5;
	
	public static int WORD_SIZE_LIMIT = 20;
	public static List<String> FORMATTING_CHARS = Arrays.asList(String.valueOf(ChatColor.BOLD.getChar()),String.valueOf(ChatColor.ITALIC.getChar()),String.valueOf(ChatColor.UNDERLINE.getChar()),String.valueOf(ChatColor.UNDERLINE.getChar()),String.valueOf(ChatColor.MAGIC.getChar()));
	public static List<String> SPECIAL_CHARS = Arrays.asList("f","k","i","l","t","!",".",",","'","\"","(",")","[","]","|","{","}"," ");
	public static int[] CHARS_LENGTH = new int[]{4,4,1,2,3,1,1,1,2,4,4,4,3,3,1,4,4,3};
	public static String SEPARATOR = "{1_?7.'9-}";
	
	public static String[] filter(String message, int width, int shift){
		String tempMessage = "";
		int length = shift;
		boolean flag = false;
		boolean bold = false;
		for(String c : message.split("")){
			if(!flag){
				if(c.equals(String.valueOf(ChatColor.COLOR_CHAR)))flag = true;
				else{
					length += RChatUtils.getLength(c, bold);
					length++;
				}
				if(!flag)tempMessage += c;
			}else{
				if(c.equalsIgnoreCase(String.valueOf(ChatColor.BOLD.getChar()))){
					bold = true;
				}else if(!RChatUtils.FORMATTING_CHARS.contains(c)){
					if(bold && !String.valueOf(ChatColor.RESET.getChar()).equals(c)){
						tempMessage += String.valueOf(ChatColor.COLOR_CHAR) + String.valueOf(ChatColor.RESET.getChar());
					}
					bold = false;
				}
				flag = false;
				tempMessage += String.valueOf(ChatColor.COLOR_CHAR) + c;
			}
			
			if(length > width){
				if(!c.equals(" ")){
					String[] words = tempMessage.split(" ");
					if(words[words.length-1].length() <= RChatUtils.WORD_SIZE_LIMIT){
						String m = "";
						for(int i = 0;i < words.length-1;i++){
							m += words[i];
							m += " ";
						}
						String lastWord = words[words.length-1];
						tempMessage = m + RChatUtils.SEPARATOR + lastWord;
						length = 0;
						for(String c1 : lastWord.split("")){
							length += RChatUtils.getLength(c1, bold);
						}
						continue;
					}
				}
				tempMessage += RChatUtils.SEPARATOR;
				length = 0;
			}
		}
		tempMessage = tempMessage.replaceAll("\n", RChatUtils.SEPARATOR);
		String[] messages = tempMessage.split(Pattern.quote(RChatUtils.SEPARATOR));
		for(int i = 1;i < messages.length;i++){
			messages[i] = ChatColor.getLastColors(messages[i-1]) + messages[i];
		}
		return messages;
	}
	
	public static int getLength(String c, boolean bold){
		int length = 0;
		if(RChatUtils.SPECIAL_CHARS.contains(c))length += RChatUtils.CHARS_LENGTH[RChatUtils.SPECIAL_CHARS.indexOf(c)];
		else length += 5;
		if(bold)length++;
		return length;
	}
	
	public static int getWordLength(String word){
		int length = 0;
		boolean flag = false;
		boolean bold = false;
		for(String c : word.split("")){
			if(!flag){
				if(c.equals(String.valueOf(ChatColor.COLOR_CHAR)))flag = true;
				else{
					length += RChatUtils.getLength(c, bold);
					length++;
				}
			}else{
				if(c.equalsIgnoreCase(String.valueOf(ChatColor.BOLD.getChar()))){
					bold = true;
				}else if(!RChatUtils.FORMATTING_CHARS.contains(c)){
					bold = false;
				}
				flag = false;
			}
		}
		return length;
	}
	
	public static void broadcastInfo(String info){
		for(RPlayer rp : RPlayer.getOnlines()){
			rp.getChat().addInfo(info);
			rp.getChat().update();
		}
	}

}
