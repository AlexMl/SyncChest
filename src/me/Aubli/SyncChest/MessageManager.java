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
	
	//Error messages
	private String no_Permissions;
	private String chest_not_available;
	private String chest_place_error;
	private String chest_remove_error;
	private String hopper_place_error;
	private String hopper_remove_error;
	private String not_a_number;
	
	
	public MessageManager(String language){
		instance = this;
		
		this.language = language;
		
		this.messageFile = new File(SyncChest.getInstance().getDataFolder().getPath() + "/Messages/" + language + "-messages.yml");
		this.messageConfig = YamlConfiguration.loadConfiguration(messageFile);
		
		if(!messageFile.exists()){
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
			
			messageConfig.set("messages.config_saved", "Config saved!");
			messageConfig.set("messages.config_reloaded", "Chests and Hoppers are loaded!");
			messageConfig.set("messages.chest_place", "Chest placed!");
			messageConfig.set("messages.chest_removed", "Chest removed!");
			messageConfig.set("messages.chest_linked", "Connection successfully set!");
			messageConfig.set("messages.chest_unlinked","Connection removed!");
			messageConfig.set("messages.hopper_placed", "Hopper placed!");
			messageConfig.set("messages.hopper_removed", "Hopper removed!");
			messageConfig.set("messages.chest_selected", "Chest selected!");
			
			messageConfig.set("error.no_permissions", "You don't have enough permissions!");
			messageConfig.set("error.chest_not_available", "These Chests are not available!");
			messageConfig.set("error.chest_place_error", "An Error occurred while attempting to place this chest!");
			messageConfig.set("error.chest_remove_error", "An Error occurred while attempting to remove this chest!");
			messageConfig.set("error.hopper_placed_error", "An Error occurred while attempting to place this hopper!");
			messageConfig.set("error.hopper_remove_error", "An Error occurred while attempting to remove this hopper!");
			messageConfig.set("error.not_a_number", "Only Numbers are allowed here!");
			
			messageConfig.save(messageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadMessages(){
		this.config_saved = messageConfig.getString("messages.config_saved");
		this.config_reloaded = messageConfig.getString("messages.config_reloaded");
		this.chest_placed = messageConfig.getString("messages.chest_place");
		this.chest_removed = messageConfig.getString("messages.chest_removed");
		this.chests_linked = messageConfig.getString("messages.chest_linked");
		this.chests_unlinked = messageConfig.getString("messages.chest_unlinked");
		this.hopper_placed = messageConfig.getString("messages.hopper_placed");
		this.hopper_removed = messageConfig.getString("messages.hopper_removed");
		this.chest_selected = messageConfig.getString("messages.chest_selected");
		
		this.no_Permissions = messageConfig.getString("error.no_permissions");
		this.chest_not_available = messageConfig.getString("error.chest_not_available");
		this.chest_place_error = messageConfig.getString("error.chest_place_error");
		this.chest_remove_error = messageConfig.getString("error.chest_remove_error");
		this.hopper_place_error = messageConfig.getString("error.hopper_placed_error");
		this.hopper_remove_error = messageConfig.getString("error.hopper_remove_error");	
		this.not_a_number = messageConfig.getString("error.not_a_number");
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
}