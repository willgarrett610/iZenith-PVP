package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.md_5.bungee.api.ChatColor;

public class Donated implements HubCommand{

	@Override
	public String getName() {
		return "donated";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			sender.sendMessage(ChatColor.RED + "This command can only be used from the console.");
			return;
		}
		Player donator = Bukkit.getPlayer(args[0]);
		if(donator != null){
			donator.sendMessage(ChatColor.GOLD + "Thank you for donating " + ChatColor.WHITE + donator.getName() + "!");
			for(Player player : Bukkit.getOnlinePlayers()){
				if(!player.equals(donator))
					player.sendMessage(ChatColor.WHITE + donator.getName() + ChatColor.GOLD + " has donated to the server!");
			}
		}
	}

	@Override
	public boolean onlyPlayers() {
		return false;
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
