package net.izenith.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.izenith.Commands.CommandSpy;
import net.izenith.Commands.HubCommand;
import net.izenith.Kit.KitManager;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		Vars.init(this);
		IPlayerHandler.init();
		Util.initScoreboard();
		CommandSpy.loadFilters();
		Util.loadAllOnlineTimes();
		Util.updatePlayerList();
		Util.LoadEssentials();
		Util.setupAutoBroadcast();
		KitManager.init();
		KitManager.loadKits();
	}
	
	@Override
	public void onDisable() {
		Util.setAllOnlineTimes();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (Vars.commands == null)
			Vars.init(this);
		for (HubCommand hC : Vars.commands) {
			List<String> aliases = new ArrayList<String>();
			aliases.add(hC.getName());
			if (hC.getAliases() != null) {
				for (String alias : hC.getAliases()) {
					aliases.add(alias);
				}
			}
			if (containsIgnoreCase(aliases, cmd.getName())) {
				if (hC.hasPermission() && !sender.hasPermission(hC.getPermission())) {
					sender.sendMessage(ChatColor.RED + "You do not have permission for that command.");
					return true;
				}
				if (hC.onlyPlayers() && !(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + hC.getName() + " is only for players!");
					return false;
				}
				try {
					hC.onCommand(sender, cmd, label, args);
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Invalid arguments!");
				}
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "Command not found");
		return false;
	}

	public boolean containsIgnoreCase(List<String> list, String s) {
		for (String string : list) {
			if (string.equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

}
