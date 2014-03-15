package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.SyncChest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SyncChestInventoryMoveListener implements Listener{
	public SyncChestInventoryMoveListener(SyncChest plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryEvent(InventoryMoveItemEvent event){
		FileConfiguration chestFileConfiguration = YamlConfiguration.loadConfiguration(plugin.chestFile);
		
		if(event.getInitiator().getType().equals(InventoryType.HOPPER)){
			Inventory eventInv = event.getInitiator();
			if(eventInv.getHolder()!=null && eventInv.getHolder() instanceof Hopper){
				Hopper hopper = (Hopper)eventInv.getHolder();				
								
				if(hopper.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
					Chest relatedChest = (Chest)hopper.getLocation().subtract(0, 1, 0).getBlock().getState();
					if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
						if(event.getDestination().getType().equals(InventoryType.CHEST)){
							//RelatedChest über einem Hopper
							//Bukkit.broadcastMessage("Hopper über RelatedChest(Y+1)");
							Location relatedChestLoc = new Location(hopper.getLocation().getWorld(), hopper.getLocation().getBlockX(), (hopper.getLocation().getBlockY()-1), hopper.getLocation().getBlockZ());						
							
							for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																		//Prüfen ob über dem hopper eine verlinkte relatedChest steht
																		if(hopper.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																			Chest anotherChest = (Chest)hopper.getLocation().add(0, 1, 0).getBlock().getState();
																			if(anotherChest.getInventory().getTitle().contains("RelatedChest")){
																				for(int k=1;chestFileConfiguration.get("config.mem.relatedChests." + k)!=null;k++){
																					if(chestFileConfiguration.get("config.mem.relatedChests." + k + ".X")!=null){
																						if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==false){
																							if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".X")==anotherChest.getLocation().getBlockX()){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Y")==anotherChest.getLocation().getBlockY()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Z")==anotherChest.getLocation().getBlockZ()){
																										if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																												Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																												Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																												
																												ItemStack item = new ItemStack(event.getItem());																												
																												mainChest.getInventory().addItem(item);
																												relatedChest.getInventory().clear();
																												
																												//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!1");
																												return;
																											}
																										}
																									}																								
																								}																							
																							}
																						}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==true){
																							if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.X")==anotherChest.getLocation().getBlockX()){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.Y")==anotherChest.getLocation().getBlockY()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.Z")==anotherChest.getLocation().getBlockZ()){
																										if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																												Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																												Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																												
																												ItemStack item = new ItemStack(event.getItem());
																												mainChest.getInventory().addItem(item);
																												relatedChest.getInventory().clear();
																												
																												//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!2");
																												return;
																											}
																										}
																									}																								
																								}																							
																							}
																							if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".X")==anotherChest.getLocation().getBlockX()){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Y")==anotherChest.getLocation().getBlockY()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Z")==anotherChest.getLocation().getBlockZ()){
																										if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																												Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																												Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																												
																												ItemStack item = new ItemStack(event.getItem());
																												mainChest.getInventory().addItem(item);
																												relatedChest.getInventory().clear();
																												
																												//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!3");
																												return;
																											}
																										}
																									}																								
																								}																							
																							}
																						}
																					}
																				}
																			}else{
																				Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																				Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																				
																				ItemStack item = new ItemStack(event.getItem());
																				mainChest.getInventory().addItem(item);
																				relatedChest.getInventory().clear();
																				
																				//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!4");
																				return;
																			}
																		}else{	
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			ItemStack item = new ItemStack(event.getItem());
																			mainChest.getInventory().addItem(item);
																			relatedChest.getInventory().clear();
																			
																			//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!5");
																			return;
																			}
																		}else{
																			return;
																		}
																	}
																}	
															}
														}
													}
												}
											}							
										}
									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			//Prüfen ob über dem hopper eine verlinkte relatedChest ist
																			if(hopper.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																				Chest anotherChest = (Chest)hopper.getLocation().add(0, 1, 0).getBlock().getState();
																				if(anotherChest.getInventory().getTitle().contains("RelatedChest")){
																					for(int k=1;chestFileConfiguration.get("config.mem.relatedChests." + k)!=null;k++){
																						if(chestFileConfiguration.get("config.mem.relatedChests." + k + ".X")!=null){
																							if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==false){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".X")==anotherChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Y")==anotherChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Z")==anotherChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																													Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																													Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																													
																													ItemStack item = new ItemStack(event.getItem());
																													mainChest.getInventory().addItem(item);
																													relatedChest.getInventory().clear();
																													
																													//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!1");
																													return;
																												}
																											}
																										}																								
																									}																							
																								}
																							}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==true){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.X")==anotherChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.Y")==anotherChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.Z")==anotherChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																													Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																													Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																													
																													ItemStack item = new ItemStack(event.getItem());
																													mainChest.getInventory().addItem(item);
																													relatedChest.getInventory().clear();
																													
																													//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!2");
																													return;
																												}
																											}
																										}																								
																									}																							
																								}
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".X")==anotherChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Y")==anotherChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Z")==anotherChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																													Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																													Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																													
																													ItemStack item = new ItemStack(event.getItem());
																													mainChest.getInventory().addItem(item);
																													relatedChest.getInventory().clear();
																													
																													//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!3");
																													return;
																												}
																											}
																										}																								
																									}																							
																								}
																							}
																						}
																					}
																				}else{
																					Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																					Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																					
																					ItemStack item = new ItemStack(event.getItem());
																					mainChest.getInventory().addItem(item);
																					relatedChest.getInventory().clear();
																					
																					//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!4");
																					return;
																				}
																			}else{	
																				Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																				Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																				
																				ItemStack item = new ItemStack(event.getItem());
																				mainChest.getInventory().addItem(item);
																				relatedChest.getInventory().clear();
																				
																				//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!5");
																				return;
																				}
																			}else{
																				return;
																			}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
										
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){																
																		if(plugin.enable==true){
																			//Prüfen ob über dem hopper eine verlinkte relatedChest steht
																			if(hopper.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																				Chest anotherChest = (Chest)hopper.getLocation().add(0, 1, 0).getBlock().getState();
																				if(anotherChest.getInventory().getTitle().contains("RelatedChest")){
																					for(int k=1;chestFileConfiguration.get("config.mem.relatedChests." + k)!=null;k++){
																						if(chestFileConfiguration.get("config.mem.relatedChests." + k + ".X")!=null){
																							if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==false){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".X")==anotherChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Y")==anotherChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Z")==anotherChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																													Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																													Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																													
																													ItemStack item = new ItemStack(event.getItem());
																													mainChest.getInventory().addItem(item);
																													relatedChest.getInventory().clear();
																													
																													//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!1");
																													return;
																												}
																											}
																										}																								
																									}																							
																								}
																							}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==true){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.X")==anotherChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.Y")==anotherChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".coChest.Z")==anotherChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																													Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																													Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																													
																													ItemStack item = new ItemStack(event.getItem());
																													mainChest.getInventory().addItem(item);
																													relatedChest.getInventory().clear();
																													
																													//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!2");
																													return;
																												}
																											}
																										}																								
																									}																							
																								}
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".X")==anotherChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Y")==anotherChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + k + ".Z")==anotherChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".world").equals(anotherChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".linkedTo")==null){
																													Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																													Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																													
																													ItemStack item = new ItemStack(event.getItem());
																													mainChest.getInventory().addItem(item);
																													relatedChest.getInventory().clear();
																													
																													//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!3");
																													return;
																												}
																											}
																										}																								
																									}																							
																								}
																							}
																						}
																					}
																				}else{
																					Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																					Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																					
																					ItemStack item = new ItemStack(event.getItem());
																					mainChest.getInventory().addItem(item);
																					relatedChest.getInventory().clear();
																					
																					//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!4");
																					return;
																				}
																			}else{	
																				Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																				Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																				
																				ItemStack item = new ItemStack(event.getItem());
																				mainChest.getInventory().addItem(item);
																				relatedChest.getInventory().clear();
																				
																				//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!5");
																				return;
																				}
																			}else{
																				return;
																			}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}
										}
									}
								}
							}
						}else{
							return;
						}
					}
				}
				
				if(hopper.getLocation().subtract(1, 0, 0).getBlock().getType().equals(Material.CHEST)){
					Chest relatedChest = (Chest)hopper.getLocation().subtract(1, 0, 0).getBlock().getState();
					if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
						if(event.getDestination().getType().equals(InventoryType.CHEST)){
							//RelatedChest neben einem Hopper
							//Bukkit.broadcastMessage("Hopper neben RelatedChest(X-1)");
							Location relatedChestLoc = new Location(hopper.getLocation().getWorld(), (hopper.getLocation().getBlockX()-1), hopper.getLocation().getBlockY(), hopper.getLocation().getBlockZ());						
							
							for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																			
																			//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																			
																			//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
										
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){																
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																			
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}
								}
							}
						}
					}else{
						return;
					}
				}
				
				if(hopper.getLocation().subtract(0, 0, 1).getBlock().getType().equals(Material.CHEST)){
					Chest relatedChest = (Chest)hopper.getLocation().subtract(0, 0, 1).getBlock().getState();
					if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
						if(event.getDestination().getType().equals(InventoryType.CHEST)){
							//RelatedChest nebn einem Hopper
							//Bukkit.broadcastMessage("Hopper neben RelatedChest(Z-1)");
							Location relatedChestLoc = new Location(hopper.getLocation().getWorld(), hopper.getLocation().getBlockX(), hopper.getLocation().getBlockY(), (hopper.getLocation().getBlockZ()-1));						
							
							for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
										
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){																
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}
								}
							}	
						}
					}
				}
				
				if(hopper.getLocation().add(1, 0, 0).getBlock().getType().equals(Material.CHEST)){
					Chest relatedChest = (Chest)hopper.getLocation().add(1, 0, 0).getBlock().getState();
					if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
						if(event.getDestination().getType().equals(InventoryType.CHEST)){
							//RelatedChest neben einem Hopper
							//Bukkit.broadcastMessage("Hopper neben RelatedChest(X+1)");
							Location relatedChestLoc = new Location(hopper.getLocation().getWorld(), (hopper.getLocation().getBlockX()+1), hopper.getLocation().getBlockY(), hopper.getLocation().getBlockZ());						
							
							for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
										
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){																
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}
								}
							}
						}
					}
				}

				if(hopper.getLocation().add(0, 0, 1).getBlock().getType().equals(Material.CHEST)){
					Chest relatedChest = (Chest)hopper.getLocation().add(0, 0, 1).getBlock().getState();				
					if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
						if(event.getDestination().getType().equals(InventoryType.CHEST)){
							//RelatedChest neben einem Hopper
							//Bukkit.broadcastMessage("Hopper neben RelatedChest(Z+1)");
							Location relatedChestLoc = new Location(hopper.getLocation().getWorld(), hopper.getLocation().getBlockX(), hopper.getLocation().getBlockY(), (hopper.getLocation().getBlockZ()+1));						
							
							for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
										
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){	
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){																
																		if(plugin.enable==true){
																			Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																			Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																			
																			mainChest.getInventory().addItem(event.getItem());
																			relatedChest.getInventory().clear();																		
																			//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");
																			return;
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}
								}
							}
						}
					}
				}

				if(hopper.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
					Chest relatedChest = (Chest)hopper.getLocation().add(0, 1, 0).getBlock().getState();
					Inventory destInv = event.getDestination();	
					if(event.getDestination().getType().equals(InventoryType.CHEST)){
						if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
							//RelatedChest über einem Hopper
							//Bukkit.broadcastMessage("Hopper unter RelatedChest(Y-1)");
							Location relatedChestLoc = new Location(hopper.getLocation().getWorld(), hopper.getLocation().getBlockX(), (hopper.getLocation().getBlockY()+1), hopper.getLocation().getBlockZ());						
							Boolean twoLinkedRelChests = false;
							
							for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
								if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																		
																		if(plugin.enable==true){																		
																			if(destInv.getTitle().contains("RelatedChest")){
																				if(destInv.getHolder()!=null && (destInv.getHolder() instanceof DoubleChest || destInv.getHolder() instanceof Chest)){
																					//Transport von verlinkte relatedChest in verlinkte relatedChest
																					
																					Location relatedDestChestLoc = null;
																					
																					if(destInv.getHolder() instanceof DoubleChest){
																						DoubleChest relatedDestChest = (DoubleChest) destInv.getHolder();
																						relatedDestChestLoc = relatedDestChest.getLocation();
																					}else if(destInv.getHolder() instanceof Chest){
																						Chest relatedDestChest = (Chest) destInv.getHolder();
																						relatedDestChestLoc = relatedDestChest.getLocation();
																					}else{
																						return;
																					}			
																																									
																					if(relatedDestChestLoc.getBlock().getState() instanceof Chest){
																						Chest relatedDestChest = (Chest) relatedDestChestLoc.getBlock().getState();																					
																							
																							for(int r=1;chestFileConfiguration.get("config.mem.relatedChests." + r)!=null;r++){
																								if(chestFileConfiguration.get("config.mem.relatedChests." + r + ".X")!=null){
																									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + r + ".doubleChest")==false){
																										//Bukkit.broadcastMessage(ChatColor.GOLD + "Kleine Kiste");
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Z")==relatedDestChest.getZ()){																													
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															twoLinkedRelChests = true;
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																			
																																			}																			
																																		}																																
																																	}
																																}
																															}
																														}	
																													}
																												}
																											}
																										}
																									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + r + ".doubleChest")==true){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															twoLinkedRelChests = true;
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															//Bukkit.broadcastMessage(ChatColor.DARK_AQUA + linkedChest);
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																		
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}
																													}
																												}
																											}
																										}
																										
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															twoLinkedRelChests = true;
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																			
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																					}else{
																						//Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "kein castbaren State in line 1304!!!");
																						return;
																					}																				
																				}
																			}	
																			twoLinkedRelChests=false;// two linkedRelChests muss an der stelle immer false sein da wenn es true wäre kommt ein return zuvor ;-)
																			if(twoLinkedRelChests==false){//eine verlinkte kiste die andere nicht
																				//transport von items aus verlinkter relatedChest in normale oder MainChest
																				
																				Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																				
																				int leer = 0;
																				for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																					if(mainChest.getInventory().getContents()[d]==null){
																						leer++;
																					}																		
																				}
																				
																				if(leer==mainChest.getInventory().getContents().length){
																					//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! " + event.getItem());
																					relatedChest.getInventory().clear();
																					event.setCancelled(false);
																					destInv.removeItem(event.getItem());
																					return;
																				}else{
																					event.setCancelled(true);
																				}																		
																				
																				for(int l=0;l<mainChest.getInventory().getSize();l++){
																					if(mainChest.getInventory().getItem(l)!=null){
																																										
																						ItemStack lastItem = event.getItem();
																						if(event.getItem().getDurability()!=0){
																							lastItem.setDurability(event.getItem().getDurability());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + lastItem.toString() + " Hat Durability");
																						}
																						if(event.getItem().getEnchantments()!=null){
																							lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + lastItem.toString() + " Hat Enchantment");
																						}
																						
																						ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																						if(mainChest.getInventory().getItem(l).getDurability()!=0){
																							item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																						}
																						if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																							item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																						}
																							
																						if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																							if(lastItem.getType()!=item.getType()){
																								//Bukkit.broadcastMessage(ChatColor.RED + item.toString());																						
																								destInv.removeItem(lastItem);
																								event.setCancelled(false);
																								eventInv.addItem(item);
																							}else if(lastItem.getDurability()!=item.getDurability()){
																								//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());
																								destInv.removeItem(lastItem);
																								event.setCancelled(false);
																								eventInv.addItem(item);
																							}else{
																								break;
																							}
																						}else{
																							event.setCancelled(true);
																						}																				
																						
																						destInv.addItem(item);																				
																						//Bukkit.broadcastMessage(event.getItem().toString());
																						//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																						mainChest.getInventory().removeItem(item);
																						relatedChest.getInventory().clear();																				
																						
																						//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																				
																						return;																			
																					}																			
																				}
																			}
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){
																		Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																		
																		if(plugin.enable==true){	
																			twoLinkedRelChests = false;
																			if(destInv.getTitle().contains("RelatedChest")){
																				if(destInv.getHolder()!=null && (destInv.getHolder() instanceof DoubleChest || destInv.getHolder() instanceof Chest)){
																					//Transport von verlinkte relatedChest in verlinkte relatedChest
																					
																					Location relatedDestChestLoc = null;
																					
																					if(destInv.getHolder() instanceof DoubleChest){
																						DoubleChest relatedDestChest = (DoubleChest) destInv.getHolder();
																						relatedDestChestLoc = relatedDestChest.getLocation();
																					}else if(destInv.getHolder() instanceof Chest){
																						Chest relatedDestChest = (Chest) destInv.getHolder();
																						relatedDestChestLoc = relatedDestChest.getLocation();
																					}else{
																						return;
																					}			
																																									
																					if(relatedDestChestLoc.getBlock().getState() instanceof Chest){
																						Chest relatedDestChest = (Chest) relatedDestChestLoc.getBlock().getState();																					
																							
																							for(int r=1;chestFileConfiguration.get("config.mem.relatedChests." + r)!=null;r++){
																								if(chestFileConfiguration.get("config.mem.relatedChests." + r + ".X")!=null){
																									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + r + ".doubleChest")==false){
																										//Bukkit.broadcastMessage(ChatColor.GOLD + "Kleine Kiste");
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															twoLinkedRelChests = true;
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																			
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}else{
																															return;
																														}
																													}
																												}
																											}
																										}
																									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + r + ".doubleChest")==true){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															twoLinkedRelChests = true;
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																		
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}else{
																															return;
																														}
																													}
																												}
																											}
																										}
																										
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															twoLinkedRelChests = true;
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//		Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																			
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}else{
																															return;
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																					}else{
																						//Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "kein castbaren State in line 1304!!!");
																						return;
																					}																				
																				}
																			}
																			
																			twoLinkedRelChests=false;// two linkedRelChests muss an der stelle immer true sein da wenn es false ist immer ein return kommt ;-)
																			if(twoLinkedRelChests==false){//eine verlinkte kiste die andere nicht
																				//transport von items aus verlinkter relatedChest in normale oder MainChest
																				Chest mainChest = (Chest)mainChestLoc.getBlock().getState();																	
																						
																				int leer = 0;
																				for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																					if(mainChest.getInventory().getContents()[d]==null){
																						leer++;
																					}																		
																				}
																				
																				if(leer==mainChest.getInventory().getContents().length){
																					//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! " + event.getItem());
																					relatedChest.getInventory().clear();
																					event.setCancelled(false);
																					destInv.removeItem(event.getItem());
																					return;
																				}else{
																					event.setCancelled(true);
																				}																		
																				
																				for(int l=0;l<mainChest.getInventory().getSize();l++){
																					if(mainChest.getInventory().getItem(l)!=null){
																																										
																						ItemStack lastItem = event.getItem();
																						if(event.getItem().getDurability()!=0){
																							lastItem.setDurability(event.getItem().getDurability());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + lastItem.toString() + " Hat Durability");
																						}
																						if(event.getItem().getEnchantments()!=null){
																							lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + lastItem.toString() + " Hat Enchantment");
																						}
																						
																						ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																						if(mainChest.getInventory().getItem(l).getDurability()!=0){
																							item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																						}
																						if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																							item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																							//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																						}
																							
																						if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																							if(lastItem.getType()!=item.getType()){
																								//Bukkit.broadcastMessage(ChatColor.RED + item.toString());																						
																								destInv.removeItem(lastItem);
																								event.setCancelled(false);
																								eventInv.addItem(item);
																							}else if(lastItem.getDurability()!=item.getDurability()){
																								//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());
																								destInv.removeItem(lastItem);
																								event.setCancelled(false);
																								eventInv.addItem(item);
																							}else{
																								break;
																							}
																						}else{
																							event.setCancelled(true);
																						}																				
																						
																						destInv.addItem(item);																				
																						//Bukkit.broadcastMessage(event.getItem().toString());
																						//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																						mainChest.getInventory().removeItem(item);
																						relatedChest.getInventory().clear();																				
																						
																						//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																				
																						return;																			
																					}	
																				}
																			}
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
										
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==relatedChestLoc.getBlockX()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==relatedChestLoc.getBlockY()){
												if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==relatedChestLoc.getBlockZ()){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
														if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
															String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
															for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y)!=null;y++){
																if(chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null){
																	if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".name").equals(linkedChest)){																
																		Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"));
																		twoLinkedRelChests = false;
																		
																		if(plugin.enable==true){
																			if(destInv.getTitle().contains("RelatedChest")){
																				if(destInv.getHolder()!=null && (destInv.getHolder() instanceof DoubleChest || destInv.getHolder() instanceof Chest)){
																					//Transport von verlinkte relatedChest in verlinkte relatedChest
																					
																					Location relatedDestChestLoc = null;
																					
																					if(destInv.getHolder() instanceof DoubleChest){
																						DoubleChest relatedDestChest = (DoubleChest) destInv.getHolder();
																						relatedDestChestLoc = relatedDestChest.getLocation();
																					}else if(destInv.getHolder() instanceof Chest){
																						Chest relatedDestChest = (Chest) destInv.getHolder();
																						relatedDestChestLoc = relatedDestChest.getLocation();
																					}else{
																						return;
																					}			
																																									
																					if(relatedDestChestLoc.getBlock().getState() instanceof Chest){
																						Chest relatedDestChest = (Chest) relatedDestChestLoc.getBlock().getState();																					
																							
																							for(int r=1;chestFileConfiguration.get("config.mem.relatedChests." + r)!=null;r++){
																								if(chestFileConfiguration.get("config.mem.relatedChests." + r + ".X")!=null){
																									if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + r + ".doubleChest")==false){
																										//Bukkit.broadcastMessage(ChatColor.GOLD + "Kleine Kiste");
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															twoLinkedRelChests = true;
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																			
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}else{
																															return;
																														}
																													}
																												}
																											}
																										}
																									}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + r + ".doubleChest")==true){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".coChest.Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															twoLinkedRelChests = true;
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//	Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																		
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}else{
																															return;
																														}
																													}
																												}
																											}
																										}
																										
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".X")==relatedDestChest.getX()){
																											if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Y")==relatedDestChest.getY()){
																												if(chestFileConfiguration.getInt("config.mem.relatedChests." + r + ".Z")==relatedDestChest.getZ()){
																													if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".world").equals(relatedDestChest.getWorld().getName())){
																														if(chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo")!=null){
																															linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + r + ".linkedTo");
																															twoLinkedRelChests = true;
																															
																															for(int destIndex=1;chestFileConfiguration.get("config.mem.mainChests." + destIndex)!=null;destIndex++){
																																if(chestFileConfiguration.get("config.mem.mainChests." + destIndex + ".X")!=null){
																																	if(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".name").equals(linkedChest)){
																																		
		
																																		Location destMainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + destIndex + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + destIndex + ".Z"));
																																		Chest destMainChest = (Chest)destMainChestLoc.getBlock().getState();
																																		Inventory destMainInv = destMainChest.getInventory(); 
																																		
																																		Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																																		
																																		int leer = 0;
																																		for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																																			if(mainChest.getInventory().getContents()[d]==null){
																																				leer++;
																																			}																		
																																		}
																																		
																																		if(leer==mainChest.getInventory().getContents().length){
																																			//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! ");
																																			relatedChest.getInventory().clear();
																																			event.setCancelled(false);
																																			destMainInv.addItem(event.getItem());																																	
																																			return;
																																		}else{
																																			event.setCancelled(true);
																																		}																		
																																		
																																		for(int l=0;l<mainChest.getInventory().getSize();l++){
																																			if(mainChest.getInventory().getItem(l)!=null){
																																																								
																																				ItemStack lastItem = event.getItem();
																																				if(event.getItem().getDurability()!=0){
																																					lastItem.setDurability(event.getItem().getDurability());
																																				}
																																				if(event.getItem().getEnchantments()!=null){
																																					lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																																				}
																																				
																																				ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																																				if(mainChest.getInventory().getItem(l).getDurability()!=0){
																																					item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																																				}
																																				if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																																					item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																																				}
																																					
																																				if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																																					
																																					if(lastItem.getType()!=item.getType()){
																																						//Bukkit.broadcastMessage(ChatColor.RED + item.toString());
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else if(lastItem.getDurability()!=item.getDurability()){
																																						//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());																																				
																																						event.setCancelled(false);
																																						destMainInv.addItem(lastItem);
																																						mainChest.getInventory().removeItem(item);
																																						eventInv.addItem(item);
																																					}else{
																																						break;
																																					}
																																				}else{																																			
																																					event.setCancelled(true);
																																				}																		
																																				
																																				destMainInv.addItem(item);																																		
																																				//Bukkit.broadcastMessage(event.getItem().toString());
																																				//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																																				mainChest.getInventory().removeItem(item);
																																				relatedChest.getInventory().clear();	
																																				relatedDestChest.getInventory().clear();
																																				//Bukkit.broadcastMessage(destMainChestLoc.getBlock().toString() + " :: " + mainChestLoc.getBlock().toString());
																																				
																																				//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																			
																																				return;																			
																																			}																			
																																		}
																																		
																																	}
																																}
																															}
																														}else{
																															return;
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																					}else{
																						//Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "kein castbaren State in line 1304!!!");
																						return;
																					}																				
																				}
																			}
																			twoLinkedRelChests=false;// two linkedRelChests muss an der stelle immer true sein da wenn es false ist immer ein return kommt ;-)
																			if(twoLinkedRelChests==false){//eine verlinkte kiste die andere nicht
																				//transport von items aus verlinkter relatedChest in normale oder MainChest
																				Chest mainChest = (Chest)mainChestLoc.getBlock().getState();																	
																						
																				int leer = 0;
																				for(int d=0;d<mainChest.getInventory().getContents().length;d++){
																					if(mainChest.getInventory().getContents()[d]==null){
																						leer++;
																					}																		
																				}
																				
																				if(leer==mainChest.getInventory().getContents().length){
																					//Bukkit.broadcastMessage(ChatColor.GOLD + "MainChest ist leer! " + event.getItem());
																					relatedChest.getInventory().clear();
																					event.setCancelled(false);
																					destInv.removeItem(event.getItem());
																					return;
																				}else{
																					event.setCancelled(true);
																				}																		
																				
																				for(int l=0;l<mainChest.getInventory().getSize();l++){
																					if(mainChest.getInventory().getItem(l)!=null){
																																									
																					ItemStack lastItem = event.getItem();
																					if(event.getItem().getDurability()!=0){
																						lastItem.setDurability(event.getItem().getDurability());
																						//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + lastItem.toString() + " Hat Durability");
																					}
																					if(event.getItem().getEnchantments()!=null){
																						lastItem.addUnsafeEnchantments(event.getItem().getEnchantments());
																						//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + lastItem.toString() + " Hat Enchantment");
																					}
																					
																					ItemStack item = new ItemStack(mainChest.getInventory().getItem(l).getType(), 1);
																					if(mainChest.getInventory().getItem(l).getDurability()!=0){
																						item.setDurability(mainChest.getInventory().getItem(l).getDurability());
																						//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																					}
																					if(mainChest.getInventory().getItem(l).getEnchantments()!=null){
																						item.addUnsafeEnchantments(mainChest.getInventory().getItem(l).getEnchantments());
																						//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																					}
																						
																					if(lastItem.getType()!=item.getType() || lastItem.getDurability()!=item.getDurability() || lastItem.getEnchantments().equals(item.getEnchantments())==false){
																						if(lastItem.getType()!=item.getType()){
																							//Bukkit.broadcastMessage(ChatColor.RED + item.toString());																						
																							destInv.removeItem(lastItem);
																							event.setCancelled(false);
																							eventInv.addItem(item);
																						}else if(lastItem.getDurability()!=item.getDurability()){
																							//Bukkit.broadcastMessage(ChatColor.DARK_RED + item.toString());
																							destInv.removeItem(lastItem);
																							event.setCancelled(false);
																							eventInv.addItem(item);
																						}else{
																							break;
																						}
																					}else{
																						event.setCancelled(true);
																					}																				
																						
																					destInv.addItem(item);																				
																					//Bukkit.broadcastMessage(event.getItem().toString());
																					//Bukkit.broadcastMessage(item.toString() + ", " + item.getDurability() + " : " + l + " InventoryMove");																				
																					mainChest.getInventory().removeItem(item);
																					relatedChest.getInventory().clear();																				
																					
																					//Bukkit.broadcastMessage(ChatColor.GREEN + "Transfer abgeschlossen!");																				
																					return;																			
																					}
																				}
																			}
																		}else{
																			return;
																		}
																	}
																}	
															}
														}else{
															return;
														}
													}
												}
											}							
										}
									}
								}
							}
						}
					}else{
						return;
					}
				}
			}
		}	
	}
	private SyncChest plugin;
}