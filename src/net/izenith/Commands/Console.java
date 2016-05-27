//disabled
package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Vars;

public class Console implements HubCommand {

	@Override
	public String getName() {

		return "console";
	}

	@Override
	public String[] getAliases() {

		return new String[] {"c","con"};
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (args.length == 0){
			sender.sendMessage(ChatColor.GREEN + "Insert Command.");
		}
		// Create a string for the message
		String message = "";
		// Add all arguments to the message
		for(String arg : args){
			message+=arg + " ";
		}
		// Send console message
		Vars.main.getServer().dispatchCommand(Bukkit.getConsoleSender(), message);

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

		return new Permission("izenith.console");
	}

}
