package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.MessageManager;
import me.Aubli.SyncChest.SyncObjects.SyncManager;
import me.Aubli.SyncChest.SyncObjects.SyncManager.ChestType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;

public class BlockPlaceListener implements Listener{
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){	
		
		Player eventPlayer = event.getPlayer();
		boolean success = false;
		MessageManager msg = MessageManager.getManager();
		
		if(event.getItemInHand()!=null){
			if(eventPlayer.hasPermission("sc.chests")){	
				if(event.getItemInHand().getType()==Material.CHEST && event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().hasDisplayName()){
					
					if(event.getItemInHand().equals(SyncManager.getManager().getNewMainChests(1))){
						success = SyncManager.getManager().addChest(ChestType.MAIN, event.getBlock().getLocation());
					}else if(event.getItemInHand().equals(SyncManager.getManager().getNewRelatedChests(1))){
						success = SyncManager.getManager().addChest(ChestType.RELATED, event.getBlock().getLocation());
					}
				
					if(success){						
						eventPlayer.sendMessage(msg.get_CHEST_PLACED());
					}else{
						eventPlayer.sendMessage(msg.ERROR_CHEST_PLACE());
					}
					return;
				}
			}
		}
		
		if(event.getBlock().getType().equals(Material.HOPPER)){
			if(eventPlayer.hasPermission("sc.hoppers")){
				Location srcLoc = event.getBlock().getLocation().clone().add(0, 1, 0);
				Inventory src = SyncManager.getManager().getInventory(srcLoc);
				
				if(src!=null){				
					if(SyncManager.getManager().getInventory(event.getBlock().getLocation().clone().add(0, -1, 0))!=null){
						if(SyncManager.getManager().getRelatedChest(srcLoc)!=null || SyncManager.getManager().getRelatedChest(event.getBlock().getLocation().clone().add(0, -1, 0))!=null){
							SyncManager.getManager().addHopper(event.getBlock().getLocation(), event.getBlock().getLocation().clone().add(0, -1, 0), srcLoc);
							SyncManager.getManager().getHopper(event.getBlock().getLocation()).initialize();
							return;
						}
					}
					if(SyncManager.getManager().getInventory(event.getBlock().getLocation().clone().add(1, 0, 0))!=null){
						if(SyncManager.getManager().getRelatedChest(srcLoc)!=null || SyncManager.getManager().getRelatedChest(event.getBlock().getLocation().clone().add(1, 0, 0))!=null){					
							SyncManager.getManager().addHopper(event.getBlock().getLocation(), event.getBlock().getLocation().clone().add(1, 0, 0), srcLoc);
							SyncManager.getManager().getHopper(event.getBlock().getLocation()).initialize();
							return;
						}
					}
					if(SyncManager.getManager().getInventory(event.getBlock().getLocation().clone().add(-1, 0, 0))!=null){
						if(SyncManager.getManager().getRelatedChest(srcLoc)!=null || SyncManager.getManager().getRelatedChest(event.getBlock().getLocation().clone().add(-1, 0, 0))!=null){
							SyncManager.getManager().addHopper(event.getBlock().getLocation(), event.getBlock().getLocation().clone().add(-1, 0, 0), srcLoc);
							SyncManager.getManager().getHopper(event.getBlock().getLocation()).initialize();
							return;
						}
					}
					if(SyncManager.getManager().getInventory(event.getBlock().getLocation().clone().add(0, 0, 1))!=null){
						if(SyncManager.getManager().getRelatedChest(srcLoc)!=null || SyncManager.getManager().getRelatedChest(event.getBlock().getLocation().clone().add(0, 0, 1))!=null){
							SyncManager.getManager().addHopper(event.getBlock().getLocation(), event.getBlock().getLocation().clone().add(0, 0, 1), srcLoc);
							SyncManager.getManager().getHopper(event.getBlock().getLocation()).initialize();
							return;
						}
					}
					if(SyncManager.getManager().getInventory(event.getBlock().getLocation().clone().add(0, 0, -1))!=null){
						if(SyncManager.getManager().getRelatedChest(srcLoc)!=null || SyncManager.getManager().getRelatedChest(event.getBlock().getLocation().clone().add(0, 0, -1))!=null){
							SyncManager.getManager().addHopper(event.getBlock().getLocation(), event.getBlock().getLocation().clone().add(0, 0, -1), srcLoc);
							SyncManager.getManager().getHopper(event.getBlock().getLocation()).initialize();
							return;
						}
					}				
				}	
			}
		}	
	}	
}
