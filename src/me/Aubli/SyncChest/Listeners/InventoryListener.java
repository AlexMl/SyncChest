package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.SyncObjects.SyncManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryinteract(InventoryDragEvent event){
		if(event.getInventory().getTitle().contains("MainChest") || event.getInventory().getTitle().contains("RelatedChest")){
			SyncManager.getManager().updateHoppers();
		}
	}
	
	@EventHandler
	public void onOtherInteraction(InventoryClickEvent event){
		if(event.getInventory().getTitle().contains("MainChest") || event.getInventory().getTitle().contains("RelatedChest")){
			SyncManager.getManager().updateHoppers();
		}
	}

	@EventHandler
	public void onOtherInteraction(InventoryEvent event){
		if(event.getInventory().getTitle().contains("MainChest") || event.getInventory().getTitle().contains("RelatedChest")){
			SyncManager.getManager().updateHoppers();
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		if(event.getInventory().getTitle().contains("MainChest") || event.getInventory().getTitle().contains("RelatedChest")){
			SyncManager.getManager().updateHoppers();
		}
	}
}
