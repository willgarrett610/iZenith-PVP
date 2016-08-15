package net.izenith.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Shop.BuyShop;
import net.izenith.Shop.SectionManager;

public class Buy implements HubCommand{

	@Override
	public String getName() {
		return "buy";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"shop"};
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		BuyShop.openShop((Player) sender, SectionManager.sectionNames.get(0).toLowerCase(), 0);
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
		return new Permission("shop.buy");
	}

}
