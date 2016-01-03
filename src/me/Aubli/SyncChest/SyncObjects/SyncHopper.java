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
	
	public SyncHopper(String hopperPath, int ID, Location hopperLoc, Location source, Location dest){
		this.hopperFile = new File(hopperPath + File.separator + ID + ".yml");
		this.hopperConfig = YamlConfiguration.loadConfiguration(hopperFile);
		
		this.HopperID = ID;
		this.hopperLoc = hopperLoc.clone();
		
		this.hopper = (Hopper)hopperLoc.getBlock().getState();
		
		this.sourceLoc = source.clone();
		this.destLoc = dest.clone();
		
		if(destLoc.getWorld()!=null && sourceLoc.getWorld()!=null){			
			this.source = SyncManager.getManager().getInventory(sourceLoc);
		}
		
		this.inUse = false;
		this.item = null;
		save();
	}
	
	public SyncHopper(File hopperFile){
		this.hopperFile = hopperFile;
		this.hopperConfig = YamlConfiguration.loadConfiguration(this.hopperFile);
		
		this.HopperID = hopperConfig.getInt("config.Hopper.ID");
		
		this.hopperLoc = new Location(Bukkit.getWorld(UUID.fromString(hopperConfig.getString("config.Hopper.Location.world"))), hopperConfig.getInt("config.Hopper.Location.X"), hopperConfig.getInt("config.Hopper.Location.Y"), hopperConfig.getInt("config.Hopper.Location.Z"));
		this.destLoc = new Location(Bukkit.getWorld(UUID.fromString(hopperConfig.getString("config.Hopper.Location.Destination.world"))), hopperConfig.getInt("config.Hopper.Location.Destination.X"), hopperConfig.getInt("config.Hopper.Location.Destination.Y"), hopperConfig.getInt("config.Hopper.Location.Destination.Z"));
		this.sourceLoc = new Location(Bukkit.getWorld(UUID.fromString(hopperConfig.getString("config.Hopper.Location.Source.world"))), hopperConfig.getInt("config.Hopper.Location.Source.X"), hopperConfig.getInt("config.Hopper.Location.Source.Y"), hopperConfig.getInt("config.Hopper.Location.Source.Z"));
		
		if(hopperLoc.getWorld()!=null){
			this.hopper = (Hopper)hopperLoc.getBlock().getState();
		}
		
		if(sourceLoc.getWorld()!=null){
			source = SyncManager.getManager().getInventory(sourceLoc);
		}
		
	}
	
	void save(){
		
		try {			
			hopperConfig.set("config.Hopper.ID", HopperID);
			
			hopperConfig.set("config.Hopper.Location.world", hopperLoc.getWorld().getUID().toString());
			hopperConfig.set("config.Hopper.Location.X", hopperLoc.getBlockX());
			hopperConfig.set("config.Hopper.Location.Y", hopperLoc.getBlockY());
			hopperConfig.set("config.Hopper.Location.Z", hopperLoc.getBlockZ());		
			
			hopperConfig.set("config.Hopper.Location.Destination.world", destLoc.getWorld().getUID().toString());
			hopperConfig.set("config.Hopper.Location.Destination.X", destLoc.getBlockX());
			hopperConfig.set("config.Hopper.Location.Destination.Y", destLoc.getBlockY());
			hopperConfig.set("config.Hopper.Location.Destination.Z", destLoc.getBlockZ());
			
			hopperConfig.set("config.Hopper.Location.Source.world", sourceLoc.getWorld().getUID().toString());
			hopperConfig.set("config.Hopper.Location.Source.X", sourceLoc.getBlockX());
			hopperConfig.set("config.Hopper.Location.Source.Y", sourceLoc.getBlockY());
			hopperConfig.set("config.Hopper.Location.Source.Z", sourceLoc.getBlockZ());
			
			hopperConfig.save(hopperFile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void delete(){
		this.hopperFile.delete();
	}
	
	public void initialize(){
		this.inUse = true;
		
		if(this.source!=null){	

			int slot = 0;
			
			for(int i=0;i<getInventory().getSize();i++){
				if(getInventory().getItem(i)==null || getInventory().getItem(i).getType()==Material.AIR){
					slot++;
				}
			}
			
			if(slot==getInventory().getSize()){
			//	Bukkit.broadcastMessage("Starte Initiirung!");
					
				if(SyncManager.getManager().getRelatedChest(sourceLoc)!=null){
					RelatedChest sourceChest = SyncManager.getManager().getRelatedChest(sourceLoc);					
					if(sourceChest.isLinked()){
						MainChest mChest = sourceChest.getLinkedChest();
						
						for(int y=0;y<mChest.getInventory().getSize();y++){
							if(mChest.getInventory().getItem(y)!=null){
									
								ItemStack item = mChest.getInventory().getItem(y).clone();
								item.setAmount(1);
							
								if(mChest.getInventory().getItem(y).getDurability()!=0){
									item.setDurability(mChest.getInventory().getItem(y).getDurability());
							//		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
								}
								if(mChest.getInventory().getItem(y).getEnchantments()!=null){
									item.addUnsafeEnchantments(mChest.getInventory().getItem(y).getEnchantments());
							//		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
								}
								
								getInventory().addItem(item);
								setItem(item);
								mChest.getInventory().removeItem(item);
							//	Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Transfer eingeleitet! 1");							
								return;
							}
						}
					}				
				}
			}else{
				//Bukkit.broadcastMessage("Init abgebrochen: Hopper ist nicht leer!");
			}
		}	
	}
	
	public void transmit(ItemStack item, Inventory eventSource){
		this.inUse = true;		
		RelatedChest destChest = SyncManager.getManager().getRelatedChest(destLoc);
		RelatedChest sourceChest = SyncManager.getManager().getRelatedChest(sourceLoc);	
		
		//Bukkit.broadcastMessage(ChatColor.RED + "Starte Transfer!");	
		
		if(destChest!=null){			
			if(destChest.isLinked()){			
				if(eventSource.getType()!=InventoryType.HOPPER){
					
					MainChest mChest = destChest.getLinkedChest();					
					mChest.getInventory().addItem(item.clone());
					destChest.getInventory().clear();
				//	Bukkit.broadcastMessage(ChatColor.DARK_RED + "Transfer abgeschlossen! 1");	
					setItem(item.clone());
					initialize();
					return;
				}else{
					if(sourceChest!=null){
						if(sourceChest.isLinked()){							
							MainChest mChest = destChest.getLinkedChest();					
							mChest.getInventory().addItem(item.clone());
							destChest.getInventory().clear();
						//	Bukkit.broadcastMessage(ChatColor.DARK_RED + "Transfer abgeschlossen! 2");	
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
	
	public void reInitialize(){
		inUse = true;
		item = null;		
		initialize();
	}
	
	public void setItem(ItemStack item){
		this.inUse = true;
		this.item = item;
	}
	
	public int getID(){
		return HopperID;
	}
	
	public Location getLocation(){
		return hopperLoc.clone();
	}
	
	public World getWorld(){
		return hopperLoc.getWorld();
	}
	
	public int getX(){
		return hopperLoc.getBlockX();
	}
	
	public int getY(){
		return hopperLoc.getBlockY();
	}

	public int getZ(){
		return hopperLoc.getBlockZ();
	}

	public ItemStack getItem(){
		return item;
	}
	
	public Inventory getInventory(){
		return hopper.getInventory();
	}
	
	public Hopper getHopper(){
		return hopper;
	}
	
	public boolean isUsed(){
		return inUse;
	}
}
