package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.Util;

public class BuyMoney implements HubCommand {

	@Override
	public String getName() {
		return "buymoney";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			return;
		}
		Util.getEconomy().depositPlayer(Bukkit.getPlayer(args[0]), Float.parseFloat(args[1]) * 2000);
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
