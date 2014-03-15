package me.Aubli.SyncChest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import me.Aubli.SyncChest.Listeners.SyncChestBlockBreakListener;
import me.Aubli.SyncChest.Listeners.SyncChestBlockPlaceListener;
import me.Aubli.SyncChest.Listeners.SyncChestInventoryListener;
import me.Aubli.SyncChest.Listeners.SyncChestInventoryMoveListener;
import me.Aubli.SyncChest.Listeners.SyncChestInventoryOpenListener;
import me.Aubli.SyncChest.Listeners.SyncChestPlayerInteractListener;

import org.util.Metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SyncChest extends JavaPlugin {
	
	public static final Logger log = Bukkit.getLogger();
	
	public File chestFile;
	public File messageFile;
	public FileConfiguration chestFileConfiguration;
	public FileConfiguration messageFileConfiguration;
	
	public ItemMeta chestMainMeta, chestRelatedMeta, wandItemMeta;
	public boolean enable;
	public boolean invOpen=false;
	public boolean useMetrics = false;
	
	public Location relatedChestLocation, mainChestLocation;
	public int wandItem;
	
	public String language = "en";
	public String mainChest = "";
	public String relatedChest = "";
	
	public ArrayList<String> mainChests = new ArrayList<String>();
	public ArrayList<String> relatedChests = new ArrayList<String>();
	
	@Override	
	public void onDisable() {
		log.info("[SyncChest] Plugin is disabled!");
	}
	
	@Override
	public void onEnable() {
		chestFile = new File("plugins/SyncChest/chests.yml");
		chestFileConfiguration = YamlConfiguration.loadConfiguration(chestFile);
		
		if(this.getConfig().get("config.settings.language")==null){
			messageFile = new File("plugins/SyncChest/en_messages.yml");
		}else if(this.getConfig().getString("config.settings.language").isEmpty()==false){
			messageFile = new File("plugins/SyncChest/" + this.getConfig().getString("config.settings.language") + "_messages.yml");
		}else{
			messageFile = new File("plugins/SyncChest/en_messages.yml");
		}
		messageFileConfiguration = YamlConfiguration.loadConfiguration(messageFile);
		
		this.getCommand("sctoggle").setExecutor(new CommandExecuter(this));
		this.getCommand("scmain").setExecutor(new CommandExecuter(this));
		this.getCommand("screlated").setExecutor(new CommandExecuter(this));
		this.getCommand("sclink").setExecutor(new CommandExecuter(this));
		this.getCommand("scconnector").setExecutor(new CommandExecuter(this));
		this.getCommand("scdelink").setExecutor(new CommandExecuter(this));
		this.getCommand("screset").setExecutor(new CommandExecuter(this));
		this.getCommand("scclear").setExecutor(new CommandExecuter(this));
		
		for(int i=0;i<Bukkit.getOnlinePlayers().length;i++){
			clearSCInventory(Bukkit.getOnlinePlayers()[i]);
		}
		
		loadConfig();
		registerEvents();
		
		if(this.getConfig().getBoolean("config.settings.enable")==true){		
			reloadHoppers(null, false);
		}
		
		if(useMetrics==true){
			try {
			    Metrics metrics = new Metrics(this);
			    metrics.start();			   
			} catch (IOException e) {
			   log.info("[SyncChest] Can't start Metrics! Skip!");
			}
		}
		
		log.info("[SyncChest] Plugin is enabled!");
	}
	
	public void toggle(CommandSender sender){
		if(this.getConfig().getBoolean("config.settings.enable")==true){
			
			this.getConfig().set("config.settings.enable", false);
			this.enable=false;
			sender.sendMessage(ChatColor.AQUA + messageFileConfiguration.getString("messages.messages.toggleOff"));
			
		}else if(this.getConfig().getBoolean("config.settings.enable")==false){
			
			this.getConfig().set("config.settings.enable", true);
			this.enable=true;		
			reloadHoppers(null, false);
			sender.sendMessage(ChatColor.AQUA + messageFileConfiguration.getString("messages.messages.toggleOn"));
		}
		
		this.saveConfig();		
	}	
		
	public void setMain(Player playerSender, int anzahl){
		
		ItemStack chest = new ItemStack(Material.CHEST, anzahl);
		chestMainMeta = chest.getItemMeta();
		
		chestMainMeta.setDisplayName("MainChest");
		
		ArrayList<String> Chestlore = new ArrayList<String>();
		Chestlore.add(ChatColor.GREEN + "MainChests must have a RelatedChest!");
		Chestlore.add(ChatColor.GREEN + "Connect this chests with the Connector!");
		chestMainMeta.setLore(Chestlore);
		
		chest.setItemMeta(chestMainMeta);
		
		playerSender.getInventory().addItem(chest);
		playerSender.updateInventory();		
	}
	
	public void setRelated(Player playerSender){
		
		this.reloadConfig();
		try {
			chestFileConfiguration.load(chestFile);
		} catch (FileNotFoundException e) {try {
		chestFile.createNewFile();
		} catch (IOException e1) {e1.printStackTrace();}
		} catch (IOException e) {e.printStackTrace();
		} catch (InvalidConfigurationException e) {e.printStackTrace();}
	

		int anzahl = 0;
		
		for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
			if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
				anzahl++;
				if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==true){
					anzahl++;
				}
			}
		}	
		
		if(anzahl==0){
			//keine MainChests
			playerSender.sendMessage(ChatColor.RED + messageFileConfiguration.getString("messages.error.error_noMainChests"));
		}else{		
			ArrayList<String> relatedChestlore = new ArrayList<String>();
			relatedChestlore.add(ChatColor.GREEN + "RelatedChests need a MainChest!");
			relatedChestlore.add(ChatColor.GREEN + "Connect this chests with the Connector!");
			
			ItemStack chest = new ItemStack(Material.CHEST, anzahl);
			chestRelatedMeta = chest.getItemMeta();
			
			chestRelatedMeta.setDisplayName("RelatedChest");
			chestRelatedMeta.setLore(relatedChestlore);
			chest.setItemMeta(chestRelatedMeta);
			
			playerSender.getInventory().addItem(chest);
			playerSender.updateInventory();		
		}
	}
	
	public void link(Player playerSender, String relatedChest, String mainChest){
	
		if(enable==true){
			if(playerSender.hasPermission("sc.link")){
				this.reloadConfig();
				try {
					chestFileConfiguration.load(chestFile);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InvalidConfigurationException e1) {
					e1.printStackTrace();
				}
				
				//playerSender.sendMessage(ChatColor.DARK_BLUE + "//" + ChatColor.DARK_GRAY + "DEBUG   " + ChatColor.RESET + mainChest + " : " + relatedChest);
				
				String connectionSetMessage = messageFileConfiguration.getString("messages.messages.connectionSet");
				String[] connectionSetArray = connectionSetMessage.split("<mainChest>");
				connectionSetMessage = ChatColor.GREEN + connectionSetArray[0] + ChatColor.GOLD + mainChest + ChatColor.GREEN + connectionSetArray[1].split("<relatedChest>")[0] + ChatColor.DARK_PURPLE + relatedChest + ChatColor.GREEN + connectionSetArray[1].split("<relatedChest>")[1];
				
				int mainChestIndex = 0;
				int relatedChestIndex = 0;	
				
				for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
					if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
						if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==true){
							if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".name").equalsIgnoreCase(mainChest)){
								mainChestIndex = i;
								break;
							}
							if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".coChest.name").equalsIgnoreCase(mainChest)){
								mainChestIndex = i;
								break;
							}
						}else if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==false){
							if(chestFileConfiguration.getString("config.mem.mainChests." + i + ".name").equalsIgnoreCase(mainChest)){
								mainChestIndex = i;
								break;
							}
						}			
					}
				}		
				
				Location relatedChestLoc = null;
				Boolean doubleChest = false;
				
				for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
					if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
						if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
							if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name").equalsIgnoreCase(relatedChest)){
								relatedChestIndex = i;
								relatedChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world")), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z"));
								doubleChest = true;
								break;
							}
							if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".coChest.name").equalsIgnoreCase(relatedChest)){
								relatedChestIndex = i;
								relatedChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world")), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z"));
								doubleChest = true;
								break;
							}
						}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
							if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".name").equalsIgnoreCase(relatedChest)){
								relatedChestIndex = i;
								relatedChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world")), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z"));
								doubleChest = false;
								break;
							}
						}			
					}
				}
				
				if(chestFileConfiguration.get("config.mem.mainChests." + mainChestIndex + ".X")!=null&&chestFileConfiguration.get("config.mem.relatedChests." + relatedChestIndex + ".X")!=null){
					if((chestFileConfiguration.getBoolean("config.mem.mainChests." + mainChestIndex + ".doubleChest")==true&&chestFileConfiguration.getBoolean("config.mem.relatedChests." + relatedChestIndex + ".doubleChest")==true)||(chestFileConfiguration.getBoolean("config.mem.mainChests." + mainChestIndex + ".doubleChest")==false&&chestFileConfiguration.getBoolean("config.mem.relatedChests." + relatedChestIndex + ".doubleChest")==false)){
						if(chestFileConfiguration.getString("config.mem.mainChests." + mainChestIndex + ".linkedTo").equalsIgnoreCase("")){
							chestFileConfiguration.set("config.mem.mainChests." + mainChestIndex + ".linkedTo", relatedChest);
							chestFileConfiguration.set("config.mem.relatedChests." + relatedChestIndex + ".linkedTo", mainChest);
						}else{
							chestFileConfiguration.set("config.mem.mainChests." + mainChestIndex + ".linkedTo", chestFileConfiguration.getString("config.mem.mainChests." + mainChestIndex + ".linkedTo") + ", " + chestFileConfiguration.getString("config.mem.relatedChests." + relatedChestIndex + ".name"));
							chestFileConfiguration.set("config.mem.relatedChests." + relatedChestIndex + ".linkedTo", mainChest);
						}
						try {
							chestFileConfiguration.save(chestFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						reloadHoppers(relatedChestLoc, doubleChest);						
							
						//Verbindung gesetzt
						playerSender.sendMessage(connectionSetMessage);
					}else{
						//Kisten passen nicht zusammen
						playerSender.sendMessage(ChatColor.RED + messageFileConfiguration.getString("messages.error.error_chestsNotCompatible"));
					}
				}else{
					//Kisten existieren nicht!
					playerSender.sendMessage(ChatColor.RED + messageFileConfiguration.getString("messages.error.error_chestNotExist"));
				}	
			}else{
				//Keine Permissions
				playerSender.sendMessage(ChatColor.DARK_RED + messageFileConfiguration.getString("messages.error.error_permissionDenieded"));
			}
		}else{
			//Plugin deaktiviert
			playerSender.sendMessage(ChatColor.RED + messageFileConfiguration.getString("messages.error.error_pluginIsDisabled"));
		}
	}	
	
	public void giveConnectorItem(Player playerSender){		
		ItemStack wandItemStack = new ItemStack(this.getConfig().getInt("config.settings.wandItem"));		
		wandItemMeta = wandItemStack.getItemMeta();
		
		wandItemMeta.setDisplayName("Connector");
		
		ArrayList<String> lore = new ArrayList<String>();
		
		lore.add(ChatColor.UNDERLINE + "The Connector:");
		lore.add(ChatColor.GREEN + "Connect a Mainchest with a Relatedchest!");
		lore.add(ChatColor.BLUE + "Right-Click: Info");
		lore.add(ChatColor.BLUE + "Left-Click: Connect");
		wandItemMeta.setLore(lore);
		
		wandItemStack.setItemMeta(wandItemMeta);
		
		for(int i=0;i<playerSender.getInventory().getContents().length;i++){
			if(playerSender.getInventory().getContents()[i]!=null){
				if(playerSender.getInventory().getContents()[i].getItemMeta().equals(wandItemMeta)){
					wandItemStack.setAmount(playerSender.getInventory().getContents()[i].getAmount());
					playerSender.getInventory().remove(wandItemStack);				
				}	
			}
		}		
		
		playerSender.getInventory().addItem(wandItemStack);	
		playerSender.updateInventory();
	}
	
	public void reloadHoppers(Location chestLocation, boolean doubleChest){		
		
		if(chestLocation==null){//Alle hopper werden neu gesetzt
			//Bukkit.broadcastMessage("Anfang");
			
			Location chestLoc = new Location(null, 0, 0, 0);
			Location hopperLocation = new Location(null, 0, 0, 0);
			ArrayList<Location> usedHoppers = new ArrayList<Location>();
			
			
			for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
				if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
					if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==false){
						chestLoc.setWorld(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world")));
						chestLoc.setX(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X"));
						chestLoc.setY(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y"));
						chestLoc.setZ(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z"));
						hopperLocation = searchHoppers(chestLoc, false);
						if(hopperLocation!=null){
							if(usedHoppers.contains(hopperLocation)){
								break;
							}else{
								reloadHoppers(hopperLocation);
								usedHoppers.add(hopperLocation);
								break;
							}
						}
					}else if(chestFileConfiguration.getBoolean("config.mem.mainChests." + i + ".doubleChest")==true){
						chestLoc.setWorld(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world")));
						chestLoc.setX(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.X"));
						chestLoc.setY(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Y"));
						chestLoc.setZ(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".coChest.Z"));
						hopperLocation = searchHoppers(chestLoc, false);
						if(hopperLocation!=null){
							if(usedHoppers.contains(hopperLocation)){
								break;
							}else{
								reloadHoppers(hopperLocation);
								usedHoppers.add(hopperLocation);
								break;
							}
						}
						
						chestLoc.setWorld(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + i + ".world")));
						chestLoc.setX(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".X"));
						chestLoc.setY(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Y"));
						chestLoc.setZ(chestFileConfiguration.getInt("config.mem.mainChests." + i + ".Z"));
						hopperLocation = searchHoppers(chestLoc, false);
						if(hopperLocation!=null){
							if(usedHoppers.contains(hopperLocation)){
								break;
							}else{
								reloadHoppers(hopperLocation);
								usedHoppers.add(hopperLocation);
								break;
							}
						}
					}
				}
			}
			
			for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
				if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
					if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
						chestLoc.setWorld(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world")));
						chestLoc.setX(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X"));
						chestLoc.setY(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y"));
						chestLoc.setZ(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z"));
						hopperLocation = searchHoppers(chestLoc, false);
						if(hopperLocation!=null){
							if(usedHoppers.contains(hopperLocation)){
								break;
							}else{
								reloadHoppers(hopperLocation);
								usedHoppers.add(hopperLocation);
								break;
							}
						}
					}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
						chestLoc.setWorld(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world")));
						chestLoc.setX(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X"));
						chestLoc.setY(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y"));
						chestLoc.setZ(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z"));
						hopperLocation = searchHoppers(chestLoc, false);
						if(hopperLocation!=null){
							if(usedHoppers.contains(hopperLocation)){
								break;
							}else{
								reloadHoppers(hopperLocation);
								usedHoppers.add(hopperLocation);
								break;
							}
						}
						
						chestLoc.setWorld(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world")));
						chestLoc.setX(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X"));
						chestLoc.setY(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y"));
						chestLoc.setZ(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z"));
						hopperLocation = searchHoppers(chestLoc, false);
						if(hopperLocation!=null){
							if(usedHoppers.contains(hopperLocation)){
								break;
							}else{
								reloadHoppers(hopperLocation);
								usedHoppers.add(hopperLocation);								
								break;
							}
						}
					}
				}
			}			
		//	Bukkit.broadcastMessage("Ende");			
			return;
		}			
		
	//	Bukkit.broadcastMessage(ChatColor.BLUE + "wird aktualisiert!");
		Location hopperLocation  = searchHoppers(chestLocation, doubleChest);
		if(hopperLocation!=null){
			reloadHoppers(hopperLocation);
	//		Bukkit.broadcastMessage(ChatColor.BLUE + "aktualisiert!");	
		}else{
//			Bukkit.broadcastMessage("nicht aktualisiert weil keine kiste mit hopper gefunden!");
			return;
		}
	}
	
	private Location searchHoppers(Location chestLocation, boolean doubleChest){		
		
		if(chestLocation!=null){
			
			if(Bukkit.getWorlds().contains(chestLocation.getWorld())){			
				if(doubleChest==true){
					if(chestLocation.clone().add(0, 1, 0).getBlock().getType().equals(Material.HOPPER)){				
						return chestLocation.clone().add(0, 1, 0);
					}
					if(chestLocation.clone().add(1, 1, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().add(1, 1, 0);
					}
					if(chestLocation.clone().add(0, 1, 1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().add(0, 1, 1);
					}
					if(chestLocation.clone().add(-1, 1, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().add(-1, 1, 0);
					}
					if(chestLocation.clone().add(0, 1, -1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().add(0, 1, -1);
					}
					
					
					if(chestLocation.clone().subtract(0, 1, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 1, 0);
					}
					if(chestLocation.clone().subtract(1, 1, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(1, 1, 0);
					}
					if(chestLocation.clone().subtract(0, 1, 1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 1, 1);
					}
					if(chestLocation.clone().subtract(-1, 1, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(-1, 1, 0);
					}
					if(chestLocation.clone().subtract(0, 1, -1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 1, -1);
					}
					
		
					if(chestLocation.clone().subtract(1, 0, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(1, 0, 0);
					}
					if(chestLocation.clone().subtract(0, 0, 1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 0, 1);
					}
					if(chestLocation.clone().subtract(-1, 0, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(-1, 0, 0);
					}
					if(chestLocation.clone().subtract(0, 0, -1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 0, -1);
					}
		
					if(chestLocation.clone().subtract(2, 0, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(2, 0, 0);
					}
					if(chestLocation.clone().subtract(0, 0, 2).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 0, 2);
					}
					if(chestLocation.clone().subtract(-2, 0, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(-2, 0, 0);
					}
					if(chestLocation.clone().subtract(0, 0, -2).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 0, -2);
					}								
				}else if(doubleChest==false){
					if(chestLocation.clone().add(0, 1, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().add(0, 1, 0);
					}
					if(chestLocation.clone().subtract(0, 1, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 1, 0);
					}
					
				
					if(chestLocation.clone().subtract(0, 0, 1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 0, 1);
					}
					if(chestLocation.clone().subtract(-1, 0, 0).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(-1, 0, 0);
					}
					if(chestLocation.clone().subtract(0, 0, -1).getBlock().getType().equals(Material.HOPPER)){
						return chestLocation.clone().subtract(0, 0, -1);
					}
				}
			}else{
				return null;
			}
		}		
		return null;
	}
	
	private void reloadHoppers(Location hopperLocation){
		
		if(hopperLocation!=null){					
			Hopper hopper = (Hopper) hopperLocation.getBlock().getState();

			int anzahl = 0;
			for(int j=0;j<hopper.getInventory().getSize();j++){				
				if(hopper.getInventory().getItem(j)!=null){
					anzahl++;
				}
			}
			
			if(anzahl!=0){
				return;
			}
			
			if(hopper.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
				Location relatedChestLoc = hopperLocation.clone();
				relatedChestLoc.add(0, 1, 0);
				if(relatedChestLoc.getBlock().getState() instanceof Chest){
					Chest relatedChest = (Chest)relatedChestLoc.getBlock().getState();										
					if(relatedChest.getInventory().getTitle().contains("RelatedChest")){
						
						for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
							if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
								if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==false){
									if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
												if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
														String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
														
														for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
															if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
																if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name").equals(linkedChest)){
																	Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + k + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Z"));
																	Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																	relatedChest.getInventory().clear();
																	
																	for(int y=0;y<mainChest.getInventory().getSize();y++){
																		if(mainChest.getInventory().getItem(y)!=null){
	//																		Bukkit.broadcastMessage(mainChest.getInventory().getItem(y).toString() + ", " + mainChest.getInventory().getItem(y).getDurability() + " : " + y + " BlockPlace");
																			ItemStack item = new ItemStack(mainChest.getInventory().getItem(y).getType(), 1);
																			if(mainChest.getInventory().getItem(y).getDurability()!=0){
																				item.setDurability(mainChest.getInventory().getItem(y).getDurability());
	//																			Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																			}
																			if(mainChest.getInventory().getItem(y).getEnchantments()!=null){
																				item.addUnsafeEnchantments(mainChest.getInventory().getItem(y).getEnchantments());
	//																			Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																			}
																			hopper.getInventory().addItem(item);
																			
																			if(hopper.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																				Chest relChest = (Chest)hopper.getLocation().subtract(0, 1, 0).getBlock().getState();
																				if(relChest.getInventory().getTitle().contains("RelatedChest")){
																					for(int relDex=1;chestFileConfiguration.get("config.mem.relatedChests." + relDex)!=null;relDex++){
																						if(chestFileConfiguration.get("config.mem.relatedChests." + relDex + ".X")!=null){
																							if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + relDex + ".doubleChest")==false){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
		//																											Bukkit.broadcastMessage("1");
																												}
																											}
																										}
																									}
																								}
																							}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + relDex + ".doubleChest")==true){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																												mainChest.getInventory().removeItem(item);
	//																											Bukkit.broadcastMessage("2");
																											}
																											}
																										}
																									}
																								}
																								
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
																												//	Bukkit.broadcastMessage("3");
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}		
																				}
																			}
																			
																		//	Bukkit.broadcastMessage("Transfer eingeleitet!");
																			return;
																		}																	
																	}
																}
															}
														}												
													}
												}
											}
										}
									}
								}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + i + ".doubleChest")==true){
									if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X")==relatedChestLoc.getBlockX()){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y")==relatedChestLoc.getBlockY()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z")==relatedChestLoc.getBlockZ()){
												if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
														String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
														
														for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
															if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
																if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name").equals(linkedChest)){
																	Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + k + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Z"));
																	Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																	relatedChest.getInventory().clear();
																	
																	for(int y=0;y<mainChest.getInventory().getSize();y++){
																		if(mainChest.getInventory().getItem(y)!=null){
																			//Bukkit.broadcastMessage(mainChest.getInventory().getItem(y).toString() + ", " + mainChest.getInventory().getItem(y).getDurability() + " : " + y + " BlockPlace");
																			ItemStack item = new ItemStack(mainChest.getInventory().getItem(y).getType(), 1);
																			if(mainChest.getInventory().getItem(y).getDurability()!=0){
																				item.setDurability(mainChest.getInventory().getItem(y).getDurability());
																		//		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																			}
																			if(mainChest.getInventory().getItem(y).getEnchantments()!=null){
																				item.addUnsafeEnchantments(mainChest.getInventory().getItem(y).getEnchantments());
																			//	Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																			}
																			hopper.getInventory().addItem(item);
																			
																			if(hopper.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																				Chest relChest = (Chest)hopper.getLocation().subtract(0, 1, 0).getBlock().getState();
																				if(relChest.getInventory().getTitle().contains("RelatedChest")){
																					for(int relDex=1;chestFileConfiguration.get("config.mem.relatedChests." + relDex)!=null;relDex++){
																						if(chestFileConfiguration.get("config.mem.relatedChests." + relDex + ".X")!=null){
																							if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + relDex + ".doubleChest")==false){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){	
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
																													//	Bukkit.broadcastMessage("4");
																												}
																											}
																										}
																									}
																								}
																							}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + relDex + ".doubleChest")==true){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
																													//		Bukkit.broadcastMessage("5");
																												}
																											}
																										}
																									}
																								}
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){	
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
																													//		Bukkit.broadcastMessage("6");
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}		
																				}
																			}
																			
																			//		Bukkit.broadcastMessage("Transfer eingeleitet!");
																			return;
																		}																	
																	}
																}
															}
														}												
													}
												}
											}
										}
									}
									
									if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.X")==relatedChestLoc.getBlockX()){
										if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Y")==relatedChestLoc.getBlockY()){
											if(chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".coChest.Z")==relatedChestLoc.getBlockZ()){
												if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world").equals(relatedChestLoc.getWorld().getName())){
													if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=null){
														String linkedChest = chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo");
														
														for(int k=1;chestFileConfiguration.get("config.mem.mainChests." + k)!=null;k++){
															if(chestFileConfiguration.get("config.mem.mainChests." + k + ".X")!=null){
																if(chestFileConfiguration.getString("config.mem.mainChests." + k + ".name").equals(linkedChest)){
																	Location mainChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.mainChests." + k + ".world")), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".X"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Y"), chestFileConfiguration.getInt("config.mem.mainChests." + k + ".Z"));
																	Chest mainChest = (Chest)mainChestLoc.getBlock().getState();
																	relatedChest.getInventory().clear();
																	
																	for(int y=0;y<mainChest.getInventory().getSize();y++){
																		if(mainChest.getInventory().getItem(y)!=null){
																			//		Bukkit.broadcastMessage(mainChest.getInventory().getItem(y).toString() + ", " + mainChest.getInventory().getItem(y).getDurability() + " : " + y + " BlockPlace");
																			ItemStack item = new ItemStack(mainChest.getInventory().getItem(y).getType(), 1);
																			if(mainChest.getInventory().getItem(y).getDurability()!=0){
																				item.setDurability(mainChest.getInventory().getItem(y).getDurability());
																				//		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Durability");
																			}
																			if(mainChest.getInventory().getItem(y).getEnchantments()!=null){
																				item.addUnsafeEnchantments(mainChest.getInventory().getItem(y).getEnchantments());
																				//		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + item.toString() + " Hat Enchantment");
																			}
																			hopper.getInventory().addItem(item);
																			
																			if(hopper.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.CHEST)){
																				Chest relChest = (Chest)hopper.getLocation().subtract(0, 1, 0).getBlock().getState();
																				if(relChest.getInventory().getTitle().contains("RelatedChest")){
																					for(int relDex=1;chestFileConfiguration.get("config.mem.relatedChests." + relDex)!=null;relDex++){
																						if(chestFileConfiguration.get("config.mem.relatedChests." + relDex + ".X")!=null){
																							if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + relDex + ".doubleChest")==false){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){	
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
																													break;
																												}
																											}
																										}
																									}
																								}
																							}else if(chestFileConfiguration.getBoolean("config.mem.relatedChests." + relDex + ".doubleChest")==true){
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".coChest.Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){	
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
																													break;
																												}
																											}
																										}
																									}
																								}
																								if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".X")==relChest.getLocation().getBlockX()){
																									if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Y")==relChest.getLocation().getBlockY()){
																										if(chestFileConfiguration.getInt("config.mem.relatedChests." + relDex + ".Z")==relChest.getLocation().getBlockZ()){
																											if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".world").equals(relChest.getWorld().getName())){	
																												if(chestFileConfiguration.getString("config.mem.relatedChests." + relDex + ".linkedTo")!=null){
																													mainChest.getInventory().removeItem(item);
																													break;
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}		
																				}
																			}																	
																			//	Bukkit.broadcastMessage("Transfer eingeleitet!");
																			return;
																		}																	
																	}
																}
															}
														}												
													}
												}
											}
										}
									}
								}
							}
						}
					}							
				}					
			}
		}
	}
	
	public boolean isChest(String chestType, Location chestLocation){
		
		String path = "";
		
		if(chestType.equals("mainchest")){
			path = "config.mem.mainChests.";
		}else if(chestType.equals("relatedchest")){
			path = "config.mem.relatedChests.";
		}		
		
		for(int y=1;chestFileConfiguration.get(path + y)!=null;y++){
			if(chestFileConfiguration.get(path + y + ".X")!=null){
				if(chestFileConfiguration.getBoolean(path + y + ".doubleChest")==false){
					if(chestFileConfiguration.getInt(path + y + ".X")==(chestLocation.getBlockX())){ 
						if(chestFileConfiguration.getInt(path + y + ".Y")==(chestLocation.getBlockY())){
							if(chestFileConfiguration.getInt(path + y + ".Z")==(chestLocation.getBlockZ())){
								if(chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
									return true;
								}
							}
						}
					}					
				}else if(chestFileConfiguration.getBoolean(path + y + ".doubleChest")==true){
					if(chestLocation.getBlockX()==chestFileConfiguration.getInt(path + y + ".X")
						&&chestLocation.getBlockZ()==chestFileConfiguration.getInt(path + y + ".Z")
						&&chestLocation.getBlockY()==chestFileConfiguration.getInt(path + y + ".Y")
						&&chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
						return true;
					}
					if(chestLocation.getBlockX()==chestFileConfiguration.getInt(path + y + ".coChest.X")
							&&chestLocation.getBlockZ()==chestFileConfiguration.getInt(path + y + ".coChest.Z")
							&&chestLocation.getBlockY()==chestFileConfiguration.getInt(path + y + ".coChest.Y")
							&&chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
							return true;
					}
				}
			}
		}					
					
		return false;
	}
	
	public String getChestPath(String chestType, Location chestLocation){
		
		String path = "";
		
		if(chestType.equals("mainchest")){
			path = "config.mem.mainChests.";
		}else if(chestType.equals("relatedchest")){
			path = "config.mem.relatedChests.";
		}	
		
		for(int y=1;chestFileConfiguration.get(path + y)!=null;y++){
			if(chestFileConfiguration.get(path + y + ".X")!=null){
				if(chestFileConfiguration.getBoolean(path + y + ".doubleChest")==false){
					if(chestFileConfiguration.getInt(path + y + ".X")==(chestLocation.getBlockX())){ 
						if(chestFileConfiguration.getInt(path + y + ".Y")==(chestLocation.getBlockY())){
							if(chestFileConfiguration.getInt(path + y + ".Z")==(chestLocation.getBlockZ())){
								if(chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
									return path + y;
								}
							}
						}
					}					
				}else if(chestFileConfiguration.getBoolean(path + y + ".doubleChest")==true){
					if(chestLocation.getBlockX()==chestFileConfiguration.getInt(path + y + ".X")
						&&chestLocation.getBlockZ()==chestFileConfiguration.getInt(path + y + ".Z")
						&&chestLocation.getBlockY()==chestFileConfiguration.getInt(path + y + ".Y")
						&&chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
						return path + y;
					}
					
					if(chestLocation.getBlockX()==chestFileConfiguration.getInt(path + y + ".coChest.X")
						&&chestLocation.getBlockZ()==chestFileConfiguration.getInt(path + y + ".coChest.Z")
						&&chestLocation.getBlockY()==chestFileConfiguration.getInt(path + y + ".coChest.Y")
						&&chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
						return path + y + ".coChest";
					}
				}
			}
		}		
		return "";
	}
	
	public boolean isDoubleChest(String chestType, Location chestLocation){
		String path = "";
		
		if(chestType.equals("mainchest")){
			path = "config.mem.mainChests.";
		}else if(chestType.equals("relatedchest")){
			path = "config.mem.relatedChests.";
		}		
		
		for(int y=1;chestFileConfiguration.get(path + y)!=null;y++){
			if(chestFileConfiguration.get(path + y + ".X")!=null){
				if(chestFileConfiguration.getBoolean(path + y + ".doubleChest")==false){
					if(chestFileConfiguration.getInt(path + y + ".X")==(chestLocation.getBlockX())){ 
						if(chestFileConfiguration.getInt(path + y + ".Y")==(chestLocation.getBlockY())){
							if(chestFileConfiguration.getInt(path + y + ".Z")==(chestLocation.getBlockZ())){
								if(chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
									return false;
								}
							}
						}
					}					
				}else if(chestFileConfiguration.getBoolean(path + y + ".doubleChest")==true){
					if(chestLocation.getBlockX()==chestFileConfiguration.getInt(path + y + ".X")
						&&chestLocation.getBlockZ()==chestFileConfiguration.getInt(path + y + ".Z")
						&&chestLocation.getBlockY()==chestFileConfiguration.getInt(path + y + ".Y")
						&&chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
						return true;
					}
					if(chestLocation.getBlockX()==chestFileConfiguration.getInt(path + y + ".coChest.X")
						&&chestLocation.getBlockZ()==chestFileConfiguration.getInt(path + y + ".coChest.Z")
						&&chestLocation.getBlockY()==chestFileConfiguration.getInt(path + y + ".coChest.Y")
						&&chestFileConfiguration.getString(path + y + ".world").equals(chestLocation.getWorld().getName())){
						return true;
					}
				}
			}
		}					
					
		
		return false;
	}
	
	public void deLink(Player playerSender){
		
		this.reloadConfig();
		try {
			chestFileConfiguration.load(chestFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InvalidConfigurationException e1) {
			e1.printStackTrace();
		}
		
		//Verlinkte related inventory löschen
		for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
			if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
				if(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".linkedTo")!=""){
					Location relatedChestLoc = new Location(Bukkit.getWorld(chestFileConfiguration.getString("config.mem.relatedChests." + i + ".world")), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".X"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Y"), chestFileConfiguration.getInt("config.mem.relatedChests." + i + ".Z"));
					if(Bukkit.getWorlds().contains(relatedChestLoc.getWorld())){
						Chest relatedChest = (Chest)relatedChestLoc.getBlock().getState();
						relatedChest.getInventory().clear();
					}
				}
			}
		}
		
		//Links löschen
		for(int i=1;chestFileConfiguration.get("config.mem.mainChests." + i)!=null;i++){
			if(chestFileConfiguration.get("config.mem.mainChests." + i + ".X")!=null){
				chestFileConfiguration.set("config.mem.mainChests." + i + ".linkedTo", "");
			}
		}
		for(int i=1;chestFileConfiguration.get("config.mem.relatedChests." + i)!=null;i++){
			if(chestFileConfiguration.get("config.mem.relatedChests." + i + ".X")!=null){
				chestFileConfiguration.set("config.mem.relatedChests." + i + ".linkedTo", "");
			}
		}
		
		try{
			chestFileConfiguration.save(chestFile);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		//Fertig ungelinkt
		playerSender.sendMessage(ChatColor.GREEN + messageFileConfiguration.getString("messages.messages.deLinked"));
	}
	
	public void reset(Player playerSender){
		
		chestFile.delete();
		try {
			chestFile.createNewFile();
			chestFileConfiguration.options().header("This file is the brain of SyncChest. Don't touch it!!\nElse SyncChest will forget your Chests!");
		//	chestFileConfiguration.save(chestFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//erfolgreich gelöscht
		playerSender.sendMessage(ChatColor.GREEN + messageFileConfiguration.getString("messages.messages.reset"));
	}
	
	public void clearSCInventory(Player playerSender){

		for(int i=0;i<playerSender.getInventory().getContents().length;i++){
			
			if(playerSender.getInventory().getContents()[i]!=null){
				if(playerSender.getInventory().getContents()[i].getItemMeta().hasDisplayName()){
					if(playerSender.getInventory().getContents()[i].getItemMeta().getDisplayName().contains("MainChest")){
						playerSender.getInventory().remove(playerSender.getInventory().getContents()[i]);
					}
				}
			}
			if(playerSender.getInventory().getContents()[i]!=null){
				if(playerSender.getInventory().getContents()[i].getItemMeta().hasDisplayName()){
					if(playerSender.getInventory().getContents()[i].getItemMeta().getDisplayName().contains("RelatedChest")){
						playerSender.getInventory().remove(playerSender.getInventory().getContents()[i]);
					}
				}
			}
			if(playerSender.getInventory().getContents()[i]!=null){
				if(playerSender.getInventory().getContents()[i].getItemMeta().hasDisplayName()){
					if(playerSender.getInventory().getContents()[i].getItemMeta().getDisplayName().contains("Connector")){
						playerSender.getInventory().remove(playerSender.getInventory().getContents()[i]);
					}		
				}
			}
		}
		playerSender.updateInventory();		
		}
	
	private void loadConfig(){
	
		this.getConfig().addDefault("config.settings.enable", true);
		this.getConfig().addDefault("config.settings.enableMetrics", true);
		this.getConfig().addDefault("config.settings.language", "en");
		this.getConfig().addDefault("config.settings.wandItem", 369);
		
		useMetrics = this.getConfig().getBoolean("config.settings.enableMetrics");
		enable = this.getConfig().getBoolean("config.settings.enable");
		wandItem = this.getConfig().getInt("config.settings.wandItem");
		
		messageFileConfiguration.addDefault("messages.messages.singleCreated", "Single chest created!");
		messageFileConfiguration.addDefault("messages.messages.largeCreated", "Large chest created!");
		messageFileConfiguration.addDefault("messages.messages.chestRemoved", " removed from config!");
		messageFileConfiguration.addDefault("messages.messages.chestSelected", " selected!");
		messageFileConfiguration.addDefault("messages.messages.connectionSet", "Connection between <mainChest> and <relatedChest> successfull set!");
		messageFileConfiguration.addDefault("messages.messages.toggleOn","SyncChest is now enabled!");
		messageFileConfiguration.addDefault("messages.messages.toggleOff","SyncChest is now disabled!");
		messageFileConfiguration.addDefault("messages.messages.chestUnLinked","Link was removed from this Chest!");
		messageFileConfiguration.addDefault("messages.messages.reset", "All Chests removed!");
		messageFileConfiguration.addDefault("messages.messages.deLinked", "All links removed!");
		
		messageFileConfiguration.addDefault("messages.error.error_permissionDenieded", "You don't have permissions for that!");
		messageFileConfiguration.addDefault("messages.error.error_onlyPlayers", "Only Players are permitted for this command!");
		messageFileConfiguration.addDefault("messages.error.error_noMainChests","No MainChests available! Create one first!");
		messageFileConfiguration.addDefault("messages.error.error_chestNotExist", "Chest haven't any config entrys!");
		messageFileConfiguration.addDefault("messages.error.error_chestsNotCompatible", "Linked chests must have the same size!");
		messageFileConfiguration.addDefault("messages.error.error_pluginIsDisabled", "SyncChest is disabled!");
		messageFileConfiguration.addDefault("messages.error.error_argumentsDoNotFit", "Your arguments don't fit!");
		messageFileConfiguration.addDefault("messages.error.error_onlyNumbers", "Only numbers are permitted!");		
		
		chestFileConfiguration.options().header("This file is the brain of SyncChest. Don't touch it!!\nElse SyncChest will forget your chests and links!");
		messageFileConfiguration.options().header("This file provides all Output from SyncChest!\n" +
				"Visit http://dev.bukkit.org/bukkit-plugins/syncchest/ to learn how to change languages.");
		
		try {
			chestFileConfiguration.save(chestFile);
			messageFileConfiguration.options().copyDefaults(true);
			messageFileConfiguration.save(messageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();		
	}
	
	private void registerEvents(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new SyncChestBlockBreakListener(this), this);
		pm.registerEvents(new SyncChestBlockPlaceListener(this), this);
		pm.registerEvents(new SyncChestPlayerInteractListener(this), this);
		pm.registerEvents(new SyncChestInventoryOpenListener(this), this);
		pm.registerEvents(new SyncChestInventoryMoveListener(this), this);
		pm.registerEvents(new SyncChestInventoryListener(this), this);
		
	}
}