package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.SyncChest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class SyncChestInventoryOpenListener implements Listener{
	public SyncChestInventoryOpenListener(SyncChest plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event){
		
		FileConfiguration chestFileConfiguration = YamlConfiguration.loadConfiguration(plugin.chestFile);
		Player eventPlayer = (Player) event.getPlayer();
		
		if(plugin.enable==true){
			if(eventPlayer.hasPermission("sc.use")){		
				if(event.getInventory().getTitle().equalsIgnoreCase("relatedchest")){
		
					String mainChestName = "";
					
					if(plugin.relatedChestLocation!=null){
										
						int mainChestIndex = -5;
						
						if(plugin.isChest("relatedchest", plugin.relatedChestLocation)){
							String path = plugin.getChestPath("relatedchest", plugin.relatedChestLocation);
							
							if(plugin.isDoubleChest("relatedchest", plugin.relatedChestLocation)){
								if(path.contains("coChest")){
									mainChestName = chestFileConfiguration.getString(path.replace(".coChest", "") + ".linkedTo");							
								}else{
									mainChestName = chestFileConfiguration.getString(path + ".linkedTo");
								}
							}else{
								mainChestName = chestFileConfiguration.getString(path + ".linkedTo");
							}
						}
						
						for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
							if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
								if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==true){
									if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".name").equalsIgnoreCase(mainChestName)){
										mainChestIndex = i;
										break;
									}
									if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".coChest.name").equalsIgnoreCase(mainChestName)){
										mainChestIndex = i;
										break;
									}
								}else if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==false){
									if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".name").equalsIgnoreCase(mainChestName)){
										mainChestIndex = i;
										break;
									}
								}			
							}
						}
						
						if(mainChestIndex==-5){
							return;
						}
						
						Location mainChestLocation = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + mainChestIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + mainChestIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + mainChestIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + mainChestIndex + ".Z"));
						//eventPlayer.sendMessage(mainChestName + " an " + mainChestIndex + ". Stelle:  " + mainChestLocation.getBlockX() + " : " + mainChestLocation.getBlockY() + " : " + mainChestLocation.getBlockZ());
											
						BlockState mainBlockState = mainChestLocation.getBlock().getState();
										
						if(mainBlockState instanceof Chest){
							plugin.mainChestLocation = mainChestLocation;
							Chest mainChest = (Chest)mainBlockState;							
							event.setCancelled(true);							
							eventPlayer.openInventory(mainChest.getInventory());
							plugin.invOpen=true;
							return;
						}else{
							//eventPlayer.sendMessage("Keine Kiste");
						}
					}
				}
			}
		}
	}	
	private SyncChest plugin;
}