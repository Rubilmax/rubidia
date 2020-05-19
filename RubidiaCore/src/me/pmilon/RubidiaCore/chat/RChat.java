package me.pmilon.RubidiaCore.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerChat;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaGuilds.guilds.GMember;
import me.pmilon.RubidiaQuests.dialogs.DialogManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R2.NBTTagCompound;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class RChat {

	public static int INFOS_LIMIT = 20;
	public static int CHAT_LIMIT = 50;
	public static int PNJ_LIMIT = 20;
	public static int BLANK_SIZE = 85;
	public static EnumWrappers.ChatType CHAT_UPDATE = EnumWrappers.ChatType.CHAT;//MUST BE CHAT!!
	public static String CHAT_UPDATE_CODE = "[RubidiaChatUpdate]";
	
	public static String FUNCTION_INFO = "info";
	public static String FUNCTION_CHAT = "chat";
	
	
	private final RPlayer rp;
	private final GMember gm;
	private final PacketAdapter listener;
	private boolean used;

	private final List<WrappedChatComponent> infos = new ArrayList<WrappedChatComponent>();
	private final List<SChatMessage> chatMessages = new ArrayList<SChatMessage>();
	private final List<String> pnjMessages = new ArrayList<String>();
	private final List<RChatFixDisplay> fixDisplayQueue = new ArrayList<RChatFixDisplay>();
	private BukkitTask fixDisplayTask;
	private ChatType defaultType = ChatType.GLOBAL;
	private final List<ChatType> shownTypes = new ArrayList<ChatType>();
	private int infoStartShift = 0;
	private int chatStartShift = 0;
	private boolean editMode = false;
	
	public RChat(RPlayer rp){
		this.rp = rp;
		this.gm = GMember.get(rp);
		this.used = rp.isUsingChat();
		this.listener = new PacketAdapter(Core.instance, PacketType.Play.Server.CHAT){
			
			@Override
	        public void onPacketSending(PacketEvent e) {
				if(e.getPlayer().equals(getPlayer())){
		        	WrapperPlayServerChat packet = new WrapperPlayServerChat(e.getPacket());
		        	if(packet.getPosition().equals(com.comphenix.protocol.wrappers.EnumWrappers.ChatType.SYSTEM) && isUsed()){
		        		addInfo(packet.getMessage());
		        	}
		        	
		        	if(packet.getPosition().equals(RChat.CHAT_UPDATE)){
		        		if(packet.getMessage() != null){
		        			if(packet.getMessage().equals(WrappedChatComponent.fromText(RChat.CHAT_UPDATE_CODE))){
		        				packet.setPosition(EnumWrappers.ChatType.SYSTEM);
		        				packet.setMessage(WrappedChatComponent.fromJson(ComponentSerializer.toString(build())));
		        				return;
		        			}
		        		}
		        	}
		        	
	        		if(!packet.getPosition().equals(EnumWrappers.ChatType.GAME_INFO)){
	        			if(isUsed() || isEditMode()){
	        				e.setCancelled(true);
	    	        		update();
	        			}
	        		}
				}
	        }
			
		};
		this.shownTypes.addAll(Arrays.asList(ChatType.GLOBAL, ChatType.SHOUT, ChatType.GUILD, ChatType.ALLIANCE));
		
		ProtocolLibrary.getProtocolManager().addPacketListener(this.listener);
	}
	
	public Player getPlayer() {
		return getRP().getPlayer();
	}

	public RPlayer getRP() {
		return rp;
	}

	public PacketAdapter getListener() {
		return listener;
	}

	public List<WrappedChatComponent> getInfos() {
		return infos;
	}
	
	public void addChatMessage(RPlayer sender, ItemStack item, String message){
		for(String m : RChatUtils.filter(message, rp.getChatboxWidth()*4+1,0)){
			SChatMessage chatMessage = new SChatMessage(sender, m, item);
			this.getChatMessages().add(chatMessage);
			if(this.getChatMessages().size() > RChat.CHAT_LIMIT)this.getChatMessages().remove(0);
			if(!this.isUsed()){
    			TextComponent line = new TextComponent(chatMessage.getFullMessage());
    			if(!sender.equals(this.getRP())){
        			line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "> " + sender.getName() + " "));
    			}
    			if(chatMessage.getItem() != null){
    			    net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(chatMessage.getItem());
	    			if(chatMessage.getItem().hasItemMeta()){
	    				NBTTagCompound compound = new NBTTagCompound();
	    			    if(nmsItemStack.hasTag())compound = nmsItemStack.save(compound);
	    				line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(compound.toString())}));
	    			}else{
	    				line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(nmsItemStack.getName().getString())}));
	    			}
    			}
    			this.getPlayer().spigot().sendMessage(line);
			}
		}
	}

	public void addInfo(String info){
		for(String message : RChatUtils.filter(info, rp.getChatboxWidth()*4+1,0)){
			this.getInfos().add(WrappedChatComponent.fromText(message));
			if(this.getInfos().size() > RChat.INFOS_LIMIT)this.getInfos().remove(0);
		}
		if(!this.isUsed())this.getPlayer().sendMessage(info);
	}
	
	public void addInfo(WrappedChatComponent info){
		this.getInfos().add(info);
		if(this.getInfos().size() > RChat.INFOS_LIMIT)this.getInfos().remove(0);
		if(!this.isUsed())this.getPlayer().spigot().sendMessage(ComponentSerializer.parse(info.getJson()));
	}
	
	public void addPNJMessage(String message){
		for(String m : RChatUtils.filter(message, rp.getChatboxWidth()*4+1,0)){
			this.getPNJMessages().add(m);
			if(this.getPNJMessages().size() > RChat.PNJ_LIMIT)this.getPNJMessages().remove(0);
		}
		if(!this.isUsed())this.getPlayer().sendMessage(message);
	}
	
	public TextComponent build(){
		int INFOS_SHOWN = rp.getChatboxHeight()-5-rp.getChatHeight();
		TextComponent text = new TextComponent(" \n");
		for(int i = 0;i < RChat.BLANK_SIZE;i++){
			text.addExtra(" \n");
		}
		if(this.isEditMode()){
		    TextComponent up = new TextComponent("§e§l▲");
		    TextComponent down = new TextComponent("§e§l▼");
		    TextComponent up1 = new TextComponent("§e§l▲");
		    TextComponent down1 = new TextComponent("§e§l▼");
		    TextComponent left = new TextComponent("§e§l◀");
		    TextComponent right = new TextComponent("§e§l▶");
		    TextComponent quit = new TextComponent(" §c§lX");
		    ClickEvent click1 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat settings chatHeight up");
		    ClickEvent click2 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat settings chatHeight down");
		    ClickEvent click3 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat settings height up");
		    ClickEvent click4 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat settings height down");
		    ClickEvent click5 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat settings width up");
		    ClickEvent click6 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat settings width down");
		    ClickEvent click7 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat settings quit");
		    
			String inspace = "";
			for(int i = 0;i < rp.getChatboxWidth()-2;i++){
				inspace += " ";
			}
			String space = "§8§m";
			for(int i = 0;i < rp.getChatboxWidth()/2-2;i++){
				space += " ";
			}
			
			TextComponent heightLimit = new TextComponent("§8§m" + space);
			up.setClickEvent(click3);
			down.setClickEvent(click4);
			quit.setClickEvent(click7);
			heightLimit.addExtra(up);
			heightLimit.addExtra(down);
			heightLimit.addExtra(space);
			heightLimit.addExtra(quit);
			heightLimit.addExtra("\n");
			text.addExtra(heightLimit);
			
			for(int i = 0;i < rp.getChatboxHeight()-3-rp.getChatHeight();i++){
				text.addExtra("§8|" + inspace + " |§r\n");
			}
			
			TextComponent chatHeightLimit = new TextComponent("§8§m|" + space);
		    up1.setClickEvent(click1);
		    down1.setClickEvent(click2);
		    chatHeightLimit.addExtra(up1);
		    chatHeightLimit.addExtra(down1);
			chatHeightLimit.addExtra(space + " |§r\n");
			text.addExtra(chatHeightLimit);
			
			for(int i = 0;i < rp.getChatHeight();i++){
				text.addExtra("§8|" + inspace + " |§r\n");
			}
			
			TextComponent widthLimit = new TextComponent("§8§m " + space);
			left.setClickEvent(click6);
			right.setClickEvent(click5);
			widthLimit.addExtra(left);
			widthLimit.addExtra(right);
			widthLimit.addExtra(space + " ");
			text.addExtra(widthLimit);
		}else{
			if(this.isFixDisplay()){
				final RChatFixDisplay fixDisplay = getFixDisplayQueue().get(0);
				text.addExtra(this.getUpSeparator(null,fixDisplay.isClosable()));
				text.addExtra("\n");
	    		for(int i = 0;i < fixDisplay.getFixDisplays().size();i++){
	    			text.addExtra(ComponentSerializer.parse(fixDisplay.getFixDisplays().get(i).getJson())[0]);
	    			text.addExtra("\n");
	    		}
				text.addExtra(this.getDownSeparator(null));
				text.addExtra("\n");
				if(this.fixDisplayTask == null){
					this.fixDisplayTask = new BukkitTask(Core.instance){

						@Override
						public void run() {
							if(fixDisplay.isClosable()){
								getFixDisplayQueue().remove(0);
								if(getFixDisplayQueue().size() > 0)update();
							}
						}

						@Override
						public void onCancel() {
						}
						
					}.runTaskLater(fixDisplay.getDelay() > -1 ? fixDisplay.getDelay() : 500);
				}
			}else{
				text.addExtra(this.getUpSeparator(RChat.FUNCTION_INFO,false));
				text.addExtra("\n");
		    	if(getInfos().size() >= INFOS_SHOWN){
					if(getInfos().size() < this.getInfoStartShift()+INFOS_SHOWN)this.setInfoStartShift(getInfos().size()-INFOS_SHOWN);
		    		for(int i = getInfos().size()-this.getInfoStartShift()-INFOS_SHOWN;i < getInfos().size()-this.getInfoStartShift();i++){
		    			WrappedChatComponent component = getInfos().get(i);
		    			if (component != null) text.addExtra(ComponentSerializer.parse(component.getJson())[0]);
		    			text.addExtra("\n");
		    		}
		    	}else{
		    		for(int i = 0;i < INFOS_SHOWN-getInfos().size();i++){
		    			text.addExtra(" \n");
		    		}
		    		for(int i = 0;i < getInfos().size();i++){
		    			WrappedChatComponent component = getInfos().get(i);
		    			if (component != null) text.addExtra(ComponentSerializer.parse(component.getJson())[0]);
		    			text.addExtra("\n");
		    		}
		    	}
				text.addExtra(this.getDownSeparator(RChat.FUNCTION_INFO));
				text.addExtra("\n");
			}
			text.addExtra(this.getFilters());
			text.addExtra(this.getUpSeparator(RChat.FUNCTION_CHAT,false));
			text.addExtra("\n");
			if(DialogManager.isInDialog(this.getPlayer())){
				List<String> messages = this.getPNJMessages();
				int chatHeight = this.isFixDisplay() ? rp.getChatboxHeight()-5-this.getFixDisplayQueue().get(0).getFixDisplays().size() : rp.getChatHeight();
				if(messages.size() >= chatHeight){
					if(messages.size() < this.getChatStartShift()+chatHeight)this.setChatStartShift(messages.size()-chatHeight);
		    		for(int i = messages.size()-chatHeight-this.getChatStartShift();i < messages.size()-this.getChatStartShift();i++){
			    		text.addExtra(messages.get(i));
		    			text.addExtra("\n");
		    		}
		    	}else{
		    		boolean flag = DialogManager.isInDialog(this.getPlayer());
		    		if(!flag){
		        		for(int i = 0;i < chatHeight-messages.size();i++){
			    			text.addExtra("\n");
		        		}
		    		}
		    		for(int i = 0;i < messages.size();i++){
			    		text.addExtra(messages.get(i));
		    			text.addExtra("\n");
		    		}
		    		if(flag){
		        		for(int i = 0;i < chatHeight-messages.size();i++){
			    			text.addExtra("\n");
		        		}
		    		}
		    	}
			}else{
				List<SChatMessage> messages = this.getChatMessages();
				int chatHeight = this.isFixDisplay() ? rp.getChatboxHeight()-5-this.getFixDisplayQueue().get(0).getFixDisplays().size() : rp.getChatHeight();
				if(messages.size() >= chatHeight){
					if(messages.size() < this.getChatStartShift()+chatHeight)this.setChatStartShift(messages.size()-chatHeight);
		    		for(int i = messages.size()-chatHeight-this.getChatStartShift();i < messages.size()-this.getChatStartShift();i++){
		    			SChatMessage message = messages.get(i);
		    			TextComponent line = new TextComponent(message.getFullMessage());
		    			if(!message.getSender().equals(this.getRP())){
			    			line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "> " + message.getSender().getName() + " "));
		    			}
		    			if(message.getItem() != null){
		    			    net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(message.getItem());
			    			if(message.getItem().hasItemMeta()){
			    			    NBTTagCompound compound = new NBTTagCompound();
			    			    if(nmsItemStack.hasTag())compound = nmsItemStack.save(compound);
			    				line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(compound.toString())}));
			    			}else{
			    				line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(nmsItemStack.getName().getString())}));
			    			}
		    			}
			    		text.addExtra(line);
		    			text.addExtra("\n");
		    		}
		    	}else{
		    		boolean flag = DialogManager.isInDialog(this.getPlayer());
		    		if(!flag){
		        		for(int i = 0;i < chatHeight-messages.size();i++){
			    			text.addExtra("\n");
		        		}
		    		}
		    		for(int i = 0;i < messages.size();i++){
		    			SChatMessage message = messages.get(i);
		    			TextComponent line = new TextComponent(message.getFullMessage());
		    			if(!message.getSender().equals(this.getRP())){
			    			line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "> " + message.getSender().getName() + " "));
		    			}
		    			if(message.getItem() != null){
		    			    net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(message.getItem());
			    			if(message.getItem().hasItemMeta()){
			    			    NBTTagCompound compound = new NBTTagCompound();
			    			    if(nmsItemStack.hasTag())compound = nmsItemStack.save(compound);
			    				line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(compound.toString())}));
			    			}else{
			    				line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(nmsItemStack.getName().getString())}));
			    			}
		    			}
			    		text.addExtra(line);
		    			text.addExtra("\n");
		    		}
		    		if(flag){
		        		for(int i = 0;i < chatHeight-messages.size();i++){
			    			text.addExtra("\n");
		        		}
		    		}
		    	}
			}
			text.addExtra(this.getDownSeparator(RChat.FUNCTION_CHAT));
		}
		
		return text;
	}

	public GMember getGM() {
		return gm;
	}
	
	public void update(){
		if(this.isUsed() && this.getPlayer() != null){
			PacketContainer container = new PacketContainer(PacketType.Play.Server.CHAT);
			WrapperPlayServerChat packet = new WrapperPlayServerChat(container);
			packet.setMessage(WrappedChatComponent.fromText(RChat.CHAT_UPDATE_CODE));
			packet.setPosition(RChat.CHAT_UPDATE);
			packet.sendPacket(this.getPlayer());
		}
	}

	public List<String> getPNJMessages() {
		return pnjMessages;
	}
	
	public void clearPNJMessages(){
		this.pnjMessages.clear();
	}

	public boolean isFixDisplay() {
		return !this.getFixDisplayQueue().isEmpty();
	}
	
	public void kill(){
		ProtocolLibrary.getProtocolManager().removePacketListener(this.getListener());
	}

	public List<SChatMessage> getChatMessages() {
		return chatMessages;
	}

	public ChatType getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(ChatType defaultType) {
		this.defaultType = defaultType;
	}

	public List<ChatType> getShownTypes() {
		return shownTypes;
	}
	
	public TextComponent getFilters(){
		String start = "   ";
		for(int i = 0;i < rp.getChatboxWidth()/(this.getRP().isOp() ? 5.75 : 5);i++){//7 for 4 types
			start += " ";
		}
		TextComponent filters = new TextComponent(start);
		for(ChatType type : this.getRP().isOp() ? new ChatType[]{ChatType.GLOBAL, ChatType.SHOUT, ChatType.STAFF, ChatType.GUILD, ChatType.ALLIANCE} : new ChatType[]{ChatType.GLOBAL, ChatType.SHOUT, ChatType.GUILD, ChatType.ALLIANCE}){
		    TextComponent filter = new TextComponent((this.getShownTypes().contains(type) ? type.getSelectedColor() : "§8") + (type.equals(this.getDefaultType()) ? "§l" : "") + "[" + (type.equals(this.getDefaultType()) ? (type.getDisplayFR()) : (type.getDisplayFR().split("")[0])) + "]");
		    ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat " + type.toString().toLowerCase());
		    filter.setClickEvent(click);
		    filters.addExtra(filter);
		    filters.addExtra("   ");
		}
	    filters.addExtra("§r\n");
		return filters;
	}
	
	public TextComponent getUpSeparator(String function, boolean cross){
		TextComponent separator = new TextComponent("");
	    StringBuilder bar = new StringBuilder("§8§m");
	    for(int i = 0;i < rp.getChatboxWidth();i++){
	    	bar.append(" ");
	    }
		if(function != null){
		    TextComponent up = new TextComponent("§b§l▲");
		    ClickEvent click1 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat up " + function);
		    up.setClickEvent(click1);
		    separator.addExtra(up);
		    separator.addExtra(bar.toString());
		}else{
			if(cross){
			    TextComponent x = new TextComponent("§4§lX");
			    ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat fixDisplay");
			    x.setClickEvent(click);
			    separator.addExtra(bar.toString());
			    separator.addExtra(x);
			}else separator.addExtra(bar.append(" ").toString());
		}
	    return separator;
	}
	public TextComponent getDownSeparator(String function){
		TextComponent separator = new TextComponent("");
	    String bar = "§8§m";
	    for(int i = 0;i < rp.getChatboxWidth();i++){
	    	bar += " ";
	    }
	    if(function != null){
		    TextComponent down = new TextComponent("§b§l▼");
		    ClickEvent click2 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat down " + function);
		    down.setClickEvent(click2);
		    separator.addExtra(down);
		    separator.addExtra(bar);
	    }else separator.addExtra(bar + " ");
	    return separator;
	}

	public int getInfoStartShift() {
		return infoStartShift;
	}

	public void setInfoStartShift(int infoStartShift) {
		this.infoStartShift = infoStartShift;
	}

	public int getChatStartShift() {
		return chatStartShift;
	}

	public void setChatStartShift(int chatStartShift) {
		this.chatStartShift = chatStartShift;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
		
	public void clear(){
		this.getFixDisplayQueue().clear();
		this.clearPNJMessages();
		this.getChatMessages().clear();
		this.getInfos().clear();
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	
	public List<RChatFixDisplay> getFixDisplayQueue() {
		return fixDisplayQueue;
	}
	
	public BukkitTask getFixDisplayTask() {
		return fixDisplayTask;
	}
	
	public void setFixDisplayTask(BukkitTask task) {
		this.fixDisplayTask = task;
	}
	
	public void closeFixDisplay(){
		if(this.getFixDisplayQueue().size() > 0){
			RChatFixDisplay display = this.getFixDisplayQueue().get(0);
			if(display.isClosable()){
				if(display.getOnClose() != null){
					display.getOnClose().run();
				}
				this.forceCloseFixDisplay();
			}
		}
	}
	
	public void forceCloseFixDisplay(){
		if(this.fixDisplayTask != null){
			this.fixDisplayTask.cancel();
			this.fixDisplayTask = null;
		}
		if(this.getFixDisplayQueue().size() > 0){
			this.getFixDisplayQueue().remove(0);
			this.update();
		}
	}

	public void addFixDisplay(RChatFixDisplay fixDisplay){
		if(this.isUsed()){
			this.getFixDisplayQueue().add(fixDisplay);
			if(this.getFixDisplayQueue().size() == 1){
				this.update();
			}
		}else{
			for(WrappedChatComponent component : fixDisplay.getFixDisplays()){
				this.addInfo(component);
			}
		}
	}
}
