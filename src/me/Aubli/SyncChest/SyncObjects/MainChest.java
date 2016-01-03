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


public class MainChest{
	
	private File chestFile;
	private FileConfiguration chestConfig;
	
	private int ChestID;
	private int doubleChestID;
	private Location chestLoc;
	
	private boolean doubleChest;
	private boolean linked;
	
	private ArrayList<RelatedChest> linkedChests;
	private ChestType chestType;
	
	public MainChest(String chestPath, ChestType type, Location chestLoc, int ID, boolean doubleChest){
		this.chestLoc = chestLoc;
		this.ChestID = ID;
		this.doubleChest = doubleChest;
		this.chestType = type;
		
		this.linked = false;
		this.doubleChest = doubleChest;
		
		this.chestFile = new File(chestPath + File.separator + ChestID + ".yml");
		this.chestConfig = YamlConfiguration.loadConfiguration(chestFile);
		
		this.linkedChests = new ArrayList<RelatedChest>();
		
		save();
	}
	
	public MainChest(File chestFile){
		this.chestFile = chestFile;
		this.chestConfig = YamlConfiguration.loadConfiguration(chestFile);
		
		this.ChestID = chestConfig.getInt("config.Chest.ID");
		this.doubleChestID = chestConfig.getInt("config.Chest.doubleChestID");
		
		this.doubleChest = chestConfig.getBoolean("config.Chest.doubleChest");
		this.linked = chestConfig.getBoolean("config.Chest.linked");
		
		this.chestType = ChestType.valueOf(chestConfig.getString("config.Chest.type"));		
		
		this.linkedChests = new ArrayList<RelatedChest>();
		
		if(this.linked){
			String linkedString = chestConfig.getString("config.Chest.linkedChests");
			linkedString = linkedString.replace("[", "").replace("]", "");
			if(linkedString.contains(",")){
				for(int i=0;i<linkedString.split(", ").length;i++){
					linkedChests.add(SyncManager.getManager().getRelatedChest(Integer.parseInt(linkedString.split(", ")[i])));
				}
			}else{
				linkedChests.add(SyncManager.getManager().getRelatedChest(Integer.parseInt(linkedString)));				
			}
		}
		
		this.chestLoc = new Location(Bukkit.getWorld(UUID.fromString(chestConfig.getString("config.Chest.Location.world"))), chestConfig.getInt("config.Chest.Location.X"), chestConfig.getInt("config.Chest.Location.Y"), chestConfig.getInt("config.Chest.Location.Z"));			
	}
	
	void save(){
		
		String linkedString = "[";
		
		if(getLinkedChests().length!=0){
			for(int i=0;i<getLinkedChests().length-1;i++){
				linkedString += getLinkedChests()[i].getID() + ", ";				
			}			
			linkedString += getLinkedChests()[getLinkedChests().length-1].getID() + "]";			
		}else{	
			linkedString += "]";
		}
		
		try{
			chestConfig.set("config.Chest.ID", ChestID);
			chestConfig.set("config.Chest.type", chestType.toString());
			chestConfig.set("config.Chest.linked", linked);
			chestConfig.set("config.Chest.doubleChest", doubleChest);
			chestConfig.set("config.Chest.doubleChestID", doubleChestID);
			chestConfig.set("config.Chest.linkedChests", linkedString);
			chestConfig.set("config.Chest.Location.world", getWorld().getUID().toString());
			chestConfig.set("config.Chest.Location.X", getX());
			chestConfig.set("config.Chest.Location.Z", getZ());
			chestConfig.set("config.Chest.Location.Y", getY());	
			
			chestConfig.save(chestFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	void delete(){
		for(int i=0;i<linkedChests.size();i++){
			unLink(linkedChests.get(i));
		}		
		chestFile.delete();
	}
	
	public void setDoubleChest(boolean doubleChest, int doubleChestID){
		this.doubleChest = doubleChest;
		this.doubleChestID = doubleChestID;
	}

	public void link(RelatedChest relChest){
		if(!linkedChests.contains(relChest)){
			this.linkedChests.add(relChest);
			this.linked = true;
			if(isDoubleChest()){
				if(getDoubleChest().isLinked()==false){
					getDoubleChest().link(relChest);
				}
			}
		}
	}
	
	public void unLink(RelatedChest relChest){
		if(this.linkedChests.contains(relChest)){	
			relChest.unLink();
			linkedChests.remove(relChest);			
			if(linkedChests.size() == 0){
				this.linked = false;
			}
			if(isDoubleChest()){
				if(getDoubleChest()!=null){
					if(getDoubleChest().isLinked()==true){
						getDoubleChest().unLink(relChest);
					}
				}
			}
		}
		
	}
	
	public int getID(){
		return ChestID;
	}
	
	public RelatedChest[] getLinkedChests(){
		RelatedChest[] chests = new RelatedChest[linkedChests.size()];
		
		for(int i=0;i<linkedChests.size();i++){
			chests[i] = linkedChests.get(i);
		}		
		return chests;
	}
	
	public ArrayList<RelatedChest> getLinkedChestsList(){
		return linkedChests;
	}
	
	public MainChest getDoubleChest(){
		return SyncManager.getManager().getMainChest(doubleChestID);
	}
	
	public int getDoubleChestID(){
		return doubleChestID;
	}
	
	public Location getLocation(){
		return chestLoc;
	}
	
	public World getWorld(){
		return chestLoc.getWorld();
	}
	
	public int getX(){
		return chestLoc.getBlockX();
	}
	
	public int getY(){
		return chestLoc.getBlockY();
	}
	
	public int getZ(){
		return chestLoc.getBlockZ();
	}
	
	public Inventory getInventory(){
		return ((Chest) chestLoc.getBlock().getState()).getInventory();
	}
	
	
	public boolean isLinked(){
		return linked;
	}
	
	public boolean isDoubleChest(){
		return doubleChest;
	}
	
	@Override
	public String toString(){
		return ChatColor.GOLD + "MainChest" + getID() + ChatColor.DARK_GRAY + " [" + ChatColor.GREEN + getWorld().getName() + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;
	}
	
}
