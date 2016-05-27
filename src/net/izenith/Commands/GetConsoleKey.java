package net.izenith.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class GetConsoleKey implements HubCommand{

	@Override
	public String getName() {
		return "getconsolekey";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.BLUE + "You're console key is " + Util.addRandomKey());
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
		return new Permission("console.remoteaccess");
	}

}
