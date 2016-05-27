package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.md_5.bungee.api.ChatColor;

public class AddPlot implements HubCommand{

	// See HubCommand for formatting
	
	@Override
	public String getName() {
		return "addplot";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Make sure the command came from the console
		if(sender instanceof Player){
			sender.sendMessage(ChatColor.RED + "This command can only be used from the console.");
			return;
		}
		// Get the targeted player
		Player player = Bukkit.getPlayer(args[0]);
		// Check that the player exists
		if(player != null){
			// Up to 100 extra plots
			for(int i = 1; i < 100; i++){
				// Find the amount of plots the player has
				if(!player.hasPermission("plots.plot." + i)){
					// Add permission for 1 more plot
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add plots.plot." + i);
					break;
				}
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
