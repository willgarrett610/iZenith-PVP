package net.izenith.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Util;

public class UpdateList implements HubCommand{

	@Override
	public String getName() {
		return "updatelist";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Util.updatePlayerList();
		sender.sendMessage(ChatColor.GREEN + "Player list updated.");
	}

	@Override
	public boolean onlyPlayers() {
		return false;
	}

	@Override
	public boolean hasPermission() {
		return true;
	}

	@Override
	public Permission getPermission() {
		return new Permission("izenith.updatelist");
	}

	
	
}
