package net.izenith.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Vars;

public class CreateClock implements HubCommand {

	@Override
	public String getName() {
		return "createclock";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			Vars.createClock.put((Player) sender, args[0].toUpperCase());
			sender.sendMessage(ChatColor.GREEN + "Click the center of the clock");
		} catch (ArrayIndexOutOfBoundsException e) {
			sender.sendMessage(ChatColor.RED + "/createclock <NORTH,EAST,SOUTH,WEST>");
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
		return new Permission("clock.create");
	}

}
