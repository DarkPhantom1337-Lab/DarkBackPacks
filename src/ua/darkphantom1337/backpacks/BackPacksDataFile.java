package ua.darkphantom1337.backpacks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BackPacksDataFile {

	private Main plugin;

	public BackPacksDataFile(Main plugin) {
		this.plugin = plugin;
		setupCfgFile();
		if (getCfgFile().isSet("BackPacks"))
			saveCfgFile();
		else
			firstFill();
	}

	public FileConfiguration filecfg;
	public File file;

	public void setupCfgFile() {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		file = new File(plugin.getDataFolder(), "backpacks-data.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadCfgFile() {
		new YamlConfiguration();
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	public void firstFill() {
		getCfgFile().set("BackPacks", " File: backpacks-data.yml || Author: DarkPhantom1337");
		saveCfgFile();
	}

	public void openBackPack(Player p, Long backpack_id) {
		if (getCfgFile().contains("BackPack_" + backpack_id)) {
			String backpack_type = getBackPackType(backpack_id);
			Inventory backpack = Bukkit.createInventory(null, plugin.bps.getBackPackSize(backpack_type),
					plugin.bps.getBackPackInventoryName(backpack_type).replaceAll("%number%", backpack_id.toString()));
			HashMap<Integer, ItemStack> items_data = getBackPackItemsData(backpack_id);
			for (Integer slot : items_data.keySet())
				backpack.setItem(slot, items_data.get(slot));
			p.openInventory(backpack);
		}
	}

	public void createBackPack(String backpack_type, Long backpack_id) {
		getCfgFile().set("BackPack_" + backpack_id + ".Type", backpack_type);
		getCfgFile().set("BackPack_" + backpack_id + ".Items", new ArrayList<String>());
		saveCfgFile();
	}

	public void saveBackPackItems(Long backpack_id, Inventory backpack_inventory) {
		HashMap<Integer, ItemStack> items = getItemsData(backpack_inventory);
		for (int i = 0; i < backpack_inventory.getSize(); i++)
			if (items.containsKey(i))
				getCfgFile().set("BackPack_" + backpack_id + ".Items." + i, items.get(i));
			else
				getCfgFile().set("BackPack_" + backpack_id + ".Items." + i, null);
		saveCfgFile();
	}

	public HashMap<Integer, ItemStack> getBackPackItemsData(Long backpack_id) { // МАТЕРИАЛ;ИМЯ;ЛОРЕ'ЛОРЕ'ЛОРЕ;ЛОМАЕМОСТЬ;КОЛИЧЕСтВО;ДУРАБИЛИТИ;МОДЕЛЬДАТА
		HashMap<Integer, ItemStack> items_data = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < 54; i++)
			if (getCfgFile().isSet("BackPack_" + backpack_id + ".Items." + i))
				items_data.put(i, getCfgFile().getItemStack("BackPack_" + backpack_id + ".Items." + i));
		return items_data;
	}

	public String getBackPackType(Long backpack_id) {
		return getCfgFile().getString("BackPack_" + backpack_id + ".Type");
	}

	public HashMap<Integer, ItemStack> getItemsData(Inventory backpack_inventory) {
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < backpack_inventory.getSize(); i++)
			if (backpack_inventory.getItem(i) != null && (backpack_inventory.getItem(i).getType() != Material.AIR))
				items.put(i, backpack_inventory.getItem(i));
		return items;
	}

	public Boolean backPackIsExist(Long backpack_id) {
		return getCfgFile().isSet("BackPack_" + backpack_id + ".Type");
	}

}
