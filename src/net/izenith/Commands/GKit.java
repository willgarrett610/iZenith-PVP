package net.izenith.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class GKit implements HubCommand {

	@Override
	public String getName() {
		return "gkit";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if(args.length > 0){
		switch (args[0]) {
		case "create":
			if (args.length > 1) {
				Util.setGlobalKit((Player) sender, args[1]);
				sender.sendMessage(ChatColor.BLUE + "Created global kit " + ChatColor.GREEN + args[1]);
			} else {
				sender.sendMessage(ChatColor.RED + "/gkit create <name>");
			}
			break;
		case "remove":
			if (args.length > 1) {
				Util.removeGlobalKit(args[1]);
				player.sendMessage(ChatColor.BLUE + "Removed global kit " + ChatColor.GREEN + args[1]);
			} else {
				sender.sendMessage(ChatColor.RED + "/gkit remove <name>");
			}
			break;
		default:
			player.sendMessage(ChatColor.RED + "/gkit <create,remove> <kit name>");
			break;
		}
		} else {
			player.sendMessage(ChatColor.RED + "/gkit <create,remove> <kit name>");
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
		return new Permission("gkit.use");
	}

}
