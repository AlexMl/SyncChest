package me.Aubli.SyncChest;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageManager {
	
	private static MessageManager instance;
	
	private String language;
	
	private File messageFile;
	private FileConfiguration messageConfig;

	//Messages
	private String config_saved;
	private String config_reloaded;
	private String chests_linked;
	private String chests_unlinked;
	private String chest_placed;
	private String chest_removed;
	private String hopper_placed;
	private String hopper_removed;
	private String chest_selected;
	private String transaction_success;
	private String current_balance;
	
	//Error messages
	private String no_Permissions;
	private String chest_not_available;
	private String chest_place_error;
	private String chest_remove_error;
	private String hopper_place_error;
	private String hopper_remove_error;
	private String not_a_number;	
	private String not_enough_money;
	private String transaction_error;
	
	public MessageManager(String language){
		instance = this;
		
		this.language = language;
		
		this.messageFile = new File(SyncChest.getInstance().getDataFolder().getPath() + File.separator + "Messages" + File.separator + language + "-messages.yml");
		this.messageConfig = YamlConfiguration.loadConfiguration(messageFile);
		
		if(!messageFile.exists() || isOutdated()){
			try {
				messageFile.createNewFile();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			newMessageFile();
		}
		
		loadMessages();
	}
	
	private void newMessageFile(){
		
		try {
			messageConfig.options().header("This file provides all messages from SyncChest!\n" +
					"Visit http://dev.bukkit.org/bukkit-plugins/syncchest/ to learn how to change languages.");		
			
			messageConfig.set("config.version", SyncChest.getInstance().getDescription().getVersion());
			
			messageConfig.addDefault("messages.config_saved", "Config saved!");
			messageConfig.addDefault("messages.config_reloaded", "Chests and Hoppers are loaded!");
			messageConfig.addDefault("messages.chest_place", "Chest placed!");
			messageConfig.addDefault("messages.chest_removed", "Chest removed!");
			messageConfig.addDefault("messages.chest_linked", "Connection successfully set!");
			messageConfig.addDefault("messages.chest_unlinked","Connection removed!");
			messageConfig.addDefault("messages.hopper_placed", "Hopper placed!");
			messageConfig.addDefault("messages.hopper_removed", "Hopper removed!");
			messageConfig.addDefault("messages.chest_selected", "Chest selected!");
			messageConfig.addDefault("messages.transaction_success", "You bought %s Chests for %s!");
			messageConfig.addDefault("messages.current_balance", "Your balance is now %s!");
			
			messageConfig.addDefault("error.no_permissions", "You don't have enough permissions!");
			messageConfig.addDefault("error.chest_not_available", "These Chests are not available!");
			messageConfig.addDefault("error.chest_place_error", "An Error occurred while attempting to place this chest!");
			messageConfig.addDefault("error.chest_remove_error", "An Error occurred while attempting to remove this chest!");
			messageConfig.addDefault("error.hopper_placed_error", "An Error occurred while attempting to place this hopper!");
			messageConfig.addDefault("error.hopper_remove_error", "An Error occurred while attempting to remove this hopper!");
			messageConfig.addDefault("error.not_a_number", "Only Numbers are allowed here!");
			messageConfig.addDefault("error.not_enough_money", "You do not own enough money!");
			messageConfig.addDefault("error.transaction_error", "An Error occurred while attempting to buy!");
			
			messageConfig.options().copyDefaults(true);
			messageConfig.save(messageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadMessages(){
		this.config_saved = messageConfig.getString("messages.config_saved")==null?"Config saved!":messageConfig.getString("messages.config_saved");
		this.config_reloaded = messageConfig.getString("messages.config_reloaded")==null?"Chests and Hoppers are loaded!":messageConfig.getString("messages.config_reloaded");
		this.chest_placed = messageConfig.getString("messages.chest_place")==null?"Chest placed!":messageConfig.getString("messages.chest_place");
		this.chest_removed = messageConfig.getString("messages.chest_removed")==null?"Chest removed!":messageConfig.getString("messages.chest_removed");
		this.chests_linked = messageConfig.getString("messages.chest_linked")==null?"Connection successfully set!":messageConfig.getString("messages.chest_linked");
		this.chests_unlinked = messageConfig.getString("messages.chest_unlinked")==null?"Connection removed!":messageConfig.getString("messages.chest_unlinked");
		this.hopper_placed = messageConfig.getString("messages.hopper_placed")==null?"Hopper placed!":messageConfig.getString("messages.hopper_placed");
		this.hopper_removed = messageConfig.getString("messages.hopper_removed")==null?"Hopper removed!":messageConfig.getString("messages.hopper_removed");
		this.chest_selected = messageConfig.getString("messages.chest_selected")==null?"Chest selected!":messageConfig.getString("messages.chest_selected");
		this.transaction_success = messageConfig.getString("messages.transaction_success");
		this.current_balance = messageConfig.getString("messages.current_balance");
		
		this.no_Permissions = messageConfig.getString("error.no_permissions")==null?"You don't have enough permissions!":messageConfig.getString("error.no_permissions");
		this.chest_not_available = messageConfig.getString("error.chest_not_available")==null?"These Chests are not available!":messageConfig.getString("error.chest_not_available");
		this.chest_place_error = messageConfig.getString("error.chest_place_error")==null?"An Error occurred while attempting to place this chest!":messageConfig.getString("error.chest_place_error");
		this.chest_remove_error = messageConfig.getString("error.chest_remove_error")==null?"An Error occurred while attempting to remove this chest!":messageConfig.getString("error.chest_remove_error");
		this.hopper_place_error = messageConfig.getString("error.hopper_placed_error")==null?"An Error occurred while attempting to place this hopper!":messageConfig.getString("error.hopper_placed_error");
		this.hopper_remove_error = messageConfig.getString("error.hopper_remove_error")==null?"An Error occurred while attempting to remove this hopper!":messageConfig.getString("error.hopper_remove_error");
		this.not_a_number = messageConfig.getString("error.not_a_number")==null?"Only Numbers are allowed here!":messageConfig.getString("error.not_a_number");
		this.not_enough_money = messageConfig.getString("error.not_enough_money");
		this.transaction_error = messageConfig.getString("error.transaction_error");
	}
	
	private boolean isOutdated() {
		return !(SyncChest.getInstance().getDescription().getVersion().equals(messageConfig.getString("config.version")));
	}
	
	public static MessageManager getManager(){
		return instance;
	}
	
	public String getLang(){
		return language;
	}
	
	public void reload(){
		new MessageManager(SyncChest.getInstance().getConfig().getString("config.settings.language"));
	}
	
	
	public String get_CONFIG_SAVED(){
		return ChatColor.GREEN + config_saved;
	}
	
	public String get_CONFIG_RELOADED(){
		return ChatColor.GREEN + config_reloaded;
	}
	
	public String get_CHEST_PLACED(){
		return ChatColor.GREEN + chest_placed;
	}
	
	public String get_CHEST_REMOVED(){
		return ChatColor.GREEN + chest_removed;
	}
	
	public String get_CHESTS_LINKED(){
		return ChatColor.GREEN + chests_linked;
	}
	
	public String get_CHESTS_UNLINKED(){
		return ChatColor.GREEN + chests_unlinked;
	}
	
	public String get_HOPPER_PLACED(){
		return ChatColor.GREEN + hopper_placed;
	}
	
	public String get_HOPPER_REMOVED(){
		return ChatColor.GREEN + hopper_removed;
	}
	
	public String get_CHEST_SELECTED(){
		return ChatColor.DARK_GRAY + chest_selected;
	}
	
	public String get_TRANSACTION_SUCCESS(){
		return ChatColor.GREEN + transaction_success;
	}
	
	public String get_CURRENT_BALNCE() {
		return ChatColor.DARK_GRAY + current_balance;
	}
	
	public String ERROR_NO_PERMISSIONS(){
		return ChatColor.DARK_RED + no_Permissions;
	}
	
	public String ERROR_CHEST_NOT_AVAILABLE(){
		return ChatColor.RED + chest_not_available;
	}
	
	public String ERROR_CHEST_PLACE(){
		return ChatColor.RED + chest_place_error;
	}
	
	public String ERROR_CHEST_REMOVE(){
		return ChatColor.RED + chest_remove_error;
	}
	
	public String ERROR_HOPPER_PLACE(){
		return ChatColor.RED + hopper_place_error;
	}
	
	public String ERROR_HOPPER_REMOVE(){
		return ChatColor.RED + hopper_remove_error;
	}
	
	public String ERROR_NOT_A_NUMBER(){
		return ChatColor.RED + not_a_number;
	}
	
	public String ERROR_NOT_ENOUGH_MONEY(){
		return ChatColor.RED + not_enough_money;
	}
	
	public String ERROR_TRANSACTION_ERROR(){
		return ChatColor.RED + transaction_error;
	}
}
