package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.md_5.bungee.api.ChatColor;

public class Ping implements HubCommand {

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			IPlayer iPlayer = IPlayerHandler.getPlayer((Player) sender);
			sender.sendMessage(ChatColor.WHITE + "Your ping is: " + iPlayer.getPingWithColor() + ChatColor.WHITE + " ms");
		} else {
			IPlayer iPlayer = IPlayerHandler.getPlayer(Bukkit.getPlayer(args[0]));
			if(iPlayer == null){
				sender.sendMessage(ChatColor.RED + "Could not find player");
				return;
			}
			sender.sendMessage(ChatColor.GREEN + iPlayer.player.getName() + "'s" + ChatColor.WHITE + " ping is: " + iPlayer.getPingWithColor() + ChatColor.WHITE + " ms");
		}
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
