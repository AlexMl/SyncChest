package me.Aubli.SyncChest.Listeners;

import java.util.HashMap;

import me.Aubli.SyncChest.MessageManager;
import me.Aubli.SyncChest.SyncChest;
import me.Aubli.SyncChest.SyncObjects.MainChest;
import me.Aubli.SyncChest.SyncObjects.RelatedChest;
import me.Aubli.SyncChest.SyncObjects.SyncManager;
import me.Aubli.SyncChest.SyncObjects.SyncManager.ChestType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class PlayerInteractListener implements Listener {
    
    public PlayerInteractListener(SyncChest plugin) {
	this.plugin = plugin;
	this.chests = new HashMap<ChestType, Integer>();
    }
    
    private HashMap<ChestType, Integer> chests;
    MessageManager msg = MessageManager.getManager();
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
	Player eventPlayer = event.getPlayer();
	
	if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
	    if (event.getItem() != null) {
		if (event.getItem().isSimilar(SyncChest.connector)) {
		    if (eventPlayer.hasPermission("sc.tool")) {
			if (event.getClickedBlock().getType().equals(Material.CHEST)) {
			    if (event.getClickedBlock().getState() instanceof Chest) {
				Chest eventChest = (Chest) event.getClickedBlock().getState();
				if (eventChest.getInventory().getTitle().contains("MainChest")) {
				    MainChest mChest = SyncManager.getManager().getMainChest(event.getClickedBlock().getLocation());
				    if (mChest != null) {
					this.chests.put(ChestType.MAIN, mChest.getID());
					eventPlayer.sendMessage(this.msg.get_CHEST_SELECTED());
				    }
				} else if (eventChest.getInventory().getTitle().contains("RelatedChest")) {
				    RelatedChest rChest = SyncManager.getManager().getRelatedChest(event.getClickedBlock().getLocation());
				    if (rChest != null) {
					this.chests.put(ChestType.RELATED, rChest.getID());
					eventPlayer.sendMessage(this.msg.get_CHEST_SELECTED());
				    }
				}
				
				if (this.chests.containsKey(ChestType.MAIN) && this.chests.containsKey(ChestType.RELATED)) {
				    SyncManager.getManager().linkChests(SyncManager.getManager().getRelatedChest(this.chests.get(ChestType.RELATED)), SyncManager.getManager().getMainChest(this.chests.get(ChestType.MAIN)));
				    this.chests.clear();
				    eventPlayer.sendMessage(this.msg.get_CHESTS_LINKED());
				    return;
				}
				
			    }
			}
		    } else {
			eventPlayer.sendMessage(this.msg.ERROR_NO_PERMISSIONS());
			this.plugin.clearPlayerInventory(eventPlayer);
			return;
		    }
		}
	    }
	}
	
	if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    if (event.getItem() != null) {
		if (event.getItem().isSimilar(SyncChest.connector)) {
		    if (eventPlayer.hasPermission("sc.tool")) {
			event.setCancelled(true);
			
			if (event.getClickedBlock().getType() == Material.CHEST) {
			    if (event.getClickedBlock().getState() instanceof Chest) {
				
				String version = this.plugin.getDescription().getVersion();
				String name = this.plugin.getDescription().getName();
				
				if (SyncManager.getManager().getMainChest(event.getClickedBlock().getLocation()) != null) {
				    MainChest mChest = SyncManager.getManager().getMainChest(event.getClickedBlock().getLocation());
				    
				    eventPlayer.sendMessage(ChatColor.BLACK + "–––––––––––––––––––––––" + ChatColor.YELLOW + name + " v" + version + ChatColor.BLACK + "–––––––––––––––––––––––––––––");
				    eventPlayer.sendMessage(ChatColor.GOLD + "MainChest:");
				    eventPlayer.sendMessage("ID: " + ChatColor.DARK_GRAY + mChest.getID());
				    eventPlayer.sendMessage("Double Chest: " + ChatColor.AQUA + mChest.isDoubleChest());
				    
				    if (mChest.isDoubleChest()) {
					eventPlayer.sendMessage("Double Chest: " + mChest.getDoubleChest().toString());
				    }
				    eventPlayer.sendMessage("Linked: " + ChatColor.AQUA + mChest.isLinked());
				    
				    if (mChest.isLinked()) {
					eventPlayer.sendMessage("Linked to: " + mChest.getLinkedChestsList().toString());
				    }
				    
				    eventPlayer.sendMessage("Location: " + ChatColor.GREEN + mChest.getWorld().getName() + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + mChest.getX() + ChatColor.RESET + "(X)" + ChatColor.GREEN + mChest.getY() + ChatColor.RESET + "(Y)" + ChatColor.GREEN + mChest.getZ() + ChatColor.RESET + "(Z)" + ChatColor.DARK_GRAY + "]");
				    return;
				    
				} else if (SyncManager.getManager().getRelatedChest(event.getClickedBlock().getLocation()) != null) {
				    RelatedChest rChest = SyncManager.getManager().getRelatedChest(event.getClickedBlock().getLocation());
				    
				    eventPlayer.sendMessage(ChatColor.BLACK + "–––––––––––––––––––––––" + ChatColor.YELLOW + name + " v" + version + ChatColor.BLACK + "–––––––––––––––––––––––––––––");
				    eventPlayer.sendMessage(ChatColor.DARK_PURPLE + "RelatedChest:");
				    eventPlayer.sendMessage("ID: " + ChatColor.DARK_GRAY + rChest.getID());
				    eventPlayer.sendMessage("Double Chest: " + ChatColor.AQUA + rChest.isDoubleChest());
				    
				    if (rChest.isDoubleChest()) {
					eventPlayer.sendMessage("Double Chest: " + rChest.getDoubleChest().toString());
				    }
				    eventPlayer.sendMessage("Linked: " + ChatColor.AQUA + rChest.isLinked());
				    
				    if (rChest.isLinked()) {
					eventPlayer.sendMessage("Linked to: " + rChest.getLinkedChest().toString());
				    }
				    
				    eventPlayer.sendMessage("Location: " + ChatColor.GREEN + rChest.getWorld().getName() + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + rChest.getX() + ChatColor.RESET + "(X)" + ChatColor.GREEN + rChest.getY() + ChatColor.RESET + "(Y)" + ChatColor.GREEN + rChest.getZ() + ChatColor.RESET + "(Z)" + ChatColor.DARK_GRAY + "]");
				    return;
				}
			    }
			}
		    } else {
			eventPlayer.sendMessage(this.msg.ERROR_NO_PERMISSIONS());
			this.plugin.clearPlayerInventory(eventPlayer);
			return;
		    }
		}
	    }
	}
    }
    
    private SyncChest plugin;
}
