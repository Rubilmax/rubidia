package me.pmilon.RubidiaCore.ui;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerBlockAction;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

public class EnderChestUI extends UIHandler{

	private final Block block;
	private final PacketAdapter adapter;
	private boolean open = false;
	public EnderChestUI(Player p, final Block block) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 27, StringUtils.abbreviate(("Coffre du Néant") + " | " + rp.getName(),32));
		this.block = block;
		this.adapter = new PacketAdapter(Core.instance, PacketType.Play.Server.BLOCK_ACTION){
	        @Override
	        public void onPacketSending(PacketEvent e) {
	            WrapperPlayServerBlockAction packet = new WrapperPlayServerBlockAction(e.getPacket());
	            if(packet.getLocation().getX() == block.getX() && packet.getLocation().getY() == block.getY() && packet.getLocation().getZ() == block.getZ()
	            		&& packet.getByte1() == 1) {
	            	packet.setByte2((byte) (open ? 1 : 0));
	            }
	        }
	    };
		ProtocolLibrary.getProtocolManager().addPacketListener(adapter);
	}

	@Override
	public String getType() {
		return "ENDER_CHEST";
	}

	@Override
	protected boolean openWindow() {
		this.changeState(true);
		for(int i : rp.getLoadedSPlayer().getEnderchest().keySet()){
			this.getMenu().setItem(i, rp.getLoadedSPlayer().getEnderchest().get(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(this.block.getType().equals(Material.ENDER_CHEST))this.changeState(false);
		for(int i = 0;i < this.getMenu().getSize();i++){
			rp.getLoadedSPlayer().getEnderchest().put(i, this.getMenu().getItem(i));
		}
	}

	@Override
	protected void closeUI() {
		ProtocolLibrary.getProtocolManager().removePacketListener(adapter);
		this.getHolder().closeInventory();
	}

	public void changeState(boolean open) {
		this.open = open;
        WrapperPlayServerBlockAction packet = new WrapperPlayServerBlockAction();
        packet.setBlockType(this.block.getType());
        packet.setLocation(new BlockPosition(this.block.getX(), this.block.getY(), this.block.getZ()));
        packet.setByte1((byte) 1);
        packet.setByte2((open) ? (byte) 1 : 0);
	    for (Player player : Bukkit.getOnlinePlayers()) {
	        if(open) player.playSound(this.block.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
	        else player.playSound(this.block.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
	        packet.sendPacket(player);
	    }
	}
	
}
