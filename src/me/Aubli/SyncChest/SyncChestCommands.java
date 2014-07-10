package me.Aubli.SyncChest;

import me.Aubli.SyncChest.SyncObjects.MainChest;
import me.Aubli.SyncChest.SyncObjects.RelatedChest;
import me.Aubli.SyncChest.SyncObjects.SyncManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SyncChestCommands implements CommandExecutor{

	private SyncChest plugin;
	
	public SyncChestCommands(SyncChest plugin) {
		this.plugin = plugin;	
	}	
	
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
		
		if(sender instanceof Player){
			Player playerSender = (Player)sender;
			
			if(cmd.getName().equalsIgnoreCase("sc") || cmd.getName().equalsIgnoreCase("syncchest")){
				
				if(args.length==0){
					printCommands(playerSender);
					return true;
				}
				
				if(args.length==1){
					if(args[0].equalsIgnoreCase("help")){
						if(playerSender.hasPermission("sc.help")){
							printCommands(playerSender);
							return true;
						}else{
							playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
							return true;
						}
					}
					
					if(args[0].equalsIgnoreCase("tool")){
						if(playerSender.hasPermission("sc.tool")){
							playerSender.getInventory().addItem(plugin.getConnector());
							return true;
						}else{
							playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
							return true;
						}
					}
					
					if(args[0].equalsIgnoreCase("status")){
						if(playerSender.hasPermission("sc.status")){
							
							String pluginVersion = plugin.getDescription().getVersion();
							String pluginName = plugin.getDescription().getName();
							
							playerSender.sendMessage("\n");	
							playerSender.sendMessage(ChatColor.GRAY + "-------------- " + ChatColor.YELLOW + pluginName + " v" + pluginVersion + ChatColor.GRAY + " ----------------");
							playerSender.sendMessage(ChatColor.GOLD + "MainChests: " + sync.getMainChests().length);
							playerSender.sendMessage(ChatColor.DARK_PURPLE + "RelatedChests: " + sync.getRelatedChests().length);
							playerSender.sendMessage(ChatColor.BLUE + "Hoppers: " + sync.getHoppers().length);
							playerSender.sendMessage("\n");
							
							return true;
						}else{
							playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
							return true;
						}
					}
					
					if(args[0].equalsIgnoreCase("save")){
						if(playerSender.hasPermission("sc.save")){
							sync.saveConfig();
							playerSender.sendMessage(msg.get_CONFIG_SAVED());
							return true;
						}else{
							playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
							return true;
						}
					}
					
					if(args[0].equalsIgnoreCase("reload")){
						if(playerSender.hasPermission("sc.reload")){
							sync.reloadConfig();
							playerSender.sendMessage(msg.get_CONFIG_RELOADED());
							return true;
						}else{
							playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
							return true;
						}
					}
					
					printCommands(playerSender);
					return true;
				}
				
				if(args.length==2){					
					try {
						
						if(args[0].equalsIgnoreCase("open")) {
							if(playerSender.hasPermission("sc.use")) {
								
								int chestID = Integer.parseInt(args[1]);
								
								MainChest mChest = SyncManager.getManager().getMainChest(chestID);
								
								if(mChest!=null) {
									playerSender.closeInventory();
									playerSender.openInventory(mChest.getInventory());
									return true;
								}else {
									playerSender.sendMessage(msg.ERROR_CHEST_NOT_AVAILABLE());
									return true;
								}
							}else {
								playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
								return true;
							}
						}
						
						if(playerSender.hasPermission("sc.chests")){
							if(args[0].equalsIgnoreCase("main")){
								playerSender.getInventory().addItem(sync.getNewMainChests(Integer.parseInt(args[1])));
								return true;
							}
							
							if(args[0].equalsIgnoreCase("related")){
								playerSender.getInventory().addItem(sync.getNewRelatedChests(Integer.parseInt(args[1])));
								return true;
							}
							
							printCommands(playerSender);
							return true;
						}else{
							playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
							return true;
						}
					
					}catch(NumberFormatException e) {
						playerSender.sendMessage(msg.ERROR_NOT_A_NUMBER());
						return true;
					}
				}
				
				if(args.length==3){
					try {
						
						if(args[0].equalsIgnoreCase("link")){
							if(playerSender.hasPermission("sc.link")){
								MainChest mChest = sync.getMainChest(Integer.parseInt(args[2]));
								RelatedChest rChest = sync.getRelatedChest(Integer.parseInt(args[1]));
								
								if(rChest!=null && mChest!=null){
									sync.linkChests(rChest, mChest);
									playerSender.sendMessage(msg.get_CHESTS_LINKED());
									return true;
								}else{
									playerSender.sendMessage(msg.ERROR_CHEST_NOT_AVAILABLE());
									return true;
								}
								
							}else{
								playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
								return true;
							}
						}
						
						if(args[0].equalsIgnoreCase("unlink")){
							if(playerSender.hasPermission("sc.link")){
								
								MainChest mChest = sync.getMainChest(Integer.parseInt(args[2]));
								RelatedChest rChest = sync.getRelatedChest(Integer.parseInt(args[1]));							
									
								if(rChest!=null && mChest!=null){
									sync.unLinkChests(rChest, mChest);
									playerSender.sendMessage(msg.get_CHESTS_UNLINKED());
									return true;
								}else{
									playerSender.sendMessage(msg.ERROR_CHEST_NOT_AVAILABLE());
									return true;
								}
															
							}else{
								playerSender.sendMessage(msg.ERROR_NO_PERMISSIONS());
								return true;
							}
						}
						
						printCommands(playerSender);
						return true;	
					}catch(NumberFormatException e) {
						playerSender.sendMessage(msg.ERROR_NOT_A_NUMBER());
						return true;
					}
				}
				
				printCommands(playerSender);
				return true;				
			}
		}else{
			sender.sendMessage("SyncChest commands are only for Players!");
			return true;		
		}
		return true;
	}
	
	private void printCommands(Player playerSender){
		
		if(playerSender.hasPermission("sc.help")){
			String pluginVersion = plugin.getDescription().getVersion();
			String pluginName = plugin.getDescription().getName();
			
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
		
		}else{
			playerSender.sendMessage(MessageManager.getManager().ERROR_NO_PERMISSIONS());
			return;
		}
	}
	
}
