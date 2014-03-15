package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.SyncChest;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;

public class SyncChestInventoryListener implements Listener {
	public SyncChestInventoryListener(SyncChest plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryinteract(InventoryDragEvent event){
		if(event.getInventory().getTitle().contains("MainChest")||event.getInventory().getTitle().contains("RelatedChest")){
			plugin.reloadHoppers(null, false);
		}
	}
	
	@EventHandler
	public void onOtherInteraction(InventoryClickEvent event){
		if(event.getInventory().getTitle().contains("MainChest")||event.getInventory().getTitle().contains("RelatedChest")){
			plugin.reloadHoppers(null, false);
		}
	}

	@EventHandler
	public void onOtherInteraction(InventoryEvent event){
		if(event.getInventory().getTitle().contains("MainChest")||event.getInventory().getTitle().contains("RelatedChest")){
			plugin.reloadHoppers(null, false);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
	
		if(plugin.invOpen==true||event.getInventory().getTitle().contains("RelatedChest")||event.getInventory().getTitle().contains("MainChest")){			
			plugin.reloadHoppers(null, false);			
			plugin.invOpen=false;
		}	
	}
	
	SyncChest plugin;	
}
