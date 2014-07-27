package me.Aubli.SyncChest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import me.Aubli.SyncChest.Listeners.BlockBreakListener;
import me.Aubli.SyncChest.Listeners.BlockPlaceListener;
import me.Aubli.SyncChest.Listeners.InventoryListener;
import me.Aubli.SyncChest.Listeners.InventoryMoveListener;
import me.Aubli.SyncChest.Listeners.InventoryOpenListener;
import me.Aubli.SyncChest.Listeners.PlayerInteractListener;
import me.Aubli.SyncChest.SyncObjects.SyncManager;
import net.milkbowl.vault.economy.Economy;

import org.util.Metrics.Metrics;
import org.util.Metrics.Metrics.Graph;
import org.util.tools.ChestFileConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

	/*TODO
	 * 
	 * - Hopper zum laufen bringen			+
	 * - Feinschliff
	 * - metrics eigene diagramme			+
	 * - einige bugs ausbügeln
	 * - plugin.yml commands				+
	 * - mainchest linked chests			+
	 * - chestfile converter				+
	 * - sc status command 					+
	 */

public class SyncChest extends JavaPlugin {
	
	public static final Logger log = Bukkit.getLogger();
	private static SyncChest instance;

	public static ItemStack connector;	
	
	private boolean useMetrics = false;	
	private boolean enable;
	private boolean useEcon;
	
	public int wandItem;
	
	private static double chestPrice;
	private static double useFee;
	
	private static Economy econ;
	
	@Override	
	public void onDisable() {
		SyncManager.getManager().saveConfig();
		
		for(Player player : Bukkit.getOnlinePlayers()){
			clearPlayerInventory(player);
		}
		
		log.info("[SyncChest] Plugin is disabled!");
	}
	
	@Override
	public void onEnable() {
		String language;
		instance = this;
		
		if(getConfig().get("config.settings.language")==null){
			language = "en";
		}else if(!getConfig().getString("config.settings.language").isEmpty()){
			language = getConfig().getString("config.settings.language");
		}else{
			language = "en";
		}	
		
		new MessageManager(language);
		new SyncManager();
		
		ChestFileConverter cfc = new ChestFileConverter();
		if(cfc.chestFileExists()){
			log.info("[SyncChest] Found old Chest file! Converting ...");
			cfc.convert();
			log.info("[SyncChest] Chest file converted successfully!");
		}		
		
		getCommand("sc").setExecutor(new SyncChestCommands());	
		getCommand("syncchest").setExecutor(new SyncChestCommands());
		loadConfig();
		
		if(enable){		
			registerEvents();
		
			if(useMetrics){
				enableMetrics();
			}
			if(useEcon) {
				boolean success = setupEconomy();
				if(!success) {
					log.info("[SyncChest] Vault not found! Skip...");
					useEcon=false;
				}
			}
			
			log.info("[SyncChest] Plugin is enabled!");
		}else{
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	private void enableMetrics(){		
		try {
		    Metrics metrics = new Metrics(this);
		    
		    Graph chestsUsedGraph = metrics.createGraph("Used Chests");
		    chestsUsedGraph.addPlotter(new Metrics.Plotter("Chests") {

		    	@Override
		        public int getValue() {
		    		return SyncManager.getManager().getMainChests().length + SyncManager.getManager().getRelatedChests().length; // Number of Chests
		        }

		    });
		    
		    Graph hoppersUsed = metrics.createGraph("Used Hoppers");
		    hoppersUsed.addPlotter(new Metrics.Plotter("Hoppers") {
				
				@Override
				public int getValue() {
					return SyncManager.getManager().getHoppers().length;
				}
			});
		    
		    metrics.start();
		} catch (IOException e) {
			 log.info("[SyncChest] Can't start Metrics! Skip!");
		}
	}
	
	private void registerEvents(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new BlockBreakListener(), this);
		pm.registerEvents(new BlockPlaceListener(), this);
		pm.registerEvents(new PlayerInteractListener(this), this);
		pm.registerEvents(new InventoryOpenListener(), this);
		pm.registerEvents(new InventoryMoveListener(), this);
		pm.registerEvents(new InventoryListener(), this);		
	}
		
	public static SyncChest getInstance(){
		return instance;
	}
		
	@SuppressWarnings("deprecation")
	public static ItemStack getConnector(){		
		connector = new ItemStack(getInstance().getConfig().getInt("config.settings.wandItem"));	
		
		ItemMeta connMeta = connector.getItemMeta();
		connMeta.setDisplayName("Connector");
		connMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		
		ArrayList<String> lore = new ArrayList<String>();		
		lore.add(ChatColor.UNDERLINE + "The Connector:");
		lore.add(ChatColor.GREEN + "Connect a Mainchest with a Relatedchest!");
		lore.add(ChatColor.BLUE + "Right-Click: Info");
		lore.add(ChatColor.BLUE + "Left-Click: Connect");
		connMeta.setLore(lore);
		
		connector.setItemMeta(connMeta);
		return connector;
	}
	
	public static Economy getEcon() {
		return econ;
	}
	
	public static double getChestPrice() {
		return chestPrice;
	}
	
	public static double getUseFee() {
		return useFee;
	}
	
	public static boolean useEconomy() {
		return (SyncChest.getInstance().useEcon &&  getEcon()!=null);
	}
	
	public void clearPlayerInventory(Player playerSender){

		ItemStack mChest = SyncManager.getManager().getNewMainChests(1);
		ItemStack rChest = SyncManager.getManager().getNewRelatedChests(1);
		
		for(int i=0; i<playerSender.getInventory().getSize(); i++) {
			if(playerSender.getInventory().getItem(i)!=null) {
				ItemStack item = playerSender.getInventory().getItem(i);
				if(item.isSimilar(mChest) || item.isSimilar(rChest) || item.isSimilar(connector)) {
					playerSender.getInventory().clear(i);
				}
			}
		}
	}
	
	private void loadConfig(){
	
		this.getConfig().addDefault("config.settings.enable", true);
		this.getConfig().addDefault("config.settings.enableMetrics", true);
		this.getConfig().addDefault("config.settings.useEconomy", false);
		this.getConfig().addDefault("config.settings.language", "en");
		this.getConfig().addDefault("config.settings.wandItem", 369);
		
		this.getConfig().addDefault("econ.chestPrice", 5.0);
		this.getConfig().addDefault("econ.useFee", 1.5);
		
		useEcon = this.getConfig().getBoolean("config.settings.useEconomy");
		useMetrics = this.getConfig().getBoolean("config.settings.enableMetrics");
		enable = this.getConfig().getBoolean("config.settings.enable");
		wandItem = this.getConfig().getInt("config.settings.wandItem");		
		
		chestPrice = this.getConfig().getDouble("econ.chestPrice");
		useFee = this.getConfig().getDouble("econ.useFee");
		
		getConnector();
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();		
	}	
	
}