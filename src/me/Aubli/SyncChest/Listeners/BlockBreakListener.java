package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.MessageManager;
import me.Aubli.SyncChest.SyncManager;
import me.Aubli.SyncChest.SyncManager.ChestType;
import me.Aubli.SyncChest.SyncChest;

import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		
		Player eventPlayer = event.getPlayer();
		MessageManager msg = MessageManager.getManager();	
		
		
		if(event.getPlayer().getItemInHand()!=null){
			if(event.getPlayer().getItemInHand().equals(SyncChest.getInstance().connector)){
				event.setCancelled(true);
				return;
			}
		}

		if(event.getBlock().getState() instanceof Chest){
			Chest chest = (Chest)event.getBlock().getState();
			
			if(chest.getInventory().getTitle().contains("MainChest") || chest.getInventory().getTitle().contains("RelatedChest")){
				if(eventPlayer.hasPermission("sc.remove")){
					boolean success = false;
					
					if(chest.getInventory().getTitle().contains("MainChest")){
						success = SyncManager.getManager().removeChest(ChestType.MAIN, event.getBlock().getLocation());
					}else if(chest.getInventory().getTitle().contains("RelatedChest")){
						success = SyncManager.getManager().removeChest(ChestType.RELATED, event.getBlock().getLocation());
					}
					
					if(success){						
						eventPlayer.sendMessage(msg.get_CHEST_REMOVED());
					}else{						
						eventPlayer.sendMessage(msg.get_CHEST_REMOVE_ERROR());
					}
					return;		
				}else{					
					eventPlayer.sendMessage(msg.get_NO_PERMISSIONS());
					event.setCancelled(true);
					return;
				}
			}			
		}
		
		if(event.getBlock().getState() instanceof Hopper){
			if(SyncManager.getManager().getHopper(event.getBlock().getLocation())!=null){
				if(eventPlayer.hasPermission("sc.remove")){
					if(SyncManager.getManager().removeHopper(event.getBlock().getLocation()) == true){
						eventPlayer.sendMessage(msg.get_HOPPER_REMOVED());						
						return;
					}else{
						eventPlayer.sendMessage(msg.get_HOPPER_REMOVE_ERROR());
						return;
					}					
				}else{
					eventPlayer.sendMessage(msg.get_NO_PERMISSIONS());
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
