package net.izenith.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

public class Rename implements HubCommand {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "rename";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return new String[] { "re" };
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		// TODO Auto-generated method stub
		Player p = (Player) sender;

		if (p.getItemInHand().equals(Material.AIR)) {
			p.sendMessage(ChatColor.RED + "You can't rename nothing.");
			return;
		}
		ItemStack i = p.getItemInHand();
		ItemMeta im = i.getItemMeta();
		String name = "";
		for (String s : args) {
			name += s + " ";
		}
		if (args.length == 0) {
			p.sendMessage(ChatColor.BLUE + "Name has been set to default!");
		} else {
			p.sendMessage(ChatColor.BLUE + "Your item has been renamed to: "
					+ ChatColor.GREEN + name);
		}
		im.setDisplayName((ChatColor.translateAlternateColorCodes('&', name)));
		i.setItemMeta(im);
		p.setItemInHand(i);

	}

	@Override
	public boolean onlyPlayers() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasPermission() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Permission getPermission() {
		// TODO Auto-generated method stub
		return new Permission("izenith.rename");
	}

}
