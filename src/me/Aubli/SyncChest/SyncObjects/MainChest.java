package me.Aubli.SyncChest.SyncObjects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


public class MainChest {
    
    private File chestFile;
    private FileConfiguration chestConfig;
    
    private int ChestID;
    private int doubleChestID;
    private Location chestLoc;
    
    private boolean doubleChest;
    private boolean linked;
    
    private ArrayList<RelatedChest> linkedChests;
    private ChestType chestType;
    
    public MainChest(String chestPath, ChestType type, Location chestLoc, int ID, boolean doubleChest) {
	this.chestLoc = chestLoc;
	this.ChestID = ID;
	this.doubleChest = doubleChest;
	this.chestType = type;
	
	this.linked = false;
	this.doubleChest = doubleChest;
	
	this.chestFile = new File(chestPath + File.separator + this.ChestID + ".yml");
	this.chestConfig = YamlConfiguration.loadConfiguration(this.chestFile);
	
	this.linkedChests = new ArrayList<RelatedChest>();
	
	save();
    }
    
    public MainChest(File chestFile) {
	this.chestFile = chestFile;
	this.chestConfig = YamlConfiguration.loadConfiguration(chestFile);
	
	this.ChestID = this.chestConfig.getInt("config.Chest.ID");
	this.doubleChestID = this.chestConfig.getInt("config.Chest.doubleChestID");
	
	this.doubleChest = this.chestConfig.getBoolean("config.Chest.doubleChest");
	this.linked = this.chestConfig.getBoolean("config.Chest.linked");
	
	this.chestType = ChestType.valueOf(this.chestConfig.getString("config.Chest.type"));
	
	this.linkedChests = new ArrayList<RelatedChest>();
	
	if (this.linked) {
	    String linkedString = this.chestConfig.getString("config.Chest.linkedChests");
	    linkedString = linkedString.replace("[", "").replace("]", "");
	    if (linkedString.contains(",")) {
		for (int i = 0; i < linkedString.split(", ").length; i++) {
		    this.linkedChests.add(SyncManager.getManager().getRelatedChest(Integer.parseInt(linkedString.split(", ")[i])));
		}
	    } else {
		this.linkedChests.add(SyncManager.getManager().getRelatedChest(Integer.parseInt(linkedString)));
	    }
	}
	
	this.chestLoc = new Location(Bukkit.getWorld(UUID.fromString(this.chestConfig.getString("config.Chest.Location.world"))), this.chestConfig.getInt("config.Chest.Location.X"), this.chestConfig.getInt("config.Chest.Location.Y"), this.chestConfig.getInt("config.Chest.Location.Z"));
    }
    
    void save() {
	
	String linkedString = "[";
	
	if (getLinkedChests().length != 0) {
	    for (int i = 0; i < getLinkedChests().length - 1; i++) {
		linkedString += getLinkedChests()[i].getID() + ", ";
	    }
	    linkedString += getLinkedChests()[getLinkedChests().length - 1].getID() + "]";
	} else {
	    linkedString += "]";
	}
	
	try {
	    this.chestConfig.set("config.Chest.ID", this.ChestID);
	    this.chestConfig.set("config.Chest.type", this.chestType.toString());
	    this.chestConfig.set("config.Chest.linked", this.linked);
	    this.chestConfig.set("config.Chest.doubleChest", this.doubleChest);
	    this.chestConfig.set("config.Chest.doubleChestID", this.doubleChestID);
	    this.chestConfig.set("config.Chest.linkedChests", linkedString);
	    this.chestConfig.set("config.Chest.Location.world", getWorld().getUID().toString());
	    this.chestConfig.set("config.Chest.Location.X", getX());
	    this.chestConfig.set("config.Chest.Location.Z", getZ());
	    this.chestConfig.set("config.Chest.Location.Y", getY());
	    
	    this.chestConfig.save(this.chestFile);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    void delete() {
	for (int i = 0; i < this.linkedChests.size(); i++) {
	    unLink(this.linkedChests.get(i));
	}
	this.chestFile.delete();
    }
    
    public void setDoubleChest(boolean doubleChest, int doubleChestID) {
	this.doubleChest = doubleChest;
	this.doubleChestID = doubleChestID;
    }
    
    public void link(RelatedChest relChest) {
	if (!this.linkedChests.contains(relChest)) {
	    this.linkedChests.add(relChest);
	    this.linked = true;
	    if (isDoubleChest()) {
		if (getDoubleChest().isLinked() == false) {
		    getDoubleChest().link(relChest);
		}
	    }
	}
    }
    
    public void unLink(RelatedChest relChest) {
	if (this.linkedChests.contains(relChest)) {
	    relChest.unLink();
	    this.linkedChests.remove(relChest);
	    if (this.linkedChests.size() == 0) {
		this.linked = false;
	    }
	    if (isDoubleChest()) {
		if (getDoubleChest() != null) {
		    if (getDoubleChest().isLinked() == true) {
			getDoubleChest().unLink(relChest);
		    }
		}
	    }
	}
	
    }
    
    public int getID() {
	return this.ChestID;
    }
    
    public RelatedChest[] getLinkedChests() {
	RelatedChest[] chests = new RelatedChest[this.linkedChests.size()];
	
	for (int i = 0; i < this.linkedChests.size(); i++) {
	    chests[i] = this.linkedChests.get(i);
	}
	return chests;
    }
    
    public ArrayList<RelatedChest> getLinkedChestsList() {
	return this.linkedChests;
    }
    
    public MainChest getDoubleChest() {
	return SyncManager.getManager().getMainChest(this.doubleChestID);
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
	return ChatColor.GOLD + "MainChest" + getID() + ChatColor.DARK_GRAY + " [" + ChatColor.GREEN + getWorld().getName() + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;
    }
    
}
