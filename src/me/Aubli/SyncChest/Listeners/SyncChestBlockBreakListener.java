package me.Aubli.SyncChest.Listeners;

import java.io.IOException;

import me.Aubli.SyncChest.SyncChest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SyncChestBlockBreakListener implements Listener {
	public SyncChestBlockBreakListener(SyncChest plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		
		FileConfiguration chestFileConfiguration = YamlConfiguration.loadConfiguration(plugin.chestFile);			
		BlockState bs = event.getBlock().getState();
		Player eventPlayer = event.getPlayer();
			
		if(event.getPlayer().getItemInHand()!=null){
			if(event.getPlayer().getItemInHand().hasItemMeta()){
				if(event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){
					if(eventPlayer.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("connector")){
						event.setCancelled(true);
						return;
					}
				}
			}
		}

		if(bs instanceof Chest){
			Chest chest = (Chest)bs;

			if(chest.getInventory().getTitle().contains("MainChest")){
				if(eventPlayer.hasPermission("sc.remove.chests")){
					if(plugin.isChest("mainchest", event.getBlock().getLocation())){
							
						String path = plugin.getChestPath("mainchest", event.getBlock().getLocation());
						String name = chestFileConfiguration.getString(path + ".name");
						
						if(!plugin.isDoubleChest("mainchest", event.getBlock().getLocation())){
							for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
									if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".linkedTo")!=null){
										if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo").equalsIgnoreCase(name)){
											chestFileConfiguration.set("config.mem.relatedChests." + i + ".linkedTo", "");
											eventPlayer.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.messages.chestUnLinked"));
											break;
										}
									}
								}
							}
							chestFileConfiguration.set(path, "");
							//Kiste Entfernt
							eventPlayer.sendMessage(ChatColor.GOLD + name + ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.chestRemoved"));												
						}else{
							if(path.contains("coChest")){
								//2. hälfte wurde entfernt (coChest)	
								String coChestName = chestFileConfiguration.getString(path + ".name");
								chestFileConfiguration.set(path.replace(".coChest", "") + ".doubleChest", false);
								chestFileConfiguration.set(path, "");
								
								//DoppelKiste Entfernt
								eventPlayer.sendMessage(ChatColor.GOLD + coChestName + ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.chestRemoved"));
							}else{
								
								//1. hälfte der doppelkiste wird entfernt									
								for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
									if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
										if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
											if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo").equalsIgnoreCase(name)){
												chestFileConfiguration.set("config.mem.relatedChests." + i + ".linkedTo", "");
												eventPlayer.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.messages.chestUnLinked"));
											}
										}
									}
								}
								
								chestFileConfiguration.set(path + ".doubleChest", false);
								chestFileConfiguration.set(path + ".name", chestFileConfiguration.getString(path + ".coChest.name"));
								chestFileConfiguration.set(path + ".X", chestFileConfiguration.getInt(path + ".coChest.X"));
								chestFileConfiguration.set(path + ".Z", chestFileConfiguration.getInt(path + ".coChest.Z"));
								chestFileConfiguration.set(path + ".Y", chestFileConfiguration.getInt(path + ".coChest.Y"));
								chestFileConfiguration.set(path + ".coChest", "");
								
								//DoppelKiste Entfernt
								eventPlayer.sendMessage(ChatColor.GOLD + name + ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.chestRemoved"));
								//eventPlayer.sendMessage("MainChest wurde von CoChest " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + y + ".name") + " überschrieben!");
								
							}
						}
					}
	
					try {
						chestFileConfiguration.save(plugin.chestFile);
					} catch (IOException e) {
							e.printStackTrace();
					}
						
					event.setCancelled(true);
					event.getBlock().setType(Material.AIR);
					return;
				}else{
					//Keine Permissions
					eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
				}
			}
			
			
			//Related Chests				
			if(chest.getInventory().getTitle().contains("RelatedChest")){
				if(eventPlayer.hasPermission("sc.remove.chests")){
					String path = plugin.getChestPath("relatedchest", event.getBlock().getLocation());
					String name = chestFileConfiguration.getString(path + ".name");
					
					if(!plugin.isDoubleChest("relatedchest", event.getBlock().getLocation())){							
						for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
							if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
								if(chestFileConfiguration.get("config.mem.mainChests." + i + ".linkedTo")!=null || !chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo").equals("")){															
									if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo").contains(name)){
										if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo").contains(",")){
											String[] content = chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo").split(", ");
											for(int k=0;k<content.length;k++){
												if(content[k]!=null){
													if(content[k].equalsIgnoreCase(name)){
														for (int m = k; m < content.length - 1; ++m){
														    content[m] = content[m+1];
														}
														content[content.length - 1] = null;
														
														String linkedChests = content[0];
														for(int n = 1; n<content.length;n++){
															if(content[n]!=null){
																linkedChests = linkedChests + ", " + content[n]; 
															}
														}
													
														chestFileConfiguration.set("config.mem.mainChests." + i + ".linkedTo", linkedChests);
														eventPlayer.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.messages.chestUnLinked"));
													}
												}
											}
										}else{
											chestFileConfiguration.set("config.mem.mainChests." + i + ".linkedTo", "");
											eventPlayer.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.messages.chestUnLinked"));
										}
									}
								}
							}
						}
						chestFileConfiguration.set(path, "");
						eventPlayer.sendMessage(ChatColor.DARK_PURPLE + name + ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.chestRemoved"));
					
					}else{
						if(path.contains("coChest")){
							//2. hälfte wurde entfernt (coChest)	
							String coChestName = chestFileConfiguration.getString(path + ".name");							
							chestFileConfiguration.set(path.replace(".coChest", "") + ".doubleChest", false);
							chestFileConfiguration.set(path, "");
							
							//DoppelKiste Entfernt
							eventPlayer.sendMessage(ChatColor.DARK_PURPLE + coChestName + ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.chestRemoved"));
						}else{
							
							//1. hälfte der doppelkiste wird entfernt									
							for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
									if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo")!=null){
										if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo").contains(name)){
											if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo").contains(",")){
												String[] content = chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo").split(", ");
												for(int k=0;k<content.length;k++){
													if(content[k]!=null){
														if(content[k].equalsIgnoreCase(name)){
															for (int m = k; m < content.length - 1; ++m){
															    content[m] = content[m+1];
															}
															content[content.length - 1] = null;
															
															String linkedChests = content[0];
															for(int n = 1; n<content.length;n++){
																if(content[n]!=null){
																	linkedChests = linkedChests + ", " + content[n]; 
																}
															}
															//linkedChests = linkedChests + ", " + chestFileConfiguration.getString("config.mem.relatedChests." + y + ".coChest.name");
															chestFileConfiguration.set("config.mem.mainChests." + i + ".linkedTo", linkedChests);
															eventPlayer.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.messages.chestUnLinked"));
														}
													}
												}
											}else{
												//chestFileConfiguration.set("config.mem.mainChests." + i + ".linkedTo", chestFileConfiguration.getString("config.mem.relatedChests." + y + ".coChest.name"));
												chestFileConfiguration.set("config.mem.mainChests." + i + ".linkedTo", "");
												eventPlayer.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.messages.chestUnLinked"));
											}
										}
									}
								}
							}									
														
							chestFileConfiguration.set(path + ".doubleChest", false);
							chestFileConfiguration.set(path + ".name", chestFileConfiguration.getString(path + ".coChest.name"));
							chestFileConfiguration.set(path + ".X", chestFileConfiguration.getInt(path + ".coChest.X"));
							chestFileConfiguration.set(path + ".Z", chestFileConfiguration.getInt(path + ".coChest.Z"));
							chestFileConfiguration.set(path + ".Y", chestFileConfiguration.getInt(path + ".coChest.Y"));
							chestFileConfiguration.set(path + ".coChest", "");
							
							//DoppelKiste Entfernt
							eventPlayer.sendMessage(ChatColor.DARK_PURPLE + name + ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.chestRemoved"));
							
						}
					}
									
					try {
						chestFileConfiguration.save(plugin.chestFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				
					event.setCancelled(true);
					event.getBlock().setType(Material.AIR);
					return;
				}else{
					//Keine Permissions
					eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
				}
			}
		}
	}
	private SyncChest plugin;
}
