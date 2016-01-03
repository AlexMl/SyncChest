package me.Aubli.SyncChest.SyncObjects;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class SyncHopper {
    
    private File hopperFile;
    private FileConfiguration hopperConfig;
    
    private int HopperID;
    private Location hopperLoc;
    
    private Inventory source;
    
    private Location destLoc;
    private Location sourceLoc;
    
    private Hopper hopper;
    
    private boolean inUse;
    private ItemStack item;
    
    public SyncHopper(String hopperPath, int ID, Location hopperLoc, Location source, Location dest) {
	this.hopperFile = new File(hopperPath + File.separator + ID + ".yml");
	this.hopperConfig = YamlConfiguration.loadConfiguration(this.hopperFile);
	
	this.HopperID = ID;
	this.hopperLoc = hopperLoc.clone();
	
	this.hopper = (Hopper) hopperLoc.getBlock().getState();
	
	this.sourceLoc = source.clone();
	this.destLoc = dest.clone();
	
	if (this.destLoc.getWorld() != null && this.sourceLoc.getWorld() != null) {
	    this.source = SyncManager.getManager().getInventory(this.sourceLoc);
	}
	
	this.inUse = false;
	this.item = null;
	save();
    }
    
    public SyncHopper(File hopperFile) {
	this.hopperFile = hopperFile;
	this.hopperConfig = YamlConfiguration.loadConfiguration(this.hopperFile);
	
	this.HopperID = this.hopperConfig.getInt("config.Hopper.ID");
	
	this.hopperLoc = new Location(Bukkit.getWorld(UUID.fromString(this.hopperConfig.getString("config.Hopper.Location.world"))), this.hopperConfig.getInt("config.Hopper.Location.X"), this.hopperConfig.getInt("config.Hopper.Location.Y"), this.hopperConfig.getInt("config.Hopper.Location.Z"));
	this.destLoc = new Location(Bukkit.getWorld(UUID.fromString(this.hopperConfig.getString("config.Hopper.Location.Destination.world"))), this.hopperConfig.getInt("config.Hopper.Location.Destination.X"), this.hopperConfig.getInt("config.Hopper.Location.Destination.Y"), this.hopperConfig.getInt("config.Hopper.Location.Destination.Z"));
	this.sourceLoc = new Location(Bukkit.getWorld(UUID.fromString(this.hopperConfig.getString("config.Hopper.Location.Source.world"))), this.hopperConfig.getInt("config.Hopper.Location.Source.X"), this.hopperConfig.getInt("config.Hopper.Location.Source.Y"), this.hopperConfig.getInt("config.Hopper.Location.Source.Z"));
	
	if (this.hopperLoc.getWorld() != null) {
	    this.hopper = (Hopper) this.hopperLoc.getBlock().getState();
	}
	
	if (this.sourceLoc.getWorld() != null) {
	    this.source = SyncManager.getManager().getInventory(this.sourceLoc);
	}
	
    }
    
    void save() {
	
	try {
	    this.hopperConfig.set("config.Hopper.ID", this.HopperID);
	    
	    this.hopperConfig.set("config.Hopper.Location.world", this.hopperLoc.getWorld().getUID().toString());
	    this.hopperConfig.set("config.Hopper.Location.X", this.hopperLoc.getBlockX());
	    this.hopperConfig.set("config.Hopper.Location.Y", this.hopperLoc.getBlockY());
	    this.hopperConfig.set("config.Hopper.Location.Z", this.hopperLoc.getBlockZ());
	    
	    this.hopperConfig.set("config.Hopper.Location.Destination.world", this.destLoc.getWorld().getUID().toString());
	    this.hopperConfig.set("config.Hopper.Location.Destination.X", this.destLoc.getBlockX());
	    this.hopperConfig.set("config.Hopper.Location.Destination.Y", this.destLoc.getBlockY());
	    this.hopperConfig.set("config.Hopper.Location.Destination.Z", this.destLoc.getBlockZ());
	    
	    this.hopperConfig.set("config.Hopper.Location.Source.world", this.sourceLoc.getWorld().getUID().toString());
	    this.hopperConfig.set("config.Hopper.Location.Source.X", this.sourceLoc.getBlockX());
	    this.hopperConfig.set("config.Hopper.Location.Source.Y", this.sourceLoc.getBlockY());
	    this.hopperConfig.set("config.Hopper.Location.Source.Z", this.sourceLoc.getBlockZ());
	    
	    this.hopperConfig.save(this.hopperFile);
	    
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }
    
    void delete() {
	this.hopperFile.delete();
    }
    
    public void initialize() {
	this.inUse = true;
	
	if (this.source != null) {
	    
	    int slot = 0;
	    
	    for (int i = 0; i < getInventory().getSize(); i++) {
		if (getInventory().getItem(i) == null || getInventory().getItem(i).getType() == Material.AIR) {
		    slot++;
		}
	    }
	    
	    if (slot == getInventory().getSize()) {
		// Bukkit.broadcastMessage("Starte Initiirung!");
		
		if (SyncManager.getManager().getRelatedChest(this.sourceLoc) != null) {
		    RelatedChest sourceChest = SyncManager.getManager().getRelatedChest(this.sourceLoc);
		    if (sourceChest.isLinked()) {
			MainChest mChest = sourceChest.getLinkedChest();
			
			for (int y = 0; y < mChest.getInventory().getSize(); y++) {
			    if (mChest.getInventory().getItem(y) != null) {
				
				ItemStack item = mChest.getInventory().getItem(y).clone();
				item.setAmount(1);
				
				if (mChest.getInventory().getItem(y).getDurability() != 0) {
				    item.setDurability(mChest.getInventory().getItem(y).getDurability());
				    // Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
				}
				if (mChest.getInventory().getItem(y).getEnchantments() != null) {
				    item.addUnsafeEnchantments(mChest.getInventory().getItem(y).getEnchantments());
				    // Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
				}
				
				getInventory().addItem(item);
				setItem(item);
				mChest.getInventory().removeItem(item);
				// Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Transfer eingeleitet! 1");
				return;
			    }
			}
		    }
		}
	    } else {
		// Bukkit.broadcastMessage("Init abgebrochen: Hopper ist nicht leer!");
	    }
	}
    }
    
    public void transmit(ItemStack item, Inventory eventSource) {
	this.inUse = true;
	RelatedChest destChest = SyncManager.getManager().getRelatedChest(this.destLoc);
	RelatedChest sourceChest = SyncManager.getManager().getRelatedChest(this.sourceLoc);
	
	// Bukkit.broadcastMessage(ChatColor.RED + "Starte Transfer!");
	
	if (destChest != null) {
	    if (destChest.isLinked()) {
		if (eventSource.getType() != InventoryType.HOPPER) {
		    
		    MainChest mChest = destChest.getLinkedChest();
		    mChest.getInventory().addItem(item.clone());
		    destChest.getInventory().clear();
		    // Bukkit.broadcastMessage(ChatColor.DARK_RED + "Transfer abgeschlossen! 1");
		    setItem(item.clone());
		    initialize();
		    return;
		} else {
		    if (sourceChest != null) {
			if (sourceChest.isLinked()) {
			    MainChest mChest = destChest.getLinkedChest();
			    mChest.getInventory().addItem(item.clone());
			    destChest.getInventory().clear();
			    // Bukkit.broadcastMessage(ChatColor.DARK_RED + "Transfer abgeschlossen! 2");
			    setItem(item.clone());
			    initialize();
			    return;
			}
		    }
		}
	    }
	}
	initialize();
    }
    
    public void reInitialize() {
	this.inUse = true;
	this.item = null;
	initialize();
    }
    
    public void setItem(ItemStack item) {
	this.inUse = true;
	this.item = item;
    }
    
    public int getID() {
	return this.HopperID;
    }
    
    public Location getLocation() {
	return this.hopperLoc.clone();
    }
    
    public World getWorld() {
	return this.hopperLoc.getWorld();
    }
    
    public int getX() {
	return this.hopperLoc.getBlockX();
    }
    
    public int getY() {
	return this.hopperLoc.getBlockY();
    }
    
    public int getZ() {
	return this.hopperLoc.getBlockZ();
    }
    
    public ItemStack getItem() {
	return this.item;
    }
    
    public Inventory getInventory() {
	return this.hopper.getInventory();
    }
    
    public Hopper getHopper() {
	return this.hopper;
    }
    
    public boolean isUsed() {
	return this.inUse;
    }
}
