package ua.darkphantom1337.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackPacksCommands implements CommandExecutor {

	private Main plugin;

	public BackPacksCommands(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission("zet")) {
			Player p = (Player) sender;
			if (args.length == 1) {
				if (args[0].equals("give")) {
					p.sendMessage("§2[Z] §f-> §cИспользуйте /zet give ник предмет §7[количество]");
					return false;
				}
				if (args[0].equals("backpack")) {
					p.sendMessage("§2[Z] §f-> §aКоманды для работы с рюкзаками: "
							+ "\n§f/zet backpack give номер_рюкзака" + "\n§f/zet backpack open номер_рюкзака");
					return false;
				}
				p.sendMessage("§2[Z] §f-> §cТакого аргумента для данной команды не существует!");
				return false;
			}
			if (args.length == 2) {
				if (args[0].equals("give")) {
					p.sendMessage("§2[Z] §f-> §7Используйте /zet give ник §cпредмет §7[количество]");
					return false;
				}
				if (args[0].equals("backpack")) {
					if (args[1].equals("give") || args[1].equals("open")) {
						p.sendMessage("§2[Z] §f-> §aИспользуйте: " + "\n§f/zet backpack give §cномер_рюкзака"
								+ "\n§f/zet backpack open §cномер_рюкзака");
						return false;
					}
					p.sendMessage("§2[Z] §f-> §cАргумента §a" + args[1] + " §c не существует для backpacks!");
					return false;
				}
				p.sendMessage("§2[Z] §f-> §cТакого аргумента для данной команды не существует!");
				return false;
			}
			if (args.length == 3) {
				if (args[0].equals("give")) {
					String nick = args[1], item = args[2];
					Player top = null;
					try {
						top = Bukkit.getPlayer(nick);
					} catch (Exception e) {
						top.sendMessage("§2[Z] §f-> §cИгрока с таким ником не существует!");
						return false;
					}
					if (plugin.bps.getEnabledBackPacks().contains(item)) {
						Long backpack_id = plugin.bps.getNextBackPackNumber();
						plugin.bpd.createBackPack(item, backpack_id);
						top.getInventory().addItem(plugin.bps.getBackPack(item, backpack_id));
						top.sendMessage("§2[Z] §f-> §aВам выдан "
								+ plugin.bps.getBackPackItemName(item).replaceAll("%number%", backpack_id.toString()));
						return true;
					} else {
						p.sendMessage("§2[Z] §f-> §cДанного предмета нету в конфиге :-(");
						return false;
					}
				}
				if (args[0].equals("backpack") && (args[1].equals("give") || args[1].equals("open"))) {
					Long backpack_id = (long) 0;
					try {
						backpack_id = Long.parseLong(args[2]);
					} catch (Exception e) {
						p.sendMessage(
								"§2[Z] §f-> §cВы ввели не номер рюкзака! Номер рюкзака может включать в себя только арабские цифры.");
						return false;
					}
					if (args[1].equals("give")) {
						if (plugin.bpd.backPackIsExist(backpack_id)) {
							p.getInventory().addItem(
									plugin.bps.getBackPack(plugin.bpd.getBackPackType(backpack_id), backpack_id));
							p.sendMessage("§2[Z] §f-> §aВам выдан рюкзак номер " + backpack_id);
							return true;
						} else {
							p.sendMessage("§2[Z] §f-> §cРюкзака с таким номером ещё не существует :-)");
							return false;
						}
					}
					if (args[1].equals("open")) {
						if (plugin.bpd.backPackIsExist(backpack_id)) {
							plugin.bpd.openBackPack(p, backpack_id);
							p.sendMessage("§2[Z] §f-> §aВы открыли рюкзак номер " + backpack_id);
							return true;
						} else {
							p.sendMessage("§2[Z] §f-> §cРюкзака с таким номером ещё не существует :-)");
							return false;
						}
					}
				}
				p.sendMessage("§2[Z] §f-> §cТакого аргумента для данной команды не существует!");
				return false;
			}
		} else
			sender.sendMessage("§cДанной команды не существует!");
		return false;
	}

}
