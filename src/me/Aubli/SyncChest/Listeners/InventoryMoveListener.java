package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.SyncObjects.SyncHopper;
import me.Aubli.SyncChest.SyncObjects.SyncManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class InventoryMoveListener implements Listener{

	@EventHandler
	public void onInventoryEvent(InventoryMoveItemEvent event){
		
		SyncHopper hopper = SyncManager.getManager().getHopper(event.getInitiator());
		
		if(hopper!=null){		
			hopper.transmit(event.getItem().clone(), event.getSource());
			return;
		}
	}
}