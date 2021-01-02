package ua.darkphantom1337.backpacks;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BackPacksSettingsFile {

	private Main plugin;

	public BackPacksSettingsFile(Main plugin) {
		this.plugin = plugin;
		setupCfgFile();
		if (getCfgFile().isSet("RPCommands"))
			saveCfgFile();
		else
			firstFill();
	}

	public FileConfiguration filecfg;
	public File file;

	public void setupCfgFile() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		file = new File(plugin.getDataFolder(), "backpacks-settings.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException localIOException) {
			}
		}
		new YamlConfiguration();
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	public FileConfiguration getCfgFile() {
		return filecfg;
	}

	public void saveCfgFile() {
		try {
			filecfg.save(file);
		} catch (IOException localIOException) {
		}
	}

	public void reloadCfgFile() {
		new YamlConfiguration();
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	public void firstFill() {
		getCfgFile().set("BackPacks", " File: backpacks-settings.yml || Author: DarkPhantom1337");
		getCfgFile().set("Next-BackPack-Number", 1);
		getCfgFile().set("Enabled-BackPacks", Arrays.asList(new String[] { "SPORT" }));
		getCfgFile().set("BackPack-Types.SPORT.ItemName", "§cСпортивный рюкзак #%number%");
		getCfgFile().set("BackPack-Types.SPORT.InventoryName", "§cСпортивный рюкзак #%number%");
		getCfgFile().set("BackPack-Types.SPORT.Lore", Arrays.asList(new String[] { "", "§7Размер: §a27 слотов", "§7Для быстрого доступа нажмите F" }));
		getCfgFile().set("BackPack-Types.SPORT.Size", 27);//9 18 27 36 45 54
		getCfgFile().set("BackPack-Types.SPORT.Material", String.valueOf(Material.SPIDER_EYE));
		getCfgFile().set("BackPack-Types.SPORT.CustomModelData", 1337001);
		saveCfgFile();
	}

	public Long getNextBackPackNumber() {
		Long l = getCfgFile().getLong("Next-BackPack-Number");
		getCfgFile().set("Next-BackPack-Number", l + 1);
		saveCfgFile();
		return l;
	}

	public HashMap<String, String> getBackPackTypes(){
		HashMap<String, String> types = new HashMap<String, String>();
		for(String type : getCfgFile().getStringList("Enabled-BackPacks"))
			types.put(getCfgFile().getString("BackPack-Types."+ type +".ItemName").split("#")[0], type);
		return types;
	}
	
	public ItemStack getBackPack(String backpack_type, Long backpack_id) {
		ItemStack item = new ItemStack(getBackPackMaterial(backpack_type));
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(getBackPackItemName(backpack_type).replaceAll("%number%", backpack_id.toString()));
		im.setLore(getBackPackLore(backpack_type));
		item.setItemMeta(im);
		return item;
	}
	
	public String getBackPackItemName(String backpack_type) {
		return getCfgFile().getString("BackPack-Types."+ backpack_type + ".ItemName");
	}
	
	public String getBackPackInventoryName(String backpack_type) {
		return getCfgFile().getString("BackPack-Types."+ backpack_type + ".InventoryName");
	}
	
	public Material getBackPackMaterial(String backpack_type) {
		return Material.valueOf(getCfgFile().getString("BackPack-Types."+ backpack_type + ".Material"));
	}
	
	public Integer getBackPackSize(String backpack_type) {
		return Integer.parseInt(getCfgFile().getString("BackPack-Types."+ backpack_type + ".Size"));
	}
	
	public List<String> getBackPackLore(String backpack_type) {
		return getCfgFile().getStringList("BackPack-Types."+ backpack_type + ".Lore");
	}
	
	public List<String> getEnabledBackPacks() {
		return getCfgFile().getStringList("Enabled-BackPacks");
	}

}
