package org.util.tools;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import me.Aubli.SyncChest.SyncChest;
import me.Aubli.SyncChest.SyncObjects.MainChest;
import me.Aubli.SyncChest.SyncObjects.RelatedChest;
import me.Aubli.SyncChest.SyncObjects.SyncManager;
import me.Aubli.SyncChest.SyncObjects.SyncManager.ChestType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class ChestFileConverter {
    
    private SyncChest plugin;
    private SyncManager sync;
    
    private Logger log;
    
    public ChestFileConverter() {
	this.plugin = SyncChest.getInstance();
	this.sync = SyncManager.getManager();
	this.log = this.plugin.getLogger();
    }
    
    public boolean chestFileExists() {
	File chestFile = new File(this.plugin.getDataFolder().getPath() + File.separator + "chests.yml");
	
	if (chestFile.exists()) {
	    return true;
	} else {
	    return false;
	}
    }
    
    public boolean worldsInNames() {
	
	File mainChestFolder = new File(this.plugin.getDataFolder().getPath() + File.separator + "Chests" + File.separator + "Main");
	File relatedChestFolder = new File(this.plugin.getDataFolder().getPath() + File.separator + "Chests" + File.separator + "Related");
	File hopperFolder = new File(this.plugin.getDataFolder().getPath() + File.separator + "Hoppers");
	
	FileConfiguration tempConf;
	
	for (File f : mainChestFolder.listFiles()) {
	    tempConf = YamlConfiguration.loadConfiguration(f);
	    
	    try {
		// System.out.println(f.toString() + " : " + tempConf.getString("config.Chest.Location.world"));
		UUID.fromString(tempConf.getString("config.Chest.Location.world"));
	    } catch (IllegalArgumentException e) {
		return true;
	    }
	}
	
	for (File f : relatedChestFolder.listFiles()) {
	    tempConf = YamlConfiguration.loadConfiguration(f);
	    
	    try {
		// System.out.println(f.toString() + " : " + tempConf.getString("config.Chest.Location.world"));
		UUID.fromString(tempConf.getString("config.Chest.Location.world"));
	    } catch (IllegalArgumentException e) {
		return true;
	    }
	}
	
	for (File f : hopperFolder.listFiles()) {
	    tempConf = YamlConfiguration.loadConfiguration(f);
	    
	    try {
		// System.out.println(f.toString() + " : " + tempConf.getString("config.Hopper.Location.world"));
		UUID.fromString(tempConf.getString("config.Hopper.Location.world"));
		UUID.fromString(tempConf.getString("config.Hopper.Location.Destination.world"));
		UUID.fromString(tempConf.getString("config.Hopper.Location.Source.world"));
	    } catch (IllegalArgumentException e) {
		return true;
	    }
	}
	return false;
    }
    
    public void convertToUUID() {
	
	if (worldsInNames()) {
	    File mainChestFolder = new File(this.plugin.getDataFolder().getPath() + File.separator + "Chests" + File.separator + "Main");
	    File relatedChestFolder = new File(this.plugin.getDataFolder().getPath() + File.separator + "Chests" + File.separator + "Related");
	    File hopperFolder = new File(this.plugin.getDataFolder().getPath() + File.separator + "Hoppers");
	    
	    FileConfiguration tempConf;
	    
	    try {
		
		for (File f : mainChestFolder.listFiles()) {
		    tempConf = YamlConfiguration.loadConfiguration(f);
		    String world = "";
		    try {
			world = tempConf.getString("config.Chest.Location.world");
			// System.out.println(f.toString() + " : " + world);
			UUID.fromString(world);
		    } catch (IllegalArgumentException e) {
			// System.out.println(Bukkit.getWorld(world).getUID());
			tempConf.set("config.Chest.Location.world", Bukkit.getWorld(world).getUID().toString());
			tempConf.save(f);
		    }
		}
		
		for (File f : relatedChestFolder.listFiles()) {
		    tempConf = YamlConfiguration.loadConfiguration(f);
		    String world = "";
		    try {
			world = tempConf.getString("config.Chest.Location.world");
			// System.out.println(f.toString() + " : " + world);
			UUID.fromString(world);
		    } catch (IllegalArgumentException e) {
			tempConf.set("config.Chest.Location.world", Bukkit.getWorld(world).getUID().toString());
			tempConf.save(f);
		    }
		}
		
		for (File f : hopperFolder.listFiles()) {
		    tempConf = YamlConfiguration.loadConfiguration(f);
		    String world = "";
		    try {
			world = tempConf.getString("config.Hopper.Location.world");
			// System.out.println(f.toString() + " : " + world);
			UUID.fromString(world);
		    } catch (IllegalArgumentException e) {
			tempConf.set("config.Hopper.Location.world", Bukkit.getWorld(world).getUID().toString());
			tempConf.save(f);
		    }
		    try {
			world = tempConf.getString("config.Hopper.Location.Destination.world");
			// System.out.println(f.toString() + " : " + world);
			UUID.fromString(world);
		    } catch (IllegalArgumentException e) {
			tempConf.set("config.Hopper.Location.Destination.world", Bukkit.getWorld(world).getUID().toString());
			tempConf.save(f);
		    }
		    try {
			world = tempConf.getString("config.Hopper.Location.Source.world");
			// System.out.println(f.toString() + " : " + world);
			UUID.fromString(world);
		    } catch (IllegalArgumentException e) {
			tempConf.set("config.Hopper.Location.Source.world", Bukkit.getWorld(world).getUID().toString());
			tempConf.save(f);
		    }
		}
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	    
	}
    }
    
    public boolean convert() {
	if (chestFileExists()) {
	    
	    File chestFile = new File(this.plugin.getDataFolder().getPath() + File.separator + "chests.yml");
	    FileConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);
	    
	    World world;
	    int x;
	    int y;
	    int z;
	    
	    boolean success;
	    
	    for (int i = 1; chestConfig.get("config.mem.mainChests." + i) != null; i++) {
		if (chestConfig.get("config.mem.mainChests." + i + ".name") != null) {
		    
		    world = Bukkit.getWorld(chestConfig.getString("config.mem.mainChests." + i + ".world"));
		    
		    if (world == null) {
			this.log.severe("Can not find World '" + chestConfig.getString("config.mem.mainChests." + i + ".world") + "'! Is it loaded? Aborting convertion!");
			return false;
		    }
		    
		    x = chestConfig.getInt("config.mem.mainChests." + i + ".X");
		    y = chestConfig.getInt("config.mem.mainChests." + i + ".Y");
		    z = chestConfig.getInt("config.mem.mainChests." + i + ".Z");
		    
		    Location chestLoc = new Location(world, x, y, z);
		    success = this.sync.addChest(ChestType.MAIN, chestLoc);
		    
		    if (success) {
			this.log.info("MainChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
		    } else {
			this.log.info("Error: Can't add MainChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
			return false;
		    }
		    
		    if (chestConfig.getBoolean("config.mem.mainChests." + i + ".doubleChest") == true) {
			
			x = chestConfig.getInt("config.mem.mainChests." + i + ".coChest.X");
			y = chestConfig.getInt("config.mem.mainChests." + i + ".coChest.Y");
			z = chestConfig.getInt("config.mem.mainChests." + i + ".coChest.Z");
			
			chestLoc = new Location(world, x, y, z);
			success = this.sync.addChest(ChestType.MAIN, chestLoc);
			
			if (success) {
			    this.log.info("MainChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
			} else {
			    this.log.severe("Error: Can't add MainChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
			    return false;
			}
		    }
		}
	    }
	    
	    for (int i = 1; chestConfig.get("config.mem.relatedChests." + i) != null; i++) {
		if (chestConfig.get("config.mem.relatedChests." + i + ".name") != null) {
		    
		    world = Bukkit.getWorld(chestConfig.getString("config.mem.relatedChests." + i + ".world"));
		    
		    if (world == null) {
			this.log.severe("Can not find World '" + chestConfig.getString("config.mem.mainChests." + i + ".world") + "'! Is it loaded? Aborting convertion!");
			return false;
		    }
		    
		    x = chestConfig.getInt("config.mem.relatedChests." + i + ".X");
		    y = chestConfig.getInt("config.mem.relatedChests." + i + ".Y");
		    z = chestConfig.getInt("config.mem.relatedChests." + i + ".Z");
		    
		    Location chestLoc = new Location(world, x, y, z);
		    success = this.sync.addChest(ChestType.RELATED, chestLoc);
		    
		    if (success) {
			this.log.info("RelatedChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
		    } else {
			this.log.severe("Error: Can't add RelatedChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
			return false;
		    }
		    
		    if (chestConfig.getBoolean("config.mem.relatedChests." + i + ".doubleChest") == true) {
			world = Bukkit.getWorld(chestConfig.getString("config.mem.relatedChests." + i + ".world"));
			
			if (world == null) {
			    this.log.severe("Can not find World '" + chestConfig.getString("config.mem.relatedChests." + i + ".world") + "'! Is it loaded? Aborting convertion!");
			    return false;
			}
			
			x = chestConfig.getInt("config.mem.relatedChests." + i + ".coChest.X");
			y = chestConfig.getInt("config.mem.relatedChests." + i + ".coChest.Y");
			z = chestConfig.getInt("config.mem.relatedChests." + i + ".coChest.Z");
			
			chestLoc = new Location(world, x, y, z);
			success = this.sync.addChest(ChestType.RELATED, chestLoc);
			
			if (success) {
			    this.log.info("RelatedChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
			} else {
			    this.log.severe("Error: Can't add RelatedChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
			    return false;
			}
		    }
		    
		    if (!chestConfig.getString("config.mem.relatedChests." + i + ".linkedTo").isEmpty()) {
			String linkedChestName = chestConfig.getString("config.mem.relatedChests." + i + ".linkedTo");
			
			for (int k = 1; chestConfig.get("config.mem.mainChests." + k) != null; k++) {
			    if (chestConfig.get("config.mem.mainChests." + k + ".name") != null) {
				if (chestConfig.get("config.mem.mainChests." + k + ".name").equals(linkedChestName)) {
				    world = Bukkit.getWorld(chestConfig.getString("config.mem.mainChests." + k + ".world"));
				    
				    if (world == null) {
					this.log.severe("Can not find World '" + chestConfig.getString("config.mem.mainChests." + k + ".world") + "'! Is it loaded? Aborting convertion!");
					return false;
				    }
				    
				    x = chestConfig.getInt("config.mem.mainChests." + k + ".X");
				    y = chestConfig.getInt("config.mem.mainChests." + k + ".Y");
				    z = chestConfig.getInt("config.mem.mainChests." + k + ".Z");
				    
				    Location mChestLoc = new Location(world, x, y, z);
				    Location rChestLoc = new Location(Bukkit.getWorld(chestConfig.getString("config.mem.relatedChests." + i + ".world")), chestConfig.getInt("config.mem.relatedChests." + i + ".X"), chestConfig.getInt("config.mem.relatedChests." + i + ".Y"), chestConfig.getInt("config.mem.relatedChests." + i + ".Z"));
				    
				    MainChest mChest = this.sync.getMainChest(mChestLoc);
				    RelatedChest rChest = this.sync.getRelatedChest(rChestLoc);
				    
				    if (mChest != null && rChest != null) {
					this.sync.linkChests(rChest, mChest);
					this.log.info(rChest.toString() + " is now linked to " + mChest.toString());
				    } else {
					this.log.severe("Error: Chests don't exists!");
					return false;
				    }
				}
			    }
			}
		    }
		}
	    }
	    chestFile.renameTo(new File(this.plugin.getDataFolder().getPath() + File.separator + "chests.old.yml"));
	    return true;
	}
	return true;
    }
}
