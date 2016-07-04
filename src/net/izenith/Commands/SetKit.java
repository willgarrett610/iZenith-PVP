package net.izenith.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;

import net.izenith.Kit.KitManager;
import net.izenith.Main.Util;

public class SetKit implements HubCommand{

	@Override
	public String getName() {
		return "setkit";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		PlayerInventory inv = player.getInventory();
		ItemStack[][] contents = { inv.getContents(), inv.getArmorContents() };
		String world = player.getWorld().getName().split("_")[0];
		try {
			if(KitManager.getKit(world,args[0]) == null){
				KitManager.createKit(world,args[0], "temp", "temp", 1, (short) 0, contents);
				player.sendMessage(Util.parseColors("&7Kit &a" + args[0] + " &7created!"));
			} else {
				KitManager.setKit(world,args[0], contents);
				player.sendMessage(Util.parseColors("&7Kit &a" + args[0] + " &7modified!"));
			}
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "Invalid arguments");
			player.sendMessage("/setkit <name>");
		}
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
		return new Permission("kit.create");
	}

}
