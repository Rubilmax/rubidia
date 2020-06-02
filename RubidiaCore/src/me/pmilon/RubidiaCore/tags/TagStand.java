package me.pmilon.RubidiaCore.tags;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.packets.FakeArmorStand;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TagStand {

	protected final Entity holder;
	protected String[] layers;
	protected boolean fix;
	protected BukkitTask[] tasks;
	protected FakeArmorStand[] displays;
	public TagStand(Entity holder, String[] layers, boolean fix){
		this.holder = holder;
		this.fix = fix;
		this.tasks = new BukkitTask[layers.length];
		this.displays = new FakeArmorStand[layers.length];
		this.setLayers(layers, false);
	}
	
	public Entity getHolder() {
		return holder;
	}
	
	public String[] getLayers() {
		return layers;
	}
	
	public FakeArmorStand[] getDisplays(){
		return displays;
	}
	
	public void setLayers(String[] layers, boolean update) {
		for(int i = 0; i < layers.length / 2; i++) {
		    String temp = layers[i];
		    layers[i] = layers[layers.length - i - 1];
		    layers[layers.length - i - 1] = temp;
		}
		
		this.layers = layers;
		if(update) this.update();
	}
	
	public void display(){
		TagStandManager.registerTagStand(this);
		for (int i = 0;i < layers.length;i++) {
			this.displays[i] = this.register(i);
		}
	}
	
	public void show(Player player) {
		if (this.getHolder().getLocation().getWorld().equals(player.getWorld())) {
			for (int i = 0;i < this.displays.length;i++) {
				FakeArmorStand stand = this.displays[i];
				stand.spawn(getLocation(i), player);
			}
		}
	}
	public void hide(Player player) {
		for (FakeArmorStand stand : this.displays) {
			if (stand != null) {
				stand.destroy(player);
			}
		}
	}
	
	public Location getLocation(int index) {
		double deviation = index * .25 + (this.getHolder().isCustomNameVisible() ? .25 : 0);
		double height = this.getHolder().getHeight() + deviation;
		return this.getHolder().getLocation().clone().add(0, height, 0);
	}
	
	public FakeArmorStand register(final int index) {
		Location location = this.getLocation(index);
		final FakeArmorStand display = new FakeArmorStand(location.getWorld(), this.layers[index], true).spawn(location);
		
		this.tasks[index] = new BukkitTask(Core.instance) {
			public void run() {
				if(!getHolder().isValid() || getHolder().isDead()) {
					remove();
				} else {
					display.teleport(getLocation(index));
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimerAsynchronously(0,0);
		
		return display;
	}
	
	public void update() {
		if (this.displays.length == this.layers.length) {
			for(int i = 0;i < this.displays.length;i++) {
				FakeArmorStand stand = this.displays[i];
				if(stand != null) {
					if(stand.getName() == null || !stand.getName().equals(this.layers[i])) {
						stand.setName(this.layers[i]);
						stand.update();
					}
				}
			}
		} else {
			this.remove();
			this.tasks = new BukkitTask[layers.length];
			this.displays = new FakeArmorStand[layers.length];
			this.display();
		}
	}
	
	public void remove() {
		for(BukkitTask task : this.tasks) {
			if(task != null) {
				task.cancel();
			}
		}
		for(FakeArmorStand stand : this.displays) {
			if(stand != null) {
				stand.destroy();
			}
		}
		TagStandManager.unregisterTagStand(this);
	}

	public boolean isFix() {
		return fix;
	}

	public void setFix(boolean fix) {
		this.fix = fix;
		this.update();
	}
	
}
