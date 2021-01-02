package ua.darkphantom1337.backpacks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_16_R2.ItemPickaxe;

public class BackPackEvents implements Listener {

	private Main plugin;

	public BackPackEvents(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onItemClick(PlayerInteractEvent e) {
		if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
			ItemStack item = e.getItem();
			ItemMeta im = item.getItemMeta();
			String dname = im.getDisplayName();
			if (dname.contains("#")) {
				if (plugin.bpt.containsKey(dname.split("#")[0])) {
					e.setCancelled(true);
					Player p = e.getPlayer();
					Long backpack_id = Long.parseLong(dname.split("#")[1]);
					if (plugin.bpd.backPackIsExist(backpack_id))
						plugin.bpd.openBackPack(p, backpack_id);
					else
						p.sendMessage(
								"§2[Z-BackPacks] §f-> §cИнформация про этот рюкзак отсутсвует! Для решения этой проблемы обратитесь к администрации!");

					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if (e.getItem().getItemMeta().getDisplayName().contains("#")) 
			if (plugin.bpt.containsKey(e.getItem().getItemMeta().getDisplayName().split("#")[0])) 
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		ItemStack i = e.getItem().getItemStack();
		if (i != null && i.getItemMeta().getDisplayName().contains("#") && plugin.bpt.containsKey(i.getItemMeta().getDisplayName().split("#")[0])) {
			for (ItemStack item : e.getPlayer().getInventory().getContents()) {
				if (item != null && item.getItemMeta().getDisplayName().contains("#") && plugin.bpt.containsKey(item.getItemMeta().getDisplayName().split("#")[0])) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cВы можете носить только 1 рюкзак.");
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getInventory() != null) {
			if (e.getView().getTitle().contains("#")) {
				Long backpack_id = Long.parseLong(e.getView().getTitle().split("#")[1]);
				if (plugin.bpd.backPackIsExist(backpack_id))
					for (ItemStack i : e.getInventory().getContents()) {
						if (i != null && i.getItemMeta().getDisplayName().contains("#") && plugin.bpt.containsKey(i.getItemMeta().getDisplayName().split("#")[0])) {
							e.getInventory().remove(i);
							e.getPlayer().getInventory().addItem(i);
							e.getPlayer().sendMessage("§cВ рюкзак нельзя ложить рюкзак!");
						}
					}
				plugin.bpd.saveBackPackItems(backpack_id, e.getInventory());
			}
		}
	}

}
