package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ClearChat implements HubCommand {

	@Override
	public String getName() {
		return "clearchat";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"cc"};
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		for(int i=0; i<100; i++){
			Bukkit.broadcastMessage("");
       }
		if (!(sender instanceof Player)){
			Bukkit.broadcastMessage(ChatColor.GRAY + "Console" + ChatColor.RED + " cleared the chat.");
		}else{
		Bukkit.broadcastMessage(ChatColor.GRAY + sender.getName() + ChatColor.RED + " cleared the chat.");
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
		return new Permission("hub.cc");
	}

}
