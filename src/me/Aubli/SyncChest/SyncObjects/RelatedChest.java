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


public class RelatedChest {
    
    private File chestFile;
    private FileConfiguration chestConfig;
    
    private int ChestID;
    private int doubleChestID;
    private Location chestLoc;
    
    private boolean doubleChest;
    private boolean linked;
    
    private MainChest linkedChest;
    private ChestType chestType;
    
    public RelatedChest(String chestPath, ChestType type, Location chestLoc, int ID, boolean doubleChest) {
	this.chestLoc = chestLoc;
	this.ChestID = ID;
	this.doubleChest = doubleChest;
	this.chestType = type;
	
	this.linked = false;
	this.doubleChest = false;
	
	this.chestFile = new File(chestPath + "/" + this.ChestID + ".yml");
	this.chestConfig = YamlConfiguration.loadConfiguration(this.chestFile);
	
	save();
    }
    
    public RelatedChest(File chestFile) {
	this.chestFile = chestFile;
	this.chestConfig = YamlConfiguration.loadConfiguration(chestFile);
	
	this.ChestID = this.chestConfig.getInt("config.Chest.ID");
	this.doubleChestID = this.chestConfig.getInt("config.Chest.doubleChestID");
	
	this.doubleChest = this.chestConfig.getBoolean("config.Chest.doubleChest");
	this.linked = this.chestConfig.getBoolean("config.Chest.linked");
	
	this.chestType = ChestType.valueOf(this.chestConfig.getString("config.Chest.type"));
	
	this.linkedChest = SyncManager.getManager().getMainChest(this.chestConfig.getInt("config.Chest.linkedChest"));
	
	this.chestLoc = new Location(Bukkit.getWorld(UUID.fromString(this.chestConfig.getString("config.Chest.Location.world"))), this.chestConfig.getInt("config.Chest.Location.X"), this.chestConfig.getInt("config.Chest.Location.Y"), this.chestConfig.getInt("config.Chest.Location.Z"));
	
    }
    
    void save() {
	
	try {
	    this.chestConfig.set("config.Chest.ID", this.ChestID);
	    this.chestConfig.set("config.Chest.type", this.chestType.toString());
	    this.chestConfig.set("config.Chest.linked", this.linked);
	    this.chestConfig.set("config.Chest.doubleChest", this.doubleChest);
	    this.chestConfig.set("config.Chest.doubleChestID", this.doubleChestID);
	    this.chestConfig.set("config.Chest.linkedChest", "");
	    this.chestConfig.set("config.Chest.Location.world", getWorld().getUID().toString());
	    this.chestConfig.set("config.Chest.Location.X", getX());
	    this.chestConfig.set("config.Chest.Location.Z", getZ());
	    this.chestConfig.set("config.Chest.Location.Y", getY());
	    
	    if (this.linkedChest != null) {
		this.chestConfig.set("config.Chest.linkedChest", this.linkedChest.getID());
	    }
	    
	    this.chestConfig.save(this.chestFile);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    void delete() {
	unLink();
	this.chestFile.delete();
    }
    
    public void setDoubleChest(boolean doubleChest, int doubleChestID) {
	this.doubleChest = doubleChest;
	this.doubleChestID = doubleChestID;
    }
    
    public void link(MainChest mChest) {
	if (isLinked()) {
	    unLink();
	}
	
	this.linkedChest = mChest;
	this.linked = true;
	
	if (isDoubleChest()) {
	    if (getDoubleChest().isLinked() == false) {
		getDoubleChest().link(mChest);
	    }
	}
    }
    
    public void unLink() {
	this.linkedChest = null;
	this.linked = false;
	
	if (isDoubleChest()) {
	    if (getDoubleChest() != null) {
		if (getDoubleChest().isLinked() == true) {
		    getDoubleChest().unLink();
		}
	    }
	}
    }
    
    public int getID() {
	return this.ChestID;
    }
    
    public MainChest getLinkedChest() {
	return this.linkedChest;
    }
    
    public RelatedChest getDoubleChest() {
	return SyncManager.getManager().getRelatedChest(this.doubleChestID);
    }
    
    public int getDoubleChestID() {
	return this.doubleChestID;
    }
    
    public Location getLocation() {
	return this.chestLoc;
    }
    
    public World getWorld() {
	return this.chestLoc.getWorld();
    }
    
    public int getX() {
	return this.chestLoc.getBlockX();
    }
    
    public int getY() {
	return this.chestLoc.getBlockY();
    }
    
    public int getZ() {
	return this.chestLoc.getBlockZ();
    }
    
    public Inventory getInventory() {
	return ((Chest) this.chestLoc.getBlock().getState()).getInventory();
    }
    
    public boolean isLinked() {
	return this.linked;
    }
    
    public boolean isDoubleChest() {
	return this.doubleChest;
    }
    
    @Override
    public String toString() {
	return ChatColor.DARK_PURPLE + "RelatedChest" + getID() + ChatColor.DARK_GRAY + " [" + ChatColor.GREEN + getWorld().getName() + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;
    }
}
