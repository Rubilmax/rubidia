package me.pmilon.RubidiaManager.chunks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

import org.bukkit.Location;

import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.extent.clipboard.io.SpongeSchematicReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.world.World;

public class ChunkManager {

	private final static HashMap<Chunk,ChunkManager> managers = new HashMap<Chunk,ChunkManager>();
	
	private final Chunk chunk;
	
	public BukkitWorld localWorld;
	public EditSession editSession;
	
	private BlockArrayClipboard clipboard;
	
	private ChunkManager(Chunk chunk) {
		this.chunk = chunk;
		
		this.localWorld = new BukkitWorld(chunk.getWorld());
		this.editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((World) this.getLocalWorld(), -1);
	}
	
	public static ChunkManager getManager(Chunk chunk){
		ChunkManager manager = null;
		if(ChunkManager.managers.containsKey(chunk)){
			manager = ChunkManager.managers.get(chunk);
		}else{
			manager = new ChunkManager(chunk);
			ChunkManager.managers.put(chunk, manager);
		}
		return manager;
	}
	
	public BukkitWorld getLocalWorld() {
		return localWorld;
	}

	public Chunk getChunk() {
		return chunk;
	}
	
	public boolean isSaved(){
		if(this.getFile() != null)return this.getFile().exists();
		return false;
	}
	
	public void pasteFromFile() {
        Closer closer = Closer.create();
        try {
    		File file = WorldEdit.getInstance().getSafeSaveFile(null, this.getFile().getParentFile(), this.getFile().getName(), "schematic", "schematic");
    		
    		FileInputStream fis = new FileInputStream(file);
    		BufferedInputStream bis = new BufferedInputStream(fis);
    		NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(bis));
    		ClipboardReader reader = new SpongeSchematicReader(nbtStream);

    		Clipboard clipboard = reader.read();
            this.paste(clipboard, true);
            reader.close();
        } catch(IOException | FilenameException | MaxChangedBlocksException | IncompleteRegionException ex) {
        	ex.printStackTrace();
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            }
        }
	}
	
	public void undoPaste() {
		if(this.getClipboard() != null) {
			try {
				this.paste(this.getClipboard(), true);
				if(this.getChunk() instanceof RChunk){
					((RChunk)chunk).setRegenerated(!((RChunk)chunk).isRegenerated());
				}
			} catch (MaxChangedBlocksException | IncompleteRegionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void paste(Clipboard clipboard, boolean save) throws MaxChangedBlocksException, IncompleteRegionException {
		if(save)this.setClipboard(this.copy());
		
		BlockVector3 origin = getBlockVector(this.getChunk().getOrigin());
        
        Operation operation = new ClipboardHolder(clipboard)
                .createPaste(this.getEditSession())
                .to(origin)
                .ignoreAirBlocks(false)
                .build();
		Operations.completeLegacy(operation);

		this.getEditSession().flushSession();

		if(this.getChunk() instanceof RChunk){
			((RChunk)chunk).setRegenerated(true);
		}
		Core.console.sendMessage("§6Pasted chunk §e" + this.getChunk().getX() + " " + this.getChunk().getZ() + " §6in world §e" + this.getChunk().getWorld().getName());
	}
	
	public BlockArrayClipboard copy() throws IncompleteRegionException, MaxChangedBlocksException {
		Region region = new CuboidRegion(getBlockVector(this.getChunk().getOrigin()), getBlockVector(this.getChunk().getDestination()));
		BlockArrayClipboard blockClipboard = new BlockArrayClipboard(region);
        blockClipboard.setOrigin(getBlockVector(this.getChunk().getOrigin()));
        ForwardExtentCopy copy = new ForwardExtentCopy(this.getEditSession(), region, blockClipboard, region.getMinimumPoint());
        Operations.completeLegacy(copy);
        return blockClipboard;
	}
	
	public void saveToFile(){
        Closer closer = Closer.create();
        
        try {
            File file = WorldEdit.getInstance().getSafeSaveFile(null, this.getFile().getParentFile(), this.getFile().getName(), "schematic", "schematic");
            FileOutputStream fos = closer.register(new FileOutputStream(file));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(bos));
            writer.write(this.copy());
            
			Core.console.sendMessage("§6Saved chunk §e" + this.getChunk().getX() + " " + this.getChunk().getZ() + " §6from world §e" + this.getChunk().getWorld().getName());
        } catch (IOException | FilenameException | MaxChangedBlocksException | IncompleteRegionException ex) {
			ex.printStackTrace();
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            	ignored.printStackTrace();
            }
        }
	}

	private BlockVector3 getBlockVector(Location loc) {
		return BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
	}

	public File getFile(){
		return new File(RubidiaManagerPlugin.getSavesFolder(this.getChunk().getWorld()), this.getChunk().getSaveName() + ".schematic");
	}
	
	public EditSession getEditSession() {
		return editSession;
	}

	public BlockArrayClipboard getClipboard() {
		return clipboard;
	}

	public void setClipboard(BlockArrayClipboard clipboard) {
		this.clipboard = clipboard;
	}

}
