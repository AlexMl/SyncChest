package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.SyncChest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SyncChestPlayerInteractListener implements Listener{
	public SyncChestPlayerInteractListener(SyncChest plugin){
		this.plugin = plugin;		
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		
		FileConfiguration chestFileConfiguration = YamlConfiguration.loadConfiguration(plugin.chestFile);
		
		Player eventPlayer = event.getPlayer();
		
		if(plugin.enable==true){			
			if(event.getAction()==Action.LEFT_CLICK_BLOCK){
				if(event.getItem()!=null && event.getItem().hasItemMeta()){
					if(event.getItem().getItemMeta().equals(plugin.wandItemMeta)){
						if(event.getClickedBlock().getState().getBlock().getType().equals(Material.CHEST)){
							if(event.getClickedBlock().getState() instanceof Chest){
								
								Chest eventChest = (Chest) event.getClickedBlock().getState();
								if(eventPlayer.hasPermission("sc.use.connector")){	
									if(eventChest.getInventory().getTitle().contains("MainChest")){
																									
										for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
											if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
												if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==false){
													if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	plugin.mainChest = chestFileConfiguration.getString("config.mem.mainChests." + i + ".name");
																	//Chest ausgewählt!
																	eventPlayer.sendMessage(ChatColor.GOLD + plugin.mainChest + ChatColor.RESET + plugin.messageFileConfiguration.getString("messages.messages.chestSelected"));
																}
															}
														}
													}
												}else if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==true){
													//Doppelkisten Mainchest
													if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	plugin.mainChest = chestFileConfiguration.getString("config.mem.mainChests." + i + ".name");
																	//Chest ausgewählt!
																	eventPlayer.sendMessage(ChatColor.GOLD + plugin.mainChest + ChatColor.RESET + plugin.messageFileConfiguration.getString("messages.messages.chestSelected"));
																}
															}
														}
													}
													if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	plugin.mainChest = chestFileConfiguration.getString("config.mem.mainChests." + i + ".name");
																	//Chest ausgewählt!
																	eventPlayer.sendMessage(ChatColor.GOLD + plugin.mainChest + ChatColor.RESET + plugin.messageFileConfiguration.getString("messages.messages.chestSelected"));
																}
															}
														}
													}
												}
											}
										}
									}
									
									if(eventChest.getInventory().getTitle().contains("RelatedChest")){
										
										for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
											if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
												if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
													if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	plugin.relatedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name");
																	//Chest ausgewählt!
																	eventPlayer.sendMessage(ChatColor.DARK_PURPLE + plugin.relatedChest + ChatColor.RESET + plugin.messageFileConfiguration.getString("messages.messages.chestSelected"));
																}
															}
														}
													}
												}else{
													if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	plugin.relatedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name");
																	//Chest ausgewählt!
																	eventPlayer.sendMessage(ChatColor.DARK_PURPLE + plugin.relatedChest + ChatColor.RESET + plugin.messageFileConfiguration.getString("messages.messages.chestSelected"));
																}
															}
														}
													}
													if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	plugin.relatedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name");
																	//Chest ausgewählt!
																	eventPlayer.sendMessage(ChatColor.DARK_PURPLE + plugin.relatedChest + ChatColor.RESET + plugin.messageFileConfiguration.getString("messages.messages.chestSelected"));
																}
															}
														}
													}
												}
											}
										}
										
										if(plugin.relatedChest.contains("RelatedChest")&&plugin.mainChest.contains("MainChest")){
											plugin.link(eventPlayer, plugin.relatedChest, plugin.mainChest);
											plugin.relatedChest = "";
											plugin.mainChest = "";
										}										
									}
								}else{
									//Keine Permissions
									eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
								}
							}
						}
					}			
				}
			}
				
				
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(event.getItem()!=null){
					if(event.getItem().getItemMeta().equals(plugin.wandItemMeta)){
						if(event.getClickedBlock().getState().getBlock().getType().equals(Material.CHEST)){
							if(event.getClickedBlock().getState() instanceof Chest){	
								
								Chest eventChest = (Chest) event.getClickedBlock().getState();										
								if(eventPlayer.hasPermission("sc.use.connector")){	
									if(eventChest.getInventory().getTitle().contains("MainChest")){
										
										for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
											if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
												if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==false){
													if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	
																	String worldName = "";
																	for(int k=1;chestFileConfiguration.get("config.mem.relatedChests." + k)!=null;k++){
																		if(chestFileConfiguration.get("config.mem.relatedChests." + k + ".X")!=null){
																			if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo").equals(chestFileConfiguration.getString("config.mem.mainChests." + i + ".name"))){
																				worldName = chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world");
																				break;
																			}
																		}
																	}																	
																	
																	if(worldName.equalsIgnoreCase("")){
																		worldName = "--";
																	}
																	
																	//eventPlayer.sendMessage(ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".name") + ChatColor.RESET + " gefunden!");
																	eventPlayer.sendMessage(ChatColor.BLACK + "––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––" + ChatColor.RESET + "\n" +
																			ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".name") + ChatColor.RESET + ":\n" +
																			ChatColor.RESET + "LargeChest: " + ChatColor.AQUA + "false\n" +
																			ChatColor.RESET + "LinkedTo: " + ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo") + 
																			ChatColor.GREEN + " (" + worldName + ")" + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "Location: " + ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X") + ChatColor.RESET + "(X)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y") + ChatColor.RESET + "(Y)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z") + ChatColor.RESET + "(Z)" +
																			ChatColor.RESET + " (" + ChatColor.GREEN + chestFileConfiguration.getString("config.mem.mainChests." + i + ".world") + ChatColor.RESET + ")");
																}
															}
														}
													}
												}else{
													if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																	
																	String worldName = "";
																	for(int k=1;chestFileConfiguration.get("config.mem.relatedChests." + k)!=null;k++){
																		if(chestFileConfiguration.get("config.mem.relatedChests." + k + ".X")!=null){
																			if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo").equals(chestFileConfiguration.getString("config.mem.mainChests." + i + ".name"))){
																				worldName = chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world");
																				break;
																			}
																		}
																	}																	
																	
																	if(worldName.equalsIgnoreCase("")){
																		worldName = "--";
																	}
																	
																	//eventPlayer.sendMessage(ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".name") + ChatColor.RESET + " gefunden!");
																	eventPlayer.sendMessage(ChatColor.BLACK + "––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––" + ChatColor.RESET + "\n" +
																			ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".name") + ChatColor.RESET + ":\n" +
																			ChatColor.RESET + "LargeChest: " + ChatColor.AQUA + "true\n" +
																			ChatColor.RESET + "CoChest: " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".coChest.name") + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "CoChest: " +	ChatColor.AQUA + "false\n" +																	
																			ChatColor.RESET + "LinkedTo: " + ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo") + 
																			ChatColor.GREEN + " (" + worldName + ")" + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "Location: " + ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X") + ChatColor.RESET + "(X)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y") + ChatColor.RESET + "(Y)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z") + ChatColor.RESET + "(Z)" +
																			ChatColor.RESET + " (" + ChatColor.GREEN + chestFileConfiguration.getString("config.mem.mainChests." + i + ".world") + ChatColor.RESET + ")");
																}
															}
														}
													}
													if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){

																	String worldName = "";
																	for(int k=1;chestFileConfiguration.get("config.mem.relatedChests." + k)!=null;k++){
																		if(chestFileConfiguration.get("config.mem.relatedChests." + k + ".X")!=null){
																			if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo").equals(chestFileConfiguration.getString("config.mem.mainChests." + i + ".name"))){
																				worldName = chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world");
																				break;
																			}
																		}
																	}																	
																	
																	if(worldName.equalsIgnoreCase("")){
																		worldName = "--";
																	}
																	
																	//eventPlayer.sendMessage("CoChest " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".coChest.name") + ChatColor.RESET + " unter " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".name") + ChatColor.RESET + " gefunden!");
																	eventPlayer.sendMessage(ChatColor.BLACK + "––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––" + ChatColor.RESET + "\n" +
																			ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".coChest.name") + ChatColor.RESET + ":\n" +
																			ChatColor.RESET + "LargeChest: " + ChatColor.AQUA + "true\n" +
																			ChatColor.RESET + "CoChest: " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.mainChests." + i + ".name") + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "CoChest: " +	ChatColor.AQUA + "true\n" +	
																			ChatColor.RESET + "LinkedTo: " + ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.mainChests." + i + ".linkedTo") +
																			ChatColor.GREEN + " (" + worldName + ")" + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "Location: " + ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.X") + ChatColor.RESET + "(X)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Y") + ChatColor.RESET + "(Y)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Z") + ChatColor.RESET + "(Z)" +
																			ChatColor.RESET + " (" + ChatColor.GREEN + chestFileConfiguration.getString("config.mem.mainChests." + i + ".world") + ChatColor.RESET + ")");
																}
															}
														}
													}
												}
											}
										}
									}
									
									if(eventChest.getInventory().getTitle().contains("RelatedChest")){
										
										for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
											if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
												if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
													if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){
																
																	String worldName = "";
																	for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
																		if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
																			if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".linkedTo").equals(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name"))){
																				worldName = chestFileConfiguration.getString("config.mem.mainChests." + k + ".world");
																				break;
																			}
																		}
																	}																	
																	
																	if(worldName.equalsIgnoreCase("")){
																		worldName = "--";
																	}																	
																	
																	//eventPlayer.sendMessage(ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name") + ChatColor.RESET + " gefunden!");
																	eventPlayer.sendMessage(ChatColor.BLACK + "––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––" + ChatColor.RESET + "\n" +
																			ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name") + ChatColor.RESET + ":\n" +
																			ChatColor.RESET + "LargeChest: " + ChatColor.AQUA + "false\n" +
																			ChatColor.RESET + "LinkedTo: " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo") +
																			ChatColor.GREEN + " (" + worldName + ")" + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "Location: " + ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X") + ChatColor.RESET + "(X)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y") + ChatColor.RESET + "(Y)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z") + ChatColor.RESET + "(Z)" +
																			ChatColor.RESET + " (" + ChatColor.GREEN + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world") + ChatColor.RESET + ")");
																}
															}
														}
													}
												}else{
													if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){

																	String worldName = "";
																	for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
																		if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
																			if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".linkedTo").equals(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name"))){
																				worldName = chestFileConfiguration.getString("config.mem.mainChests." + k + ".world");
																				break;
																			}
																		}
																	}																	
																	
																	if(worldName.equalsIgnoreCase("")){
																		worldName = "--";
																	}																	
																	
																	//eventPlayer.sendMessage(ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name") + ChatColor.RESET + " gefunden!");
																	eventPlayer.sendMessage(ChatColor.BLACK + "––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––" + ChatColor.RESET + "\n" +
																			ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name") + ChatColor.RESET + ":\n" +
																			ChatColor.RESET + "LargeChest: " + ChatColor.AQUA + "true\n" +
																			ChatColor.RESET + "CoChest: " + ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".coChest.name") + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "CoChest: " +	ChatColor.AQUA + "false\n" +	
																			ChatColor.RESET + "LinkedTo: " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo") +
																			ChatColor.GREEN + " (" + worldName + ")" + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "Location: " + ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X") + ChatColor.RESET + "(X)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y") + ChatColor.RESET + "(Y)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z") + ChatColor.RESET + "(Z)" +
																			ChatColor.RESET + " (" + ChatColor.GREEN + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world") + ChatColor.RESET + ")");
																}
															}
														}
													}
													if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==event.getClickedBlock().getX()){
														if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==event.getClickedBlock().getY()){
															if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==event.getClickedBlock().getZ()){
																if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(event.getClickedBlock().getWorld().getName())){

																	String worldName = "";
																	for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
																		if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
																			if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".linkedTo").equals(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name"))){
																				worldName = chestFileConfiguration.getString("config.mem.mainChests." + k + ".world");
																				break;
																			}
																		}
																	}																	
																	
																	if(worldName.equalsIgnoreCase("")){
																		worldName = "--";
																	}																	
																	
																	//eventPlayer.sendMessage("CoChest " + ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".coChest.name") + ChatColor.RESET + " unter " + ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name") + ChatColor.RESET + " gefunden!");
																	eventPlayer.sendMessage(ChatColor.BLACK + "––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––" + ChatColor.RESET + "\n" +
																			ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".coChest.name") + ChatColor.RESET + ":\n" +
																			ChatColor.RESET + "LargeChest: " + ChatColor.AQUA + "true\n" +
																			ChatColor.RESET + "CoChest: " + ChatColor.DARK_PURPLE + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name") + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "CoChest: " +	ChatColor.AQUA + "true\n" +	
																			ChatColor.RESET + "LinkedTo: " + ChatColor.GOLD + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo") +
																			ChatColor.GREEN + " (" + worldName + ")" + ChatColor.RESET + "\n" +
																			ChatColor.RESET + "Location: " + ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X") + ChatColor.RESET + "(X)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y") + ChatColor.RESET + "(Y)" +
																			ChatColor.GREEN + chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z") + ChatColor.RESET + "(Z)" +
																			ChatColor.RESET + " (" + ChatColor.GREEN + chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world") + ChatColor.RESET + ")");
																}
															}
														}
													}
												}
											}
										}
									}									
								}else{
									//Keine Permissions
									eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
								}
							}
							event.setCancelled(true);
						}				
					}else{
						plugin.relatedChestLocation = event.getClickedBlock().getLocation();
					}
				}else{
					plugin.relatedChestLocation = event.getClickedBlock().getLocation();
				}
			}			
		}
	}
	
	private SyncChest plugin;
}