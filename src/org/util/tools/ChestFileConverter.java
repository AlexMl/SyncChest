package org.util.tools;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Aubli.SyncChest.MainChest;
import me.Aubli.SyncChest.RelatedChest;
import me.Aubli.SyncChest.SyncChest;
import me.Aubli.SyncChest.SyncManager;
import me.Aubli.SyncChest.SyncManager.ChestType;

public class ChestFileConverter {
	
	private SyncChest plugin;
	private SyncManager sync;
	
	private static final Logger log = Bukkit.getLogger();
	
	public ChestFileConverter(){
		plugin = SyncChest.getInstance();
		sync = SyncManager.getManager();		
	}
	
	public boolean chestFileExists(){		
		File chestFile = new File(plugin.getDataFolder().getPath() + "/chests.yml");
		
		if(chestFile.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	public void convert(){
		if(chestFileExists()){
			
			File chestFile = new File(plugin.getDataFolder().getPath() + "/chests.yml");
			FileConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);
			
			World world;
			int x;
			int y;
			int z;
			
			boolean success;
			
			for(int i=1;chestConfig.get("config.mem.mainChests." + i)!=null;i++){
				if(chestConfig.get("config.mem.mainChests." + i + ".name")!=null){
					
					world = Bukkit.getWorld(chestConfig.getString("config.mem.mainChests." + i + ".world"));
					
					x = chestConfig.getInt("config.mem.mainChests." + i + ".X");
					y = chestConfig.getInt("config.mem.mainChests." + i + ".Y");
					z = chestConfig.getInt("config.mem.mainChests." + i + ".Z");
					
					Location chestLoc = new Location(world, x, y, z);
					success = sync.addChest(ChestType.MAIN, chestLoc);
				
					if(success){
						log.info("[SyncChest] MainChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
					}else{
						log.info("[SyncChest] Error: Can't add MainChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
					}
					
					if(chestConfig.getBoolean("config.mem.mainChests." + i + ".doubleChest")==true){
						world = Bukkit.getWorld(chestConfig.getString("config.mem.mainChests." + i + ".world"));
						
						x = chestConfig.getInt("config.mem.mainChests." + i + ".coChest.X");
						y = chestConfig.getInt("config.mem.mainChests." + i + ".coChest.Y");
						z = chestConfig.getInt("config.mem.mainChests." + i + ".coChest.Z");
						
						chestLoc = new Location(world, x, y, z);
						success = sync.addChest(ChestType.MAIN, chestLoc);
					
						if(success){
							log.info("[SyncChest] MainChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
						}else{
							log.info("[SyncChest] Error: Can't add MainChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
						}
					}
				}
			}
			
			for(int i=1;chestConfig.get("config.mem.relatedChests." + i)!=null;i++){
				if(chestConfig.get("config.mem.relatedChests." + i + ".name")!=null){
					
					world = Bukkit.getWorld(chestConfig.getString("config.mem.relatedChests." + i + ".world"));
					
					x = chestConfig.getInt("config.mem.relatedChests." + i + ".X");
					y = chestConfig.getInt("config.mem.relatedChests." + i + ".Y");
					z = chestConfig.getInt("config.mem.relatedChests." + i + ".Z");
					
					Location chestLoc = new Location(world, x, y, z);
					success = sync.addChest(ChestType.RELATED, chestLoc);
				
					if(success){
						log.info("[SyncChest] RelatedChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
					}else{
						log.info("[SyncChest] Error: Can't add RelatedChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
					}
					
					if(chestConfig.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
						world = Bukkit.getWorld(chestConfig.getString("config.mem.relatedChests." + i + ".world"));
						
						x = chestConfig.getInt("config.mem.relatedChests." + i + ".coChest.X");
						y = chestConfig.getInt("config.mem.relatedChests." + i + ".coChest.Y");
						z = chestConfig.getInt("config.mem.relatedChests." + i + ".coChest.Z");
						
						chestLoc = new Location(world, x, y, z);					
						success = sync.addChest(ChestType.RELATED, chestLoc);
					
						if(success){
							log.info("[SyncChest] RelatedChest found @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
						}else{
							log.info("[SyncChest] Error: Can't add RelatedChest @ " + world.getName() + "[" + x + ", " + y + ", " + z + "]");
						}
					}
					
					if(!chestConfig.getString("config.mem.relatedChests." + i + ".linkedTo").isEmpty()){
						String linkedChestName = chestConfig.getString("config.mem.relatedChests." + i + ".linkedTo");
						
						for(int k=1;chestConfig.get("config.mem.mainChests." + k)!=null;k++){
							if(chestConfig.get("config.mem.mainChests." + k + ".name")!=null){
								if(chestConfig.get("config.mem.mainChests." + k + ".name").equals(linkedChestName)){
									world = Bukkit.getWorld(chestConfig.getString("config.mem.mainChests." + k + ".world"));
									
									x = chestConfig.getInt("config.mem.mainChests." + k + ".X");
									y = chestConfig.getInt("config.mem.mainChests." + k + ".Y");
									z = chestConfig.getInt("config.mem.mainChests." + k + ".Z");
									
									Location mChestLoc = new Location(world, x, y, z);
									Location rChestLoc = new Location(Bukkit.getWorld(chestConfig.getString("config.mem.relatedChests." + i + ".world")), chestConfig.getInt("config.mem.relatedChests." + i + ".X"), chestConfig.getInt("config.mem.relatedChests." + i + ".Y"), chestConfig.getInt("config.mem.relatedChests." + i + ".Z"));
									
									MainChest mChest = sync.getMainChest(mChestLoc);
									RelatedChest rChest = sync.getRelatedChest(rChestLoc);
									
									if(mChest!=null && rChest!=null){
										sync.linkChests(rChest, mChest);
										log.info("[SyncChest] " + rChest.toString() + " is now linked to " + mChest.toString()); 
									}else{
										log.info("[SyncChest] Error: Chests don't exists!");
									}
								}
							}
						}
						
					}
				}
			}
			chestFile.renameTo(new File(plugin.getDataFolder().getPath() + "/chests.old.yml"));
			return;
		}
	}
}
