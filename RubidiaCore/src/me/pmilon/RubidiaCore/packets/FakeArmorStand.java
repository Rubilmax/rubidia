package me.pmilon.RubidiaCore.packets;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

import net.minecraft.server.v1_13_R2.EntityArmorStand;

public class FakeArmorStand {
	
	private String name;
	private boolean invisible;
	private final EntityArmorStand stand;
	public FakeArmorStand(World world, String name, boolean invisible) {
		this.name = name;
		this.invisible = invisible;
		this.stand = new EntityArmorStand(((CraftWorld) world).getHandle(), 0, 0, 0);
	}

	private WrapperPlayServerSpawnEntityLiving getSpawnPacket(Location location) {
		WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving(this.getEntity().getBukkitEntity());
		packet.setX(location.getX());
		packet.setY(location.getY());
		packet.setZ(location.getZ());
		
		return packet;
	}
	public FakeArmorStand spawn(Location location) {
		this.destroy(); // to remove armorstand to any former observer
		this.getSpawnPacket(location).sendPacket();
		this.update();
		return this;
	}
	public FakeArmorStand spawn(Location location, Player receiver) {
		this.getSpawnPacket(location).sendPacket(receiver);
		this.update(receiver);
		return this;
	}

	private WrapperPlayServerRelEntityMove getMovePacket(Vector dp) {
		WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove();
		packet.setEntityID(this.getEntity().getId());
		
		packet.setDx(dp.getX());
		packet.setDy(dp.getY());
		packet.setDz(dp.getZ());
		packet.setOnGround(false);
		
		return packet;
	}
	public FakeArmorStand move(Vector dp, Player receiver) {
		this.getMovePacket(dp).sendPacket(receiver.getLocation(), 64);
		return this;
	}
	public FakeArmorStand move(Vector dp) {
		this.getMovePacket(dp).sendPacket();
		return this;
	}

	private WrapperPlayServerEntityTeleport getTeleportPacket(Location location) {
		WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();
		packet.setEntityID(this.getEntity().getId());
		
		packet.setX(location.getX());
		packet.setY(location.getY());
		packet.setZ(location.getZ());
		packet.setPitch(location.getPitch());
		packet.setYaw(location.getYaw());
		packet.setOnGround(false);
		
		return packet;
	}
	public FakeArmorStand teleport(Location location, Player receiver) {
		this.getTeleportPacket(location).sendPacket(receiver.getLocation(), 64);
		return this;
	}
	public FakeArmorStand teleport(Location location) {
		this.getTeleportPacket(location).sendPacket(location, 64);
		return this;
	}

	private WrapperPlayServerEntityMetadata getUpdatePacket() {
		WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
		packet.setEntityID(this.getEntity().getId());
		
		WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getMetadata());

		if (this.isInvisible()) {
			WrappedDataWatcherObject invisible = new WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
			
			watcher.setObject(invisible, (byte) 0x20);
		}
		
		if (this.getName() != null && !this.getName().isEmpty()) {
			WrappedDataWatcherObject name = new WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true));
			WrappedDataWatcherObject nameVisible = new WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class));
			
			watcher.setObject(name, Optional.of(WrappedChatComponent.fromText(this.getName()).getHandle()));
			watcher.setObject(nameVisible, true);
		}
		
		packet.setMetadata(watcher.getWatchableObjects());
		return packet;
	}
	public FakeArmorStand update() {
		this.getUpdatePacket().sendPacket();
		return this;
	}
	public FakeArmorStand update(Player receiver) {
		this.getUpdatePacket().sendPacket(receiver);
		return this;
	}
	
	public WrapperPlayServerEntityDestroy getDestroyPacket() {
		WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
		packet.setEntityIds(new int[]{ this.getEntity().getId() });
		return packet;
	}
	public FakeArmorStand destroy() {
		this.getDestroyPacket().sendPacket();
		return this;
	}
	public FakeArmorStand destroy(Player receiver) {
		this.getDestroyPacket().sendPacket(receiver);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public EntityArmorStand getEntity() {
		return stand;
	}

}
