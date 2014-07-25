package me.Aubli.SyncChest.Listeners;

import me.Aubli.SyncChest.MessageManager;
import me.Aubli.SyncChest.SyncChest;
import me.Aubli.SyncChest.SyncObjects.MainChest;
import me.Aubli.SyncChest.SyncObjects.RelatedChest;
import me.Aubli.SyncChest.SyncObjects.SyncManager;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryOpenListener implements Listener{
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event){
	
		Player eventPlayer = (Player) event.getPlayer();
		Location chestLoc = null;
		
		if(event.getInventory().getHolder()!=null){
			if(event.getInventory().getHolder() instanceof DoubleChest){
				chestLoc = ((DoubleChest)event.getInventory().getHolder()).getLocation();				
			}else if(event.getInventory().getHolder() instanceof Chest){
				chestLoc = ((Chest)event.getInventory().getHolder()).getLocation();	
			}					
			
			if(eventPlayer.hasPermission("sc.use")){				
				if(event.getInventory().getTitle().equalsIgnoreCase("relatedchest")){
					if(chestLoc!=null){
						RelatedChest rChest = SyncManager.getManager().getRelatedChest(chestLoc);
								
						if(rChest!=null && rChest.isLinked() && rChest.getLinkedChest()!=null){
							
							if(SyncChest.useEconomy()) {
								if(SyncChest.getEcon().has(Bukkit.getOfflinePlayer(eventPlayer.getUniqueId()), SyncChest.getUseFee())){
									EconomyResponse er = SyncChest.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(eventPlayer.getUniqueId()), SyncChest.getUseFee());
								
									if(!er.transactionSuccess()) {
										eventPlayer.sendMessage(MessageManager.getManager().ERROR_TRANSACTION_ERROR());
										event.setCancelled(true);
										return;
									}else {
										eventPlayer.sendMessage(String.format(MessageManager.getManager().get_CURRENT_BALNCE(), ChatColor.GOLD + "" + SyncChest.getEcon().getBalance(Bukkit.getOfflinePlayer(eventPlayer.getUniqueId())) + "" + ChatColor.DARK_GRAY));
									}
								}else {
									eventPlayer.sendMessage(MessageManager.getManager().ERROR_NOT_ENOUGH_MONEY());
									event.setCancelled(true);
									return;
								}
							}
							
							MainChest mChest = rChest.getLinkedChest();		
							eventPlayer.closeInventory();
							eventPlayer.openInventory(mChest.getInventory());
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}
}