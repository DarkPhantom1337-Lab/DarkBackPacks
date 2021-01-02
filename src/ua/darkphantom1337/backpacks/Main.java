package ua.darkphantom1337.backpacks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public BackPacksDataFile bpd;
	public BackPacksSettingsFile bps;
	public HashMap<String, String> bpt;
	
	public void onLoad() {
		this.getLogger().info("Loading backpacks files...");
		bpd = new BackPacksDataFile(this);
		bps = new BackPacksSettingsFile(this);
		this.getLogger().info("Succesfully loaded files :-)");
		this.getLogger().info("Loading backpack types in ram...");
		bpt = bps.getBackPackTypes();
		this.getLogger().info("Succesfully loaded types :-)");
	}

	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BackPackEvents(this), this);
		this.getCommand("zet").setExecutor(new BackPacksCommands(this));
		this.getLogger().info("Plugin enabled! // by DarkPhantom1337, 2020");
	}
	
	public void onDisable() {
		this.getLogger().info("Plugin disabled! // by DarkPhantom1337, 2020");

	}

}
