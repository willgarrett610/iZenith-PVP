package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class Trusted implements HubCommand{

	@Override
	public String getName() {
		return "trusted";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"void","tr"};
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Bukkit.dispatchCommand(sender, "warp trusted");
	}

	@Override
	public boolean onlyPlayers() {
		return true;
	}

	@Override
	public boolean hasPermission() {
		return false;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
