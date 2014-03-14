package me.Aubli.SyncChest;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecuter implements CommandExecutor{

	private SyncChest plugin;
	
	public CommandExecuter(SyncChest plugin) {
		this.plugin = plugin;	
	}	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		
		if(sender instanceof Player){
			Player playerSender = ((Player) sender).getPlayer();

			if(cmd.getName().equalsIgnoreCase("sctoggle")){
				if(playerSender.hasPermission("sc.toggle")){					
					if(args.length==0){							
						plugin.toggle(playerSender);
						return true;	
					}else{
						//Keine passenden Argumente
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
						return false;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}			
			
			if(cmd.getName().equalsIgnoreCase("scmain")){
				if(playerSender.hasPermission("sc.set.main")){
					if(plugin.enable==true){
						if(args.length==1){
							if(args[0].matches("\\d*")){
								plugin.setMain(playerSender, Integer.parseInt(args[0]));
								return true;
							}else{
								//Nur Zahlen
								playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_onlyNumbers"));
								return true;
							}						
						}else{
							//Keine passenden Argumente
							playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
							return false;
						}
					}else{
						//Plugin deaktiviert
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_pluginIsDisabled"));
						return true;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("screlated")){
				if(playerSender.hasPermission("sc.set.related")){
					if(plugin.enable==true){
						if(args.length==0){
							plugin.setRelated(playerSender);
							return true;					
						}else{
							//Keine passenden Argumente
							playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
							return false;
						}
					}else{
						//Plugin deaktiviert
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_pluginIsDisabled"));
						return true;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("sclink")){
				if(playerSender.hasPermission("sc.link")){
					if(plugin.enable==true){
						if(args.length==2){
							plugin.link(playerSender, args[0], args[1]);
							return true;					
						}else{
							//Keine passenden Argumente
							playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
							return false;
						}
					}else{
						//Plugin deaktiviert
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_pluginIsDisabled"));
						return true;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("scconnector")){
				if(playerSender.hasPermission("sc.get.connector")){
					if(plugin.enable==true){
						if(args.length==0){
							plugin.giveConnectorItem(playerSender);
							return true;					
						}else{
							//Keine passenden Argumente
							playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
							return false;
						}
					}else{
						//Plugin deaktiviert
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_pluginIsDisabled"));
						return true;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("scdelink")){
				if(playerSender.hasPermission("sc.remove.links")){
					if(args.length==0){
						plugin.deLink(playerSender);
						return true;
					}else{
						//Keine passenden Argumente
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
						return false;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}			
			
			if(cmd.getName().equalsIgnoreCase("scclear")){
				if(playerSender.hasPermission("sc.clear")){
					if(args.length==0){
						plugin.clearSCInventory(playerSender);
						return true;					
					}else{
						//Keine passenden Argumente
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
						return false;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}			
			
			if(cmd.getName().equalsIgnoreCase("screset")){
				if(playerSender.hasPermission("sc.reset")){
					if(args.length==0){
						plugin.reset(playerSender);
						return true;					
					}else{
						//Keine passenden Argumente
						playerSender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
						return false;
					}
				}else{
					//Keine Permissions
					playerSender.sendMessage(ChatColor.DARK_RED + plugin.messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
					return true;
				}
			}
			
		}else{		
			
			if(cmd.getName().equalsIgnoreCase("sctoggle")){						
				if(args.length==0){							
					plugin.toggle(sender);
					return true;	
				}else{
					//Keine passenden Argumente
					sender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_argumentsDoNotFit"));
					return false;
				}				
			}else{	
			
				sender.sendMessage(ChatColor.RED + plugin.messageFileConfiguration.getString("messages.error.error_onlyPlayers"));
				//Kein Spieler
				return true;
			}
		}	
		return false;
	}
}