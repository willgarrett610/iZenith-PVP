package net.izenith.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Kit.KitManager;

public class ClearKit implements HubCommand{

	@Override
	public String getName() {
		return "clearkit";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"ckit"};
	}

	@Override
	public void onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
		KitManager.clearKit((Player) sender);
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
