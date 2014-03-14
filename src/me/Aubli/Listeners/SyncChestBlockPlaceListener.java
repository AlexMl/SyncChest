package me.Aubli.Listeners;

import java.io.IOException;

import me.Aubli.SyncChest.SyncChest;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class SyncChestBlockPlaceListener implements Listener{
	
	public SyncChestBlockPlaceListener(SyncChest plugin){
		this.plugin = plugin;		
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){	
		FileConfiguration chestFileConfiguration = YamlConfiguration.loadConfiguration(plugin.chestFile);
		
		Player eventPlayer = event.getPlayer();
		
		plugin.mainChests.clear();
		plugin.relatedChests.clear();
		
		if(plugin.enable==true){
			//MainChests
						
			if(event.getItemInHand().getItemMeta().equals(plugin.chestMainMeta)){
				if(plugin.chestMainMeta!=null){	
					if(eventPlayer.hasPermission("sc.set.main")){
						int i=1;
						while(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
							i++;
						}
						
						if(i>1){
							plugin.mainChests.clear();
							for(int k=1;k<i;k++){
								if(chestFileConfiguration.getBoolean("config.mem.mainChests." + k + ".doubleChest")==false){
									plugin.mainChests.add(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name"));
								}else if(chestFileConfiguration.getBoolean("config.mem.mainChests." + k + ".doubleChest")==true){
									plugin.mainChests.add(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name"));
									plugin.mainChests.add(chestFileConfiguration.getString("config.mem.mainChests." + k + ".coChest.name"));
								}
							}
							
							int[] mainChestIndex = new int[plugin.mainChests.size()];
							
							for(int k=0;k<plugin.mainChests.size();k++){
								mainChestIndex[k] = Integer.parseInt(plugin.mainChests.get(k).split("Chest")[1]);
							}								
							java.util.Arrays.sort(mainChestIndex);
							
							for(int index=0;index<mainChestIndex.length;index++){
								if(index+1!=mainChestIndex[index]){
									i=(index+1);
								//	eventPlayer.sendMessage(ChatColor.DARK_BLUE + "//" + ChatColor.DARK_GRAY + "DEBUG   " + ChatColor.DARK_AQUA + "Eine Lücke wurde gefunden zwischen " + index + " und " + Integer.toString(mainChestIndex[index]));
									break;
								}
								
								if(index==(mainChestIndex.length-1)){
									i=(index+2);
								//	eventPlayer.sendMessage(ChatColor.DARK_BLUE + "//" + ChatColor.DARK_GRAY + "DEBUG   " + ChatColor.GREEN + "Keine Lücke gefunden! Nehme neuen Wert " + i);
									break;
								}
							}					
						}
						
						int freierIndex=1;
						while(chestFileConfiguration.get("config.mem.mainChests." + freierIndex + ".X")!=null){
							freierIndex++;
						}
						
						for(int y=1;chestFileConfiguration.get("config.mem.mainChests." + y + ".X")!=null;y++){			
							
							//Prüfung auf Doppelkiste
							
							if((chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"))==event.getBlock().getX()&&(chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"))==event.getBlock().getY()){
								if((chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"))+1==event.getBlock().getZ()||(chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"))-1==event.getBlock().getZ()){	
									if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world").equals(event.getBlock().getWorld().getName())){
										//Doppelkiste gefunden
										chestFileConfiguration.set("config.mem.mainChests." + y + ".doubleChest", true);
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.X", event.getBlock().getX());
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.Z", event.getBlock().getZ());
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.Y", event.getBlock().getY());
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.name", "MainChest" + i);
										
										try{
											chestFileConfiguration.save(plugin.chestFile);
										}catch(IOException e){
											e.printStackTrace();
										}
										
										//Doppelkiste gesetzt
										eventPlayer.sendMessage(ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.largeCreated"));	
										Location blockLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
										plugin.reloadHoppers(blockLocation, true);
										return;
									}
								}
							}
							
							if((chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Z"))==event.getBlock().getZ()&&(chestFileConfiguration.getInt("config.mem.mainChests." + y + ".Y"))==event.getBlock().getY()){
								if((chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"))+1==event.getBlock().getX()||(chestFileConfiguration.getInt("config.mem.mainChests." + y + ".X"))-1==event.getBlock().getX()){
									if(chestFileConfiguration.getString("config.mem.mainChests." + y + ".world").equals(event.getBlock().getWorld().getName())){
										//Doppelkiste gefunden
										chestFileConfiguration.set("config.mem.mainChests." + y + ".doubleChest", true);
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.X", event.getBlock().getX());
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.Z", event.getBlock().getZ());
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.Y", event.getBlock().getY());
										chestFileConfiguration.set("config.mem.mainChests." + y + ".coChest.name", "MainChest" + i);
										
										try{
											chestFileConfiguration.save(plugin.chestFile);
										}catch(IOException e){
											e.printStackTrace();
										}
										
										//Doppelkiste gesetzt
										eventPlayer.sendMessage(ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.largeCreated"));		
										Location blockLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
										plugin.reloadHoppers(blockLocation, true);
										return;
									}
								}
							}
						}
						
						//Keine Doppelkiste
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".name" , "MainChest" + i);
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".world", event.getBlock().getWorld().getName());
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".X", event.getBlock().getX());
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".Z", event.getBlock().getZ());
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".Y", event.getBlock().getY());	
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".linkedTo", "");
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".doubleChest", false);
						chestFileConfiguration.set("config.mem.mainChests." + freierIndex + ".coChest", "");	
						
						try {
							chestFileConfiguration.save(plugin.chestFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
			
						//chest gespeichert
						eventPlayer.sendMessage(ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.singleCreated"));
						Location blockLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
						plugin.reloadHoppers(blockLocation, false);
						return;
					}else{
						//Keine Permissions
						eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					}
				}
			}
		
			//Related chests
			if(event.getItemInHand().getItemMeta().equals(plugin.chestRelatedMeta)){
				if(plugin.chestRelatedMeta!=null){						
					if(eventPlayer.hasPermission("sc.set.related")){						
						int i=1;
						while(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
							i++;
						}
						
						if(i>1){
							plugin.relatedChests.clear();
							for(int k=1;k<i;k++){
								if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==false){
									plugin.relatedChests.add(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".name"));
								}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + k + ".doubleChest")==true){
									plugin.relatedChests.add(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".name"));
									plugin.relatedChests.add(chestFileConfiguration.getString("config.mem.relatedChests." + k + ".coChest.name"));
								}
							}
							
							int[] relatedChestIndex = new int[plugin.relatedChests.size()];
							
							for(int k=0;k<plugin.relatedChests.size();k++){
								relatedChestIndex[k] = Integer.parseInt(plugin.relatedChests.get(k).split("Chest")[1]);
							}								
							java.util.Arrays.sort(relatedChestIndex);
							
							for(int index=0;index<relatedChestIndex.length;index++){
								if(index+1!=relatedChestIndex[index]){
									i=(index+1);
								//	eventPlayer.sendMessage(ChatColor.DARK_BLUE + "//" + ChatColor.DARK_GRAY + "DEBUG   " + ChatColor.DARK_AQUA + "Eine Lücke wurde gefunden zwischen " + index + " und " + Integer.toString(relatedChestIndex[index]));
									break;
								}
								
								if(index==(relatedChestIndex.length-1)){
									i=(index+2);
								//	eventPlayer.sendMessage(ChatColor.DARK_BLUE + "//" + ChatColor.DARK_GRAY + "DEBUG   " + ChatColor.GREEN + "Keine Lücke gefunden! Nehme neuen Wert " + i);
									break;
								}
							}					
						}
						
						int freierIndex=1;
						while(chestFileConfiguration.get("config.mem.relatedChests." + freierIndex + ".X")!=null){
							freierIndex++;
						}
						
						for(int y=1;chestFileConfiguration.get("config.mem.relatedChests." + y + ".X")!=null;y++){			
							
							//Prüfung auf Doppelkiste
							
							if((chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".X"))==event.getBlock().getX()&&(chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".Y"))==event.getBlock().getY()){
								if((chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".Z"))+1==event.getBlock().getZ()||(chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".Z"))-1==event.getBlock().getZ()){								
									if(chestFileConfiguration.getString("config.mem.relatedChests." + y + ".world").equals(event.getBlock().getWorld().getName())){
										//Doppelkiste gefunden
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".doubleChest", true);
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.X", event.getBlock().getX());
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.Z", event.getBlock().getZ());
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.Y", event.getBlock().getY());
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.name", "RelatedChest" + i);
										
										try{
											chestFileConfiguration.save(plugin.chestFile);
										}catch(IOException e){
											e.printStackTrace();
										}
										
										//Doppelkiste gesetzt
										eventPlayer.sendMessage(ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.largeCreated"));								
										return;
									}
								}
							}
							
							if((chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".Z"))==event.getBlock().getZ()&&(chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".Y"))==event.getBlock().getY()){
								if((chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".X"))+1==event.getBlock().getX()||(chestFileConfiguration.getInt("config.mem.relatedChests." + y + ".X"))-1==event.getBlock().getX()){
									if(chestFileConfiguration.getString("config.mem.relatedChests." + y + ".world").equals(event.getBlock().getWorld().getName())){
										//Doppelkiste gefunden
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".doubleChest", true);
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.X", event.getBlock().getX());
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.Z", event.getBlock().getZ());
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.Y", event.getBlock().getY());
										chestFileConfiguration.set("config.mem.relatedChests." + y + ".coChest.name", "RelatedChest" + i);
										
										try{
											chestFileConfiguration.save(plugin.chestFile);
										}catch(IOException e){
											e.printStackTrace();
										}
										
										//Doppelkiste gesetzt
										eventPlayer.sendMessage(ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.largeCreated"));								
										return;
									}
								}
							}
						}
						
						//Keine Doppelkiste
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".name" , "RelatedChest" + i);
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".world", event.getBlock().getWorld().getName());
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".X", event.getBlock().getX());
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".Z", event.getBlock().getZ());
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".Y", event.getBlock().getY());	
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".linkedTo", "");
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".doubleChest", false);
						chestFileConfiguration.set("config.mem.relatedChests." + freierIndex + ".coChest", "");	
						
						try {
							chestFileConfiguration.save(plugin.chestFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
			
						//chest gespeichert
						eventPlayer.sendMessage(ChatColor.GREEN + plugin.messageFileConfiguration.getString("messages.messages.singleCreated"));
						return;
					}else{
						//Keine Permissions
						eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					}
				}
			}			
			
			//Trichter platzieren
			if(event.getBlock().getType().equals(Material.HOPPER)){
				if(event.getBlock().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
					Location relatedChestLoc = new Location(event.getBlock().getWorld(), event.getBlock().getX(), (event.getBlock().getY()+1), event.getBlock().getZ());
					if(relatedChestLoc.getBlock().getState() instanceof Chest){
						Chest relatedChest = (Chest)relatedChestLoc.getBlock().getState();
						Hopper hopper = (Hopper)event.getBlock().getState();						
						if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
							
							if(plugin.isChest("relatedchest", relatedChestLoc)){
								String path = plugin.getChestPath("relatedchest", relatedChestLoc);
								
								if(plugin.isDoubleChest("relatedchest", relatedChestLoc)){
									if(path.contains("coChest")){
										if(chestFileConfiguration.getString(path.replace(".coChest", "") + ".linkedTo")!=null){
											String linkedChest = chestFileConfiguration.getString(path.replace(".coChest", "") + ".linkedTo");
											
											for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
												if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
													if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name").equals(linkedChest)){
														Location mainChestLoc = new Location(event.getBlock().getWorld(), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Z"));
														Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
														relatedChest.getInventory().clear();
														
														for(int y=0;y<mainChest.getInventory().getSize();y++){
															if(mainChest.getInventory().getItem(y)!=null){
																//Bukkit.broadcastMessage(mainChest.getInventory().getItem(y).toString() + ", " + mainChest.getInventory().getItem(y).getDurability() + " : " + y + " BlockPlace");
																ItemStack item = new ItemStack(mainChest.getInventory().getItem(y).getType(), 1);
																if(mainChest.getInventory().getItem(y).getDurability()!=0){
																	item.setDurability(mainChest.getInventory().getItem(y).getDurability());
																	//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																}
																if(mainChest.getInventory().getItem(y).getEnchantments()!=null){
																	item.addUnsafeEnchantments(mainChest.getInventory().getItem(y).getEnchantments());
																	//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																}
																hopper.getInventory().addItem(item);
																
																if(hopper.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																	Chest relChest = (Chest)hopper.getLocation().subtract(0, 1, 0).getBlock().getState();
																	if(relChest.getInventory().getTitle().contains("RelatedChest")){
																		
																		if(plugin.isDoubleChest("relatedchest", relChest.getLocation())){
																			if(path.contains("coChest")){
																				if(chestFileConfiguration.getString(path.replace(".coChest", "") + ".linkedTo")!=null){
																					mainChest.getInventory().removeItem(item);
																				}
																			}else{
																				if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
																					mainChest.getInventory().removeItem(item);
																				}
																			}
																		}else{
																			if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
																				mainChest.getInventory().removeItem(item);
																			}
																		}		
																	}
																}																
																//Bukkit.broadcastMessage("Transfer eingeleitet!");
																return;
															}																	
														}
													}
												}
											}												
										}
									}else{
										if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
											String linkedChest = chestFileConfiguration.getString(path + ".linkedTo");
											
											for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
												if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
													if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name").equals(linkedChest)){
														Location mainChestLoc = new Location(event.getBlock().getWorld(), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Z"));
														Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
														relatedChest.getInventory().clear();
														
														for(int y=0;y<mainChest.getInventory().getSize();y++){
															if(mainChest.getInventory().getItem(y)!=null){
																//Bukkit.broadcastMessage(mainChest.getInventory().getItem(y).toString() + ", " + mainChest.getInventory().getItem(y).getDurability() + " : " + y + " BlockPlace");
																ItemStack item = new ItemStack(mainChest.getInventory().getItem(y).getType(), 1);
																if(mainChest.getInventory().getItem(y).getDurability()!=0){
																	item.setDurability(mainChest.getInventory().getItem(y).getDurability());
																	//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																}
																if(mainChest.getInventory().getItem(y).getEnchantments()!=null){
																	item.addUnsafeEnchantments(mainChest.getInventory().getItem(y).getEnchantments());
																	//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																}
																hopper.getInventory().addItem(item);
																
																if(hopper.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																	Chest relChest = (Chest)hopper.getLocation().subtract(0, 1, 0).getBlock().getState();
																	if(relChest.getInventory().getTitle().contains("RelatedChest")){
																		
																		if(plugin.isDoubleChest("relatedchest", relChest.getLocation())){
																			if(path.contains("coChest")){
																				if(chestFileConfiguration.getString(path.replace(".coChest", "") + ".linkedTo")!=null){
																					mainChest.getInventory().removeItem(item);
																				}
																			}else{
																				if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
																					mainChest.getInventory().removeItem(item);
																				}
																			}
																		}else{
																			if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
																				mainChest.getInventory().removeItem(item);
																			}
																		}	
																	}
																}																				
																//Bukkit.broadcastMessage("Transfer eingeleitet!");
																return;
															}																	
														}
													}
												}
											}												
										}
									}
								}else{								
									if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
										String linkedChest = chestFileConfiguration.getString(path + ".linkedTo");
										
										for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
											if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
												if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name").equals(linkedChest)){
													Location mainChestLoc = new Location(event.getBlock().getWorld(), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Z"));
													Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
													relatedChest.getInventory().clear();
													
													for(int y=0;y<mainChest.getInventory().getSize();y++){
														if(mainChest.getInventory().getItem(y)!=null){
															//Bukkit.broadcastMessage(mainChest.getInventory().getItem(y).toString() + ", " + mainChest.getInventory().getItem(y).getDurability() + " : " + y + " BlockPlace");
															ItemStack item = new ItemStack(mainChest.getInventory().getItem(y).getType(), 1);
															if(mainChest.getInventory().getItem(y).getDurability()!=0){
																item.setDurability(mainChest.getInventory().getItem(y).getDurability());
																//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
															}
															if(mainChest.getInventory().getItem(y).getEnchantments()!=null){
																item.addUnsafeEnchantments(mainChest.getInventory().getItem(y).getEnchantments());
																//Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
															}
															hopper.getInventory().addItem(item);
															
															if(hopper.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																Chest relChest = (Chest)hopper.getLocation().subtract(0, 1, 0).getBlock().getState();
																if(relChest.getInventory().getTitle().contains("RelatedChest")){
																	if(plugin.isChest("relatedchest", relChest.getLocation())){
																		path = plugin.getChestPath("relatedchest", relChest.getLocation());
																		
																		if(plugin.isDoubleChest("relatedchest", relChest.getLocation())){
																			if(path.contains("coChest")){
																				if(chestFileConfiguration.getString(path.replace(".coChest", "") + ".linkedTo")!=null){
																					mainChest.getInventory().removeItem(item);
																				}
																			}else{
																				if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
																					mainChest.getInventory().removeItem(item);
																				}
																			}
																		}else{
																			if(chestFileConfiguration.getString(path + ".linkedTo")!=null){
																				mainChest.getInventory().removeItem(item);
																			}
																		}
																	}																		
																}
															}														
															//Bukkit.broadcastMessage("Transfer eingeleitet!");
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
	}	
	private SyncChest plugin;
}
