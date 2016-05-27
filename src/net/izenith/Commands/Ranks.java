package net.izenith.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class Ranks implements HubCommand{

	@Override
	public String getName() {
		return "ranks";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		List<String> ranks = Util.getConfig().getStringList("ranks");
		sender.sendMessage(ChatColor.BLUE + "Ranks:");
		for(String s : ranks){
			sender.sendMessage(Util.parseColors(s));
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
