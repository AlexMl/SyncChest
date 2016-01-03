package me.Aubli.SyncChest;

import me.Aubli.SyncChest.SyncObjects.MainChest;
import me.Aubli.SyncChest.SyncObjects.RelatedChest;
import me.Aubli.SyncChest.SyncObjects.SyncManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SyncChestCommands implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
	/*
	 * SyncChest Commands:
	 *  - sc help
	 *  - sc status
	 *  - sc save
	 *  - sc reload
	 *  - sc tool
	 *  - sc open [ID]
	 *  - sc main [amount]
	 *  - sc related [amount]
	 *  - sc link [RelatedChest-ID] [MainChest-ID]
	 *  - sc unlink [RelatedChest-ID] [MainChest-ID]
	 */
	
	MessageManager msg = MessageManager.getManager();
	SyncManager sync = SyncManager.getManager();
	
	if (sender instanceof Player) {
	    Player playerSender = (Player) sender;
	    
	    if (cmd.getName().equalsIgnoreCase("sc") || cmd.getName().equalsIgnoreCase("syncchest")) {
		
		if (args.length == 0) {
		    printCommands(playerSender);
		    return true;
		}
		
		if (args.length == 1) {
		    if (args[0].equalsIgnoreCase("help")) {
			if (playerSender.hasPermission("sc.help")) {
			    printCommands(playerSender);
			    return true;
			} else {
			    playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
			    return true;
			}
		    }
		    
		    if (args[0].equalsIgnoreCase("tool")) {
			if (playerSender.hasPermission("sc.tool")) {
			    playerSender.getInventory().addItem(SyncChest.getConnector());
			    return true;
			} else {
			    playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
			    return true;
			}
		    }
		    
		    if (args[0].equalsIgnoreCase("status")) {
			if (playerSender.hasPermission("sc.status")) {
			    
			    String pluginVersion = SyncChest.getInstance().getDescription().getVersion();
			    String pluginName = SyncChest.getInstance().getDescription().getName();
			    
			    playerSender.sendMessage("\n");
			    playerSender.sendMessage(ChatColor.GRAY + "-------------- " + ChatColor.YELLOW + pluginName + " v" + pluginVersion + ChatColor.GRAY + " ----------------");
			    playerSender.sendMessage(ChatColor.GOLD + "MainChests: " + sync.getMainChests().length);
			    playerSender.sendMessage(ChatColor.DARK_PURPLE + "RelatedChests: " + sync.getRelatedChests().length);
			    playerSender.sendMessage(ChatColor.BLUE + "Hoppers: " + sync.getHoppers().length);
			    playerSender.sendMessage("\n");
			    
			    return true;
			} else {
			    playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
			    return true;
			}
		    }
		    
		    if (args[0].equalsIgnoreCase("save")) {
			if (playerSender.hasPermission("sc.save")) {
			    sync.saveConfig();
			    playerSender.sendMessage(msg.get_CONFIG_SAVED());
			    return true;
			} else {
			    playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
			    return true;
			}
		    }
		    
		    if (args[0].equalsIgnoreCase("reload")) {
			if (playerSender.hasPermission("sc.reload")) {
			    sync.reloadConfig();
			    playerSender.sendMessage(msg.get_CONFIG_RELOADED());
			    return true;
			} else {
			    playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
			    return true;
			}
		    }
		    
		    printCommands(playerSender);
		    return true;
		}
		
		if (args.length == 2) {
		    try {
			
			if (args[0].equalsIgnoreCase("open")) {
			    if (playerSender.hasPermission("sc.use")) {
				
				int chestID = Integer.parseInt(args[1]);
				
				MainChest mChest = SyncManager.getManager().getMainChest(chestID);
				
				if (mChest != null) {
				    playerSender.closeInventory();
				    playerSender.openInventory(mChest.getInventory());
				    return true;
				} else {
				    playerSender.sendMessage(msg.ERROR_CHEST_NOT_AVAILABLE());
				    return true;
				}
			    } else {
				playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
				return true;
			    }
			}
			
			if (playerSender.hasPermission("sc.chests")) {
			    
			    int amount = Integer.parseInt(args[1]);
			    OfflinePlayer p = Bukkit.getOfflinePlayer(playerSender.getUniqueId());
			    
			    if ((args[0].equalsIgnoreCase("main") || args[0].equalsIgnoreCase("related")) && SyncChest.useEconomy()) {
				Economy econ = SyncChest.getEcon();
				if (econ.has(p, SyncChest.getChestPrice() * amount)) {
				    EconomyResponse er = econ.withdrawPlayer(p, SyncChest.getChestPrice() * amount);
				    if (er.transactionSuccess()) {
					playerSender.sendMessage(String.format(msg.get_TRANSACTION_SUCCESS(), ChatColor.GOLD + "" + amount + "" + ChatColor.GREEN, ChatColor.BLUE + "" + SyncChest.getChestPrice() * amount + " " + econ.currencyNameSingular() + ChatColor.GREEN));
				    } else {
					playerSender.sendMessage(msg.ERROR_TRANSACTION_ERROR());
					return true;
				    }
				} else if (econ.has(p, SyncChest.getChestPrice())) {
				    amount = (int) (econ.getBalance(p) / SyncChest.getChestPrice());
				    
				    if (econ.has(p, amount * SyncChest.getChestPrice())) {
					EconomyResponse er = econ.withdrawPlayer(p, SyncChest.getChestPrice() * amount);
					if (er.transactionSuccess()) {
					    playerSender.sendMessage(String.format(msg.get_TRANSACTION_SUCCESS(), ChatColor.GOLD + "" + amount + "" + ChatColor.GREEN, ChatColor.BLUE + "" + SyncChest.getChestPrice() * amount + " " + econ.currencyNameSingular() + ChatColor.GREEN));
					} else {
					    playerSender.sendMessage(msg.ERROR_TRANSACTION_ERROR());
					    return true;
					}
				    }
				} else {
				    playerSender.sendMessage(msg.ERROR_NOT_ENOUGH_MONEY());
				    return true;
				}
			    }
			    
			    if (args[0].equalsIgnoreCase("main")) {
				playerSender.getInventory().addItem(sync.getNewMainChests(amount));
				return true;
			    }
			    
			    if (args[0].equalsIgnoreCase("related")) {
				playerSender.getInventory().addItem(sync.getNewRelatedChests(amount));
				return true;
			    }
			    
			    printCommands(playerSender);
			    return true;
			} else {
			    playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
			    return true;
			}
			
		    } catch (NumberFormatException e) {
			playerSender.sendMessage(msg.ERROR_NOT_A_NUMBER());
			return true;
		    }
		}
		
		if (args.length == 3) {
		    try {
			
			if (args[0].equalsIgnoreCase("link")) {
			    if (playerSender.hasPermission("sc.link")) {
				MainChest mChest = sync.getMainChest(Integer.parseInt(args[2]));
				RelatedChest rChest = sync.getRelatedChest(Integer.parseInt(args[1]));
				
				if (rChest != null && mChest != null) {
				    sync.linkChests(rChest, mChest);
				    playerSender.sendMessage(msg.get_CHESTS_LINKED());
				    return true;
				} else {
				    playerSender.sendMessage(msg.ERROR_CHEST_NOT_AVAILABLE());
				    return true;
				}
				
			    } else {
				playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
				return true;
			    }
			}
			
			if (args[0].equalsIgnoreCase("unlink")) {
			    if (playerSender.hasPermission("sc.link")) {
				
				MainChest mChest = sync.getMainChest(Integer.parseInt(args[2]));
				RelatedChest rChest = sync.getRelatedChest(Integer.parseInt(args[1]));
				
				if (rChest != null && mChest != null) {
				    sync.unLinkChests(rChest, mChest);
				    playerSender.sendMessage(msg.get_CHESTS_UNLINKED());
				    return true;
				} else {
				    playerSender.sendMessage(msg.ERROR_CHEST_NOT_AVAILABLE());
				    return true;
				}
				
			    } else {
				playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
				return true;
			    }
			}
			
			printCommands(playerSender);
			return true;
		    } catch (NumberFormatException e) {
			playerSender.sendMessage(msg.ERROR_NOT_A_NUMBER());
			return true;
		    }
		}
		
		printCommands(playerSender);
		return true;
	    }
	} else {
	    sender.sendMessage("SyncChest commands are only for Players!");
	    return true;
	}
	return true;
    }
    
    private void printCommands(Player playerSender) {
	
	if (playerSender.hasPermission("sc.help")) {
	    String pluginVersion = SyncChest.getInstance().getDescription().getVersion();
	    String pluginName = SyncChest.getInstance().getDescription().getName();
	    
	    playerSender.sendMessage("\n\n");
	    playerSender.sendMessage(ChatColor.BLUE + "|---------- " + pluginName + " v" + pluginVersion + " ----------|");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc help");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc tool");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc status");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc save");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc reload");
	    playerSender.sendMessage(ChatColor.BLUE + "----------------------------");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc open [MainChest-ID]");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc main [amount]");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc related [amount]");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc link [RelatedChest-ID] [MainChest-ID]");
	    playerSender.sendMessage(ChatColor.BLUE + "|" + ChatColor.GRAY + " sc unlink [RelatedChest-ID] [MainChest-ID]");
	    
	} else {
	    playerSender.sendMessage(MessageManager.getManager().ERROR_NO_PERMISSIONS());
	    return;
	}
    }
    
}
