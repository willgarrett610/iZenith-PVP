package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Kit.KitManager;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class RankBuy implements HubCommand {
	
	String[] ranks = {"iron","gold","emerald","diamond"};
	String[] colors = {"f","6","a","b"};
	
	@Override
	public String getName() {
		return "rankbuy";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				String rankName = KitManager.getKit("kitpvp",args[1]).guiItem.getItemMeta().getDisplayName();
				if (player.getName().equalsIgnoreCase(args[0])) {
					player.sendMessage(Util.parseColors("&5Thank you for purchasing a "
							+ rankName
							+ " &5rank! We truly appreciate your support!"));
				} else {
					String name = args[0];
					for(int i = 0; i < ranks.length; i++){
						String s = ranks[i];
						if(s.equals(args[1])){
							name = "&" + colors[i] + name;
						}
					}
					player.sendMessage(Util.parseColors(name + " &5just purchased a " + rankName + " &5rank!"));
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "This command can only be executed ");
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
