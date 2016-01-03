package me.Aubli.SyncChest.SyncObjects;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import me.Aubli.SyncChest.MessageManager;
import me.Aubli.SyncChest.SyncChest;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SyncManager {
	
	public enum ChestType{
		RELATED,
		MAIN,
		;
	}
	
	private static SyncManager instance;
	
	private ArrayList<MainChest> mainChests;
	private ArrayList<RelatedChest> relatedChests;
	private ArrayList<SyncHopper> syncHoppers;
	
	private String mainChestPath;
	private String relatedChestPath;
	private String hopperPath;
	
	public SyncManager(){
		instance = this;
		
		mainChestPath = SyncChest.getInstance().getDataFolder().getPath() + File.separator + "Chests" + File.separator + "Main";
		relatedChestPath = SyncChest.getInstance().getDataFolder().getPath() + File.separator + "Chests" + File.separator + "Related";
		hopperPath = SyncChest.getInstance().getDataFolder().getPath() + File.separator + "Hoppers";
		
		if(!new File(mainChestPath).exists() || !new File(relatedChestPath).exists() || !new File(hopperPath).exists()){
			new File(mainChestPath).mkdirs();
			new File(relatedChestPath).mkdirs();	
			new File(hopperPath).mkdirs();
		}
		
		mainChests = new ArrayList<MainChest>();
		relatedChests = new ArrayList<RelatedChest>();		
		syncHoppers = new ArrayList<SyncHopper>();
		
		reloadConfig();
	}
	
	public static SyncManager getManager(){
		return instance;
	}
	
	public void saveConfig(){
		saveChests();
		saveHoppers();
	}
	
	public void reloadConfig(){
		mainChests.clear();
		relatedChests.clear();
		syncHoppers.clear();
		
		loadChests();
		loadHoppers();
		
		MessageManager.getManager().reload();
	}
	
	private void saveChests(){
		
		for(int i=0;i<getMainChests().length;i++){
			getMainChests()[i].save();
		}
		for(int i=0;i<getRelatedChests().length;i++){
			getRelatedChests()[i].save();
		}
		
	}
	
	private void saveHoppers(){
		
		for(int i=0;i<getHoppers().length;i++){
			getHoppers()[i].save();
		}
	}
	
	private void loadChests(){		
		File rFolder = new File(relatedChestPath);
		for(int i=0;i<rFolder.listFiles().length;i++){
			File rChestFile = rFolder.listFiles()[i];
			RelatedChest chest = new RelatedChest(rChestFile);
			if(chest.getWorld()!=null){
				relatedChests.add(chest);
			}
		}	
		
		File mFolder = new File(mainChestPath);
		for(int i=0;i<mFolder.listFiles().length;i++){
			File mChestFile = mFolder.listFiles()[i];
			MainChest chest = new MainChest(mChestFile);
			if(chest.getWorld()!=null){
				mainChests.add(chest);
			}
		}	
		relatedChests.clear();
		for(int i=0;i<rFolder.listFiles().length;i++){
			File rChestFile = rFolder.listFiles()[i];
			RelatedChest chest = new RelatedChest(rChestFile);
			if(chest.getWorld()!=null){
				relatedChests.add(chest);
			}
		}	
	}
	
	private void loadHoppers(){
		File hFolder = new File(hopperPath);
		for(int i=0;i<hFolder.listFiles().length;i++){
			File hopperFile = hFolder.listFiles()[i];
			SyncHopper hopper = new SyncHopper(hopperFile);			
			if(hopper.getWorld()!=null){
				syncHoppers.add(hopper);
				updateHopper(hopper);
			}
		}	
	}
	
	
	private int getNewID(ChestType type){
		
		if(type == ChestType.MAIN){			
			return getNewID(mainChestPath);
		}else if(type == ChestType.RELATED){
			return getNewID(relatedChestPath);
		}
		return -1;
	}
	
	private int getNewID(){
		return getNewID(hopperPath);
	}
	
	private int getNewID(String path){
		if(new File(path).listFiles().length==0){
			return 1;
		}else{
			int[] chests = new int[new File(path).listFiles().length];
			
			for(int i=0;i<chests.length;i++){
				chests[i] = Integer.parseInt(new File(path).listFiles()[i].getName().split(".y")[0]);
			}		
			
			Arrays.sort(chests);
				
			for(int k=0;k<chests.length;k++){
				if(chests[k]!=k+1){
					return k+1;
				}
					
				if(k+1==chests.length){
					return chests.length+1;
				}
			}			
		}
		return -1;		
	}
	
	
	public ItemStack getNewMainChests(int amount){
		
		ItemStack mStack = new ItemStack(Material.CHEST, amount);
		
		ItemMeta mMeta = mStack.getItemMeta();
		mMeta.setDisplayName("MainChest");
		
		ArrayList<String> chestLore = new ArrayList<String>();
		chestLore.add(ChatColor.GOLD + "MainChests need RelatedChests to work!");
		chestLore.add(ChatColor.GOLD + "Connect Main and RelatedChests with the Connector!");
		mMeta.setLore(chestLore);
		mStack.setItemMeta(mMeta);
		
		return mStack;
	}
	
	public ItemStack getNewRelatedChests(int amount){
		
		ItemStack rStack = new ItemStack(Material.CHEST, amount);
		
		ItemMeta rMeta = rStack.getItemMeta();
		rMeta.setDisplayName("RelatedChest");
		
		ArrayList<String> chestLore = new ArrayList<String>();
		chestLore.add(ChatColor.DARK_PURPLE + "RelatedChests need MainChests to work!");
		chestLore.add(ChatColor.DARK_PURPLE + "Connect Main and RelatedChests with the Connector!");	
		rMeta.setLore(chestLore);
		rStack.setItemMeta(rMeta);
		
		return rStack;
	}
	
	public Object[] getChests(ChestType type){
		
		if(type == ChestType.MAIN){
			Object[] chests = new Object[getMainChests().length];
		
			for(int i=0;i<getMainChests().length;i++){
				chests[i] = (Object)getMainChests()[i];
			}
			return chests;
		}
		
		if(type == ChestType.RELATED){
			Object[] chests = new Object[getRelatedChests().length];
			
			for(int i=0;i<getRelatedChests().length;i++){
				chests[i] = (Object)getRelatedChests()[i];
			}
			return chests;
		}
		return null;
	}
	
	public MainChest[] getMainChests(){		
		MainChest[] chests = new MainChest[mainChests.size()];
		
		for(int i=0;i<mainChests.size();i++){
			chests[i] = mainChests.get(i);
		}		
		return chests;
	}
	
	public RelatedChest[] getRelatedChests(){
		RelatedChest[] chests = new RelatedChest[relatedChests.size()];
		
		for(int i=0;i<relatedChests.size();i++){
			chests[i] = relatedChests.get(i);
		}		
		return chests;
	}
	
	public SyncHopper[] getHoppers(){
		SyncHopper[] hopper = new SyncHopper[syncHoppers.size()];
		
		for(int i=0;i<syncHoppers.size();i++){
			hopper[i] = syncHoppers.get(i);
		}		
		return hopper;
	}
	
	
 	public MainChest getMainChest(Location chestLocation){
		
		for(int i=0;i<getMainChests().length;i++){
			MainChest mChest = getMainChests()[i];
			
			if(mChest.getX()==chestLocation.getBlockX()){
				if(mChest.getY()==chestLocation.getBlockY()){
					if(mChest.getZ()==chestLocation.getBlockZ()){
						if(mChest.getWorld()==chestLocation.getWorld()){
							return mChest;
						}
					}
				}
			}
		}
		return null;
	}
	
	public RelatedChest getRelatedChest(Location chestLocation){
		
		for(int i=0;i<getRelatedChests().length;i++){
			RelatedChest rChest = getRelatedChests()[i];
			
			if(rChest.getX()==chestLocation.getBlockX()){
				if(rChest.getY()==chestLocation.getBlockY()){
					if(rChest.getZ()==chestLocation.getBlockZ()){
						if(rChest.getWorld()==chestLocation.getWorld()){
							return rChest;
						}
					}
				}
			}
		}
		return null;
	}
	
	public SyncHopper getHopper(Location hopperLocation){
		for(int i=0;i<getHoppers().length;i++){
			SyncHopper hopper = getHoppers()[i];
			
			if(hopper.getX()==hopperLocation.getBlockX()){
				if(hopper.getY()==hopperLocation.getBlockY()){
					if(hopper.getZ()==hopperLocation.getBlockZ()){
						if(hopper.getWorld()==hopperLocation.getWorld()){
							return hopper;
						}
					}
				}
			}
		}
		return null;
	}
	
	public MainChest getMainChest(int ChestID){
		
		for(int i=0;i<getMainChests().length;i++){
			if(getMainChests()[i].getID()==ChestID){
				return getMainChests()[i];
			}
		}		
		return null;
	}
	
	public RelatedChest getRelatedChest(int ChestID){
		
		for(int i=0;i<getRelatedChests().length;i++){
			if(getRelatedChests()[i].getID()==ChestID){
				return getRelatedChests()[i];
			}
		}		
		return null;
	}
	
	public SyncHopper getHopper(int HopperID){
		for(int i=0;i<getHoppers().length;i++){
			if(getHoppers()[i].getID()==HopperID){
				return getHoppers()[i];
			}
		}		
		return null;
	}
	
	public SyncHopper getHopper(Inventory inventory){
		
		if(inventory.getHolder()!=null && inventory.getHolder() instanceof Hopper){
			if(getHopper(((Hopper)inventory.getHolder()).getLocation())!=null){
				return getHopper(((Hopper)inventory.getHolder()).getLocation());
			}
		}
		return null;
	}
	
	public Inventory getInventory(Location loc){
		
		if(loc.getBlock().getState() instanceof MainChest){
			return ((MainChest)loc.getBlock().getState()).getInventory();
		}
		
		if(loc.getBlock().getState() instanceof RelatedChest){
			return ((RelatedChest)loc.getBlock().getState()).getInventory();
		}
		
		if(loc.getBlock().getState() instanceof Chest){
			return ((Chest)loc.getBlock().getState()).getInventory();
		}
		
		if(loc.getBlock().getState() instanceof DoubleChest){
			return ((DoubleChest)loc.getBlock().getState()).getInventory();
		}
		
		if(loc.getBlock().getState() instanceof BrewingStand){
			return ((BrewingStand)loc.getBlock().getState()).getInventory();
		}
		
		if(loc.getBlock().getState() instanceof Dispenser){
			return ((Dispenser)loc.getBlock().getState()).getInventory();
		}
		
		if(loc.getBlock().getState() instanceof Dropper){
			return ((Dropper)loc.getBlock().getState()).getInventory();
		}
		return null;
	}
	
	
	public boolean addChest(ChestType chest, Location loc){

		int ChestID = getNewID(chest);
		
		for(int i=0;i<getChests(chest).length;i++){
			if(getChests(chest)[i] instanceof MainChest){
				MainChest mChest = (MainChest)getChests(chest)[i];
				
				if(mChest.getY()==loc.getBlockY()){
					if(mChest.getX()==loc.getBlockX()){
						if(mChest.getZ()+1==loc.getBlockZ() || mChest.getZ()-1==loc.getBlockZ()){	
							if(mChest.getWorld()==loc.getWorld()){
								//Doppelkiste gefunden
								MainChest newMChest = new MainChest(mainChestPath, ChestType.MAIN, loc, ChestID, true);
								newMChest.setDoubleChest(true, mChest.getID());
								mChest.setDoubleChest(true, ChestID);
								mainChests.add(newMChest);
								
								//Doppelkiste gesetzt
							//	playerSender.sendMessage("Doppelkiste gespeichert");
								return true;
							}
						}
					}
					
					if(mChest.getZ()==loc.getBlockZ()){
						if(mChest.getX()+1==loc.getBlockX() || mChest.getX()-1==loc.getBlockX()){
							if(mChest.getWorld()==loc.getWorld()){
								//Doppelkiste gefunden
								MainChest newMChest = new MainChest(mainChestPath, ChestType.MAIN, loc, ChestID, true);
								newMChest.setDoubleChest(true, mChest.getID());
								mChest.setDoubleChest(true, ChestID);
								mainChests.add(newMChest);
								
								//Doppelkiste gesetzt
								//playerSender.sendMessage("Doppelkiste gespeichert");
								return true;
							}
						}
					}
				}
				
			}
			
			if(getChests(chest)[i] instanceof RelatedChest){
				RelatedChest rChest = (RelatedChest)getChests(chest)[i];
				
				if(rChest.getY()==loc.getBlockY()){
					if(rChest.getX()==loc.getBlockX()){
						if(rChest.getZ()+1==loc.getBlockZ() || rChest.getZ()-1==loc.getBlockZ()){	
							if(rChest.getWorld()==loc.getWorld()){
								//Doppelkiste gefunden
								RelatedChest newRChest = new RelatedChest(relatedChestPath, ChestType.RELATED, loc, ChestID, true);
								newRChest.setDoubleChest(true, rChest.getID());
								rChest.setDoubleChest(true, ChestID);
								relatedChests.add(newRChest);
								
								//Doppelkiste gesetzt
								//playerSender.sendMessage("Doppelkiste gespeichert");
								return true;
							}
						}
					}
					
					if(rChest.getZ()==loc.getBlockZ()){
						if(rChest.getX()+1==loc.getBlockX() || rChest.getX()-1==loc.getBlockX()){
							if(rChest.getWorld()==loc.getWorld()){
								//Doppelkiste gefunden
								RelatedChest newRChest = new RelatedChest(relatedChestPath, ChestType.RELATED, loc, ChestID, true);
								newRChest.setDoubleChest(true, rChest.getID());
								rChest.setDoubleChest(true, ChestID);
								relatedChests.add(newRChest);
								
								//Doppelkiste gesetzt
								//playerSender.sendMessage("Doppelkiste gespeichert");
								return true;
							}
						}
					}
				}
			}
		}
		
		//chest gespeichert
		//playerSender.sendMessage("Einzelkiste gespeichert");
				
		if(chest == ChestType.MAIN){
			MainChest m = new MainChest(mainChestPath, ChestType.MAIN, loc, ChestID, false);
			mainChests.add(m);
		}else if(chest == ChestType.RELATED){
			RelatedChest r = new RelatedChest(relatedChestPath, ChestType.RELATED, loc, ChestID, false);
			relatedChests.add(r);
		}
		return true;
	}
	
	public boolean removeChest(ChestType chest, Location loc){
		
		if(chest==ChestType.MAIN){
			MainChest mChest = getMainChest(loc);
			
			if(mChest!=null){				
				mChest.delete();
				mainChests.remove(mChest);
				return true;
			}else{
				return false;
			}
		}
		
		if(chest==ChestType.RELATED){
			RelatedChest rChest = getRelatedChest(loc);
			
			if(rChest!=null){
				rChest.unLink();
				rChest.delete();
				relatedChests.remove(rChest);
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public void addHopper(Location hopperLoc, Location destLoc, Location sourceLoc){		
		int HopperID = getNewID();		
		SyncHopper h = new SyncHopper(hopperPath, HopperID, hopperLoc, sourceLoc, destLoc);
		syncHoppers.add(h);
	}
	
	public boolean removeHopper(Location hopperLoc){
		SyncHopper hopper = getHopper(hopperLoc);
		
		if(hopper!=null){
			hopper.delete();
			syncHoppers.remove(hopper);
			return true;
		}else{
			return false;
		}		
	}

	
	public void linkChests(RelatedChest relatedChest, MainChest mainChest){	
		relatedChest.link(mainChest);
		mainChest.link(relatedChest);	
	}
	
	public void unLinkChests(RelatedChest relatedChest, MainChest mainChest){
		relatedChest.unLink();
		mainChest.unLink(relatedChest);
	}
	
	
	public void updateHoppers(){		
		for(int i=0;i<getHoppers().length;i++){
			updateHopper(getHoppers()[i]);
		}
	}
	
	public void updateHopper(SyncHopper hopper){
		hopper.reInitialize();
	}
}
