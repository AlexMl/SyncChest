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
    
    // Messages
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
    
    // Error messages
    private String no_Permissions;
    private String chest_not_available;
    private String chest_place_error;
    private String chest_remove_error;
    private String hopper_place_error;
    private String hopper_remove_error;
    private String not_a_number;
    private String not_enough_money;
    private String transaction_error;
    
    public MessageManager(String language) {
	instance = this;
	
	this.language = language;
	
	this.messageFile = new File(SyncChest.getInstance().getDataFolder().getPath() + File.separator + "Messages" + File.separator + language + "-messages.yml");
	this.messageConfig = YamlConfiguration.loadConfiguration(this.messageFile);
	
	if (!this.messageFile.exists() || isOutdated()) {
	    try {
		this.messageFile.createNewFile();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    newMessageFile();
	}
	
	loadMessages();
    }
    
    private void newMessageFile() {
	
	try {
	    this.messageConfig.options().header("This file provides all messages from SyncChest!\n" + "Visit http://dev.bukkit.org/bukkit-plugins/syncchest/ to learn how to change languages.");
	    
	    this.messageConfig.set("config.version", SyncChest.getInstance().getDescription().getVersion());
	    
	    this.messageConfig.addDefault("messages.config_saved", "Config saved!");
	    this.messageConfig.addDefault("messages.config_reloaded", "Chests and Hoppers are loaded!");
	    this.messageConfig.addDefault("messages.chest_place", "Chest placed!");
	    this.messageConfig.addDefault("messages.chest_removed", "Chest removed!");
	    this.messageConfig.addDefault("messages.chest_linked", "Connection successfully set!");
	    this.messageConfig.addDefault("messages.chest_unlinked", "Connection removed!");
	    this.messageConfig.addDefault("messages.hopper_placed", "Hopper placed!");
	    this.messageConfig.addDefault("messages.hopper_removed", "Hopper removed!");
	    this.messageConfig.addDefault("messages.chest_selected", "Chest selected!");
	    this.messageConfig.addDefault("messages.transaction_success", "You bought %s Chests for %s!");
	    this.messageConfig.addDefault("messages.current_balance", "Your balance is now %s!");
	    
	    this.messageConfig.addDefault("error.no_permissions", "You don't have enough permissions!");
	    this.messageConfig.addDefault("error.chest_not_available", "These Chests are not available!");
	    this.messageConfig.addDefault("error.chest_place_error", "An Error occurred while attempting to place this chest!");
	    this.messageConfig.addDefault("error.chest_remove_error", "An Error occurred while attempting to remove this chest!");
	    this.messageConfig.addDefault("error.hopper_placed_error", "An Error occurred while attempting to place this hopper!");
	    this.messageConfig.addDefault("error.hopper_remove_error", "An Error occurred while attempting to remove this hopper!");
	    this.messageConfig.addDefault("error.not_a_number", "Only Numbers are allowed here!");
	    this.messageConfig.addDefault("error.not_enough_money", "You do not own enough money!");
	    this.messageConfig.addDefault("error.transaction_error", "An Error occurred while attempting to buy!");
	    
	    this.messageConfig.options().copyDefaults(true);
	    this.messageConfig.save(this.messageFile);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    private void loadMessages() {
	this.config_saved = this.messageConfig.getString("messages.config_saved") == null ? "Config saved!" : this.messageConfig.getString("messages.config_saved");
	this.config_reloaded = this.messageConfig.getString("messages.config_reloaded") == null ? "Chests and Hoppers are loaded!" : this.messageConfig.getString("messages.config_reloaded");
	this.chest_placed = this.messageConfig.getString("messages.chest_place") == null ? "Chest placed!" : this.messageConfig.getString("messages.chest_place");
	this.chest_removed = this.messageConfig.getString("messages.chest_removed") == null ? "Chest removed!" : this.messageConfig.getString("messages.chest_removed");
	this.chests_linked = this.messageConfig.getString("messages.chest_linked") == null ? "Connection successfully set!" : this.messageConfig.getString("messages.chest_linked");
	this.chests_unlinked = this.messageConfig.getString("messages.chest_unlinked") == null ? "Connection removed!" : this.messageConfig.getString("messages.chest_unlinked");
	this.hopper_placed = this.messageConfig.getString("messages.hopper_placed") == null ? "Hopper placed!" : this.messageConfig.getString("messages.hopper_placed");
	this.hopper_removed = this.messageConfig.getString("messages.hopper_removed") == null ? "Hopper removed!" : this.messageConfig.getString("messages.hopper_removed");
	this.chest_selected = this.messageConfig.getString("messages.chest_selected") == null ? "Chest selected!" : this.messageConfig.getString("messages.chest_selected");
	this.transaction_success = this.messageConfig.getString("messages.transaction_success");
	this.current_balance = this.messageConfig.getString("messages.current_balance");
	
	this.no_Permissions = this.messageConfig.getString("error.no_permissions") == null ? "You don't have enough permissions!" : this.messageConfig.getString("error.no_permissions");
	this.chest_not_available = this.messageConfig.getString("error.chest_not_available") == null ? "These Chests are not available!" : this.messageConfig.getString("error.chest_not_available");
	this.chest_place_error = this.messageConfig.getString("error.chest_place_error") == null ? "An Error occurred while attempting to place this chest!" : this.messageConfig.getString("error.chest_place_error");
	this.chest_remove_error = this.messageConfig.getString("error.chest_remove_error") == null ? "An Error occurred while attempting to remove this chest!" : this.messageConfig.getString("error.chest_remove_error");
	this.hopper_place_error = this.messageConfig.getString("error.hopper_placed_error") == null ? "An Error occurred while attempting to place this hopper!" : this.messageConfig.getString("error.hopper_placed_error");
	this.hopper_remove_error = this.messageConfig.getString("error.hopper_remove_error") == null ? "An Error occurred while attempting to remove this hopper!" : this.messageConfig.getString("error.hopper_remove_error");
	this.not_a_number = this.messageConfig.getString("error.not_a_number") == null ? "Only Numbers are allowed here!" : this.messageConfig.getString("error.not_a_number");
	this.not_enough_money = this.messageConfig.getString("error.not_enough_money");
	this.transaction_error = this.messageConfig.getString("error.transaction_error");
    }
    
    private boolean isOutdated() {
	return !(SyncChest.getInstance().getDescription().getVersion().equals(this.messageConfig.getString("config.version")));
    }
    
    public static MessageManager getManager() {
	return instance;
    }
    
    public String getLang() {
	return this.language;
    }
    
    public void reload() {
	new MessageManager(SyncChest.getInstance().getConfig().getString("config.settings.language"));
    }
    
    public String get_CONFIG_SAVED() {
	return ChatColor.GREEN + this.config_saved;
    }
    
    public String get_CONFIG_RELOADED() {
	return ChatColor.GREEN + this.config_reloaded;
    }
    
    public String get_CHEST_PLACED() {
	return ChatColor.GREEN + this.chest_placed;
    }
    
    public String get_CHEST_REMOVED() {
	return ChatColor.GREEN + this.chest_removed;
    }
    
    public String get_CHESTS_LINKED() {
	return ChatColor.GREEN + this.chests_linked;
    }
    
    public String get_CHESTS_UNLINKED() {
	return ChatColor.GREEN + this.chests_unlinked;
    }
    
    public String get_HOPPER_PLACED() {
	return ChatColor.GREEN + this.hopper_placed;
    }
    
    public String get_HOPPER_REMOVED() {
	return ChatColor.GREEN + this.hopper_removed;
    }
    
    public String get_CHEST_SELECTED() {
	return ChatColor.DARK_GRAY + this.chest_selected;
    }
    
    public String get_TRANSACTION_SUCCESS() {
	return ChatColor.GREEN + this.transaction_success;
    }
    
    public String get_CURRENT_BALNCE() {
	return ChatColor.DARK_GRAY + this.current_balance;
    }
    
    public String ERROR_NO_PERMISSIONS() {
	return ChatColor.DARK_RED + this.no_Permissions;
    }
    
    public String ERROR_CHEST_NOT_AVAILABLE() {
	return ChatColor.RED + this.chest_not_available;
    }
    
    public String ERROR_CHEST_PLACE() {
	return ChatColor.RED + this.chest_place_error;
    }
    
    public String ERROR_CHEST_REMOVE() {
	return ChatColor.RED + this.chest_remove_error;
    }
    
    public String ERROR_HOPPER_PLACE() {
	return ChatColor.RED + this.hopper_place_error;
    }
    
    public String ERROR_HOPPER_REMOVE() {
	return ChatColor.RED + this.hopper_remove_error;
    }
    
    public String ERROR_NOT_A_NUMBER() {
	return ChatColor.RED + this.not_a_number;
    }
    
    public String ERROR_NOT_ENOUGH_MONEY() {
	return ChatColor.RED + this.not_enough_money;
    }
    
    public String ERROR_TRANSACTION_ERROR() {
	return ChatColor.RED + this.transaction_error;
    }
}
