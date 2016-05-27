package net.izenith.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Util;
import net.izenith.Main.Vars;
import net.md_5.bungee.api.ChatColor;

public class AdminChat implements HubCommand {

	// See HubCommand for formatting
	
	@Override
	public String getName() {
		return "adminchat";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"ac"};
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Make sure there is only one argument and not a message that starts with toggle
		if(args.length == 0){
			toggle((Player)sender);
			return;
		} else if(args.length == 1){
			if(args[0].equalsIgnoreCase("toggle")){
				// if admin chat is enabled, disable. if admin chat is disabled, enable.
				toggle((Player) sender);
				// return so it doesn't continue on and send the message "toggle"
				return;
			}
		}
		// Create a string for the message
		String message = "";
		// Add all arguments to the message
		for(String arg : args){
			message+=arg + " ";
		}
		// Send message to admins
		Util.sendAdminMessage(message, ((Player)sender));
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
		return new Permission("izenith.adminchat");
	}

	public void toggle(Player player){
		if(Vars.adminChat.contains(player)){
			Vars.adminChat.remove(player);
			player.sendMessage(ChatColor.RED + "Admin Chat disabled!");
		}else{
			Vars.adminChat.add(player);
			player.sendMessage(ChatColor.GREEN + "Admin Chat enabled!");
		}
	}
	
}
