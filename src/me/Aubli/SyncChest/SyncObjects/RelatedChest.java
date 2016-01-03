package me.Aubli.SyncChest.SyncObjects;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import me.Aubli.SyncChest.SyncObjects.SyncManager.ChestType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

public class RelatedChest{
	
	private File chestFile;
	private FileConfiguration chestConfig;
	
	private int ChestID;
	private int doubleChestID;
	private Location chestLoc;
	
	private boolean doubleChest;
	private boolean linked;
	
	private MainChest linkedChest;
	private ChestType chestType;
	
	public RelatedChest(String chestPath, ChestType type, Location chestLoc, int ID, boolean doubleChest){
		this.chestLoc = chestLoc;
		this.ChestID = ID;
		this.doubleChest = doubleChest;
		this.chestType = type;
		
		this.linked = false;
		this.doubleChest = false;
		
		this.chestFile = new File(chestPath + File.separator + ChestID + ".yml");
		this.chestConfig = YamlConfiguration.loadConfiguration(chestFile);
		
		save();
	}
	
	public RelatedChest(File chestFile){
		this.chestFile = chestFile;
		this.chestConfig = YamlConfiguration.loadConfiguration(chestFile);
		
		this.ChestID = chestConfig.getInt("config.Chest.ID");
		this.doubleChestID = chestConfig.getInt("config.Chest.doubleChestID");
		
		this.doubleChest = chestConfig.getBoolean("config.Chest.doubleChest");
		this.linked = chestConfig.getBoolean("config.Chest.linked");
		
		this.chestType = ChestType.valueOf(chestConfig.getString("config.Chest.type"));		
		
		this.linkedChest = SyncManager.getManager().getMainChest(chestConfig.getInt("config.Chest.linkedChest"));
		
		this.chestLoc = new Location(Bukkit.getWorld(UUID.fromString(chestConfig.getString("config.Chest.Location.world"))), chestConfig.getInt("config.Chest.Location.X"), chestConfig.getInt("config.Chest.Location.Y"), chestConfig.getInt("config.Chest.Location.Z"));	
		
	}
	
	void save(){
		
		
		try{
			chestConfig.set("config.Chest.ID", ChestID);
			chestConfig.set("config.Chest.type", chestType.toString());
			chestConfig.set("config.Chest.linked", linked);
			chestConfig.set("config.Chest.doubleChest", doubleChest);
			chestConfig.set("config.Chest.doubleChestID", doubleChestID);
			chestConfig.set("config.Chest.linkedChest", "");
			chestConfig.set("config.Chest.Location.world", getWorld().getUID().toString());
			chestConfig.set("config.Chest.Location.X", getX());
			chestConfig.set("config.Chest.Location.Z", getZ());
			chestConfig.set("config.Chest.Location.Y", getY());	
			
			if(linkedChest!=null){
				chestConfig.set("config.Chest.linkedChest", linkedChest.getID());
			}			
			
			chestConfig.save(chestFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	void delete(){
		unLink();
		chestFile.delete();
	}
	
	public void setDoubleChest(boolean doubleChest, int doubleChestID){
		this.doubleChest = doubleChest;
		this.doubleChestID = doubleChestID;
	}

	public void link(MainChest mChest){
		if(isLinked()){
			unLink();
		}
		
		this.linkedChest = mChest;
		this.linked = true;
			
		if(isDoubleChest()){
			if(getDoubleChest().isLinked()==false){
				getDoubleChest().link(mChest);
			}
		}
	}
	
	public void unLink(){		
		this.linkedChest = null;
		this.linked = false;
		
		if(isDoubleChest()){
			if(getDoubleChest()!=null){
				if(getDoubleChest().isLinked()==true){
					getDoubleChest().unLink();
				}
			}
		}
	}
	
	public int getID(){
		return ChestID;
	}
	
	public MainChest getLinkedChest(){
		return linkedChest;
	}
	
	public RelatedChest getDoubleChest(){
		return SyncManager.getManager().getRelatedChest(doubleChestID);
	}
	
	public int getDoubleChestID(){
		return doubleChestID;
	}
	
	public Location getLocation(){
		return chestLoc;
	}
	
	public World getWorld(){
		return chestLoc.getWorld();
	}
	
	public int getX(){
		return chestLoc.getBlockX();
	}
	
	public int getY(){
		return chestLoc.getBlockY();
	}
	
	public int getZ(){
		return chestLoc.getBlockZ();
	}
	
	public Inventory getInventory(){
		return ((Chest) chestLoc.getBlock().getState()).getInventory();
	}
	
	
	public boolean isLinked(){
		return linked;
	}
	
	public boolean isDoubleChest(){
		return doubleChest;
	}

	@Override
	public String toString(){
		return ChatColor.DARK_PURPLE + "RelatedChest" + getID() + ChatColor.DARK_GRAY + " [" + ChatColor.GREEN + getWorld().getName() + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;
	}
}
