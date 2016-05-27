package net.izenith.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Util;

public class Suspend extends Util implements HubCommand {

	@Override
	public String getName() {
		return "suspend";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			if(args.length > 1){
				if(args[0].equalsIgnoreCase("remove")){
					Player p = getServer().getPlayer(args[1]);
					if(p == null){
						sender.sendMessage(ChatColor.RED + "That player does not exist!");
						return;
					}
					Util.removeSuspend(p);
					sender.sendMessage(ChatColor.GREEN + "Removed players suspension");
					return;
				}
			}
			Player p = getServer().getPlayer(args[0]);
			if(p == null){
				sender.sendMessage(ChatColor.RED + "That player does not exist!");
				return;
			}
			Util.suspend(p);
			sender.sendMessage(ChatColor.RED + "Suspended player");
		} catch (ArrayIndexOutOfBoundsException e) {
			sender.sendMessage(ChatColor.RED + "Please enter a player");
		}
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
		return new Permission("hub.suspend");
	}

}
