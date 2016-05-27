package net.izenith.Commands;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class Kit implements HubCommand {

	@Override
	public String getName() {
		return "kit";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "kits" };
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		if (args.length == 0) {
			player.sendMessage(ChatColor.BLUE + "Global Kits");
			if (Util.getConfig().contains("kits.global")) {
				Set<String> globalKits = Util.getConfig().getConfigurationSection("kits.global").getKeys(false);
				for (String kitName : globalKits) {
					player.sendMessage(ChatColor.GREEN + kitName);
				}
			} else {
				player.sendMessage(ChatColor.RED + "None");
			}

			player.sendMessage(ChatColor.BLUE + "Private Kits");
			if (Util.getConfig().contains("kits." + player.getUniqueId())) {
				Set<String> privateKits = IPlayerHandler.getPlayer(player).config.getConfigurationSection("kits").getKeys(false);
				for (String kitName : privateKits) {
					player.sendMessage(ChatColor.GREEN + kitName);
				}
			} else {
				player.sendMessage(ChatColor.RED + "None");
			}
		} else {
			switch (args[0]) {
			case "create":
				if (args.length > 1) {
					iPlayer.setKit(args[1]);
					sender.sendMessage(ChatColor.BLUE + "Created kit " + ChatColor.GREEN + args[1]);
				} else {
					sender.sendMessage(ChatColor.RED + "/kit create <name>");
				}
				break;
			case "remove":
				if (args.length > 1) {
					iPlayer.removeKit(args[1]);
					player.sendMessage(ChatColor.BLUE + "Removed the kit " + ChatColor.GREEN + args[1]);
				} else {
					sender.sendMessage(ChatColor.RED + "/kit remove <name>");
				}
				break;
			default:
				String name = args[0].toLowerCase();
				if (iPlayer.config.get("kits." + name) != null
						|| Util.getConfig().get("kits.global." + name) != null) {
					iPlayer.getKit(name,true);
				} else {
					player.sendMessage(ChatColor.RED + "There is not a kit named " + args[0] + ".");
				}
				break;
			}
		}
	}

	@Override
	public boolean onlyPlayers() {
		return true;
	}

	@Override
	public boolean hasPermission() {
		return true;
	}

	@Override
	public Permission getPermission() {
		return new Permission("kit.use");
	}

}
